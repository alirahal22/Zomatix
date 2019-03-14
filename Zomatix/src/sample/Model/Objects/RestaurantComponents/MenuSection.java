package sample.Model.Objects.RestaurantComponents;

import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;

import java.util.ArrayList;

public class MenuSection {
    private String category;
    private int categoryid;
    private int menuid;
    private ArrayList<MenuItem> items;

    public MenuSection(String category, ArrayList<MenuItem> items) {
        this.category = category;
        this.items = items;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public int getMenuid() {
        return menuid;
    }

    public void setMenuid(int menuid) {
        this.menuid = menuid;
    }
}
