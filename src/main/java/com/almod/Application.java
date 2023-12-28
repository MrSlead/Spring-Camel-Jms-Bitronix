package com.almod;

import com.almod.db.H2Service;
import com.almod.db.H2ServiceImpl;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("app-config.xml");
        CamelContext camelContext = (CamelContext) context.getBean("camelContext");
        Thread.sleep(15000);

        H2Service h2Service = (H2Service) context.getBean("h2Service");
        h2Service.showDataDB();

        //camelContext.stop();
        //System.exit(0);
    }


}
