package com.almod;

import com.almod.db.H2Repo;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.Main;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("app-config.xml");

        CamelContext camelContext = (CamelContext) context.getBean("camelContext");
        ProducerTemplate template = camelContext.createProducerTemplate();
        camelContext.start();
        for (int i = 0; i < 5; i++) {
            template.sendBody("activemq:queue:start", "body" + i);
        }
        Thread.sleep(3000);

        camelContext.stop();
        System.exit(0);
    }


}
