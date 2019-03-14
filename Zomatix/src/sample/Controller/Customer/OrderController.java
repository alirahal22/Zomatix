package sample.Controller.Customer;

//import com.jfoenix.controls.JFXPopup;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sample.Model.Database.CheckOrderTask;
import sample.Model.Database.DatabaseModel;
import sample.Model.Database.TrackOrderTask;
import sample.Model.Objects.Users.Customer;
import sample.View.Cells.OrderCell.OrderItem;
import sample.View.Cells.OrderCell.OrderItemCellFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;

public class OrderController implements Initializable {

    private Customer activeCustomer;

    @FXML
    public Button checkOutButton;

    @FXML
    public ListView<OrderItem> orderItemsListView;

    @FXML
    public AnchorPane ordersAnchorPane;

    @FXML
    public Label orderEmptyLabel;

    @FXML
    public TextArea orderLocationTextArea;

    private void clear(){
        activeCustomer.getOrder().getItemsArrayList().clear();
        orderEmptyLabel.setVisible(true);
        checkOutButton.setDisable(true);
        orderLocationTextArea.clear();
        orderItemsListView.setItems(FXCollections.observableArrayList(activeCustomer.getOrder().getItemsArrayList()));
    }

    @FXML
    public void clearHandler(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Are you sure you want to clear your order?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() != ButtonType.OK)
            return;
        clear();
    }

    @FXML
    public void submitOrderHandler(){
        String location = orderLocationTextArea.getText();
        if (location.length() < 16) {
            Alert alert = new Alert(Alert.AlertType.ERROR); //Alert to inform the user that Location is invalid
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Location");
            alert.setContentText("Location must be at least 15 characters");
            alert.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION); //Alert to inform the user that Payment was successful
        alert.setTitle("Success");
        alert.setHeaderText("Payment Successfull");
        alert.setContentText("You paid " + activeCustomer.getOrder().getTotalPrice() + "$");
        alert.show();
        activeCustomer.getOrder().setLocation(location);
        activeCustomer.getOrder().checkOut();
        clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeCustomer = DatabaseModel.getDatabaseModel().getActiveCustomer();
        if (activeCustomer.getOrder().getItemsArrayList().size() > 0) {
            orderEmptyLabel.setVisible(false);
            checkOutButton.setDisable(false);

        }
        AnchorPane.setRightAnchor(ordersAnchorPane, 0d);
        AnchorPane.setLeftAnchor(ordersAnchorPane, 0d);
        AnchorPane.setBottomAnchor(ordersAnchorPane, 0d);
        AnchorPane.setTopAnchor(ordersAnchorPane, 0d);
        orderItemsListView.setFocusTraversable(false);

        //Should be received from the logged in Customer, but for testing
        //OrderItem orderItem = new OrderItem(2);
        //MainController.test_customer.getOrder().getItemsArrayList().add(orderItem);
        //MainController.test_customer.getOrder().setItemsArrayList(orderItems);


//        JFXPopup k = new JFXPopup();
//        VBox vbox = new VBox();
//        vbox.setStyle("-fx-background-color: #FFFFAA");
//        ordersAnchorPane.getChildren().add(vbox);
//        k.setHeight(100);
//        k.setWidth(100);
//        k.setPopupContent(vbox);
//        k.show(ordersAnchorPane);

        orderItemsListView.setCellFactory(new OrderItemCellFactory());
        orderItemsListView.setItems(FXCollections.observableArrayList(activeCustomer.getOrder().getItemsArrayList()));

    }

    public void setActiveCustomer(Customer activeCustomer) {
        this.activeCustomer = activeCustomer;
    }

}
