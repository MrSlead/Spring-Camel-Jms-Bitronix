package com.almod.jms;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class JmsFileListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(JmsFileListener.class);

    @Override
    public void onMessage(Message message) {
        if(message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage) message;
            try {
                byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);
                String out = new String(bytes);
                logger.info(String.format("Queue = %s; File '%s' contains = \n%s",
                        message.getJMSDestination().toString(), message.getStringProperty(Exchange.FILE_NAME), out));
            } catch (Exception e) {
                logger.error("Error in JmsFileListener class", e);
            }
        }
    }
}