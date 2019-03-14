package sample.Model.Objects.RestaurantComponents.Composite;

import sample.Model.Database.DatabaseModel;

import java.util.ArrayList;

public abstract class RestaurantComponent extends RestaurantLeaf{

    protected DatabaseModel databaseModel = DatabaseModel.getDatabaseModel();


    protected ArrayList<RestaurantLeaf> children = new ArrayList<>();

    public void add(RestaurantLeaf restaurantLeaf) {
        children.add(restaurantLeaf);
    }

    public void delete(RestaurantLeaf restaurantLeaf) {
        children.remove(restaurantLeaf);
    }

    public void clear() {
        children.clear();
    }

    public ArrayList<RestaurantLeaf> getAllChildren() {
        return children;
    }
}
