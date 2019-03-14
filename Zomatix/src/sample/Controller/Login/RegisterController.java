package sample.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Model.Authentication;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.Users.Customer;
import sample.Model.Objects.Users.Manager;
import sample.Model.Objects.Users.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;


@SuppressWarnings("ALL")
public class RegisterController implements Initializable {

    private FormController formController;

    private Stage mainStage;

    @FXML
    private TextField nameTextField;

    @FXML
    public CheckBox managerCheckBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField emailFeld;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void backHandler() {
        loadLoginForm();
    }

    @FXML
    private ImageView nameReq;

    @FXML
    private ImageView usernameReq;

    @FXML
    private ImageView passwordReq;

    @FXML
    private ImageView confirmPasswordReq;

    @FXML
    private ImageView emailReq;

    @FXML
    private ImageView phoneReq;

    @FXML
    private ImageView confirmError;

    @FXML
    public AnchorPane parentAnchorPane;

    public void setFormController(FormController formController) {
        this.formController = formController;
    }

    public boolean validRegistration() {
        boolean validRegistration = false;
        if(!passwordField.getText().equals(confirmPasswordField.getText())){
            confirmError.setVisible(true);
            validRegistration = true;
        }
        else{
            confirmError.setVisible(false);
        }
        if(nameTextField.getText().isEmpty()){
            nameReq.setVisible(true);
            validRegistration = true;
        }
        else{
            nameReq.setVisible(false);
        }
        if(usernameTextField.getText().isEmpty()){
            usernameReq.setVisible(true);
            validRegistration = true;
        }
        else{
            usernameReq.setVisible(false);
        }
        if(passwordField.getText().isEmpty()){
            passwordReq.setVisible(true);
            validRegistration = true;
        }
        else{
            passwordReq.setVisible(false);
        }
        if(confirmPasswordField.getText().isEmpty()){
            confirmPasswordReq.setVisible(true);
            validRegistration = true;
        }
        else{
            confirmPasswordReq.setVisible(false);
        }
        if(emailFeld.getText().isEmpty() || !emailFeld.getText().contains("@") || !emailFeld.getText().contains(".")){
            emailReq.setVisible(true);
            validRegistration = true;
        }
        else {
            emailReq.setVisible(false);
        }
        if(phoneTextField.getText().isEmpty()) {
            phoneReq.setVisible(true);
            validRegistration = true;
        }
        else {
            phoneReq.setVisible(false);
        }
        return !validRegistration;
    }

    @FXML
    void submitHandler() {
        if (!validRegistration())
            return;
        if (!Authentication.availableUsername(usernameTextField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText("Username already taken");
            alert.showAndWait();
            return;
        }
        String password = "";
        try {
            password = Authentication.encryptPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        User user;
        if(managerCheckBox.isSelected()){
            user = new Manager(nameTextField.getText(), usernameTextField.getText(), password, phoneTextField.getText(), emailFeld.getText());
            DatabaseModel.getDatabaseModel().createManager((Manager)user);
        }
        else {
            user = new Customer(nameTextField.getText(), usernameTextField.getText(), password, phoneTextField.getText(), emailFeld.getText());
            DatabaseModel.getDatabaseModel().createCustomer((Customer)user);
        }
        Authentication.sendRegistrationMail(user);
        Authentication.registerAccountOnline(user);

        phoneReq.setVisible(false);
        emailReq.setVisible(false);
        confirmPasswordReq.setVisible(false);
        nameReq.setVisible(false);
        usernameReq.setVisible(false);
        passwordReq.setVisible(false);
        confirmError.setVisible(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration successful");
        alert.setHeaderText("Verify Account!");
        alert.setContentText("We sent a verification link to your email.\nPlease verify your account");
        alert.show();



        loadLoginForm();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadLoginForm() {

        Pane newLoadedPane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Login/form_login.fxml"));
            newLoadedPane = loader.load();
            parentAnchorPane.getChildren().setAll(newLoadedPane);

            LoginController loginController = loader.getController();
            loginController.parentAnchorPane = parentAnchorPane;
            loginController.setFormController(this.formController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }


}
