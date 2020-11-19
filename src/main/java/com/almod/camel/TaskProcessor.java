package com.almod.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

public class TaskProcessor implements Processor {
    private final static int BATCH_FILES = 2;
    private final static String TXT = "txt";
    private final static String XML = "xml";
    private final static String UNDEFINED = "undefined";

    private static Map<String, Integer> countByTypeFiles;
    private static int countFiles = 0;

    static {
        countByTypeFiles = new HashMap<String, Integer>();
        countByTypeFiles.put(XML, 0);
        countByTypeFiles.put(TXT, 0);
        countByTypeFiles.put(UNDEFINED, 0);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        handlingMessage(exchange);
        countFiles++;

        if(countFiles % BATCH_FILES == 0) {
            System.out.println(countByTypeFiles.get(XML));
            System.out.println(countByTypeFiles.get(TXT));
            System.out.println(countByTypeFiles.get(UNDEFINED));
        }

    }

    private void handlingMessage(Exchange exchange) {
        String fileName = getFileName(exchange);
        System.out.println();
        System.out.println(fileName);
        System.out.println();

        String typeFile = getTypeFile(fileName);
        System.out.println();
        System.out.println(typeFile);
        System.out.println();

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

}
