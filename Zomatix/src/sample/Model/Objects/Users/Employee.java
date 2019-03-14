package sample.Model.Objects.Users;

import sample.Model.Visitor.UsersVisitor;

public class Employee extends User {

    private int restaurantid;

    public void setRestaurantid(int restaurantid) {
        this.restaurantid = restaurantid;
    }

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "restaurantid=" + restaurantid +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Employee(String username, String password, String name, int restaurantid) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.restaurantid = restaurantid;
    }

    public Employee(int customerid, String name, String username, String password, String phone, String email) {
        this.id = customerid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public Employee(String name, String username, String password, String phone, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public int getRestaurantid() {
        return restaurantid;
    }

    public void accept(UsersVisitor usersVisitor) {
        usersVisitor.visit(this);
    }



}

