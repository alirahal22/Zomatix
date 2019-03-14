package sample.Model.Observer;

import sample.Model.Objects.RestaurantComponents.Order;

public interface OrderObserver {
    void update(Order.OrderStatus orderStatus);
}
