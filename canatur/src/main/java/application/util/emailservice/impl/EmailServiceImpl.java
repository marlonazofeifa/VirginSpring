package application.util.emailservice.impl;

import application.util.emailservice.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;
import java.util.Scanner;

@Service
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements EmailService {

    @Value("${emailUsername}")
    private String emailUsername;

    @Value("${emailPass}")
    private String emailPass;

    @Value("${emailOut}")
    private String emailOut;

    @Value("${emailPort}")
    private String emailPort;

    @Value("${newUserMail}")
    private String newUserMail;

    @Value("${recoverPassMail}")
    private String recoverPassMail;

    private Message message;

    public boolean sendRecoverEmail(String userEmail, String username, String password) {
        String messageSubject = "Recuperación de contraseña";
        String htmlBody = " ";
        Scanner scanner = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(recoverPassMail).getFile());
            scanner = new Scanner( file, "UTF-8" );
            htmlBody = scanner.useDelimiter("\\A").next();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        htmlBody = htmlBody
                .replace("$password$", password)
                .replace("$username$", username);
        return sendEmail(htmlBody, messageSubject, userEmail);
    }

    public boolean sendNewUserRegister(String email,String username, String password) {
        String messageSubject = "Registro en sistema canatur";
        String htmlBody = " ";
        Scanner scanner = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(newUserMail).getFile().replace("%20"," "));
            scanner = new Scanner( file, "UTF-8" );
            htmlBody = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        return sendEmail(htmlBody.replace("$password$", password).replace("$username$",username), messageSubject, email);
    }

    private boolean sendEmail(String htmlBody, String emailSubject, String sendTo) {

        Thread emailSender = new Thread(() -> {
            Boolean sent = false;
            int attempts = 0;
            while (!sent && (attempts < 10)) {
                try {
                    message = new MimeMessage(getSession());
                    message.getSession().setDebug(false);
                    message.setFrom(new InternetAddress(emailUsername));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(sendTo));
                    message.setSubject(emailSubject);
                    message.setContent(htmlBody, "text/html; charset=utf-8");
                    message.getSession().setProtocolForAddress("transport", "smtps");
                    Transport.send(message);
                    System.out.println("The email was sent successfully");
                    sent = true;
                } catch (MessagingException e) {
                    System.out.println("Error sending email: ");
                    e.printStackTrace();
                    attempts++;
                }
            }
        }, "Email sender thread");
        emailSender.start();
        return true;
    }

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailOut);
        props.put("mail.smtp.port", emailPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPass);
            }
        });
    }

}
