package org.dracula.test.dubbo.test3.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.test3.TestAnnotationInterface;

/**
 * @author dk
 */
@Service(protocol = "dubbo")    //有了多协议(dubbo, rest)，默认都会上多协议，而rest又要求有@Path注解，注意
public class TestAnnotationInterfaceImpl implements TestAnnotationInterface {

    @Override
    public String sayHelloAnnotation() {
        return "hello annotation";
    }
}
