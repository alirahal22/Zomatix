package sample.Controller.Employee;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import sample.Model.Database.CheckOrderTask;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.Users.Employee;
import sample.Model.Objects.RestaurantComponents.Order;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

public class EmployeeController implements Initializable,
        PendingOrderController.OnPendingOrderStatusChangeListener,
        ActiveOrderController.OnActiveOrderStatusChangeListener {
    
    private final String PENDING_ORDER_NODE_URL = "../../View/Employee/pending_order_node.fxml";
    private final String ACTIVE_ORDER_NODE_URL = "../../View/Employee/active_order_node.fxml";
    private final String DELIVERING_ORDER_NODE_URL = "../../View/Employee/delivering_order_node.fxml";

    private Employee activeEmployee;

    @FXML
    public ScrollPane pendingOrdersScrollPane, activeOrdersScrollPane, deliveringOrdersScrollPane;

    @FXML
    public TilePane pendingOrdersTilePane, activeOrdersTilePane, deliveringOrdersTilePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setActiveEmployee(Employee activeEmployee) {
        this.activeEmployee = activeEmployee;
        setUpPendingOrders();
        setUpActiveOrders();
        setUpDeliveringOrders();

    }

    public void setUpPendingOrders() {
        pendingOrdersScrollPane.setPannable(true);
        pendingOrdersScrollPane.setFitToHeight(true);
        pendingOrdersScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ArrayList<Order> orders = DatabaseModel.getDatabaseModel().getRestaurantOrders(activeEmployee.getRestaurantid(), Order.OrderStatus.PENDING);
        System.out.println(orders.size());
        for(Order order : orders){
            addOrderNode(order, PENDING_ORDER_NODE_URL);
        }
        int lastOrderId = DatabaseModel.getDatabaseModel().checkNewOrder(activeEmployee.getRestaurantid());

        //setting up the timer to receive live updates
        Timer timer = new Timer();
        timer.schedule(new CheckOrderTask(this, activeEmployee.getRestaurantid(), lastOrderId), 0, 1000);
    }

    public void setUpActiveOrders() {
        activeOrdersScrollPane.setPannable(true);
        activeOrdersScrollPane.setFitToHeight(true);
        activeOrdersScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ArrayList<Order> orders = DatabaseModel.getDatabaseModel().getRestaurantOrders(activeEmployee.getRestaurantid(), Order.OrderStatus.ACTIVE);
        for(Order order : orders)
            addOrderNode(order, ACTIVE_ORDER_NODE_URL);

    }

    public void setUpDeliveringOrders() {
        deliveringOrdersScrollPane.setPannable(true);
        deliveringOrdersScrollPane.setFitToHeight(true);
        deliveringOrdersScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ArrayList<Order> orders = DatabaseModel.getDatabaseModel().getRestaurantOrders(activeEmployee.getRestaurantid(), Order.OrderStatus.DELIVERING);
        System.out.println(orders.size());
        for(Order order : orders)
            addOrderNode(order, DELIVERING_ORDER_NODE_URL);

    }

    public void getNewOrder(int orderId) {
        Order newOrder = DatabaseModel.getDatabaseModel().getNewOrder(orderId);
        if (newOrder != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sendNewOrderNotification();
                }
            });
            addOrderNode(newOrder, PENDING_ORDER_NODE_URL);
        }
    }

    public void addOrderNode(Order order, String nodeURL) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(nodeURL));
            Pane pane = loader.load();

            switch (nodeURL) {
                case PENDING_ORDER_NODE_URL:
                    PendingOrderController pendingOrderController = loader.getController();
                    pendingOrderController.setOrder(order);
                    pendingOrderController.setMyNode(pane);
                    pendingOrderController.setParentPane(pendingOrdersTilePane);
                    pendingOrderController.setEmployeeController(this);
                    Platform.runLater(() ->
                            pendingOrdersTilePane.getChildren().add(pane));
                    pendingOrdersTilePane.setPrefColumns(pendingOrdersTilePane.getPrefColumns() + 1);
                    break;
                case ACTIVE_ORDER_NODE_URL:
                    ActiveOrderController activeOrderController = loader.getController();
                    activeOrderController.setOrder(order);
                    activeOrderController.setMyNode(pane);
                    activeOrderController.setParentPane(activeOrdersTilePane);
                    activeOrderController.setEmployeeController(this);
                    Platform.runLater(() ->
                            activeOrdersTilePane.getChildren().add(pane));
                    activeOrdersTilePane.setPrefColumns(activeOrdersTilePane.getPrefColumns() + 1);
                    break;
                case DELIVERING_ORDER_NODE_URL:
                    DeliveringOrderController deliveringOrderController = loader.getController();
                    deliveringOrderController.setOrder(order);
                    deliveringOrderController.setMyNode(pane);
                    deliveringOrderController.setParentPane(deliveringOrdersTilePane);
                    Platform.runLater(() ->
                            deliveringOrdersTilePane.getChildren().add(pane));
                    deliveringOrdersTilePane.setPrefColumns(deliveringOrdersTilePane.getPrefColumns() + 1);
                    break;
            }

            System.out.println("NEW PANE LOADED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Create alert dialogs to ask for confirmation where necessary

    @Override
    public void onOrderAccepted(Order order) {
        //add the node to the active orders tilepane
        addOrderNode(order, ACTIVE_ORDER_NODE_URL);
        //decrease the size of the pending tilepane
        pendingOrdersTilePane.setPrefColumns(pendingOrdersTilePane.getPrefColumns() - 1);
    }

    public void sendNewOrderNotification() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("New Order");
        tray.setMessage("A new order arrived");
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(5));
    }

    @Override
    public void onOrderRejected() {
        pendingOrdersTilePane.setPrefColumns(pendingOrdersTilePane.getPrefColumns() - 1);
    }

    @Override
    public void onOrderDelivered(Order order) {
        addOrderNode(order, DELIVERING_ORDER_NODE_URL);
        activeOrdersTilePane.setPrefColumns(activeOrdersTilePane.getPrefColumns() - 1);
    }

    @Override
    public void onOrderCancelled() {
        activeOrdersTilePane.setPrefColumns(activeOrdersTilePane.getPrefColumns() - 1);
    }
}
