package sample.Controller.Customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Controller.Manager.AddItemController;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;
import sample.View.Cells.OrderCell.OrderItem;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuItemNodeController implements Initializable, AddItemController.OnItemSubmitListener {

    private MenuItem menuItem;
    private OnItemDeleteListener onItemDeleteListener;

    Stage editItemStage;

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    private FlowPane parent;
    private Pane myNode;

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;

//        menuItemImageView.setImage(new Image("sample/Resources/images/items/burger_and_fries.png"));
        menuItemNameLabel.setText(menuItem.getName());
        menuItemDescriptionLabel.setText(menuItem.getDescription());
        menuItemPriceLabel.setText("" + menuItem.getPrice());
    }

    @FXML
    public ImageView menuItemImageView;

    @FXML
    public Label menuItemNameLabel, menuItemPriceLabel, menuItemDescriptionLabel, quantityLabel;


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

    ///Add Button Handler
    @FXML
    public void addToCartHandler() {
        int quantity = getQuantity();
        if (menuItem.isOrdered()) {
            System.out.println("Item already added, hehehe");
            //TODO: show alert
            return;
        }
        if (quantity != 0) {
            DatabaseModel.getDatabaseModel().getActiveCustomer().getOrder().getItemsArrayList().add(new OrderItem(menuItem, quantity));
            menuItem.setOrdered(true);
        }
    }

    public void setParent(FlowPane parent) {
        this.parent = parent;
    }

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    @FXML
    public void deleteItemHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete menu section");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Deleting menu section will delete all items in it");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() != ButtonType.OK){
            return;
        }
        System.out.println("Deleting item");
        parent.getChildren().remove(myNode);
        onItemDeleteListener.onItemDeletedListener(menuItem, myNode);
        DatabaseModel.getDatabaseModel().deleteItem(menuItem.getId());
        System.out.println("Deleting item with id: " + menuItem.getId());

    }

    @FXML
    public void editIngredientsHandler() {
        System.out.println("Editing ingredients");

        editItemStage = new Stage();

        Pane root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/add_item_view.fxml"));
            root = loader.load();
            AddItemController addItemController = loader.getController();
            addItemController.setMenuItem(menuItem);
            addItemController.setOnItemSubmitListener(this);

            Scene scene = new Scene(root, 647, 460);
            editItemStage.setScene(scene);
            editItemStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public int getQuantity() {
        return Integer.valueOf(quantityLabel.getText());
    }

    @Override
    public void onItemSubmittedListener(MenuItem menuItem) {
        editItemStage.close();
        System.out.println("Closing edit item stage...");
        System.out.println(menuItem.getId() + ": " + menuItem.getName());
        setMenuItem(menuItem);
        DatabaseModel.getDatabaseModel().updateItem(menuItem);
    }


    public interface OnItemDeleteListener {
        void onItemDeletedListener(MenuItem menuItem, Pane node);
    }

}
