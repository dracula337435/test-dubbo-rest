package org.dracula.test.dubbo.test3.provider;

import org.dracula.test.dubbo.test3.SomeParam;
import org.dracula.test.dubbo.test3.TestInterface;

/**
 * @author dk
 */
public class TestInterfaceImpl implements TestInterface {

    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }

    @Override
    public SomeParam sayHello2(SomeParam someParam){
        if(someParam == null){
            return new SomeParam();
        }
        someParam.setId("hello "+someParam.getId());
        return someParam;
    }

    @Override
    public SomeParam sayHello3(){
        SomeParam someParam = new SomeParam();
        someParam.setId("hello gxk");
        return someParam;
    }

}
