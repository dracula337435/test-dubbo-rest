# 希望通过扩展点而非修改源码的方式解决rest连接池大小设置不生效的问题

## 问题简述

按官网```<dubbo:reference protocol="rest"/>```中```connections```可设置连接池大小，但是却发现实际未生效（```2.7.4```修复）。  
连接池大小确实被```dubbo```读到并设置给一个对象，但是这个对象未被使用，而是new了一个新同类型对象，连接池大小为默认值2和20。 

## 问题的代码一级解释

见```com.alibaba.dubbo.rpc.protocol.rest.RestProtocol```类的```doRefer(...)```函数片段：  
```
// TODO more configs to add
PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
// 20 is the default maxTotal of current PoolingClientConnectionManager
connectionManager.setMaxTotal(url.getParameter(Constants.CONNECTIONS_KEY, 20));
connectionManager.setDefaultMaxPerRoute(url.getParameter(Constants.CONNECTIONS_KEY, 20));
// （略）
CloseableHttpClient httpClient = HttpClientBuilder.create()
        // （略，但是注意没有使用connectionManager变量）
        .build();
```
其中，应重点关注```connections```设置被读取并设置到```connectionManager```，但接下来```httpClient```并没有使用```connectionManager```  
继续看```HttpClientBuilder```的```build()```方法片段：  
```
HttpClientConnectionManager connManagerCopy = this.connManager;
if (connManagerCopy == null) {
    // （略，但是注意接下来新建了poolingmgr）
    final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
            RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactoryCopy)
                .build(),
            null,
            null,
            dnsResolver,
            connTimeToLive,
            connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
    // （略）
    connManagerCopy = poolingmgr;
}
```
其中，应重点关注```PoolingHttpClientConnectionManager```是新对象

## 2.7.4的修正方法

见[2.7.4版本的RestProtocol类](https://github.com/apache/dubbo/blob/1d7e8fac4523d7482d5f2d8f25dd024807e3d3d5/dubbo-rpc/dubbo-rpc-rest/src/main/java/org/apache/dubbo/rpc/protocol/rest/RestProtocol.java)的```doRefer(...)```函数片段：  
```
// TODO more configs to add
PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
// 20 is the default maxTotal of current PoolingClientConnectionManager
connectionManager.setMaxTotal(url.getParameter(CONNECTIONS_KEY, HTTPCLIENTCONNECTIONMANAGER_MAXTOTAL));
connectionManager.setDefaultMaxPerRoute(url.getParameter(CONNECTIONS_KEY, HTTPCLIENTCONNECTIONMANAGER_MAXPERROUTE));
// （略，但是注意此处使用了connectionManager）
CloseableHttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(connectionManager)
        // （略）
        .build();
```
其中，应重点关注```httpClient```使用了```connectionManager```  
相关信息：  
1. 可见[github上此次提交443b6ff49f5ee0670c10e559c8adb30ac6206bc2](https://github.com/apache/dubbo/commit/443b6ff49f5ee0670c10e559c8adb30ac6206bc2)
1. 可见[github上对应pr#4614](https://github.com/apache/dubbo/pull/4614)
1. ```2.7.4.1```版本中包括此修改

## 未升级版本的补丁包

用的是```2.6.2```，着急修正的话方式也不复杂，拉下源码，参照前述方法加一行，重新编译。  
但是希望能在不动源码的情况下在```2.6.2```上修正，最终使用的方案是借助```wrapper```机制，做了一个类似于```AOP```后置通知，顺着```doRefer```的返回逐层找回去，修改连接池大小。  

### dubbo的wrapper机制

可见```com.alibaba.dubbo.common.extension.ExtensionLoader```。  
先看使用```wrapper```机制的效果，见```createExtension(...)```函数：
```
private T createExtension(String name) {
    // （略）
    Set<Class<?>> wrapperClasses = cachedWrapperClasses;
    if (wrapperClasses != null && !wrapperClasses.isEmpty()) {
        for (Class<?> wrapperClass : wrapperClasses) {
            instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
        }
    }
    // （略）
}
```  
即做好的类会被一层层包上```wrapper```类示例，作为最后使用的类  
再看```Set<Class<?>> cachedWrapperClasses```何时被写入，见```loadClass(...)```方法：  
```
private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name) throws NoSuchMethodException {
    if (!type.isAssignableFrom(clazz)) {
        // （略）
    }
    if (clazz.isAnnotationPresent(Adaptive.class)) {
        // （略）
    } else if (isWrapperClass(clazz)) {
        Set<Class<?>> wrappers = cachedWrapperClasses;
        if (wrappers == null) {
            cachedWrapperClasses = new ConcurrentHashSet<Class<?>>();
            wrappers = cachedWrapperClasses;
        }
        wrappers.add(clazz);
    } else {
        // （略）
    }
}
```
即，若判定某类为```wrapper```类，放入```cachedWrapperClasses```中。  
最后看```isWrapperClass(...)```方法：  
```
private boolean isWrapperClass(Class<?> clazz) {
    try {
        clazz.getConstructor(type);
        return true;
    } catch (NoSuchMethodException e) {
        return false;
    }
}
```
即，判断是```wrapper```类的标准为，这个类实现某接口A，且它有一个构造函数，其有且仅有一个A类型入参，例如：  
```
public class SomeProtocolWrapper implements Protocol {

    public SomeProtocolWrapper(Protocol wrapped){
        // （略）
    }

}
```

### 一层层去皮
  
内部类
```
$ javap AbstractProxyProtocol$2.class
Compiled from "AbstractProxyProtocol.java"
class com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol$2 extends com.alibaba.dubbo.rpc.protocol.AbstractInvoker<T> {
  final com.alibaba.dubbo.rpc.Invoker val$target;
  final java.lang.Class val$type;
  final com.alibaba.dubbo.common.URL val$url;
  final com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol this$0;
  com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol$2(com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol, java.lang.Class, com.alibaba.dubbo.common.URL, com.alibaba.dubbo.rpc.Invoker, java.lang.Class, com.alibaba.dubbo.common.URL);
  protected com.alibaba.dubbo.rpc.Result doInvoke(com.alibaba.dubbo.rpc.Invocation) throws java.lang.Throwable;
}
```

```
$ javap JavassistProxyFactory$1.class
Compiled from "JavassistProxyFactory.java"
class com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory$1 extends com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker<T> {
  final com.alibaba.dubbo.common.bytecode.Wrapper val$wrapper;
  final com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory this$0;
  com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory$1(com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory, java.lang.Object, java.lang.Class, com.alibaba.dubbo.common.URL, com.alibaba.dubbo.common.bytecode.Wrapper);
  protected java.lang.Object doInvoke(T, java.lang.String, java.lang.Class<?>[], java.lang.Object[]) throws java.lang.Throwable;
}
```