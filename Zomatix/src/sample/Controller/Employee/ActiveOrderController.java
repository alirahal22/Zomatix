package sample.Controller.Employee;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import sample.Model.Objects.RestaurantComponents.Order;

public class ActiveOrderController {

    private OnActiveOrderStatusChangeListener employeeController;

    private Pane myNode;
    private TilePane parentPane;

    private Order order;

    @FXML
    public Label orderCustomerNameLabel, orderLocationLabel, orderContentLabel;


    public void setOrder(Order order) {
        this.order = order;

        orderCustomerNameLabel.setText(order.getCustomerName());
        orderLocationLabel.setText(order.getLocation());
//        orderContentLabel.setText("TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD ");
        orderContentLabel.setText(order.getOrganizedDescription());
    }

    public void deliverOrderHandler() {
        System.out.println("Delivering Order");
        order.delivered();

        parentPane.getChildren().remove(myNode);
        employeeController.onOrderDelivered(order);
    }

    public void cancelOrderHandler() {
        System.out.println("Order Cancelled");
        order.cancelled();

        parentPane.getChildren().remove(myNode);
        employeeController.onOrderCancelled();
    }

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    public void setParentPane(TilePane parentPane) {
        this.parentPane = parentPane;
    }

    public void setEmployeeController(OnActiveOrderStatusChangeListener employeeController) {
        this.employeeController = employeeController;
    }

    public interface OnActiveOrderStatusChangeListener {
        void onOrderDelivered(Order order);
        void onOrderCancelled();
    }
}
