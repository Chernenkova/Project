package edu.nc.service;

public class EmailSender extends Thread {

    private String emailTo;
    private String theme;
    private String message;

    public EmailSender(String emailTo, String theme, String message) {
        this.emailTo = emailTo;
        this.theme = theme;
        this.message = message;
    }

    public void send() {
        this.start();
    }

    @Override
    public void run() {
        SendEmail sendEmail = new SendEmail(emailTo, theme);
        sendEmail.sendMessage(message);
    }
}
