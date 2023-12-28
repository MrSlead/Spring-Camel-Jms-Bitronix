package com.almod.util;

import com.almod.camel.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

    private EmailConfig emailConfig;

    @Autowired
    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public void sendMailMessage(String to, String subject, String body) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(emailConfig.getSessionForSendEmailMessage());

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(emailConfig.getEmailFromUsername()));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            LOG.info("Sending email message...");
            // Send message
            Transport transport = emailConfig.getSessionForSendEmailMessage().getTransport();
            transport.connect(emailConfig.getEmailFromUsername(), emailConfig.getEmailFromPassword());
            Transport.send(message, message.getAllRecipients());
            LOG.info("Sent message successfully");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
