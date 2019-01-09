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