package sample.Controller.Manager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import sample.Controller.Customer.MenuItemNodeController;
import sample.Controller.Customer.MenuSectionNodeController;
import sample.Controller.Customer.RestaurantsController;
import sample.Main;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Menu;
import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;
import sample.Model.Objects.RestaurantComponents.MenuSection;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;
import sample.Model.Objects.Users.Manager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ManagerRestaurantViewController implements Initializable, MenuItemNodeController.OnItemDeleteListener,
        MenuSectionNodeController.OnMenuSectionDeleteListener {

    private Restaurant restaurant;
    private Pane myNode;


    private OnRestaurantDeleteListener onRestaurantDeleteListener;

    public void setOnRestaurantDeleteListener(OnRestaurantDeleteListener onRestaurantDeleteListener) {
        this.onRestaurantDeleteListener = onRestaurantDeleteListener;
    }

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public AnchorPane restaurantViewAnchorPane, scrollPaneAnchorPane;

    @FXML
    public ImageView restaurantViewImageView;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @FXML
    public Label restaurantViewNameLabel, restaurantViewPhoneLabel, restaurantViewLocationLabel, restaurantViewRatingLabel;

    @FXML
    public HBox restaurantViewRatingHBox;

    @FXML
    public VBox menusVBox;

    @FXML
    public AnchorPane restaurantDetailsAnchorPane;

    @FXML
    public TextField categoryTextField;

    @FXML
    public StackPane changeImageAnchorPane;

    @FXML
    public Pane changeImageDarkPane;

    @FXML
    public ImageView cameraImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restaurantViewImageView.setPreserveRatio(false);

        AnchorPane.setTopAnchor(restaurantViewAnchorPane, 0.0);
        AnchorPane.setRightAnchor(restaurantViewAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(restaurantViewAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(restaurantViewAnchorPane, 0.0);

        changeImageAnchorPane.prefWidthProperty().bind(restaurantViewAnchorPane.widthProperty());
        changeImageDarkPane.prefWidthProperty().bind(restaurantViewAnchorPane.widthProperty());

        scrollPaneAnchorPane.prefWidthProperty().bind(restaurantViewAnchorPane.widthProperty());
        scrollPaneAnchorPane.prefHeightProperty().bind(restaurantViewAnchorPane.heightProperty());
        restaurantViewImageView.fitWidthProperty().bind(restaurantViewAnchorPane.widthProperty());
        scrollPane.setVvalue(0.5);
    }

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;

        restaurantViewNameLabel.setText(restaurant.getName());
        restaurantViewLocationLabel.setText(restaurant.getLocation());
        restaurantViewPhoneLabel.setText(restaurant.getPhone());
        restaurantViewRatingLabel.setText("" + restaurant.getRating());
        if(restaurant.getImage_path() != null) {
            try {
                restaurantViewImageView.setImage(new Image(restaurant.getImage_path()));
            } catch (Exception e) {
                if(restaurant.getId() %2 == 0)
                    restaurantViewImageView.setImage(new Image("sample/Resources/images/restaurants/restaurant2.jpg"));
                else
                    restaurantViewImageView.setImage(new Image("sample/Resources/images/restaurants/restaurant1.jpg"));
                System.out.println("Image not found -- in ManagerRestaurantViewController --");
            }
        }
        RestaurantsController.displayRating(restaurantViewRatingHBox, restaurant.getRating());
        setUpMenusAnchorPane();
    }

    @FXML
    public void changeImageHandler() {
        System.out.println("Changing image");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"), new FileChooser.ExtensionFilter("All files", "*.*"));
        File file = fileChooser.showOpenDialog(Main.primaryStage);

        if(file != null) {
            int i = file.getName().lastIndexOf('.');
            if(i > 0) {
                String extension = "." + file.getName().substring(i + 1);
                String path = "src/sample/Resources/custom_images/" + restaurant.getName() + "_" + restaurant.getLocation() + extension;
                //path = path.replaceAll(" ", "%20");

                File to = new File(path);
                try {
                    Files.copy(file.toPath(), to.toPath(), REPLACE_EXISTING);
                    String image_proj_path = to.getPath().substring(4);
                    DatabaseModel.getDatabaseModel().editRestaurantImage(image_proj_path, restaurant.getId());
                } catch (NoSuchFileException e1){
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void showCamera() {
        changeImageDarkPane.setVisible(true);
        changeImageAnchorPane.setOpacity(1);
    }

    @FXML
    public void hideCamera() {
        changeImageDarkPane.setVisible(false);
        changeImageAnchorPane.setOpacity(0);
    }

    @FXML
    public void addCategoryHandler() {
        if (categoryTextField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field");
            alert.setContentText("Please enter a value");
            alert.showAndWait();
            return;
        }
        MenuSection menuSection = DatabaseModel.getDatabaseModel().addCategory(categoryTextField.getText());

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../View/Manager/manager_menu_section_node.fxml"));
            Pane pane = loader.load();

            menuSection.setCategoryid(menuSection.getCategoryid());
            menuSection.setMenuid(restaurant.getAllChildren().get(0).getId());
            MenuSectionNodeController menuSectionNodeController = loader.getController();
            menuSectionNodeController.setMenuSection(menuSection);
            menuSectionNodeController.setMySection(pane);
            menuSectionNodeController.setOnItemDeleteListener(this);
            menuSectionNodeController.setOnMenuSectionDeleteListener(this);
            menusVBox.getChildren().add(0,pane);


            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);

            VBox.setVgrow(pane, Priority.NEVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpMenusAnchorPane() {


        for (Map.Entry<String, ArrayList<MenuItem>> section : ((Menu) restaurant.getAllChildren().get(0)).getMenuSectionsHashMap().entrySet())
            try {
                if (section.getValue().size() != 0) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../../View/Manager/manager_menu_section_node.fxml"));
                    Pane pane = loader.load();

                    MenuSection menuSection = new MenuSection(section.getKey(), section.getValue());
                    System.out.println();
                    menuSection.setCategoryid(section.getValue().get(0).getCategoryid());
                    menuSection.setMenuid(restaurant.getAllChildren().get(0).getId());
                    MenuSectionNodeController menuSectionNodeController = loader.getController();
                    menuSectionNodeController.setMenuSection(menuSection);
                    menuSectionNodeController.setMySection(pane);
                    menuSectionNodeController.setOnItemDeleteListener(this);
                    menuSectionNodeController.setOnMenuSectionDeleteListener(this);
                    menusVBox.getChildren().add(pane);


                    AnchorPane.setTopAnchor(pane, 0.0);
                    AnchorPane.setRightAnchor(pane, 0.0);
                    AnchorPane.setBottomAnchor(pane, 0.0);
                    AnchorPane.setLeftAnchor(pane, 0.0);

                    VBox.setVgrow(pane, Priority.NEVER);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void submitChangesHandler() {
        System.out.println("Submitting changes");
    }

    public void deleteRestaurantHandler() {
        System.out.println("Deleting restaurant");
        onRestaurantDeleteListener.onRestaurantDeletedListener(restaurant.getId(), myNode);
    }

    @Override
    public void onItemDeletedListener(MenuItem menuItem, Pane node) {
        Menu menu = (Menu) restaurant.getAllChildren().get(0);
        menu.getAllChildren().remove(menuItem);
    }

    @Override
    public void onMenuSectionDeletedListener(Pane section) {
        menusVBox.getChildren().remove(section);
    }

    public interface OnRestaurantDeleteListener {
        void onRestaurantDeletedListener(int restaurantid, Pane pane);
    }
}
