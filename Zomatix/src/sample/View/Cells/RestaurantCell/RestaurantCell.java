package sample.View.Cells.RestaurantCell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import sample.Controller.Customer.RestaurantViewController;
import sample.Controller.Customer.RestaurantsController;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class RestaurantCell extends ListCell<Restaurant> implements Initializable {
    DatabaseModel model = DatabaseModel.getDatabaseModel();

    @FXML
    public ImageView restaurantImageView;

    @FXML
    public Label restaurantNameLabel, restaurantLocationLabel, restaurantPhoneLabel, restaurantDescriptionLabel;

    @FXML
    public HBox ratingHBox;

    public AnchorPane parentAnchorPane;

    public RestaurantCell(AnchorPane parentAnchorPane) {
        this.parentAnchorPane = parentAnchorPane;
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurant_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Restaurant item, boolean empty) {
        super.updateItem(item, empty);

        if(empty){
            restaurantDescriptionLabel.setText("");
            restaurantLocationLabel.setText("");
            restaurantPhoneLabel.setText("");
            restaurantNameLabel.setText("");
        }
        else {
            restaurantDescriptionLabel.setText(item.getDescription());
            restaurantLocationLabel.setText(item.getLocation());
            restaurantPhoneLabel.setText(item.getPhone());
            restaurantNameLabel.setText(item.getName());
            if(item.getImage_path() != null) {
                try {
                    restaurantImageView.setImage(new Image(item.getImage_path()));
                } catch (IllegalArgumentException e) {
                    restaurantImageView.setImage(new Image("sample/Resources/images/restaurants/restaurant2.jpg"));

                    System.out.println("Image not found -- in RestaurantCell --");
                }
            }

            RestaurantsController.displayRating(ratingHBox, item.getRating());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        this.setOnMouseClicked(event -> {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../Customer/restaurant_view.fxml"));

            Pane newLoadedPane = null;
            try {
                newLoadedPane = loader.load();
                parentAnchorPane.getChildren().setAll(newLoadedPane);

                RestaurantViewController restaurantController = loader.getController();
                restaurantController.setRestaurant(model.restaurantsHashMap.get(item.getRestaurantid()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
