package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;

/**
 * @author dk
 */
@Path("/restService")
public interface TestInterface {

    /**
     *
     * @param name
     * @return
     */
    @GET
    @Path("/sayHello")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    String sayHello(@QueryParam("name") String name);

    /**
     *
     * @param someParam
     * @return
     */
    @POST
    @Path("/sayHello2")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    SomeParam sayHello2(SomeParam someParam);

    /**
     *
     * @return
     */
    @GET
    @Path("/sayHello3")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    SomeParam sayHello3();

}
