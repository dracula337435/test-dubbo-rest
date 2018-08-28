package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;

/**
 * @author dk
 */
@Path("/restService")
public class TestInterfaceImpl implements TestInterface{

    @GET
    @Path("/sayHello")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }

    @POST
    @Path("/sayHello2")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    @Override
    public SomeParam sayHello2(SomeParam someParam){
        if(someParam == null){
            return new SomeParam();
        }
        someParam.setId("hello "+someParam.getId());
        return someParam;
    }

    @GET
    @Path("/sayHello3")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    @Override
    public SomeParam sayHello3(){
        SomeParam someParam = new SomeParam();
        someParam.setId("hello gxk");
        return someParam;
    }

}
