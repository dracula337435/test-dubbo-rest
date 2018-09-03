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

## 一些坑
1. 多协议，使用多个<dubbo:protocol>，之后的<dubbo:service>默认会使用多个protocol。具体到rest的情形，如果没有@Path，提供方启动会报错