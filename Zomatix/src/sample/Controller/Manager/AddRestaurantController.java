package sample.Controller.Manager;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;

public class AddRestaurantController {

    @FXML
    public TextField nameTextField, locationTextField, phoneTextField;

    @FXML
    public TextArea descriptionTextArea;

    private OnRestaurantAddListener onRestaurantAddListener;

    public void setOnRestaurantAddListener(OnRestaurantAddListener onRestaurantAddListener) {
        this.onRestaurantAddListener = onRestaurantAddListener;
    }

    @FXML
    public void addHandler() {
        Restaurant restaurant = new Restaurant();

        restaurant.setName(nameTextField.getText());
        restaurant.setLocation(locationTextField.getText());
        restaurant.setPhone(phoneTextField.getText());
        restaurant.setDescription(descriptionTextArea.getText());

        onRestaurantAddListener.onRestaurantAddedListener(restaurant);
    }

    @FXML void cancelHandler() {
        onRestaurantAddListener.onRestaurantAddCancel();
    }

    public interface OnRestaurantAddListener {
        void onRestaurantAddedListener(Restaurant restaurant);
        void onRestaurantAddCancel();
    }
}
