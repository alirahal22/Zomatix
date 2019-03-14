package sample.Controller.Manager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.Database.DatabaseModel;
import sample.Model.Database.SQLThread;
import sample.Model.Objects.Users.Manager;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerRestaurantsController implements Initializable, AddRestaurantController.OnRestaurantAddListener,
        ManagerRestaurantViewController.OnRestaurantDeleteListener {
    DatabaseModel databaseModel = DatabaseModel.getDatabaseModel();
    private Manager activeManager;


    Stage addRestaurantStage;

    @FXML
    public ImageView restaurantImageView;


    @FXML
    public HBox ratingHBox;

    @FXML
    public FlowPane restaurantsFlowPane;

    @FXML
    public AnchorPane restaurantsAnchorPane;

    public AnchorPane parentAnchorPane;

    public static void displayRating(HBox hBox, float rating) {
        //star2 0.5 stars
        //star3 is 1 Star

        if (rating >= 0.26)
            ((ImageView) (hBox.getChildren().get(0))).setImage(new Image("sample/Resources/icons/star2.png"));
        if (rating >= 0.76)
            ((ImageView) (hBox.getChildren().get(0))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 1.26)
            ((ImageView) (hBox.getChildren().get(1))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 1.76)
            ((ImageView) (hBox.getChildren().get(1))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 2.26)
            ((ImageView) (hBox.getChildren().get(2))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 2.76)
            ((ImageView) (hBox.getChildren().get(2))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 3.26)
            ((ImageView) (hBox.getChildren().get(3))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 3.76)
            ((ImageView) (hBox.getChildren().get(3))).setImage(new Image("sample/Resources/icons/star3.png"));

        if (rating >= 4.26)
            ((ImageView) (hBox.getChildren().get(4))).setImage(new Image("sample/Resources/icons/star2.png"));

        if (rating >= 4.76)
            ((ImageView) (hBox.getChildren().get(4))).setImage(new Image("sample/Resources/icons/star3.png"));
    }

    public void setUpNode(Restaurant restaurant, Pane node) {
        //filling the variables with their corresponding nodes from the Node(restaurant_node.fxml)
        ImageView restaurantImage = (ImageView) node.getChildren().get(0);
        HBox ratingHBox = (HBox) node.getChildren().get(1);
        Label restaurantName = (Label) node.getChildren().get(2);
        Label restaurantLocation = (Label) node.getChildren().get(3);
        Label restaurantNumber = (Label) node.getChildren().get(4);
        Label restaurantDescription = (Label) node.getChildren().get(5);
        Label restaurantRatingLabel = (Label) node.getChildren().get(6);

        if (restaurant.getImage_path() != null) {
            try {
                restaurantImage.setImage(new Image(restaurant.getImage_path()));
            } catch (IllegalArgumentException e) {
                if (restaurant.getId() % 2 == 0)
                    restaurantImage.setImage(new Image("sample/Resources/images/restaurants/restaurant2.jpg"));
                else
                    restaurantImage.setImage(new Image("sample/Resources/images/restaurants/restaurant1.jpg"));
                System.out.println("Image not found -- in ManagerRestaurantsController --");
            }
        }
//        else
//            restaurantImage.setImage(new Image("sample/Resources/images/restaurants/restaurant2.jpg"));

        restaurantName.setText(restaurant.getName());
        restaurantLocation.setText(restaurant.getLocation());
        restaurantNumber.setText(restaurant.getPhone());
        restaurantDescription.setText(restaurant.getDescription());
        restaurantRatingLabel.setText("" + restaurant.getRating());

        //Setting the rating of the restaurant

        displayRating(ratingHBox, restaurant.getRating());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeManager = databaseModel.getActiveManager();

        //Setting Restaurants AnchorPane constraints
        AnchorPane.setBottomAnchor(restaurantsAnchorPane, 0.0);
        AnchorPane.setTopAnchor(restaurantsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(restaurantsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(restaurantsAnchorPane, 0.0);

        restaurantsFlowPane.prefWidthProperty().bind(restaurantsAnchorPane.widthProperty());
        restaurantsFlowPane.minHeightProperty().bind(restaurantsAnchorPane.heightProperty());

        restaurantsFlowPane.setPadding(new Insets(8, -50, 0, 15));
        restaurantsFlowPane.setHgap(15);
        restaurantsFlowPane.setVgap(11);

        DatabaseModel.getDatabaseModel().getManagerRestaurants();
        for (Restaurant r : databaseModel.restaurantsHashMap.values()) {
            Pane newLoadedPane;
            try {
                //TODO: maybe make a function??

                newLoadedPane = (AnchorPane) FXMLLoader.load(getClass().getResource("../../View/Nodes/manager_restaurant_node.fxml"));

                setUpNode(r, newLoadedPane);

                //set on click for the whole node.
                newLoadedPane.setOnMouseClicked(event -> {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("View/Manager/manager_restaurant_view.fxml"));

                    Pane restaurantView = null;
                    try {
                        restaurantView = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    parentAnchorPane.getChildren().setAll(restaurantView);

                    ManagerRestaurantViewController restaurantController = loader.getController();
                    restaurantController.setOnRestaurantDeleteListener(this);
                    restaurantController.setRestaurant(r);
                });
                restaurantsFlowPane.getChildren().add(newLoadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadAddRestaurantButton();
    }

    public void loadAddRestaurantButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Manager/manager_add_restaurant_node.fxml"));
            Pane addButtonPane = loader.load();

            ImageView imageView = (ImageView) addButtonPane.getChildren().get(0);
            imageView.setOnMouseClicked( e -> addHandler());

            restaurantsFlowPane.getChildren().add(addButtonPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addHandler() {
        addRestaurantStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/add_restaurant_view.fxml"));
            Pane root = loader.load();

            AddRestaurantController addRestaurantController = loader.getController();
            addRestaurantController.setOnRestaurantAddListener(this);

            Scene scene = new Scene(root, 600, 400);
            addRestaurantStage.setScene(scene);
            addRestaurantStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRestaurantAddedListener(Restaurant restaurant) {
        DatabaseModel.getDatabaseModel().addRestaurant(restaurant);
        try {
            Pane newLoadedPane = (AnchorPane) FXMLLoader.load(getClass().getResource("../../View/Nodes/manager_restaurant_node.fxml"));
            setUpNode(restaurant, newLoadedPane);

            restaurantsFlowPane.getChildren().add(restaurantsFlowPane.getChildren().size() - 1, newLoadedPane);

            newLoadedPane.setOnMouseClicked( event -> {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("View/Manager/manager_restaurant_view.fxml"));

                Pane restaurantView = null;
                try {
                    restaurantView = loader.load();
                    parentAnchorPane.getChildren().setAll(restaurantView);
                    ManagerRestaurantViewController restaurantController = loader.getController();
                    restaurantController.setRestaurant(restaurant);
                    restaurantController.setMyNode(newLoadedPane);
                    restaurantController.setOnRestaurantDeleteListener(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        addRestaurantStage.close();
    }

    @Override
    public void onRestaurantAddCancel() {
        addRestaurantStage.close();
    }

    @Override
    public void onRestaurantDeletedListener(int restaurantid, Pane node) {
        System.out.println("Deleting restaurant from database");
        DatabaseModel.getDatabaseModel().deleteRestaurant(restaurantid);
        restaurantsFlowPane.getChildren().remove(node);
        databaseModel.restaurantsHashMap.remove(restaurantid);
        onRestaurantDeleted.onRestaurantDeleted();

    }

    private OnRestaurantDeleted onRestaurantDeleted;

    public void setOnRestaurantDeleted(OnRestaurantDeleted onRestaurantDeleted) {
        this.onRestaurantDeleted = onRestaurantDeleted;
    }

    public interface OnRestaurantDeleted {
        void onRestaurantDeleted();
    }
}
