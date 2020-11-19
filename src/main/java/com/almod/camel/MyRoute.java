package com.almod.camel;

import org.apache.camel.builder.RouteBuilder;

public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        System.out.println(1234);

        /*from("file:data?noop=true")
                .to("file:output");*/

        from("activemq:queue:start")
                .to("stream:out");
    }

}
