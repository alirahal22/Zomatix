package sample.Controller.Login;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FormController implements Initializable {

    private Stage loginStage;

    @FXML
    public AnchorPane formAnchorPane;

    @FXML
    public ImageView formImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLoginForm();
        startSlides();
    }

    public void loadLoginForm() {

        Pane newLoadedPane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Login/form_login.fxml"));
            newLoadedPane = loader.load();
            LoginController loginController = loader.getController();
            loginController.setFormController(this);
            loginController.parentAnchorPane = formAnchorPane;

            formAnchorPane.getChildren().setAll(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeLoginStage() {
        loginStage.close();
    }

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    private void startSlides() {
        Image image1 = formImageView.getImage();
        Image image2 = new Image("sample/Resources/restaurant_login_items/login1.png");
        Image image3 = new Image("sample/Resources/restaurant_login_items/login2.png");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(formImageView.imageProperty(), image1)),
                new KeyFrame(Duration.seconds(2), new KeyValue(formImageView.imageProperty(), image2)),
                new KeyFrame(Duration.seconds(4), new KeyValue(formImageView.imageProperty(), image3)),
                new KeyFrame(Duration.seconds(6), new KeyValue(formImageView.imageProperty(), null))
        );
        timeline.setCycleCount(100);
        timeline.setOnFinished( e -> {
            timeline.play();
        });
        timeline.play();
    }
}
