package sample.Model.Database;

import sample.Model.Authentication;
import sample.Model.Objects.RestaurantComponents.*;
import sample.Model.Objects.RestaurantComponents.Composite.*;
import sample.Model.Objects.Users.Customer;
import sample.Model.Objects.Users.Employee;
import sample.Model.Objects.Users.Manager;
import sample.View.Cells.OrderCell.OrderItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({"Duplicates"})
public class DatabaseModel {
    private static DatabaseModel databaseModel = null; //DatabaseModel is a singleton class

    private DatabaseConnection databaseConnection;

    private Customer activeCustomer;
    private Manager activeManager;

    public enum TableName {
        RESTAURANT, MENU, ITEM, ITEM_INGREDIENT
    }


    private DatabaseModel(){
        databaseConnection = DatabaseConnection.getDatabaseConnection();
        restaurantsHashMap = new HashMap<>();
        dealsHashMap = new HashMap<>();
        complaintsHashMap = new HashMap<>();
    }

    public HashMap<Integer, Restaurant> restaurantsHashMap;
    public HashMap<Integer, Deal> dealsHashMap;
    public HashMap<Integer, Complaint> complaintsHashMap;
    public ArrayList<Employee> employeesArrayList;

    public Customer getActiveCustomer() {
        return activeCustomer;
    }

    public Manager getActiveManager() {
        return activeManager;
    }

    public void setActiveCustomer(Customer activeCustomer) {
        this.activeCustomer = activeCustomer;
    }

    public void setActiveManager(Manager activeManager) {
        this.activeManager = activeManager;
    }

    public static DatabaseModel getDatabaseModel(){ //Get instance method of DatabaseModel class
        if(databaseModel == null){
            databaseModel = new DatabaseModel();
        }
        return databaseModel;
    }

    private ArrayList<RestaurantLeaf> getRestaurantMenus(int restaurantid) {

        ArrayList<RestaurantLeaf> restaurantMenusArrayList = new ArrayList<>();
        try {
            String query = "SELECT * FROM Menu WHERE restaurantid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            ResultSet menuResultSet = preparedStatement.executeQuery();

            int menuid;
            String menuName;
            while (menuResultSet.next()){
                menuid = menuResultSet.getInt("menuid");
                menuName = menuResultSet.getString("name");

                ArrayList<RestaurantLeaf> menuItemArrayList = getMenuItems(menuid);



                Menu menu = new Menu(menuid, menuName, restaurantid, menuItemArrayList);
                restaurantMenusArrayList.add(menu);
            }

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error Fetching Menus");
        }
        return restaurantMenusArrayList;

    }

