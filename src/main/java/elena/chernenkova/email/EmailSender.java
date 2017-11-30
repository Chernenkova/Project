package elena.chernenkova.email;

public class EmailSender extends Thread{

    private String emailTo;
    private String theme;
    private String message;

    public EmailSender(String emailTo, String theme, String message) {
        this.emailTo = emailTo;
        this.theme = theme;
        this.message = message;
    }

    @Override
    public void run() {
        SendEmail sendEmail = new SendEmail(emailTo, theme);
        sendEmail.sendMessage(message);
    }
}
