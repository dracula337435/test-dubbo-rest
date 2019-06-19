package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 一个全限定名相同的接口
 * @author dk
 */
@Path("/restService")
public interface TestInterface {

    /**
     *
     * @param anotherParam
     * @return
     */
    @POST
    @Path("/sayHello2")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    AnotherParam sayHello(AnotherParam anotherParam);

}
