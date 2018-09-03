package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/consumer-annotation.xml")
public class ConsumerAnnotationTest {

    @Reference
    TestInterface testInterface;

    @Test
    public void test(){
        System.out.println(testInterface.sayHello3());
    }

}
