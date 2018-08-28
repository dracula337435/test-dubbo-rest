package org.dracula.test.dubbo.test3;

/**
 * @author dk
 */
public interface TestInterface {

    /**
     *
     * @param name
     * @return
     */
    String sayHello(String name);

    /**
     *
     * @param someParam
     * @return
     */
    SomeParam sayHello2(SomeParam someParam);

    /**
     *
     * @return
     */
    SomeParam sayHello3();

}
