package com.almod.camel;

import com.almod.Application;
import com.almod.db.H2Repo;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRoute extends RouteBuilder {
    private static Logger logger = LoggerFactory.getLogger(MyRoute.class);

    @Autowired
    H2Repo h2Repo;

    @Autowired
    private TaskProcessor taskProcessor;

    public void configure() throws Exception {
        /*from("file:data?noop=true")
                .to("activemq:queue:start");

        from("activemq:queue:start")
                .to("stream:out");*/
        logger.info("---------------------------------------------------------------------------");
        logger.info("Start of Apache Camel actions");
        logger.info("---------------------------------------------------------------------------");

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
                    .process(taskProcessor)
                    .to("activemq:queue:invalid-queue");
    }
}
