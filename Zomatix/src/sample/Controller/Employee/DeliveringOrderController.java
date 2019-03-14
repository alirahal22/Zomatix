package sample.Controller.Employee;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import sample.Model.Objects.RestaurantComponents.Order;

public class DeliveringOrderController {

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

    public void completeOrderHandler() {
        System.out.println("Order Completed");
        order.completed();

        parentPane.getChildren().remove(myNode);
    }

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    public void setParentPane(TilePane parentPane) {
        this.parentPane = parentPane;
    }



}
