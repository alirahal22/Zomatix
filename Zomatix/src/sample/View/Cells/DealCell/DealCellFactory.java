package sample.View.Cells.DealCell;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sample.Model.Objects.RestaurantComponents.Deal;

public class DealCellFactory implements Callback<ListView<Deal>, ListCell<Deal>> {
    @Override
    public ListCell<Deal> call(ListView<Deal> param) {
        return new DealCell();
    }
}
