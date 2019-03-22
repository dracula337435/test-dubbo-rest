# 试验dubbo

## 主要目的
1. rest  
```org.dracula.test.dubbo.test3.provider.TestInterfaceImpl```类，定义若干网址localhost:8080/restService/sayHello，sayHello2，sayHello3  
在pom里引入了不少东西，暂用jetty
1. curator  
pom里引入即可
1. dubbo的Filter  
/META-INF/下加文件，类SPI，key=value形式说明某key的实现，<dubbo:service>下加入filter说明  
Filter做的事为在调用前后，向控制台打印before和after  
test中的```org.dracula.test.dubbo.test3.consumer.ConsumerTest```会可看到效果
1. 泛化调用和实现  
接口为```org.dracula.test.dubbo.test3.FakeGenericInterface```，提供方实现类为```org.dracula.test.dubbo.test3.provider.TestGenericServiceImpl```
1. 注解  
消费方为```org.dracula.test.dubbo.test3.consumer.ConsumerAnnotationTest```，对应xml配置中有<dubbo:annotation>
1. 直连  
主要是在reference中增加url配置  
rest的直连url类似于```rest://127.0.0.1:8080```

## 一些坑
1. 多协议，使用多个<dubbo:protocol>，之后的<dubbo:service>默认会使用多个protocol。具体到rest的情形，如果没有@Path，提供方启动会报错
1. 如果web service系列注解在实现类上，可通过浏览器等方式访问，但是使用接口的消费者不行；将注解移入接口，消费者就可用了  
1. 发布rest服务后，可以直接，用idea的新```HTTP Client```，新建```scratches```，名为```rest-api.http```，内容如下：  
```
POST http://localhost:8080/restService/sayHello2
Content-Type: application/json

{"id": "gxk"}
```
这里注意加了```Content-Type```，json报文里属性名有引号

试验传递attachments，见```org.dracula.test.dubbo.test3.provider.TestFilter```，在consumer端set，在provider端get
```
rpcContext.setAttachment("test-attachment-key", "some-msg");
```
dubbo协议下，consumer端没打印出序列化后的内容，provider端打印出attachments这个```Map```为，可见
```
[DubboServerHandler-124.126.21.38:20880-thread-4] INFO org.dracula.test.dubbo.test3.provider.TestFilter - {test-attachment-key=some-msg, input=231, interface=org.dracula.test.dubbo.test3.TestInterface}
```
rest协议下，consumer端可见使用了报文头```Dubbo-Attachments```
```
DEBUG org.apache.http.headers - http-outgoing-0 >> GET /restService/sayHello?name=gxk HTTP/1.1
DEBUG org.apache.http.headers - http-outgoing-0 >> Accept: application/json;charset=UTF-8
DEBUG org.apache.http.headers - http-outgoing-0 >> Dubbo-Attachments: test-attachment-key=some-msg
DEBUG org.apache.http.headers - http-outgoing-0 >> Host: 124.126.21.38:8080
DEBUG org.apache.http.headers - http-outgoing-0 >> Connection: Keep-Alive
DEBUG org.apache.http.headers - http-outgoing-0 >> User-Agent: Apache-HttpClient/4.5.2 (Java/11.0.2)
DEBUG org.apache.http.headers - http-outgoing-0 >> Accept-Encoding: gzip,deflate
```
provider打印出attachments这个```Map```为
```
{test-attachment-key=some-msg}
```
对比```dubbo```协议和```rest```协议下```zookeeper```中信息的不同，可见```url```最前端协议不同：
```
[zk: localhost:2181(CONNECTED) 1] ls /dubbo/io.dracula.test.dubbo.brave.InterfaceB/providers
[rest%3A%2F%2F172.18.0.1%3A10880%2Fio.dracula.test.dubbo.brave.InterfaceB%3Fanyhost%3Dtrue%26application%3Dtest-dubbo-brave-B%26default.service.filter%3Dtracing%26dubbo%3D2.6.2%26generic%3Dfalse%26interface%3Dio.dracula.test.dubbo.brave.InterfaceB%26methods%3DtoB%26pid%3D3298%26side%3Dprovider%26timestamp%3D1553216539006]
[zk: localhost:2181(CONNECTED) 2] ls /dubbo/io.dracula.test.dubbo.brave.InterfaceC/providers
[dubbo%3A%2F%2F172.18.0.1%3A20881%2Fio.dracula.test.dubbo.brave.InterfaceC%3Fanyhost%3Dtrue%26application%3Dtest-dubbo-brave-C%26default.service.filter%3Dtracing%26dubbo%3D2.6.2%26generic%3Dfalse%26interface%3Dio.dracula.test.dubbo.brave.InterfaceC%26methods%3DtoC%26pid%3D3053%26side%3Dprovider%26timestamp%3D1553216511361]
```