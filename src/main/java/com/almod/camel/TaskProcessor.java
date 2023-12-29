package com.almod.camel;

import com.almod.mail.EmailSender;
import com.almod.util.PayloadLog;
import com.almod.util.TypeDoc;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class TaskProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TaskProcessor.class);

    public static final int BATCH_FILES = 3;
    public static final Map<String, Integer> numberOfFilesByType;
    private static int countProcessedFiles = 0;
    private static long startTimeHandlingBatchFiles;
    private static boolean isStartHandlingBatchFiles = true;

    static {
        numberOfFilesByType = new HashMap<>();
        initializeMap();
    }

    private static void initializeMap() {
        for(TypeDoc typeDoc : TypeDoc.values()) {
            numberOfFilesByType.put(typeDoc.getValue(), 0);
        }
    }

    private static void toZeroMap() {
        initializeMap();
    }

    private EmailSender emailSender;

    public void setMailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if(isStartHandlingBatchFiles) {
            startTimeHandlingBatchFiles = System.currentTimeMillis();
            isStartHandlingBatchFiles = false;
        }

        handlingMessage(exchange);
        countProcessedFiles++;

        if(countProcessedFiles % BATCH_FILES == 0) {
            PayloadLog.printInfoByProcessedTypeDoc();
            emailSender.sendMailMessage(exchange.getIn().getHeader("emailTo").toString(),
                    "Information on document types - Spring APP", PayloadLog.getInfoByProcessedTypeDocString());
            toZeroMap();
            isStartHandlingBatchFiles = true;
        }

    }

    private void handlingMessage(Exchange exchange) {
        String fileName = getFileName(exchange);
        String typeFile = getTypeFile(fileName).toLowerCase();

        LOG.info("Found file: " + fileName);

        if(typeFile.equals(TypeDoc.XML.toString()) || typeFile.equals(TypeDoc.TXT.toString())) {
            incrementCountTypeFiles(typeFile);
        }
        else {
            incrementCountTypeFiles(TypeDoc.UNDEFINED.toString());
        }
    }

    private void incrementCountTypeFiles(String typeFile) {
        int count = numberOfFilesByType.get(typeFile);
        numberOfFilesByType.put(typeFile, ++count);
    }

    private String getFileName(Exchange exchange) {
        return exchange.getIn().getHeader(Exchange.FILE_NAME).toString();
    }

    private String getTypeFile(String fileName) {
        String [] blocks = fileName.split("\\.");
        return blocks[blocks.length - 1].toLowerCase();
    }

    public static long getStartTimeHandlingBatchFiles() {
        return startTimeHandlingBatchFiles;
    }
}
