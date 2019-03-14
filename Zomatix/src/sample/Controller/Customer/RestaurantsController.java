package sample.Controller.Customer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import sample.Model.Database.SQLThread;
import sample.Main;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class RestaurantsController implements Initializable {
    DatabaseModel model = DatabaseModel.getDatabaseModel();

    @FXML
    public ImageView restaurantImageView;

    @FXML
    public Label restaurantNameLabel, restaurantLocationLabel, restaurantPhoneLabel, restaurantDescriptionLabel;

    @FXML
    public HBox ratingHBox;

    @FXML
    public FlowPane restaurantsFlowPane;

    @FXML
    public AnchorPane restaurantsAnchorPane;

    public AnchorPane parentAnchorPane;

    public static void displayRating(HBox hBox ,float rating) {
        //star2 0.5 stars
        //star3 is 1 Star

        if (rating >= 0.26)
            ((ImageView)(hBox.getChildren().get(0))).setImage(new Image("sample/Resources/icons/star2.png"));
        if (rating >= 0.76)
            ((ImageView)(hBox.getChildren().get(0))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 1.26)
            ((ImageView)(hBox.getChildren().get(1))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 1.76)
            ((ImageView)(hBox.getChildren().get(1))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 2.26)
            ((ImageView)(hBox.getChildren().get(2))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 2.76)
            ((ImageView)(hBox.getChildren().get(2))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 3.26)
            ((ImageView)(hBox.getChildren().get(3))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 3.76)
            ((ImageView)(hBox.getChildren().get(3))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 4.26)
            ((ImageView)(hBox.getChildren().get(4))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 4.76)
            ((ImageView)(hBox.getChildren().get(4))).setImage(new Image("sample/Resources/icons/star3.png"));
    }

    public void setUpNode(Restaurant restaurant, Pane node){
        //filling the variables with their corresponding nodes from the Node(restaurant_node.fxml)
        ImageView restaurantImage = (ImageView) node.getChildren().get(0);
        HBox ratingHBox = (HBox) node.getChildren().get(1);
        Label restaurantName = (Label) node.getChildren().get(2);
        Label restaurantLocation = (Label) node.getChildren().get(3);
        Label restaurantNumber = (Label) node.getChildren().get(4);
        Label restaurantDescription = (Label) node.getChildren().get(5);
        Label restaurantRatingLabel = (Label) node.getChildren().get(6);

//        if(restaurant.getRestaurantid() % 2 == 0)
//            restaurantImage.setImage(new Image("sample/Resources/images/restaurants/restaurant1.jpg"));
//        else
        if (restaurant.getImage_path() != null) {
            try {
                restaurantImage.setImage(new Image(restaurant.getImage_path()));
            } catch (Exception e){
                if(restaurant.getId() %2 == 0)
                    restaurantImage.setImage(new Image("sample/Resources/images/restaurants/restaurant2.jpg"));
                else
                    restaurantImage.setImage(new Image("sample/Resources/images/restaurants/restaurant1.jpg"));
                System.out.println("Image not found -- in RestaurantsController --");
            }
        }

        restaurantName.setText(restaurant.getName());
        restaurantLocation.setText(restaurant.getLocation());
        restaurantNumber.setText(restaurant.getPhone());
        restaurantDescription.setText(restaurant.getDescription());
        restaurantRatingLabel.setText("" +restaurant.getRating());

        //Setting the rating of the restaurant

        displayRating(ratingHBox, restaurant.getRating());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setting Restaurants AnchorPane constraints
        AnchorPane.setBottomAnchor(restaurantsAnchorPane, 0.0);
        AnchorPane.setTopAnchor(restaurantsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(restaurantsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(restaurantsAnchorPane, 0.0);

        restaurantsFlowPane.prefWidthProperty().bind(restaurantsAnchorPane.widthProperty());
        restaurantsFlowPane.setPadding(new Insets(8, -50, 0, 15));
        restaurantsFlowPane.setHgap(15);
        restaurantsFlowPane.setVgap(11);

            for(Restaurant r : model.restaurantsHashMap.values()) {
                new SQLThread() {
                    @Override
                    public boolean success() {
                        Pane newLoadedPane;
                        try {
                            newLoadedPane = (AnchorPane) FXMLLoader.load(getClass().getResource("../../View/Nodes/restaurant_node.fxml"));

                        setUpNode(r, newLoadedPane);
                        newLoadedPane.setOnMouseClicked( event -> {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../../View/Customer/restaurant_view.fxml"));

                            Pane restaurantView = null;
                            try {
                                restaurantView = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            parentAnchorPane.getChildren().setAll(restaurantView);

                            RestaurantViewController restaurantController = loader.getController();
                            restaurantController.setRestaurant(r);
                        });
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    restaurantsFlowPane.getChildren().add(newLoadedPane);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public void failure() {
                        System.out.println("faillll");
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();



            }

    }

}
