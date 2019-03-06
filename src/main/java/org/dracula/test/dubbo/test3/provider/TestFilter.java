package org.dracula.test.dubbo.test3.provider;

import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author dk
 */
public class TestFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(TestFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.info("before filter");
        // 想试验一下attachments的传递
        RpcContext rpcContext = RpcContext.getContext();
        if(rpcContext.isConsumerSide()){
            logger.info("消费方");
            // 提供方
            rpcContext.setAttachment("test-attachment-key", "some-msg");
        }else{
            logger.info("提供方");
            Map<String, String> attachments = rpcContext.getAttachments();
            logger.info(attachments.toString());
        }
        //
        Result result = invoker.invoke(invocation);
        logger.info("after filter");
        return result;
    }

}
