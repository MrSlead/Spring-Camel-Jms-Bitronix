package com.almod.camel;

import com.almod.Application;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class TaskProcessor implements Processor {
    private static Logger logger = LoggerFactory.getLogger(String.valueOf(Application.class));

    private final static int BATCH_FILES = 3;
    private final static String TXT = "txt";
    private final static String XML = "xml";
    private final static String UNDEFINED = "undefined";

    private static Map<String, Integer> countByTypeFiles;
    private static int countFiles = 0;
    private static long startTimeHandlingBatchFiles;
    private static boolean isStartHandilngBatchFiles = true;

    @Value("${emailAddress}")
    private String toEmail;

    static {
        countByTypeFiles = new HashMap<>();
        countByTypeFiles.put(XML, 0);
        countByTypeFiles.put(TXT, 0);
        countByTypeFiles.put(UNDEFINED, 0);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if(isStartHandilngBatchFiles) {
            startTimeHandlingBatchFiles = System.currentTimeMillis();
            isStartHandilngBatchFiles = false;
        }

        handlingMessage(exchange);
        countFiles++;

        if(countFiles % BATCH_FILES == 0) {
            sendMailMessage(exchange);
            logger.info("---------------------------------------------------------------------------");
            logger.info("A BATCH OF FILES HAS BEEN GENERATED");
            logger.info(String.format("Number of %s files: %d", XML, countByTypeFiles.get(XML)));
            logger.info(String.format("Number of %s files: %d", TXT, countByTypeFiles.get(TXT)));
            logger.info(String.format("Number of %s files: %d", UNDEFINED, countByTypeFiles.get(UNDEFINED)));
            logger.info("Time handling batch files: " + (System.currentTimeMillis() - startTimeHandlingBatchFiles) + " ms");
            isStartHandilngBatchFiles = true;
        }

    }

    private void handlingMessage(Exchange exchange) {
        String fileName = getFileName(exchange);

        String typeFile = getTypeFile(fileName);

        logger.info("---------------------------------------------------------------------------");
        logger.info("Found file: " + fileName);

        if(typeFile.equals("xml") || typeFile.equals("txt")) {
            incrementCountTypeFiles(typeFile);
        }
        else {
            incrementCountTypeFiles(UNDEFINED);
        }
    }

    private String getFileName(Exchange exchange) {
        return exchange.getIn().getHeader(Exchange.FILE_NAME).toString();
    }

    private String getTypeFile(String fileName) {
        String [] blocks = fileName.split("\\.");
        return blocks[blocks.length - 1].toLowerCase();
    }

    private void incrementCountTypeFiles(String typeFile) {
        int count = countByTypeFiles.get(typeFile);
        countByTypeFiles.put(typeFile, ++count);
    }

    private void sendMailMessage(Exchange exchange) {
        Message message = exchange.getOut();
        message.setHeader("to", toEmail);
        message.setHeader("subject", "Info about Batch files");
        message.setBody(getMessageBody());
    }

    private String getMessageBody() {
        StringBuilder mailBody = new StringBuilder();
        mailBody.append("A BATCH OF FILES HAS BEEN GENERATED\n");
        mailBody.append(String.format("Number of %s files: %d\n", XML, countByTypeFiles.get(XML)));
        mailBody.append(String.format("Number of %s files: %d\n", TXT, countByTypeFiles.get(TXT)));
        mailBody.append(String.format("Number of %s files: %d\n", UNDEFINED, countByTypeFiles.get(UNDEFINED)));
        mailBody.append("Time handling batch files: " + (System.currentTimeMillis() - startTimeHandlingBatchFiles) + " ms");

        return mailBody.toString();
    }

}
