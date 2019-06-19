package org.dracula.test.dubbo.test3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author dk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/consumer.xml")
public class TestDecoupleTest {

    @Autowired
    TestInterface testInterface;

    @Test
    public void test(){
        AnotherParam anotherParam = new AnotherParam();
        anotherParam.setId("gxk");
        System.out.println(testInterface.sayHello(anotherParam).getId());
    }

}
