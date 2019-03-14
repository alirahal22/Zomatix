package sample.View.Cells.OrderCell;

import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;

public class OrderItem extends MenuItem {
    private int quantity;
    private String description;

    public OrderItem() {

    }

    public OrderItem(int quantity) {
        this.quantity = quantity;
    }

    public OrderItem(MenuItem menuItem, int quantity) {
        super(menuItem.getId(), menuItem.getName(), menuItem.getParentid(), menuItem.getPrice(), menuItem.getDescription(), menuItem.getCategoryid(), menuItem.getAllChildren());
        this.quantity = quantity;

    }

    public OrderItem(MenuItem menuItem, int quantity, String description) {
        super(menuItem.getId(), menuItem.getName(), menuItem.getParentid(), menuItem.getPrice(), menuItem.getDescription(), menuItem.getCategoryid(), menuItem.getAllChildren());
        this.quantity = quantity;
        this.description = description;

    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return quantity * getPrice();
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        if (quantity != 0)
        this.quantity--;
    }




}
