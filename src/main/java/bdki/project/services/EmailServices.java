package bdki.project.services;

import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailServices {

    private final String username = "helencinderaapindi@gmail.com";
    private final String password = "Ramaini14"; 

    public void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        try {
            Message message = new MimeMessage(Session.getInstance(props, authenticator));
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
