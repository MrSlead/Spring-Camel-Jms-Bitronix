package com.almod.camel;

import com.almod.db.H2Service;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MyRoute extends RouteBuilder {
    private static Logger logger = LoggerFactory.getLogger(MyRoute.class);

    private H2Service h2Service;

    private TaskProcessor taskProcessor;

    @Value("${destination.valid.type}")
    private String destinationValidType;

    @Value("${destination.error}")
    private String destinationError;

    @Value("${email.to}")
    private String emailTo;

    public void configure() {
        logger.info("---------------------------------------------------------------------------");
        logger.info("Start of Apache Camel actions");
        logger.info("---------------------------------------------------------------------------");


        onException(Exception.class)
                .to("activemq:queue:" + destinationError);

        from("file:data?noop=true").transacted()
                .setHeader("emailTo").constant(emailTo)
                .choice()
                .when(header(Exchange.FILE_NAME).endsWith(".xml"))
                    .process(taskProcessor)
                    .to("activemq:queue:" + destinationValidType)
                .when(header(Exchange.FILE_NAME).endsWith(".txt"))
                    .process(taskProcessor)
                    .bean(h2Service, "writeInDB")
                    .to("activemq:queue:" + destinationValidType)
                .otherwise()
                    .process(taskProcessor)
                    .to("activemq:queue:" + destinationError)
                .end();
                /*.choice()
                .when(header("to").isNotNull())
                    .to("smtps://" + emailUrl)
                .end();*/
    }

    public void setH2Service(H2Service h2Service) {
        this.h2Service = h2Service;
    }

    public void setTaskProcessor(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
    }
}
