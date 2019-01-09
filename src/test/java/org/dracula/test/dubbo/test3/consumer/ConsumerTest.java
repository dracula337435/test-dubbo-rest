package org.dracula.test.dubbo.test3.consumer;

import org.dracula.test.dubbo.test3.SomeParam;
import org.dracula.test.dubbo.test3.TestInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/consumer.xml")
public class ConsumerTest {

    @Autowired
    TestInterface testInterface;

    @Test
    public void test(){
        System.out.println(testInterface.sayHello("gxk"));
    }

    @Test
    public void test2(){
        SomeParam someParam = new SomeParam();
        someParam.setId("gxk");
        System.out.println(testInterface.sayHello2(someParam).getId());
    }

    @Test
    public void test3(){
        SomeParam someParam = testInterface.sayHello3();
        System.out.println(someParam.getId());
    }

}
