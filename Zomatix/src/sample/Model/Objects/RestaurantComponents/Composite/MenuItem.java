package sample.Model.Objects.RestaurantComponents.Composite;

import sample.Model.Database.DatabaseModel;

import java.util.ArrayList;

public class MenuItem extends RestaurantComponent {
    private float price;
    private Restaurant restaurant;
    private String description;
    private int categoryid;
    private String categoryName;

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    private boolean isOrdered = false;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public MenuItem() {

    }

    public MenuItem(int itemid, String name, int menuid, float price, String description, int categoryid, ArrayList<RestaurantLeaf> ingredientsArrayList) {
        this.id = itemid;
        this.name = name;
        this.parentid = menuid;
        this.price = price;
        this.description = description;
        this.categoryid = categoryid;
        this.children = ingredientsArrayList;
    }

    public MenuItem(int itemid, String name, int menuid, float price, String description, int categoryid) {
        this.id = itemid;
        this.name = name;
        this.parentid = menuid;
        this.price = price;
        this.description = description;
        this.categoryid = categoryid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    @Override
    public void add(RestaurantLeaf restaurantLeaf) {
        super.add(restaurantLeaf);
        databaseModel.addIngredient((Ingredient) restaurantLeaf, getParentid());
    }

    @Override
    public void delete(RestaurantLeaf restaurantLeaf) {
        super.delete(restaurantLeaf);
        databaseModel.deleteIngredient(restaurantLeaf.getId());
    }

    public int getRestaurantId() {
        return DatabaseModel.getDatabaseModel().getItemRestaurantId(this);
    }


}
