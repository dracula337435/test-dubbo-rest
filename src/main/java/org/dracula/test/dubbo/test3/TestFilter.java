package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.rpc.*;

/**
 * @author dk
 */
public class TestFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("before filter");
        Result result = invoker.invoke(invocation);
        System.out.println("after filter");
        return result;
    }

}
