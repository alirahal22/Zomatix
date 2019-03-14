package sample.Model.Objects.Users;

import javafx.application.Platform;
import sample.Model.NotificationContainer;
import sample.Model.Objects.RestaurantComponents.Order;
import sample.Model.Observer.OrderObserver;
import sample.Model.Visitor.UsersVisitor;

public class Customer extends User implements OrderObserver {

    private Order order = new Order(this);

    public Customer(){

    }

    public Customer(String username, String password, String name) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Customer(int customerid, String name, String username, String password, String phone, String email) {
        this.id = customerid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.order.setCustomerid(customerid);
        this.order.setOrderObserver(this);
    }

    public Customer(String name, String username, String password, String phone, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public void accept(UsersVisitor usersVisitor) {
        usersVisitor.visit(this);
    }

    @Override
    public void update(Order.OrderStatus orderStatus) {
        Platform.runLater(() -> {
            switch (orderStatus) {
                case ACTIVE:
                    NotificationContainer.orderAccepted();
                    break;
                case REJECTED:
                    NotificationContainer.orderRejected();
                    break;
                case DELIVERING:
                    NotificationContainer.orderDelivered();
                    break;
                case CANCELLED:
                    NotificationContainer.orderCancelled();
                    break;
                case COMPLETED:
                    NotificationContainer.orderCompleted();
                    break;
                default:
                    System.out.println("Undefined status");
                    break;
            }
        });
    }
}
