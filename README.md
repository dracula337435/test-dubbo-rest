# 试验dubbo

## 主要目的
1. rest  
```org.dracula.test.dubbo.test3.TestInterfaceImpl```类，定义若干网址localhost:8080/restService/sayHello，sayHello2，sayHello3  
在pom里引入了不少东西，暂用jetty
1. curator  
pom里引入即可
1. dubbo的Filter  
/META-INF/下加文件，类SPI，key=value形式说明某key的实现，<dubbo:service>下加入filter说明  
Filter做的事为在调用前后，向控制台打印before和after  
test中的```org.dracula.test.dubbo.test3.ConsumerTest```会可看到效果
1. 泛化调用和实现  
接口为```org.dracula.test.dubbo.test3.FakeGenericInterface```，提供方实现类为```org.dracula.test.dubbo.test3.TestGenericServiceImpl```