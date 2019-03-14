package sample.View.Cells.DealCell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Deal;
import sample.View.Cells.OrderCell.OrderItem;


import java.io.IOException;
import java.util.ArrayList;

public class DealCell extends ListCell<Deal> {

    private Deal deal;

    @FXML
    public ImageView dealImageView;

    @FXML
    public Label dealNameLabel;

    @FXML
    public Label dealNewPriceLabel;

    @FXML
    public Text dealOldPriceTextLabel;

    @FXML
    public Label dealRestaurantNameLabel;

    @FXML
    public Label quantityLabel;

    @FXML
    public Label dealDescriptionLabel;

    public DealCell() {
        loadFXML();
    }

    public void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deal_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Deal item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            deal = item;

            dealImageView.setImage(new Image("sample/Resources/images/items/burger_and_fries.png"));
            dealNameLabel.setText(item.getMenuItem().getName());
            dealNewPriceLabel.setText("" + item.getNewPrice() + "$");
            dealOldPriceTextLabel.setText("" + item.getMenuItem().getPrice() + "$");
            dealRestaurantNameLabel.setText(item.getMenuItem().getRestaurant().getName());
//           dealDescriptionLabel.setText(item.getMenuItem().getDescription());
        }
    }

    ///Add Button Handler
    @FXML
    public void addToCartHandler() {
        if (deal.getMenuItem().isOrdered())
            return;
        if (!deal.getMenuItem().isOrdered() && getQuantity() != 0) {
            deal.getMenuItem().setPrice(deal.getNewPrice());
            DatabaseModel.getDatabaseModel().getActiveCustomer().getOrder().getItemsArrayList().add(new OrderItem(deal.getMenuItem(), getQuantity()));
            deal.getMenuItem().setOrdered(true);
        }
        else if(deal.getMenuItem().isOrdered() && getQuantity() != 0){
            //wont enter this if
            //since getting new MenuItems has isOrdered = false
            System.out.println("ordered deal already");
            int id = deal.getMenuItem().getId();
            ArrayList<OrderItem> orderItems = DatabaseModel.getDatabaseModel().getActiveCustomer().getOrder().getItemsArrayList();
            for(int i = 0; i < orderItems.size(); i++){
                System.out.println("orderID = " + orderItems.get(i).getId() + " id= " + id);
                if(orderItems.get(i).getId() == id) {
                    orderItems.get(i).setQuantity(orderItems.get(i).getQuantity() + getQuantity());
                    return;
                }
            }
        }
        quantityLabel.setText("0");
    }

    @FXML
    public void increaseQuantity() {
        int quantity = getQuantity();
        quantity++;
        quantityLabel.setText("" + quantity);
    }

    @FXML
    public void decreaseQuantity() {
        int quantity = getQuantity();
        if (quantity != 0)
            quantity--;
        quantityLabel.setText("" + quantity);
    }

    public int getQuantity() {
        return Integer.valueOf(quantityLabel.getText());
//        return 2;
    }
}


