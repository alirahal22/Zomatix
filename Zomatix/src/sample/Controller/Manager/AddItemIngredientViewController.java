package sample.Controller.Manager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Model.Objects.RestaurantComponents.Composite.Ingredient;

public class AddItemIngredientViewController {

    @FXML
    public TextField nameTextField;

    public void addHandler() {
        System.out.println("Ingredient Add Handler");
        Ingredient ingredient = new Ingredient(nameTextField.getText());
        onIngredientAddListener.onIngredientAddedListener(ingredient);
    }

    public void chooseImage() {

    }

    private OnIngredientAddListener onIngredientAddListener;

    public void setOnIngredientAddListener(OnIngredientAddListener onIngredientAddListener) {
        this.onIngredientAddListener = onIngredientAddListener;
    }

    public interface OnIngredientAddListener {
        void onIngredientAddedListener(Ingredient ingredient);
    }
}
