package sample.Controller.Customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import sample.Model.Database.SQLThread;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Deal;
import sample.View.Cells.DealCell.DealCellFactory;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;
import sample.View.Cells.RestaurantCell.RestaurantCellFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    DatabaseModel model = DatabaseModel.getDatabaseModel();

    public AnchorPane parentAnchorPane;

    @FXML
    public AnchorPane homeAnchorPane;

    @FXML
    public ListView<Restaurant> topRatedRestaurantsListView;

    @FXML
    public ListView<Deal> topDealsListView;
    public void setUpViews(){
        AnchorPane.setTopAnchor(homeAnchorPane, 0.0);
        AnchorPane.setRightAnchor(homeAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(homeAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(homeAnchorPane, 0.0);

        setUpRestaurants();
        setUpTopDeals();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUpRestaurants() {
        new SQLThread() {
            @Override
            public boolean success() {
                ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                Platform.runLater( ()->{
                    model.getRestaurants();
                    restaurantArrayList.addAll(model.restaurantsHashMap.values());
                    topRatedRestaurantsListView.setCellFactory(new RestaurantCellFactory(parentAnchorPane));
                    topRatedRestaurantsListView.setItems(FXCollections.observableArrayList(restaurantArrayList));
                    topRatedRestaurantsListView.setFocusTraversable(false);
                } );

                return true;
            }

            @Override
            public void failure() {

            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    public void setUpTopDeals() {
        new SQLThread() {
            @Override
            public boolean success() {
                ArrayList<Deal> dealsArrayList = new ArrayList<>();
                Platform.runLater( ()->{
                    model.getDeals();
                    dealsArrayList.addAll(model.dealsHashMap.values());
                    topDealsListView.setCellFactory(new DealCellFactory());
                    ObservableList<Deal> deals = FXCollections.observableArrayList(dealsArrayList);
                    topDealsListView.setItems(deals);
                    topDealsListView.setFocusTraversable(false);
                    topDealsListView.setFocusModel(null);
                } );
                return false;
            }

            @Override
            public void failure() {

            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

}
