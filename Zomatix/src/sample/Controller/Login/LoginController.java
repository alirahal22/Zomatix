package sample.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Model.Authentication;
import sample.Model.Visitor.LoginVisitor;
import sample.Model.Objects.Users.Customer;
import sample.Model.Objects.Users.Employee;
import sample.Model.Objects.Users.Manager;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class LoginController implements Initializable {

    private FormController formController;

    @FXML
    public AnchorPane parentAnchorPane;

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public ImageView passwordErrorImageView;

    @FXML
    public ImageView usernameErrorImageView;

    @FXML
    public void registerHandler(){
        loadRegistrationForm();
    }

    @FXML
    public void loginHandler(){

        //check if all fields are filled
        if(!validLogin())
            return;
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        Customer customer = Authentication.verifyCustomer(username, password);
        if(customer != null){
            if (!Authentication.verifyActiveAccount(username))
                return;
            //close the login stage
            formController.closeLoginStage();
            customer.accept(new LoginVisitor());
            return;
        }

        Employee employee = Authentication.verifyEmployee(username, password);
        if (employee != null) {
            if (!Authentication.verifyActiveAccount(username))
                return;
            //close the login stage
            formController.closeLoginStage();
            employee.accept(new LoginVisitor());
            return;
        }

        Manager manager = Authentication.verifyManager(username, password);
        if (manager != null) {
            if (!Authentication.verifyActiveAccount(username))
                return;
            //close the login stage
            formController.closeLoginStage();
            manager.accept(new LoginVisitor());
            return;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("User not found");
        alert.setHeaderText("Username and password do NOT match");
        alert.setContentText("Wrong username or password!");
        alert.showAndWait();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadRegistrationForm() {

        Pane newLoadedPane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Login/form_registration.fxml"));
            newLoadedPane = loader.load();
            parentAnchorPane.getChildren().setAll(newLoadedPane);

            RegisterController registerController = loader.getController();
            registerController.parentAnchorPane = parentAnchorPane;
            registerController.setFormController(this.formController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validLogin() {
        if(usernameTextField.getText().isEmpty()){
            usernameErrorImageView.setVisible(true);
            return false;
        }
        else
            usernameErrorImageView.setVisible(false);

        if(passwordTextField.getText().isEmpty()) {
            passwordErrorImageView.setVisible(true);
            return false;
        }
        else
            passwordErrorImageView.setVisible(false);
        return true;
    }



    public void setFormController(FormController formController) {
        this.formController = formController;
    }
}
