package sample.Controller.Customer;

import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sample.Model.Objects.RestaurantComponents.Composite.Ingredient;

import java.net.URL;
import java.util.ResourceBundle;

public class IngredientNodeController implements Initializable {
    @FXML
    public JFXToggleButton ingredientToggleButton;
    @FXML
    public Label ingredientNameLabel;

    private OnToggleButtonListener popup;
    private Ingredient ingredient;

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        if (!ingredient.isRemoved())
            ingredientToggleButton.setSelected(true);
        ingredientNameLabel.setText(ingredient.getName());
    }

    public void setPopup(OnToggleButtonListener popup) {
        this.popup = popup;
    }

    @FXML
    public void ingredientToggleButtonHandler() {
        ingredientToggleButton.setText("OFF");
        if (ingredientToggleButton.isSelected())
            ingredientToggleButton.setText("ON");
        ingredient.setRemoved(!ingredient.isRemoved());

        popup.onToggleButtonChangeListener(ingredientToggleButton.isSelected(), ingredient);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public interface OnToggleButtonListener {
        void onToggleButtonChangeListener(boolean isSelected, Ingredient ingredient);
    }

    public void initializeToggleButton() {
        if (ingredient.isRemoved()) {
            ingredientToggleButton.setText("OFF");
        } else {
            ingredientToggleButton.setText("ON");
        }
    }
}
