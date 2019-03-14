package sample.Model.Objects.RestaurantComponents;

import sample.Model.Database.DatabaseModel;
import sample.Model.Database.TrackOrderTask;
import sample.Model.Observer.Observable;
import sample.Model.Observer.OrderObserver;
import sample.View.Cells.OrderCell.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Order implements Observable {
    private int orderid;
    private int customerid;
    private int restaurantid;
    private String customerName;
    private String location;
    private ArrayList<OrderItem> itemsArrayList = new ArrayList<>();

    private OrderObserver orderObserver;

    //<restaurantid, order>
    HashMap<Integer, Order> orderHashMap = new HashMap<>();

    private OrderStatus orderStatus = OrderStatus.PENDING;



    public enum OrderStatus {
        PENDING, ACTIVE, REJECTED, COMPLETED, DELIVERING, CANCELLED;
    }

    public Order() {
        //empty constructor
    }

    public Order(OrderObserver orderObserver) {
        this.orderObserver = orderObserver;
    }

    public Order(int orderid, int customerid) {
        this.orderid = orderid;
        this.customerid = customerid;
    }

    public Order(int customerid, int restaurantid, String location) {
        this.customerid = customerid;
        this.restaurantid = restaurantid;
        this.location = location;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLocation() {
        return location;
    }

    public int getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(int restaurantid) {
        this.restaurantid = restaurantid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public ArrayList<OrderItem> getItemsArrayList() {
        return itemsArrayList;
    }

    public float getTotalPrice() {
        float total = 0;
        for (OrderItem orderItem : itemsArrayList)
            total += orderItem.getTotalPrice();
        return total;
    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    //TODO: Send notification to customer that order status was modified
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setItemsArrayList(ArrayList<OrderItem> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }

    public void addItem(OrderItem orderItem){
        itemsArrayList.add(orderItem);
    }

    //TODO: Send each order to its corresponding restaurant
    public void splitOrder() {
        for (OrderItem orderItem: itemsArrayList) {
            if (!orderHashMap.containsKey(orderItem.getRestaurantId())) {
                Order order = new Order(this.customerid, orderItem.getRestaurantId(), this.location);
                order.setOrderObserver(DatabaseModel.getDatabaseModel().getActiveCustomer());
                orderHashMap.put(orderItem.getRestaurantId(), order);
            }
            orderHashMap.get(orderItem.getRestaurantId()).getItemsArrayList().add(orderItem);
        }
    }

    public void checkOut(){
        splitOrder();
        for (Map.Entry<Integer, Order> entry : orderHashMap.entrySet())
        {
            entry.getValue().setOrderObserver(DatabaseModel.getDatabaseModel().getActiveCustomer());
            DatabaseModel.getDatabaseModel().sendOrder(entry.getValue());
            Timer timer = new Timer();
            timer.schedule(new TrackOrderTask(entry.getValue()), 0, 1000);
        }
    }



    public String getOrganizedDescription() {
        String content = "";
        for (OrderItem orderItem : itemsArrayList) {
            String desc[] = orderItem.getDescription().split("&");
            content += orderItem.getQuantity() + " " + orderItem.getName() + "\n";
            for (String ingredient: desc)
                content += "\t*without " + ingredient + "\n";
        }

        return content;
    }

    public void accepted() {
        DatabaseModel.getDatabaseModel().changeOrderStatus(this.orderid, OrderStatus.ACTIVE);
        this.orderStatus = OrderStatus.ACTIVE;
    }

    public void rejected() {
        DatabaseModel.getDatabaseModel().changeOrderStatus(this.orderid, OrderStatus.REJECTED);
        this.orderStatus = OrderStatus.REJECTED;
    }

    public void delivered() {
        DatabaseModel.getDatabaseModel().changeOrderStatus(this.orderid, OrderStatus.DELIVERING);
        this.orderStatus = OrderStatus.DELIVERING;
    }

    public void cancelled() {
        DatabaseModel.getDatabaseModel().changeOrderStatus(this.orderid, OrderStatus.CANCELLED);
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public void completed() {
        DatabaseModel.getDatabaseModel().changeOrderStatus(this.orderid, OrderStatus.COMPLETED);
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public void setOrderObserver(OrderObserver orderObserver) {
        this.orderObserver = orderObserver;
    }

    @Override
    public void notifyChange() {
        orderObserver.update(this.orderStatus);
    }

}
