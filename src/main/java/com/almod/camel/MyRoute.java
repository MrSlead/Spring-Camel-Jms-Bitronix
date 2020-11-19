package com.almod.camel;

import com.almod.db.H2Repo;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRoute extends RouteBuilder {

    @Autowired
    H2Repo h2Repo;

    @Autowired
    private TaskProcessor taskProcessor;

    public void configure() throws Exception {
        /*from("file:data?noop=true")
                .to("activemq:queue:start");

        from("activemq:queue:start")
                .to("stream:out");*/

        from("file:data?noop=true")
                .choice()
                .when(header(Exchange.FILE_NAME).endsWith(".xml"))
                    .process(taskProcessor)
                    .to("activemq:queue:queue")
                .when(header(Exchange.FILE_NAME).endsWith(".txt"))
                    .process(taskProcessor)
                    .bean(h2Repo, "writeInDB")
                    .to("activemq:queue:queue")
                .otherwise()
                    .to("activemq:queue:invalid-queue");
    }
}
