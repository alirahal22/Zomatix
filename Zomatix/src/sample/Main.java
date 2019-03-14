package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Controller.Login.FormController;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("View/Login/login.fxml"));
        Pane root = loader.load();
        FormController formController = loader.getController();
        formController.setLoginStage(primaryStage);

        //remove root background
        root.setBackground(null);

        Scene scene = new Scene(root, 1020, 650);
        scene.getStylesheets().add("sample/style.css");
        //make scene transparent
        scene.setFill(Color.TRANSPARENT);

        //remove stage borders
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Zomatix");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
