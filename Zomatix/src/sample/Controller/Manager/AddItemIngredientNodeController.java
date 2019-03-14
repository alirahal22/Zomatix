package sample.Controller.Manager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Ingredient;
import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;

import java.util.Optional;

public class AddItemIngredientNodeController {

    private Pane myNode;

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    private MenuItem menuItem;

    private OnIngredientDeleteListener onIngredientDeleteListener;


    private Ingredient ingredient;

    @FXML
    public Label nameLabel;

    @FXML
    public void deleteHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ingredient");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Are you sure you want to delete " + ingredient.getName() + " from this item");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() != ButtonType.OK){
            return;
        }

        onIngredientDeleteListener.onIngredientDeletedListener(ingredient, myNode);

    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        nameLabel.setText(ingredient.getName());
    }

    public interface OnIngredientDeleteListener {
        void onIngredientDeletedListener(Ingredient ingredient, Pane node);
    }

    public void setOnIngredientDeleteListener(OnIngredientDeleteListener onIngredientDeleteListener) {
        this.onIngredientDeleteListener = onIngredientDeleteListener;
    }

}
