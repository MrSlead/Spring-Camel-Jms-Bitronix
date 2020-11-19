package com.almod;

import com.almod.db.H2Repo;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;




public class Application {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        logger.info("HELLO!");
        ApplicationContext context = new ClassPathXmlApplicationContext("app-config.xml");
        CamelContext camelContext = (CamelContext) context.getBean("camelContext");
        /*ProducerTemplate template = camelContext.createProducerTemplate();
        camelContext.start();
        for (int i = 0; i < 5; i++) {
            template.sendBody("activemq:queue:start", "body" + i);
        }*/
        Thread.sleep(5000);


        H2Repo h2Repo = (H2Repo) context.getBean("H2Repo");
        h2Repo.showDataDB();

        camelContext.stop();
        System.exit(0);
    }


}
