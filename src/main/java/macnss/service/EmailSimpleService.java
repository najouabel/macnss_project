package macnss.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Random;

public class EmailSimpleService {

    public static LocalTime sendMail(String body,String subject ,String email) {
        final String username = "najouabelhaj7@gmail.com";
        final String password = "sfoy kzkg rbna pnnx";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();

        }
        return LocalTime.now();
    }

    public static String codeGenerator() {
        int len = 6;
        String characters = "0123456789";

        Random rndm_method = new Random();
        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            otp[i] = characters.charAt(rndm_method.nextInt(characters.length()));
        }

        return String.valueOf(otp);
    }
}
