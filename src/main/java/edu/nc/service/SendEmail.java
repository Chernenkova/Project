package edu.nc.service;

import edu.nc.dataaccess.email.EmailAuthenticator;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;

public class SendEmail {
    private final static String PROPS_FILE = "src/main/resources/email.properties";


    private Message message = null;
    protected static String SMTP_SERVER = null;
    protected static String SMTP_Port = null;
    protected static String SMTP_AUTH_USER = null;
    protected static String SMTP_AUTH_PWD = null;
    protected static String EMAIL_FROM = null;
    protected static String FILE_PATH = null;
    protected static String REPLY_TO = null;

    private static void init() {
        try {
            InputStream is = new FileInputStream(PROPS_FILE);
            Reader reader = new InputStreamReader(is, "UTF-8");
            Properties pr = new Properties();
            pr.load(reader);
            SendEmail.SMTP_SERVER = pr.getProperty("server");
            SendEmail.SMTP_Port = pr.getProperty("port");
            SendEmail.EMAIL_FROM = pr.getProperty("from");
            SendEmail.SMTP_AUTH_USER = pr.getProperty("user");
            SendEmail.SMTP_AUTH_PWD = pr.getProperty("pass");
            SendEmail.REPLY_TO = pr.getProperty("replyto");
            SendEmail.FILE_PATH = PROPS_FILE;

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SendEmail(final String emailTo, final String thema) {
        init();

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", SMTP_Port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        try {
            Authenticator auth = new EmailAuthenticator(SMTP_AUTH_USER,
                    SMTP_AUTH_PWD);
            Session session = Session.getDefaultInstance(properties, auth);
            session.setDebug(false);

            InternetAddress email_from = new InternetAddress(EMAIL_FROM);
            InternetAddress email_to = new InternetAddress(emailTo);
            InternetAddress reply_to = (REPLY_TO != null) ?
                    new InternetAddress(REPLY_TO) : null;
            message = new MimeMessage(session);
            message.setFrom(email_from);
            message.setRecipient(Message.RecipientType.TO, email_to);
            message.setSubject(thema);
            if (reply_to != null)
                message.setReplyTo(new Address[]{reply_to});
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean sendMessage(final String text) {
        boolean result = false;
        try {

            Multipart mmp = new MimeMultipart();

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(text, "text/html; charset=utf-8");
            mmp.addBodyPart(bodyPart);

            if (FILE_PATH != null) {
                MimeBodyPart mbr = createFileAttachment(FILE_PATH);
                mmp.addBodyPart(mbr);
            }

            message.setContent(mmp);

            Transport.send(message);
            result = true;
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    private MimeBodyPart createFileAttachment(String filepath)
            throws MessagingException {

        MimeBodyPart mbp = new MimeBodyPart();

        FileDataSource fds = new FileDataSource(filepath);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName(fds.getName());
        return mbp;
    }
}
