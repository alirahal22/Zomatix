package sample.Controller.Customer;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.JFXOkayCancelDialog;
import sample.Main;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.Users.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class MainController implements Initializable {

    private Customer activeCustomer;

    private HomeController homeController;

    private Stage mainStage;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public HBox customerHomeHBox;

    @FXML
    public HBox customerRestaurantsHBox;

    @FXML
    public HBox customerOrderHBox;

    @FXML
    public HBox customerComplaintsHBox;

    @FXML
    public Label customerUsernameLabel;

    @FXML
    public Label homeLabel, restaurantLabel, ordersLabel, complaintsLabel, aboutUsLabel;

    @FXML
    public ImageView homeImageView, restaurantImageView, ordersImageView, complaintsImageView, aboutUsImageView;

    @FXML
    public void customerHomeHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(customerHomeHBox);

        homeImageView.setImage(new Image("sample/Resources/icons/home.png"));

        homeLabel.setTextFill(Color.BLACK);

        lastSelected = Selected.HOME;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/Customer/home.fxml"));




        Pane newLoadedPane = null;
        try {
            newLoadedPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        anchorPane.getChildren().setAll(newLoadedPane);

        homeController = loader.getController();
        homeController.parentAnchorPane = anchorPane;
        homeController.setUpViews();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        activeCustomer = DatabaseModel.getDatabaseModel().getActiveCustomer();

        // TODO: Fix This!!

        setAvatar();
        try {
            customerHomeHBoxHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void customerRestaurantsHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(customerRestaurantsHBox);

        ImageView imageView = (ImageView) customerRestaurantsHBox.getChildren().get(0);
        imageView.setImage(new Image("sample/Resources/icons/restaurantLogo.png"));

        restaurantLabel.setTextFill(Color.BLACK);

        lastSelected = Selected.RESTAURANTS;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/Customer/restaurants.fxml"));
        Pane newLoadedPane = loader.load();
        RestaurantsController restaurantsController = loader.getController();
        //sending the global anchor pane to the Restaurants Controller
        restaurantsController.parentAnchorPane = anchorPane;
        //
        anchorPane.getChildren().setAll(newLoadedPane);
    }

    @FXML
    public void customerOrdersHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(customerOrderHBox);

        ImageView imageView = (ImageView) customerOrderHBox.getChildren().get(0);
        imageView.setImage(new Image("sample/Resources/icons/order.png"));

        ordersLabel.setTextFill(Color.BLACK);

        lastSelected = Selected.ORDER;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../../View/Customer/order.fxml"));
        Pane newLoadedPane = loader.load();

        OrderController orderController = loader.getController();
        orderController.setActiveCustomer(activeCustomer);

        anchorPane.getChildren().setAll(newLoadedPane);
    }

    @FXML
    public void customerComplaintsHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(customerComplaintsHBox);

        complaintsLabel.setTextFill(Color.BLACK);

        ImageView imageView = (ImageView) customerComplaintsHBox.getChildren().get(0);
        imageView.setImage(new Image("sample/Resources/icons/complain.png"));

        lastSelected = Selected.COMPLAINT;

        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("../../View/Customer/complaints.fxml"));
        anchorPane.getChildren().setAll(newLoadedPane);
    }

    @FXML
    public void customerAboutUsHBoxHandler(){
        System.out.println("About Us");
    }

    @FXML
    public void customerLogoutHBoxHandler() throws Exception {
        System.out.println("Logout");
        String headingText = "Logout?";
        String bodyText = "Are you sure you want to logout from Zomatix?";
        String dialogBtnStyle = "";
        int dialogWidth = 300;
        int dialogHeight = 70;
        EventHandler<ActionEvent> okayAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //        test_customer = null;
                activeCustomer = null;
                mainStage.close();

                //open login stage
                Main main = new Main();
                Stage stage = new Stage();
                try {
                    main.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        EventHandler<ActionEvent> cancelAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                return;
            }
        };
        String okayText = "OK";
        String cancelText = "CANCEL";

        JFXOkayCancelDialog dialog = new JFXOkayCancelDialog(headingText, bodyText, dialogBtnStyle, dialogWidth, dialogHeight, okayAction, cancelAction, anchorPane, okayText, cancelText);
        dialog.show();

    }

    @FXML
    public Circle avatar;
    private enum Selected {
        HOME, RESTAURANTS, ORDER, COMPLAINT

    }

    private Selected lastSelected = Selected.HOME;

    public void setAvatar() {
        Image image = new Image("sample/Resources/avatars/1.jpg");
        avatar.setFill(new ImagePattern(image));
        avatar.setEffect(new DropShadow(+25d, -3d, +2d, Color.BLACK));
    }

    public void toggleOff(Selected lastSelected) {
        Background background = new Background(new BackgroundFill(Color.web("0xE50D0D"), null, null));
        ImageView imageView;
        switch (lastSelected) {
            case HOME:
                customerHomeHBox.setBackground(background);
                imageView = (ImageView) customerHomeHBox.getChildren().get(0);
                imageView.setImage(new Image("sample/Resources/icons/home_white.png"));
                homeLabel.setTextFill(Color.WHITE);
                break;
            case RESTAURANTS:
                customerRestaurantsHBox.setBackground(background);
                imageView = (ImageView) customerRestaurantsHBox.getChildren().get(0);
                imageView.setImage(new Image("sample/Resources/icons/restaurantLogo_white.png"));
                restaurantLabel.setTextFill(Color.WHITE);
                break;
            case ORDER:
                customerOrderHBox.setBackground(background);
                imageView = (ImageView) customerOrderHBox.getChildren().get(0);
                imageView.setImage(new Image("sample/Resources/icons/order_white.png"));
                ordersLabel.setTextFill(Color.WHITE);
                break;
            case COMPLAINT:
                customerComplaintsHBox.setBackground(background);
                imageView = (ImageView) customerComplaintsHBox.getChildren().get(0);
                imageView.setImage(new Image("sample/Resources/icons/complain_white.png"));
                complaintsLabel.setTextFill(Color.WHITE);
                break;
        }
    }

    public void toggleOn(HBox hBox) {
        Background background = new Background(new BackgroundFill(Color.web("0xFFFFFF"), null, null));
        hBox.setBackground(background);
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setActiveCustomer(Customer activeCustomer) {
        this.activeCustomer = activeCustomer;

        customerUsernameLabel.setText(DatabaseModel.getDatabaseModel().capitalizeName(activeCustomer.getName()));
    }
}
