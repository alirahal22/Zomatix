package sample.View.Cells.OrderCell;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class OrderItemCellFactory implements Callback<ListView<OrderItem>, ListCell<OrderItem>> {

    @Override
    public ListCell<OrderItem> call(ListView<OrderItem> param) {
        return new OrderItemCell();
    }
}