    private ArrayList<RestaurantLeaf> getMenuItems(int menuid) {

        ArrayList<RestaurantLeaf> menuItemArrayList = new ArrayList<>();

        try {
            String query = "SELECT * FROM ItemView WHERE menuid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, menuid);
            ResultSet itemResultSet = preparedStatement.executeQuery();

            int itemid, itemCategoryid;
            String itemName, itemDescription, categoryName;
            float itemPrice;
            while (itemResultSet.next()) {
                itemid = itemResultSet.getInt("itemid");
                itemCategoryid = itemResultSet.getInt("categoryid");
                itemName = itemResultSet.getString("name");
                itemDescription = itemResultSet.getString("description");
                itemPrice = itemResultSet.getFloat("price");
                categoryName = itemResultSet.getString("category_name");
                ArrayList<RestaurantLeaf> itemIngredientsArrayList = getItemIngredients(itemid);

                MenuItem menuItem = new MenuItem(itemid, itemName, menuid, itemPrice, itemDescription, itemCategoryid, itemIngredientsArrayList);
                menuItem.setCategoryName(categoryName);
                menuItemArrayList.add(menuItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Fetching Items");
        }
        return  menuItemArrayList;
    }

    private ArrayList<RestaurantLeaf> getItemIngredients(int itemid) {


        ArrayList<RestaurantLeaf> ingredientsArrayList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Item_Ingredients WHERE itemid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, itemid);
            ResultSet ingredientsResultSet = preparedStatement.executeQuery();

            while (ingredientsResultSet.next()) {
                String name = ingredientsResultSet.getString("name");
                Ingredient ingredient = new Ingredient(name);
                ingredient.setId(ingredientsResultSet.getInt("ingredientid"));
                ingredientsArrayList.add(ingredient);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error Fetching Ingredients");
        }
        return ingredientsArrayList;
    }

    public void getRestaurants() {
        new SQLThread() {
            @Override
            public boolean success() {
                try {
                    String query = "SELECT * FROM Restaurant";
                    PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
                    ResultSet restaurantResultSet = preparedStatement.executeQuery();

                    int restaurantid;
                    String name, location, phone, description, image_path;
                    float rating;


                    while (restaurantResultSet.next()) {

                        //Getting Restaurant Attributes
                        restaurantid = restaurantResultSet.getInt("restaurantid");
                        name = restaurantResultSet.getString("name");
                        location = restaurantResultSet.getString("location");
                        phone = restaurantResultSet.getString("phone");
                        description = restaurantResultSet.getString("description");
                        image_path = restaurantResultSet.getString("image_path");
                        rating = restaurantResultSet.getFloat("rating");
                        ArrayList<RestaurantLeaf> restaurantMenusArrayList = getRestaurantMenus(restaurantid);



                        ArrayList<Deal> deals = getRestaurantDeals(restaurantid);

                        Restaurant restaurant = new Restaurant(restaurantid, name, location, phone, rating, image_path, description, restaurantMenusArrayList, deals);
                        restaurantsHashMap.put(restaurantid, restaurant);

                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error Fetching Restaurants");
                    return false;
                }
                return true;
            }

            @Override
            public void failure() {
                System.out.println("fail");
            }

            @Override
            public void onFinish() {

            }
        }.run();


        //1 not thread

    }

    public void getDeals() {
        try {
            String query = "SELECT * FROM dealview";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            float newPrice;
            int dealid,itemid;

            while (resultSet.next()) {
                newPrice = resultSet.getFloat("new_price");
                itemid = resultSet.getInt("itemid");
                MenuItem menuItem = getMenuItem(itemid);
                dealid = resultSet.getInt("dealid");

                if (dealsHashMap.containsKey(dealid))
                    continue;

                int restaurantid = resultSet.getInt("restaurantid");

                Deal deal = new Deal(dealid, menuItem, newPrice);
                deal.getMenuItem().setRestaurant(restaurantsHashMap.get(restaurantid));
                dealsHashMap.put(dealid, deal);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<Deal> getRestaurantDeals(int restaurantid) {
        ArrayList<Deal> deals = new ArrayList<>();
        try {
            String query = "SELECT * FROM DealView WHERE restaurantid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            ResultSet resultSet = preparedStatement.executeQuery();

            float newPrice;
            int dealid,itemid;

            while (resultSet.next()) {
                itemid = resultSet.getInt("itemid");
                MenuItem menuItem = getMenuItem(itemid);
                newPrice = resultSet.getFloat("new_price");
                dealid = resultSet.getInt("dealid");

                Deal deal = new Deal(dealid, menuItem, newPrice);
                deals.add(deal);
//                dealsHashMap.put(dealid, deal);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return deals;
    }

    private MenuItem getMenuItem(int itemid) {
        MenuItem menuItem = null;
        try {

            String query = "SELECT * FROM itemview where itemid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,itemid);
            ResultSet itemResultSet = preparedStatement.executeQuery();

            int itemCategoryid, itemMenuid, itemRestaurantid;
            String itemName, itemDescription, categoryName;
            float itemPrice;


            while (itemResultSet.next()) {
                itemMenuid = itemResultSet.getInt("menuid");
                itemCategoryid = itemResultSet.getInt("categoryid");
                itemRestaurantid = itemResultSet.getInt("restaurantid");
                itemName = itemResultSet.getString("name");
                itemDescription = itemResultSet.getString("description");
                itemPrice = itemResultSet.getFloat("price");
                categoryName = itemResultSet.getString("category_name");
                ArrayList<RestaurantLeaf> itemIngredientsArrayList = getItemIngredients(itemid);


                menuItem = new MenuItem(itemid, itemName, itemMenuid, itemPrice, itemDescription, itemCategoryid, itemIngredientsArrayList);
                menuItem.setCategoryName(categoryName);
                menuItem.setRestaurant(restaurantsHashMap.get(itemRestaurantid));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return menuItem;
    }

    public void getComplaints(int restaurantid){
        int complaintid;
        String fullname;
        String email;
        String subject;
        String description;
        int numberOfCustomers;
        String restaurant_name;
        try {
            String query = "SELECT * FROM Complaint c join Restaurant r where c.restaurantid = ? and r.restaurantid = ?;";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            preparedStatement.setInt(2, restaurantid);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                complaintid = resultSet.getInt("complaintid");
                fullname = resultSet.getString("fullname");
                email = resultSet.getString("email");
                subject = resultSet.getString("subject");
                description = resultSet.getString("description");
                numberOfCustomers = resultSet.getInt("numberOfCustomers");
                restaurant_name = resultSet.getString("name");
                Complaint complaint = new Complaint(complaintid, restaurantid, fullname, email, subject, description, numberOfCustomers, restaurant_name);
                complaintsHashMap.put(complaintid, complaint);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ReviewRating> getRatings(int restaurantid){
        ArrayList<ReviewRating> reviewRatingArrayList = new ArrayList<>();
        try {
            String query = "select * from rating where restaurantid = ? order by ratingid desc limit 3;";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int ratingid;
                float rating;
                String review, datetime;

                ratingid = resultSet.getInt("ratingid");
                rating = resultSet.getFloat("rating");
                review = resultSet.getString("review");
                datetime = resultSet.getString("review_datetime");

                ReviewRating reviewRating = new ReviewRating(ratingid, restaurantid, rating, review, datetime);
                reviewRatingArrayList.add(reviewRating);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reviewRatingArrayList;
    }

    public void addComplaint(Complaint complaint){
        try {
            String query = "Insert into Complaint (restaurantid, fullname, email, subject, description, numberOfCustomers)" +
                    " values (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, complaint.getRestaurantid());
            preparedStatement.setString(2, complaint.getFullname());
            preparedStatement.setString(3, complaint.getEmail());
            preparedStatement.setString(4, complaint.getSubject());
            preparedStatement.setString(5, complaint.getDescription());
            preparedStatement.setInt(6, complaint.getNumberOfCustomers());
            preparedStatement.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRating(ReviewRating reviewRating) {
        try {
            boolean reviewed = !reviewRating.getReview().equals("");
            if (reviewed) {
                String query1 = "INSERT INTO RATING (restaurantid, rating, review) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement1 = databaseConnection.getConnection().prepareStatement(query1);
                preparedStatement1.setString(3, reviewRating.getReview());
                preparedStatement1.setInt(1, reviewRating.getRestaurantid());
                preparedStatement1.setFloat(2, reviewRating.getRating());
                preparedStatement1.execute();
                return;
            }

            String query = "INSERT INTO RATING (restaurantid, rating) VALUES (?, ?)";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, reviewRating.getRestaurantid());
            preparedStatement.setFloat(2, reviewRating.getRating());
            preparedStatement.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Manager//////////////////////////////////////

    public void addRestaurant(Restaurant restaurant){
        try {

            String query = "Insert into Restaurant (name, location, phone, description, managerid, image_path)" +
                    " values (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setString(2, restaurant.getLocation());
            preparedStatement.setString(3, restaurant.getPhone());
            preparedStatement.setString(4, restaurant.getDescription());
            preparedStatement.setInt(5, activeManager.getId());
            preparedStatement.setString(6, restaurant.getImage_path());
            preparedStatement.execute();


            //Then create a menu for this restaurant
            restaurant.setId(getLastInsertedId("Restaurant", "restaurantid"));
            addMenu(restaurant.getId());
            ArrayList<RestaurantLeaf> menus = getRestaurantMenus(restaurant.getId());
            restaurant.add(menus.get(0));

            //Then add restaurant to the hashmap for current session
            restaurantsHashMap.put(restaurant.getRestaurantid(), restaurant);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMenu(int restaurantid){
        try {
            String query = "Insert into Menu (restaurantid, name)" +
                    " values (?, ?);";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            preparedStatement.setString(2, "Menu Name");
            preparedStatement.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MenuSection addCategory(String categoryName){
        try {
            String query = "Insert into Category (name)" +
                    " values (?);";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, capitalizeName(categoryName));
            preparedStatement.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MenuSection menuSection = new MenuSection(categoryName, new ArrayList<>());
        menuSection.setCategoryid(getLastInsertedId("category", "categoryid"));
        return menuSection;
    }

    public void addMenuItem(MenuItem menuItem, int menuid){
        try {
            String query = "Insert into Item (name, menuid, price, description, categoryid) values (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setInt(2, menuid);
            preparedStatement.setFloat(3, menuItem.getPrice());
            preparedStatement.setString(4, menuItem.getDescription());
            preparedStatement.setInt(5, menuItem.getCategoryid());
            preparedStatement.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addIngredient(Ingredient ingredient, int itemid) {

        try {
            String query = "Insert Into Item_Ingredients (itemid, name) values (?, ?)";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, itemid);
            preparedStatement.setString(2, ingredient.getName());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ingredient.setId(getLastInsertedId("Item_Ingredients", "ingredientid"));
    }

    public void deleteRestaurant(int restaurantid){
        try {
            String query = "Delete From Restaurant Where restaurantid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(int menuid){
        try {
            String query = "Delete From Menu Where menuid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, menuid);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(int itemid){
        try {
            String query = "Delete From Item Where itemid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, itemid);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIngredient(int ingredientid){
        try {
            String query = "Delete From Item_ingredients Where ingredientid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, ingredientid);
            System.out.println(preparedStatement.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editRestaurantImage(String path, int restaurantid){
        try {
            String query = "Update Restaurant set image_path = ? Where restaurantid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, path);
            preparedStatement.setInt(2, restaurantid);
            preparedStatement.executeUpdate();
            restaurantsHashMap.get(restaurantid).setImage_path(path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getManagerRestaurants() {
        new SQLThread() {
            @Override
            public boolean success() {
                try {
                    String query = "SELECT * FROM Restaurant where managerid = ?";
                    PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
                    preparedStatement.setInt(1, activeManager.getId());
                    ResultSet restaurantResultSet = preparedStatement.executeQuery();

                    int restaurantid;
                    String name, location, phone, description, image_path;
                    float rating;


                    while (restaurantResultSet.next()) {

                        //Getting Restaurant Attributes
                        restaurantid = restaurantResultSet.getInt("restaurantid");
                        name = restaurantResultSet.getString("name");
                        location = restaurantResultSet.getString("location");
                        phone = restaurantResultSet.getString("phone");
                        description = restaurantResultSet.getString("description");
                        image_path = restaurantResultSet.getString("image_path");
                        rating = restaurantResultSet.getFloat("rating");
                        ArrayList<RestaurantLeaf> restaurantMenusArrayList = getRestaurantMenus(restaurantid);



                        ArrayList<Deal> deals = getRestaurantDeals(restaurantid);

                        Restaurant restaurant = new Restaurant(restaurantid, name, location, phone, rating, image_path, description, restaurantMenusArrayList, deals);
                        restaurantsHashMap.put(restaurantid, restaurant);

                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error Fetching Restaurants");
                    return false;
                }
                return true;
            }

            @Override
            public void failure() {
                System.out.println("fail");
            }

            @Override
            public void onFinish() {
                System.out.println("success");

            }
        }.run();
    }

    public void getManagerEmployees() {
        employeesArrayList = new ArrayList<>();
        new SQLThread() {
            @Override
            public boolean success() {
                try {
                    String query = "SELECT * FROM Employee where managerid = ?";
                    PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
                    preparedStatement.setInt(1,activeManager.getId());
                    ResultSet resultSet = preparedStatement.executeQuery();


                    while (resultSet.next()) {

                        Employee employee = new Employee();
                        employee.setId(resultSet.getInt("employeeid"));
                        employee.setRestaurantid(resultSet.getInt("restaurantid"));
                        employee.setName(resultSet.getString("name"));
                        employee.setEmail(resultSet.getString("email"));
                        employee.setPhone(resultSet.getString("phone"));
                        employee.setUsername(resultSet.getString("username"));

                        employeesArrayList.add(employee);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error Fetching Restaurants");
                    return false;
                }
                return true;
            }

            @Override
            public void failure() {
                System.out.println("fail");
            }

            @Override
            public void onFinish() {

            }
        }.run();


        //1 not thread

    }
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Authentication//////////////////////////////////

    public void createCustomer(Customer customer) {
        String query = "insert into Customer (name, username, password, phone, email) values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, capitalizeName(customer.getName())); //Capitalized name
            preparedStatement.setString(2, customer.getUsername());
            preparedStatement.setString(3, customer.getPassword());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createManager(Manager manager) {
        String query = "insert into Manager (name, username, password, phone, email) values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, capitalizeName(manager.getName())); //Capitalized name
            preparedStatement.setString(2, manager.getUsername());
            preparedStatement.setString(3, manager.getPassword());
            preparedStatement.setString(4, manager.getPhone());
            preparedStatement.setString(5, manager.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createEmployee(Employee employee) {
        String query = "insert into Employee (name, username, password, phone, email, restaurantid, managerid) values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, capitalizeName(employee.getName())); //Capitalized name
            preparedStatement.setString(2, employee.getUsername());
            preparedStatement.setString(3, employee.getPassword());
            preparedStatement.setString(4, employee.getPhone());
            preparedStatement.setString(5, employee.getEmail());
            preparedStatement.setInt(6, employee.getRestaurantid());
            preparedStatement.setInt(7, activeManager.getId());
            preparedStatement.executeUpdate();

            employee.setId(getLastInsertedId("Employee", "employeeid"));

            employeesArrayList.add(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteEmployee(int employeeid) {
        try {
            String query = "DELETE from Employee where employeeid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, employeeid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendOrder(Order order) {
        try {
            String query = "INSERT into `Order` (customerid, total_bill, `status`, restaurantid, location) values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, order.getCustomerid());
            preparedStatement.setFloat(2, order.getTotalPrice());
            preparedStatement.setString(3, order.getOrderStatus().name());
            preparedStatement.setString(5, order.getLocation());

            preparedStatement.setInt(4, order.getRestaurantid());
            preparedStatement.execute();

            String orderidQuery = "SELECT orderid from `Order` order by orderid desc limit 1";
            PreparedStatement orderIdStatement = databaseConnection.getConnection().prepareStatement(orderidQuery);
            ResultSet resultSet = orderIdStatement.executeQuery();
            if (resultSet.first())
                order.setOrderid(resultSet.getInt("orderid"));
            sendOrderDetails(order);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendOrderDetails(Order order) {
        for (OrderItem orderItem : order.getItemsArrayList()) {
            try {
                String query = "INSERT into `Order_Items` (orderid, itemid, quantity, `description`) values (?, ?, ?, ?)";
                PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, order.getOrderid());
                preparedStatement.setInt(2, orderItem.getId());
                preparedStatement.setInt(3, orderItem.getQuantity());
                String description = "";
                if (orderItem.getAllChildren().size() != 0) {
                    description = orderItem.getAllChildren().get(0).getName();
                }
                for (int i=1; i<orderItem.getAllChildren().size(); i++) {
                    Ingredient ingredient = (Ingredient) orderItem.getAllChildren().get(i);
                    if (ingredient.isRemoved())
                        description += "&" + ingredient.getName();
                }
                if (description.equals(""))
                    description = "Normal";
                preparedStatement.setString(4, description);
                preparedStatement.execute();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Employee/////////////////////////////////////

    public ArrayList<Order> getRestaurantOrders(int restaurantid, Order.OrderStatus orderStatus) {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        try {
            String query = "SELECT * from Restaurant_Orders_View where restaurantid = ? and status = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, restaurantid);
            preparedStatement.setString(2, orderStatus.name());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderid(resultSet.getInt("orderid"));
                order.setCustomerid(resultSet.getInt("customerid"));
                order.setOrderStatus(orderStatus);
                order.setLocation(resultSet.getString("location"));
                order.setCustomerName(resultSet.getString("name"));

                order.setItemsArrayList(getOrderItems(resultSet.getInt("orderid")));

                orderArrayList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderArrayList;
    }

    public ArrayList<OrderItem> getOrderItems(int orderid) {
        ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
        try {
            String query = "SELECT * from Order_Items_View where orderid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, orderid);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(resultSet.getInt("quantity"));
                orderItem.setName(resultSet.getString("name"));
                orderItem.setDescription(resultSet.getString("description"));

                orderItemArrayList.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItemArrayList;
    }

    public void changeOrderStatus(int orderid, Order.OrderStatus orderStatus) {
        try {
            String query = "Update `Order` set status = ? where orderid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, orderStatus.name());
            preparedStatement.setInt(2, orderid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(MenuItem menuItem) {
        try {
            String query = "Update Item set name = ?, price = ?, description = ? where itemid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setFloat(2, menuItem.getPrice());
            preparedStatement.setString(3, menuItem.getDescription());
            preparedStatement.setInt(4, menuItem.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order getNewOrder(int orderId) {
        try {
            String query = "SELECT * from Restaurant_Orders_View where orderid = ?";

            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                Order order = new Order();
                order.setOrderid(resultSet.getInt("orderid"));
                order.setCustomerid(resultSet.getInt("customerid"));
                order.setOrderStatus(Order.OrderStatus.PENDING);
                order.setLocation(resultSet.getString("location"));
                order.setCustomerName(resultSet.getString("name"));

                order.setItemsArrayList(getOrderItems(resultSet.getInt("orderid")));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int checkNewOrder(int restaurantid) {
        try {
            String query = "SELECT orderid from `Order` where restaurantid = ? order by orderid desc";

            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,restaurantid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                return resultSet.getInt("orderid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Order.OrderStatus checkOrderStatus(int orderid) {
        try {
            String query = "SELECT status from `Order` where orderid = ?";

            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,orderid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                return Order.OrderStatus.valueOf(resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Order.OrderStatus.CANCELLED;
    }




    public int getItemRestaurantId(MenuItem menuItem) {
        //item is a leaf, so it has the menuid as parentid
        try {
            String query = "SELECT restaurantid from Menu where menuid = ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, menuItem.getParentid());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first())
                return resultSet.getInt("restaurantid");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }




    ///Utility Functions

    public static String capitalizeName(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public String formatDate(String s) {
        String[] split = s.split("-");
        String date = split[2] + "-" + split[0] + "-" + split[1];
        return date;
    }

    public int getLastInsertedId(String tableName, String column) {
        String query = "SELECT " + column +
                        " FROM " + tableName +
                        " ORDER BY " + column + " DESC LIMIT 1";

        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first())
            return resultSet.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}























