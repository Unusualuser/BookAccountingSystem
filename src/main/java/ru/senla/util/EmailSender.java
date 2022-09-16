package ru.senla.util;

import org.apache.log4j.Logger;
import ru.senla.exception.EmailSendingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private final static Logger LOGGER = Logger.getLogger(EmailSender.class);
    private final String username;
    private final String password;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendMessage(String toEmail, String subj, String textMessage) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    private PasswordAuthentication getPassAuth() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subj);
            message.setText(textMessage);

            Transport.send(message, username, password);
        } catch (MessagingException e) {
            String errorMessage = String.format("Не удалось отправить сообщение по email %s.", toEmail);
            LOGGER.debug(String.format("%s %s", "Ошибка при отправке email.", e.getMessage()), e);
            LOGGER.error(errorMessage);
            throw new EmailSendingException(errorMessage);
        }
    }
}
