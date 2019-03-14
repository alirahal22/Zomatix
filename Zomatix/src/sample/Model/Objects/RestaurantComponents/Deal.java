package sample.Model.Objects.RestaurantComponents;

import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;

public class Deal {
    int dealid;
    private MenuItem menuItem;
    private float newPrice;

    public Deal() {

    }

    public Deal(int dealid, MenuItem menuItem, float newPrice) {
        this.dealid = dealid;
//        this.menuItem = new MenuItem(1,"Deal Menu Item",1, (float)32.99,"Null Menu Item", 1);
        this.menuItem = menuItem;
        this.newPrice = newPrice;
    }

    public MenuItem getMenuItem() {
            return this.menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public float getNewPrice() {
        return this.newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }
}
