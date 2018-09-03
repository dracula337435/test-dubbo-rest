package org.dracula.test.dubbo.test3.consumer;

import org.dracula.test.dubbo.test3.FakeGenericInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/consumer-generic.xml")
public class ConsumerGenericTest {

    @Autowired
    FakeGenericInterface fakeGenericInterface;

    @Test
    public void test(){
        String result = fakeGenericInterface.someFunction("some-name");
        System.out.println(result);
    }

}
