package sample.Model;

import javafx.scene.control.Alert;
import sample.Model.Database.DatabaseConnection;
import sample.Model.Objects.Users.Customer;
import sample.Model.Objects.Users.Employee;
import sample.Model.Objects.Users.Manager;
import sample.Model.Objects.Users.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Authentication {

    private static String toHex(byte [] password){
        String hash = "";
        MessageDigest md = null;

        try {

            for (int i = 0; i < password.length; i++) {
                int number = Byte.toUnsignedInt(password[i]);

                hash += Integer.toHexString(number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    //Password encryption
    public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfPassword = password.getBytes("UTF-8");
        byte[] thedigest = md.digest(bytesOfPassword);
        return Authentication.toHex(thedigest);
    }

    //Check if account is activated
    public static boolean verifyActiveAccount(String username){
        URL url = null;
        try {
            url = new URL("http://zomatix.alirahal.com/authenticate.php?username=" + username);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                if(inputLine.contains("0")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Activation required!");
                    alert.setHeaderText("Your account is not yet activated.");
                    alert.setContentText("Please check your inbox/junk and verify your account!");
                    alert.showAndWait();
                    return false;
                }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //check if username is already taken
    public static boolean availableUsername(String username) {
        try {
            //check if username is taken by a customer
            String query = "SELECT username from Customer where username = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getDatabaseConnection().getConnection().prepareStatement(query);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first())
                return false;

            //check if username is taken by a manager
            query = "SELECT username from Manager where username = ?";
            preparedStatement = DatabaseConnection.getDatabaseConnection().getConnection().prepareStatement(query);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first())
                return false;

            //check if username is taken by an employee
            query = "SELECT username from Employee where username = ?";
            preparedStatement = DatabaseConnection.getDatabaseConnection().getConnection().prepareStatement(query);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first())
                return false;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void sendRegistrationMail(User user) {
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
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Welcome to Zomatix!");
            if(user instanceof Customer) {
                message.setText("Dear " + user.getName() + ","
                        + "\n\n You have successfully created an account on Zomatix!\n" +
                        "Verify your Account here: \n" +
                        "http://zomatix.alirahal.com/email_verification.php?h=" + user.getPassword() +
                        "\n Enjoy your food!!");
            }
            else if(user instanceof Employee){
                message.setText("Dear " + user.getName() + ","
                        + "\n\n You have successfully created an account on Zomatix!\n" +
                        "Verify your Account here: \n" +
                        "http://zomatix.alirahal.com/email_verification.php?h=" + user.getPassword() +
                        "\n Serve customers right!!");
            }
            else if (user instanceof Manager){
                message.setText("Dear " + user.getName() + ","
                        + "\n\n You have successfully created an account on Zomatix!\n" +
                        "Verify your Account here: \n" +
                        "http://zomatix.alirahal.com/email_verification.php?h=" + user.getPassword() +
                        "\n Manage on!!");
            }

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //Insert the username in the online database
    public static void registerAccountOnline(User user) {
        try {
            String username = user.getUsername();
            String password = user.getPassword();

            String create_user_link = "http://zomatix.alirahal.com/create_user.php?username=" + username + "&h=" + password;
            URL url = new URL(create_user_link);
            url.openStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Customer verifyCustomer(String username, String password) {
        try {
            String query = "Select * from Customer where username = ? and password = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getDatabaseConnection().getConnection().prepareStatement(query);

            password = Authentication.encryptPassword(password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                int customerid = resultSet.getInt("customerid");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");


                return new Customer(customerid, name,username,password,phone,email);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Employee verifyEmployee(String username, String password) {
        try {
            String query = "Select username, password, name, restaurantid from Employee where username = ? and password = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getDatabaseConnection().getConnection().prepareStatement(query);

            password = Authentication.encryptPassword(password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                String name = resultSet.getString("name");
                int restaurantid = resultSet.getInt("restaurantid");
                return new Employee(username, password, name, restaurantid);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Manager verifyManager(String username, String password) {
        try {
            String query = "Select * from Manager where username = ? and password = ?";
            PreparedStatement preparedStatement = DatabaseConnection.getDatabaseConnection().getConnection().prepareStatement(query);

            password = Authentication.encryptPassword(password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                String name = resultSet.getString("name");
                int managerid = resultSet.getInt("managerid");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                return new Manager(managerid, name,username, password, phone, email);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
