package com.almod.mail;

import javax.mail.*;
import java.util.Properties;

public class EmailConfig {
    private String emailFromUsername;
    private String emailFromPassword;
    private String emailHostName;
    private String emailHostPort;
    private String emailHostSSLEnable;
    private String emailHostAuth;
    private Session sessionForSendEmailMessage;

    public EmailConfig(String emailFromUsername, String emailFromPassword, String emailHostName, String emailHostPort, String emailHostSSLEnable, String emailHostAuth) {
        this.emailFromUsername = emailFromUsername;
        this.emailFromPassword = emailFromPassword;
        this.emailHostName = emailHostName;
        this.emailHostPort = emailHostPort;
        this.emailHostSSLEnable = emailHostSSLEnable;
        this.emailHostAuth = emailHostAuth;

        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", emailHostName);
        properties.put("mail.smtp.port", emailHostPort);
        properties.put("mail.smtp.ssl.enable", emailHostSSLEnable);
        properties.put("mail.smtp.auth", emailHostAuth);

        // Get the Session object.// and pass username and password
        sessionForSendEmailMessage = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(emailFromUsername, emailFromPassword);

            }

        });

        // Used to debug SMTP issues
        //sessionForSendEmailMessage.setDebug(true);
    }

    public String getEmailFromUsername() {
        return emailFromUsername;
    }

    public String getEmailFromPassword() {
        return emailFromPassword;
    }

    public String getEmailHostName() {
        return emailHostName;
    }

    public String getEmailHostPort() {
        return emailHostPort;
    }

    public String getEmailHostSSLEnable() {
        return emailHostSSLEnable;
    }

    public String getEmailHostAuth() {
        return emailHostAuth;
    }

    public Session getSessionForSendEmailMessage() {
        return sessionForSendEmailMessage;
    }
}
