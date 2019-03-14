package sample.Controller.Customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.controlsfx.control.Rating;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.*;
import sample.Model.Objects.RestaurantComponents.Composite.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class RestaurantViewController implements Initializable {

    DatabaseModel model = DatabaseModel.getDatabaseModel();

    public Restaurant restaurant;

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public AnchorPane restaurantViewAnchorPane, scrollPaneAnchorPane;

    @FXML
    public ImageView restaurantViewImageView;

    @FXML
    public Rating customerRating;

    @FXML
    public TextArea reviewTextArea;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @FXML
    public Label restaurantViewNameLabel, restaurantViewPhoneLabel, restaurantViewLocationLabel, restaurantViewRatingLabel;

    @FXML
    public HBox restaurantViewRatingHBox;

    @FXML
    public VBox reviewsVBox;

    @FXML
    public VBox menusVBox;

    @FXML
    public AnchorPane restaurantDetailsAnchorPane, ratingsAnchorPane;


    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;

        System.out.println(this.restaurant.getRating());

        restaurantViewNameLabel.setText(restaurant.getName());
        restaurantViewLocationLabel.setText(restaurant.getLocation());
        restaurantViewPhoneLabel.setText(restaurant.getPhone());
        restaurantViewRatingLabel.setText("" + restaurant.getRating());
        if(restaurant.getImage_path() != null) {
            try {
                restaurantViewImageView.setImage(new Image(restaurant.getImage_path()));
            } catch (IllegalArgumentException e) {
                if (restaurant.getId() % 2 == 0)
                    restaurantViewImageView.setImage(new Image("sample/Resources/images/restaurants/restaurant2.jpg"));
                else
                    restaurantViewImageView.setImage(new Image("sample/Resources/images/restaurants/restaurant1.jpg"));
                System.out.println("Image not found -- in RestaurantViewController --");
            }
        }
        RestaurantsController.displayRating(restaurantViewRatingHBox, restaurant.getRating());
        setUpMenusAnchorPane();
        restaurantViewAnchorPane.getChildren().remove(ratingsAnchorPane);
        menusVBox.getChildren().add(ratingsAnchorPane);
        setUpReviewsVBox();
    }

    public void clear(){
         reviewTextArea.clear();
         reviewTextArea.setPromptText("Enter review here...");
         customerRating.setRating(0);
    }

    @FXML
    public void ratingButtonHandler() {
        int rating = (int) customerRating.getRating();
        String review = reviewTextArea.getText();

        ReviewRating reviewRating = new ReviewRating(restaurant.getRestaurantid(), rating, review);

        model.addRating(reviewRating);
        clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restaurantViewImageView.setPreserveRatio(false);

        AnchorPane.setTopAnchor(restaurantViewAnchorPane, 0.0);
        AnchorPane.setRightAnchor(restaurantViewAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(restaurantViewAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(restaurantViewAnchorPane, 0.0);

        scrollPaneAnchorPane.prefWidthProperty().bind(restaurantViewAnchorPane.widthProperty());
        scrollPaneAnchorPane.prefHeightProperty().bind(restaurantViewAnchorPane.heightProperty());
        restaurantViewImageView.fitWidthProperty().bind(restaurantViewAnchorPane.widthProperty());
        clear();
        scrollPane.setVvalue(0.5);

//        ratingsAnchorPane.layoutYProperty().bind();



    }

    public void setUpReviewsVBox() {
        ArrayList<ReviewRating> reviewRatingArrayList = model.getRatings(restaurant.getRestaurantid());
        for (ReviewRating reviewRating: reviewRatingArrayList){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../../View/Nodes/customer_review_node.fxml"));
                Pane pane = loader.load();
                CustomerReviewNodeController customerReviewNodeController = loader.getController();

                customerReviewNodeController.setReviewRating(reviewRating);
                                reviewsVBox.getChildren().add(pane);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUpMenusAnchorPane() {


        for (Map.Entry<String, ArrayList<MenuItem>> section : ((Menu) restaurant.getAllChildren().get(0)).getMenuSectionsHashMap().entrySet())
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../../View/Nodes/menu_section_node.fxml"));
                Pane pane = loader.load();

                MenuSection menuSection = new MenuSection(section.getKey(), section.getValue());

                MenuSectionNodeController menuSectionNodeController = loader.getController();
                menuSectionNodeController.setMenuSection(menuSection);
                menusVBox.getChildren().add(pane);

                AnchorPane.setTopAnchor(pane,0.0);
                AnchorPane.setRightAnchor(pane,0.0);
                AnchorPane.setBottomAnchor(pane,0.0);
                AnchorPane.setLeftAnchor(pane,0.0);

                VBox.setVgrow(pane, Priority.NEVER);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
}
