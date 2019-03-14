package sample.Model.Objects.RestaurantComponents;


import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;

public class Complaint {
    private int complaintid;
    private int restaurantid;
    private String fullname;
    private String email;
    private String subject;
    private String description;
    private int numberOfCustomers;

    private String restaurant_name;

    public Complaint(){
        //empty constructor
    }

    public Complaint(int complaintid, int restaurantid, String fullname, String email, String subject, String description, int numberOfCustomers, String restaurant_name) {
        this.complaintid = complaintid;
        this.restaurantid = restaurantid;
        this.fullname = fullname;
        this.email = email;
        this.subject = subject;
        this.description = description;
        this.numberOfCustomers = numberOfCustomers;
        this.restaurant_name = restaurant_name;
    }

    public int getComplaintid() {
        return complaintid;
    }

    public void setComplaintid(int complaintid) {
        this.complaintid = complaintid;
    }

    public int getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(int restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setRestaurant(Restaurant restaurant){
        this.restaurantid = restaurant.getRestaurantid();
        this.restaurant_name = restaurant.getName();
    }
}
