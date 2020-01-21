package io.dracula.test.dubbo.fix.http.pool.size;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.client.jaxrs.internal.ClientWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.proxy.ClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author dk
 */
public class RestProtocolWrapperForHttpPoolSizeFix implements Protocol {

    private static Logger logger = LoggerFactory.getLogger(RestProtocolWrapperForHttpPoolSizeFix.class);

    private Protocol wrapped;

    public RestProtocolWrapperForHttpPoolSizeFix(Protocol wrapped){
        this.wrapped = wrapped;
    }

    @Override
    public int getDefaultPort() {
        return wrapped.getDefaultPort();
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return wrapped.export(invoker);
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        logger.info("about to invoke wrapped function refer(...)");
        logger.info("type="+type);
        logger.info("url="+url);
        if(url != null && "rest".equalsIgnoreCase(url.getProtocol())){
            Invoker<T> result = wrapped.refer(type, url);
            try {
                Class<?> c0 = Class.forName("com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol$2");
                if(c0.equals(result.getClass())){
                    try {
                        Field f0 = c0.getDeclaredField("val$target");
                        try {
                            f0.setAccessible(true);
                            Object obj = f0.get(result);
                            System.out.println(obj);
                            {
                                //AbstractProxyInvoker的子类
                                if(obj instanceof AbstractProxyInvoker){
                                    try {
                                        Field f1 = AbstractProxyInvoker.class.getDeclaredField("proxy");
                                        f1.setAccessible(true);
                                        obj = f1.get(obj);
                                        //jdk动态代理，Proxy的子类
                                        if(obj instanceof Proxy){
                                            try {
                                                Field f2 = Proxy.class.getDeclaredField("h");
                                                f2.setAccessible(true);
                                                obj = f2.get(obj);
                                                if(obj instanceof ClientProxy){
                                                    Field f3 = ClientProxy.class.getDeclaredField("target");
                                                    f3.setAccessible(true);
                                                    obj = f3.get(obj);
                                                    if(obj instanceof ClientWebTarget){
                                                        Field f4 = ClientWebTarget.class.getDeclaredField("client");
                                                        f4.setAccessible(true);
                                                        obj = f4.get(obj);
                                                        if(obj instanceof ResteasyClient){
                                                            Field f5 = ResteasyClient.class.getDeclaredField("httpEngine");
                                                            f5.setAccessible(true);
                                                            obj = f5.get(obj);
                                                            if(obj instanceof ApacheHttpClient4Engine){
                                                                Field f6 = ApacheHttpClient4Engine.class.getDeclaredField("httpClient");
                                                                f6.setAccessible(true);
                                                                obj = f6.get(obj);
                                                                {
                                                                    try {
                                                                        Class<?> c7 = Class.forName("org.apache.http.impl.client.InternalHttpClient");
                                                                        if(c7.equals(obj.getClass())){
                                                                            Field f7 = c7.getDeclaredField("connManager");
                                                                            f7.setAccessible(true);
                                                                            obj = f7.get(obj);
                                                                            if(obj instanceof PoolingHttpClientConnectionManager){
                                                                                PoolingHttpClientConnectionManager connectionManager = (PoolingHttpClientConnectionManager)obj;
                                                                                //fix
                                                                                connectionManager.setMaxTotal(url.getParameter(Constants.CONNECTIONS_KEY, 20));
                                                                                connectionManager.setDefaultMaxPerRoute(url.getParameter(Constants.CONNECTIONS_KEY, 20));
                                                                                //
                                                                                logger.info("DefaultMaxPerRoute="+connectionManager.getDefaultMaxPerRoute());
                                                                                logger.info("MaxTotal"+connectionManager.getMaxTotal());
                                                                            }
                                                                        }
                                                                    } catch (ClassNotFoundException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (NoSuchFieldException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (NoSuchFieldException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return result;
        }
        return wrapped.refer(type, url);
    }

    @Override
    public void destroy() {
        wrapped.destroy();
    }
}
