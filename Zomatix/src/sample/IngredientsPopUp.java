package sample;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import sample.Controller.Customer.IngredientNodeController;
import sample.Model.Objects.RestaurantComponents.Composite.Ingredient;
import sample.View.Cells.OrderCell.OrderItem;

import java.io.IOException;
import java.util.ArrayList;

public class IngredientsPopUp extends Popup implements IngredientNodeController.OnToggleButtonListener {

    OrderItem orderItem;
    VBox root;

    ArrayList<IngredientNodeController> controllers = new ArrayList<>();

    public IngredientsPopUp(OrderItem orderItem) {
        this.orderItem = orderItem;

        try {
            root = FXMLLoader.load(getClass().getResource("View/Nodes/ingredients_popup.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.setAlignment(Pos.BOTTOM_CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        for (int i=0; i<orderItem.getAllChildren().size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("./View/Nodes/ingredient_node.fxml"));
                Pane newLoadedNode = loader.load();
                IngredientNodeController ingredientNodeController = loader.getController();
                controllers.add(ingredientNodeController);
                ingredientNodeController.setPopup(this);
                ingredientNodeController.setIngredient((Ingredient) orderItem.getAllChildren().get(i));

                root.getChildren().add(newLoadedNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getContent().add(root);

        setAutoHide(true);
        setHideOnEscape(true);
    }

    @Override
    public void onToggleButtonChangeListener(boolean isSelected, Ingredient ingredient) {
        System.out.println("Listened Successfully");
        if (!isSelected)
//            orderItem.getRemovedIngredients().add(ingredient);
            System.out.println("Removed " + ingredient.getName());
        if (isSelected)
//            orderItem.getRemovedIngredients().remove(ingredient);
            System.out.println("Added " + ingredient.getName());
    }


    @Override
    public void show(Node ownerNode, double anchorX, double anchorY) {
        if (orderItem.getAllChildren().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No ingredients");
            alert.setHeaderText("The restaurant owner hasn't specified any ingredients");
            alert.setContentText("Don't be picky!!");
            alert.showAndWait();
            return;
        }
         super.show(ownerNode, anchorX, anchorY);
        for(int i=0; i<controllers.size(); i++) {
            controllers.get(i).initializeToggleButton();
        }

    }
}
