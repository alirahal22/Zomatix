package sample.Controller.Customer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Complaint;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ComplaintsController implements Initializable {

    public DatabaseModel model = DatabaseModel.getDatabaseModel();

    @FXML
    public AnchorPane complaintsAnchorPane;


    @FXML
    public ComboBox<Restaurant> restaurantComboBox;

    @FXML
    public TextField fullNameTextField;

    @FXML
    public TextField emailTextField;

    @FXML
    public TextField subjectTextField;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public TextField nbOfCustomersTextField;

    @FXML
    public Button submitComplaintButton;

    @FXML
    public void submitComplaintHandler(){
        String fullname = fullNameTextField.getText();
        String email = emailTextField.getText();
        String subject = subjectTextField.getText();
        String description = descriptionTextArea.getText();
        int numberOfCustomers;
        try {
            numberOfCustomers = Integer.parseInt(nbOfCustomersTextField.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong input");
            alert.setContentText("Please enter a number in Number of Customers");
            alert.showAndWait();
            return;
        }
        Complaint complaint = new Complaint();
        complaint.setFullname(fullname);
        complaint.setEmail(email);
        complaint.setSubject(subject);
        complaint.setDescription(description);
        complaint.setNumberOfCustomers(numberOfCustomers);
        try {
            complaint.setRestaurant_name(restaurantComboBox.getSelectionModel().getSelectedItem().getName());
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a restaurant");
            alert.showAndWait();
            return;
        }
        complaint.setRestaurantid(restaurantComboBox.getSelectionModel().getSelectedItem().getRestaurantid());

        model.addComplaint(complaint);
        clear();
        setUpView();
    }

    public void clear(){
        fullNameTextField.setPromptText("Your full name...");
        fullNameTextField.clear();
        emailTextField.setPromptText("example@xchange.domain");
        emailTextField.clear();
        subjectTextField.setPromptText("Subject...");
        subjectTextField.clear();
        descriptionTextArea.setPromptText("Description here...");
        descriptionTextArea.clear();
        nbOfCustomersTextField.setPromptText("Number of customers...");
        nbOfCustomersTextField.clear();
        restaurantComboBox.getSelectionModel().clearSelection();
        restaurantComboBox.setPromptText("Restaurant");
    }

    @FXML
    public TableView<Complaint> complaintsTableView;

    @FXML
    public TableColumn<Complaint, String> fullnameTableColumn;

    @FXML
    public TableColumn<Complaint, String> emailTableColumn;

    @FXML
    public TableColumn<Complaint, String> subjectTableColumn;

    @FXML
    public TableColumn<Complaint, String> descriptionTableColumn;

    @FXML
    public TableColumn<Complaint, Integer> numberOfCustomersTableColumn;

    @FXML
    public TableColumn<Complaint, String> restaurantNameTableColumn;



    public void setUpView(){
        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();

        if (DatabaseModel.getDatabaseModel().getActiveCustomer() != null) {
            restaurantArrayList = new ArrayList<>(model.restaurantsHashMap.values());
            restaurantComboBox.setItems(FXCollections.observableArrayList(restaurantArrayList));
            return;
        }

        fullnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        subjectTableColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        numberOfCustomersTableColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCustomers"));
        restaurantNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("restaurant_name"));



        restaurantArrayList = new ArrayList<>(model.restaurantsHashMap.values());
        for(Restaurant r : restaurantArrayList) {
            model.getComplaints(r.getRestaurantid());
        }


        ArrayList<Complaint> complaintsArrayList = new ArrayList<>(model.complaintsHashMap.values());
        complaintsTableView.setItems(FXCollections.observableArrayList(complaintsArrayList));
        complaintsTableView.refresh();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        AnchorPane.setTopAnchor(complaintsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(complaintsAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(complaintsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(complaintsAnchorPane, 0.0);


        setUpView();
    }

}
