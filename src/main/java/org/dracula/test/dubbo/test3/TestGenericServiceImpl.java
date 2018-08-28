package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;

/**
 * @author dk
 */
public class TestGenericServiceImpl implements GenericService {

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        System.out.println("method = [" + method + "], parameterTypes = [" + parameterTypes + "], args = [" + args + "]");
        return null;
    }

}
