package sample.Model.Objects.RestaurantComponents.Composite;


import sample.Model.Objects.RestaurantComponents.Deal;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Restaurant extends RestaurantComponent {
    private String location;
    private String phone;
    private float rating;
    private String image_path;
    private String description;

    private ArrayList<Deal> deals;

    public Restaurant() {

    }

    public Restaurant(int restaurantid, String name, String location, String phone, float rating, String image_path, String description, ArrayList<RestaurantLeaf> menuArrayList, ArrayList<Deal> deals) {
        this.id = restaurantid;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.rating = rating;
        this.image_path = image_path;
        this.description = description;
        this.children = menuArrayList;
        this.deals = deals;
    }

    public Restaurant(int restaurantid, String name, String location, String phone, float rating, String image_path, String description, ArrayList<RestaurantLeaf> menuArrayList) {
        this.id = restaurantid;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.rating = rating;
        this.image_path = image_path;
        this.description = description;
        this.children = menuArrayList;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getRestaurantid() {
        return id;
    }

    public void setRestaurantid(int restaurantid) {
        this.id = restaurantid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getRating() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Float.valueOf(decimalFormat.format(rating));
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<Deal> getDeals() {
        return deals;
    }

    public void setDeals(ArrayList<Deal> deals) {
        this.deals = deals;
    }


    @Override
    public void add(RestaurantLeaf restaurantLeaf) {
        super.add(restaurantLeaf);
        restaurantLeaf.setParentid(this.id);
        databaseModel.addMenu(id);

    }

    @Override
    public void delete(RestaurantLeaf restaurantLeaf) {
        super.delete(restaurantLeaf);
        databaseModel.deleteMenu(((Menu)restaurantLeaf).getId());
    }
}
