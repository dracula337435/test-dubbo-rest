package org.dracula.test.dubbo.test3.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import org.dracula.test.dubbo.test3.TestAnnotationInterface;
import org.dracula.test.dubbo.test3.TestInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/consumer-annotation.xml")
public class ConsumerAnnotationTest {

    //这个的提供方是传统xml的

    @Reference
    TestInterface testInterface;

    @Test
    public void test(){
        System.out.println(testInterface.sayHello3());
    }

    //这个的提供方也是注解的

    @Reference
    TestAnnotationInterface testAnnotationInterface;

    @Test
    public void testAnnotationProvider(){
        System.out.println(testAnnotationInterface.sayHelloAnnotation());
    }

}
