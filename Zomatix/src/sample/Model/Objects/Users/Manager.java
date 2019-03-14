package sample.Model.Objects.Users;

import sample.Model.Visitor.UsersVisitor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Manager extends User {

    public Manager(String username, String password, String name) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Manager(int customerid, String name, String username, String password, String phone, String email) {
        this.id = customerid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public Manager(String name, String username, String password, String phone, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public void sendEmployeeMail(Employee employee) {
        final String username = "zomatix.co@gmail.com";
        final String password = "1234567890qweQWE";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("zomatix.co@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(employee.getEmail()));
            message.setSubject("Welcome to Zomatix");
            message.setText("Dear " + employee.getName() + ","
                    + "\n\n You were hired by " + this.name + " to organize their deliveries"
                    + "\nGood Luck!!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void accept(UsersVisitor usersVisitor) {
        usersVisitor.visit(this);
    }
}
