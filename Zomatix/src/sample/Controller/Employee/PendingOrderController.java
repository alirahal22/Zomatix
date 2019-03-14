package sample.Controller.Employee;

import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import sample.Model.Objects.RestaurantComponents.Order;

import java.util.Optional;

public class PendingOrderController {

    private OnPendingOrderStatusChangeListener employeeController;

    private Pane myNode;
    private TilePane parentPane;

    private Order order;

    @FXML
    public Label orderCustomerNameLabel, orderLocationLabel, orderContentLabel;

    public void setOrder(Order order) {
        this.order = order;

        orderCustomerNameLabel.setText(order.getCustomerName());
        orderLocationLabel.setText(order.getLocation());
        orderContentLabel.setText(order.getOrganizedDescription());
    }

    public void acceptOrderHandler() {
        order.accepted();
        //TODO: send notification

        parentPane.getChildren().remove(myNode);
        employeeController.onOrderAccepted(order);
    }

    public void rejectOrderHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reject Order Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Are you sure you want to reject this order?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() != ButtonType.OK){
            return;
        }
        order.rejected();
        //TODO: send notification

        parentPane.getChildren().remove(myNode);
        employeeController.onOrderRejected();
    }

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    public void setParentPane(TilePane parentPane) {
        this.parentPane = parentPane;
    }

    public void setEmployeeController(OnPendingOrderStatusChangeListener employeeController) {
        this.employeeController = employeeController;
    }


    public interface OnPendingOrderStatusChangeListener {
        void onOrderAccepted(Order order);
        void onOrderRejected();
    }
}
