package sample.Controller.Manager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.Controller.Customer.HomeController;
import sample.JFXOkayCancelDialog;
import sample.Main;
import sample.Model.Objects.Users.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerMainController implements Initializable, ManagerRestaurantsController.OnRestaurantDeleted {


    private Manager activeManager;

    private Selected lastSelected = Selected.RESTAURANTS;

    private Stage mainStage;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public HBox managerEmployeesHBox;

    @FXML
    public HBox managerRestaurantsHBox;

    @FXML
    public HBox managerComplaintsHBox;

    @FXML
    public Label managerUsernameLabel;

    @FXML
    public Label employeesLabel, restaurantLabel, complaintsLabel, aboutUsLabel;

    @FXML
    public ImageView employeesImageView, restaurantsImageView, complaintsImageView, aboutUsImageView;

    @FXML
    public Circle avatar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAvatar();
        try {
            managerRestaurantsHBoxHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestaurantDeleted() {
        try {
            managerRestaurantsHBoxHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum Selected {
        EMPLOYEES, RESTAURANTS, COMPLAINTS
    }

    @FXML
    public void managerEmployeesHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(managerEmployeesHBox);

        employeesImageView.setImage(new Image("sample/Resources/icons/employees.png"));

        employeesLabel.setTextFill(Color.BLACK);

        lastSelected = Selected.EMPLOYEES;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/Manager/manager_employees.fxml"));

        Pane newLoadedPane = null;
        try {
            newLoadedPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        anchorPane.getChildren().setAll(newLoadedPane);

    }

    @FXML
    public void managerRestaurantsHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(managerRestaurantsHBox);

        restaurantsImageView.setImage(new Image("sample/Resources/icons/restaurantLogo.png"));

        restaurantLabel.setTextFill(Color.BLACK);

        lastSelected = Selected.RESTAURANTS;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/Manager/manager_restaurants.fxml"));
        Pane newLoadedPane = loader.load();
        ManagerRestaurantsController managerRestaurantsController = loader.getController();
        //sending the global anchor pane to the Restaurants Controller
        managerRestaurantsController.parentAnchorPane = anchorPane;
        //
        anchorPane.getChildren().setAll(newLoadedPane);

        managerRestaurantsController.setOnRestaurantDeleted(this);
    }

    @FXML
    public void managerComplaintsHBoxHandler() throws IOException {

        toggleOff(lastSelected);
        toggleOn(managerComplaintsHBox);

        complaintsLabel.setTextFill(Color.BLACK);

        complaintsImageView.setImage(new Image("sample/Resources/icons/complain.png"));

        lastSelected = Selected.COMPLAINTS;

        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("../../View/Manager/complaints.fxml"));
        anchorPane.getChildren().setAll(newLoadedPane);
    }


    @FXML
    public void managerAboutUsHBoxHandler(){
        System.out.println("About Us");
    }


    @FXML
    public void managerLogoutHBoxHandler() throws Exception {
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









    public void setAvatar() {
        Image image = new Image("sample/Resources/avatars/1.jpg");
        avatar.setFill(new ImagePattern(image));
        avatar.setEffect(new DropShadow(+25d, -3d, +2d, Color.BLACK));
    }



    public void toggleOff(Selected lastSelected) {
        Background background = new Background(new BackgroundFill(Color.web("0xE50D0D"), null, null));
        switch (lastSelected) {
            case EMPLOYEES:
                managerEmployeesHBox.setBackground(background);
                employeesImageView.setImage(new Image("sample/Resources/icons/employees_white.png"));
                employeesLabel.setTextFill(Color.WHITE);
                break;
            case RESTAURANTS:
                managerRestaurantsHBox.setBackground(background);
                restaurantsImageView.setImage(new Image("sample/Resources/icons/restaurantLogo_white.png"));
                restaurantLabel.setTextFill(Color.WHITE);
                break;
            case COMPLAINTS:
                managerComplaintsHBox.setBackground(background);
                complaintsImageView.setImage(new Image("sample/Resources/icons/complain_white.png"));
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

    public void setActiveManager(Manager activeManager) {
        this.activeManager = activeManager;
        managerUsernameLabel.setText(activeManager.getUsername());
    }

}
