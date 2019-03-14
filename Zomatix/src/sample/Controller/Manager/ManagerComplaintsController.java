package sample.Controller.Manager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Complaint;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagerComplaintsController implements Initializable {

    @FXML
    public AnchorPane complaintsAnchorPane;

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

        fullnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        subjectTableColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        numberOfCustomersTableColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCustomers"));
        restaurantNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("restaurant_name"));



        restaurantArrayList = new ArrayList<>(DatabaseModel.getDatabaseModel().restaurantsHashMap.values());

        for(Restaurant r : restaurantArrayList) {
            DatabaseModel.getDatabaseModel().getComplaints(r.getRestaurantid());
        }

        ArrayList<Complaint> complaintsArrayList = new ArrayList<>(DatabaseModel.getDatabaseModel().complaintsHashMap.values());

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
