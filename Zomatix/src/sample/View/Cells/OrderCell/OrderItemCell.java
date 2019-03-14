package sample.View.Cells.OrderCell;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import sample.Controller.Customer.MainController;
import sample.IngredientsPopUp;
import sample.Model.Database.DatabaseModel;

import java.awt.*;
import java.io.IOException;

public class OrderItemCell extends ListCell<OrderItem> {
    public OrderItem orderItem;
    IngredientsPopUp popUp;

    @FXML
    private ImageView itemImageView, plusImageView, minusImageView, deleteImageView, customizeImageView;

    @FXML
    private Label quantityLabel, totalLabel, orderItemNameLabel, pricePerItemLabel;

    public OrderItemCell() {
        loadFXML();
    }

    @FXML
    public void minusHandler(){
        if (orderItem.getQuantity() == 1)
            return;
        orderItem.decreaseQuantity();
        quantityLabel.setText("" + orderItem.getQuantity());
        totalLabel.setText("" + (orderItem.getTotalPrice()) + "$");
    }

    @FXML
    public void plusHandler(){
        orderItem.increaseQuantity();
        quantityLabel.setText("" + orderItem.getQuantity());
        totalLabel.setText("" + (orderItem.getTotalPrice()) + "$");

    }

    @FXML
    public void deleteItemHandler(){
        //OrderItem tmp = this.getItem();
        int index = this.getIndex();
        System.out.println(index);
        DatabaseModel.getDatabaseModel().getActiveCustomer().getOrder().getItemsArrayList().remove(index);
        //OrderController.order.getItemsArrayList().remove(index);

        this.getListView().setItems(FXCollections.observableArrayList(DatabaseModel.getDatabaseModel().getActiveCustomer().getOrder().getItemsArrayList()));
    }



    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart_item_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(OrderItem item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            //setText(null);
            //setGraphic(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            orderItem = item;
//            itemImageView.setImage(new Image("sample/Resources/images/items/Burger-Food-PNG.png"));
            quantityLabel.setText("" + item.getQuantity());
            totalLabel.setText("" + (Integer.parseInt(quantityLabel.getText()) * orderItem.getPrice()) + "$");
            orderItemNameLabel.setText(item.getName());
            if(item.getName().length() > 17)
                orderItemNameLabel.setAlignment(Pos.TOP_LEFT);
            pricePerItemLabel.setText("" + orderItem.getPrice() + "$/meal");

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    @FXML
    public void customizeHandler(){
        if (popUp != null && popUp.isShowing())
            return;
        System.out.println("Customize");
        popUp = new IngredientsPopUp(orderItem);
        Bounds bounds = customizeImageView.localToScreen(customizeImageView.getBoundsInLocal());
        popUp.show(this, bounds.getMinX() - 138, bounds.getMaxY());

    }
}
