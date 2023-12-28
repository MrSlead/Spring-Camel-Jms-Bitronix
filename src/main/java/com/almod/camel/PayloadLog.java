package com.almod.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PayloadLog {
    private static final Logger LOG = LoggerFactory.getLogger(PayloadLog.class);

    private static final Map localMap = TaskProcessor.numberOfFilesByType;

    public static void printInfoByProcessedTypeDoc() {
        for(String log : getInfoByProcessedTypeDocList()) {
            LOG.info(log);
        }
    }

    public static String getInfoByProcessedTypeDocString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(String log : getInfoByProcessedTypeDocList()) {
            stringBuilder.append(log + "\n");
        }

        return stringBuilder.toString();
    }

    private static List<String> getInfoByProcessedTypeDocList() {
        List<String> list = new ArrayList<>();
        list.add("A BATCH OF FILES HAS BEEN GENERATED");
        for(Object key : localMap.keySet()) {
            list.add(String.format("Number of %s files: %s", key, localMap.get(key)));
        }
        list.add("Time handling batch files: " + (System.currentTimeMillis() - TaskProcessor.getStartTimeHandlingBatchFiles()) + " ms");

        return list;
    }
}
