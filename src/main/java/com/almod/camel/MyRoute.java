package com.almod.camel;

import com.almod.db.H2Repo;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MyRoute extends RouteBuilder {
    private static Logger logger = LoggerFactory.getLogger(MyRoute.class);

    @Autowired
    private H2Repo h2Repo;

    @Autowired
    private TaskProcessor taskProcessor;

    @Value("${destinationJms}")
    private String destination;

    public void configure() throws Exception {
        logger.info("---------------------------------------------------------------------------");
        logger.info("Start of Apache Camel actions");
        logger.info("---------------------------------------------------------------------------");


        onException(Exception.class)
                .to("activemq:queue:invalid-queue");

        from("file:data?noop=true").transacted()
                .choice()
                .when(header(Exchange.FILE_NAME).endsWith(".xml"))
                    .process(taskProcessor)
                    .to("activemq:queue:" + destination)
                .when(header(Exchange.FILE_NAME).endsWith(".txt"))
                    .process(taskProcessor)
                    .bean(h2Repo, "writeInDB")
                    .to("activemq:queue:" + destination)
                .otherwise()
                    .process(taskProcessor)
                    .to("activemq:queue:invalid-queue")
                .end()
                .choice()
                .when(header("to").isNotNull())
                    .to("smtps://smtp.gmail.com:465?username=springcamelapp@gmail.com&password=6V01k$_p4ElA1")
                .end();
    }
}
