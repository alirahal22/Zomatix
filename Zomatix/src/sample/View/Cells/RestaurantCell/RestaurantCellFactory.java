package sample.View.Cells.RestaurantCell;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;


public class RestaurantCellFactory implements Callback<ListView<Restaurant>, ListCell<Restaurant>> {
    public AnchorPane parentAnchorPane;

    public RestaurantCellFactory(AnchorPane parentAnchorPane) {
        this.parentAnchorPane = parentAnchorPane;
    }

    @Override
    public ListCell<Restaurant> call(ListView<Restaurant> param) {
        return new RestaurantCell(parentAnchorPane);
    }
}
