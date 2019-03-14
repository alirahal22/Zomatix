package sample.Model.Database;

import sample.Model.Objects.RestaurantComponents.Order;
import sample.Model.Objects.Users.Customer;

import java.util.TimerTask;

public class TrackOrderTask extends TimerTask {



    private Order order;

    public TrackOrderTask(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        Order.OrderStatus newOrderStatus = DatabaseModel.getDatabaseModel().checkOrderStatus(order.getOrderid());
        if (newOrderStatus != order.getOrderStatus()) {
            order.setOrderStatus(newOrderStatus);
            System.out.println(order.getOrderStatus());
            order.notifyChange();
        }
    }
}
