package sample.Controller.Manager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import sample.Model.Authentication;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;
import sample.Model.Objects.Users.Employee;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class AddEmployeeViewController implements Initializable {

    private OnEmployeeAddListener onEmployeeAddListener;

    public void setOnEmployeeAddListener(OnEmployeeAddListener onEmployeeAddListener) {
        this.onEmployeeAddListener = onEmployeeAddListener;
    }

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordField, emailTextField, phoneTextField;

    @FXML
    private ComboBox<Restaurant> restaurantComboBox;

    @FXML
    public void submitHandler() {
        Employee employee = new Employee();
        employee.setName(fullNameTextField.getText());
        employee.setUsername(usernameTextField.getText());
        employee.setEmail(emailTextField.getText());
        employee.setPhone(phoneTextField.getText());
        employee.setRestaurantid(restaurantComboBox.getSelectionModel().getSelectedItem().getId());

        try {
            employee.setPassword(Authentication.encryptPassword(passwordField.getText()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        onEmployeeAddListener.onEmployeeAddedListener(employee);
    }

    @FXML
    public void cancelHandler() {
        onEmployeeAddListener.onCancel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        Iterator it = DatabaseModel.getDatabaseModel().restaurantsHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            restaurants.add( (Restaurant) pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        restaurantComboBox.setItems(FXCollections.observableArrayList(restaurants));

    }

    public interface OnEmployeeAddListener {
        void onEmployeeAddedListener(Employee employee);
        void onCancel();
    }
}
