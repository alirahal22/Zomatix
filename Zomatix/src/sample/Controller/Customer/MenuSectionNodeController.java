package sample.Controller.Customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Controller.Manager.AddItemController;
import sample.Main;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Menu;
import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;
import sample.Model.Objects.RestaurantComponents.Composite.Restaurant;
import sample.Model.Objects.RestaurantComponents.MenuSection;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuSectionNodeController implements Initializable, MenuItemNodeController.OnItemDeleteListener, AddItemController.OnItemSubmitListener {

    private Pane mySection;

    private Stage addItemStage;

    private MenuSection menuSection;

    MenuItemNodeController.OnItemDeleteListener onItemDeleteListener;

    public void setOnItemDeleteListener(MenuItemNodeController.OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setMySection(Pane mySection) {
        this.mySection = mySection;
    }

    @FXML
    public AnchorPane menuSectionAnchorPane;

    @FXML
    public FlowPane menuItemsFlowPane;

    @FXML
    public VBox menuSectionVBox;

    @FXML
    public Label menuCategoryLabel;

    @FXML
    public ImageView deleteImageView;

    public MenuSection getMenuSection() {
        return menuSection;
    }

    public void setMenuSection(MenuSection menuSection) {
        this.menuSection = menuSection;

        menuCategoryLabel.setText(menuSection.getCategory());
        setUpFlowPane();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        menuSectionVBox.prefHeightProperty().bind(menuItemsFlowPane.heightProperty());
//        menuSectionVBox.minHeightProperty().bind(menuItemsFlowPane.heightProperty());
//        menuSectionVBox.maxHeightProperty().bind(menuItemsFlowPane.heightProperty());
//        menuItemsFlowPane.prefHeightProperty().bind(menuSectionVBox.heightProperty());

        menuItemsFlowPane.setPrefWidth(800);
        menuItemsFlowPane.setMinWidth(800);
        menuItemsFlowPane.setMaxWidth(1200);

    }

    public void setUpFlowPane() {


        for(MenuItem menuItem : menuSection.getItems()){
            try {
                FXMLLoader loader = new FXMLLoader();
                if (DatabaseModel.getDatabaseModel().getActiveCustomer() != null)
                    loader.setLocation(getClass().getResource("../../View/Nodes/menu_item_node.fxml"));
                else
                    loader.setLocation(getClass().getResource("../../View/Manager/manager_menu_item_node.fxml"));
                Pane pane = loader.load();

                MenuItemNodeController menuItemNodeController = loader.getController();
                menuItemNodeController.setMenuItem(menuItem);
                menuItemNodeController.setOnItemDeleteListener(this);
                menuItemNodeController.setMyNode(pane);
                menuItemNodeController.setParent(menuItemsFlowPane);
                menuItemsFlowPane.getChildren().add(pane);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (DatabaseModel.getDatabaseModel().getActiveManager() != null) {
            if (menuSection.getItems().size() != 0)
                deleteImageView.setVisible(false);
            loadAddRestaurantButton();
        }

    }

    public void loadAddRestaurantButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Manager/manager_add_item_node.fxml"));
            Pane addButtonPane = loader.load();

            ImageView imageView = (ImageView) addButtonPane.getChildren().get(0);
            imageView.setOnMouseClicked( e -> addItemHandler());

            menuItemsFlowPane.getChildren().add(addButtonPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMenuSectionHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete menu section");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Deleting menu section will delete all items in it");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() != ButtonType.OK){
            return;
        }

        System.out.println("Section Delete");
        onMenuSectionDeleteListener.onMenuSectionDeletedListener(mySection);

    }

    public void addItemHandler() {
        System.out.println("Adding menu item");
        addItemStage = new Stage();

        Pane root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/add_item_view.fxml"));
            root = loader.load();
            AddItemController addItemController = loader.getController();
            addItemController.setOnItemSubmitListener(this);
            addItemController.setCategoryid(menuSection.getCategoryid());
            addItemController.disableAddIngredients();

            Scene scene = new Scene(root, 647, 460);
            addItemStage.setScene(scene);
            addItemStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemDeletedListener(MenuItem menuItem, Pane node) {
//        onItemDeleteListener.onItemDeletedListener(menuItem, node);
        menuItemsFlowPane.getChildren().remove(node);
        menuSection.getItems().remove(menuItem);
        if (menuSection.getItems().size() == 0)
            deleteImageView.setVisible(true);
    }

    @Override
    public void onItemSubmittedListener(MenuItem menuItem) {
        System.out.println(menuSection.getMenuid());
        DatabaseModel.getDatabaseModel().addMenuItem(menuItem, menuSection.getMenuid());

        try {
            FXMLLoader loader = new FXMLLoader();
            if (DatabaseModel.getDatabaseModel().getActiveCustomer() != null)
                loader.setLocation(getClass().getResource("../../View/Nodes/menu_item_node.fxml"));
            else
                loader.setLocation(getClass().getResource("../../View/Manager/manager_menu_item_node.fxml"));
            Pane pane = loader.load();

            MenuItemNodeController menuItemNodeController = loader.getController();
            menuItem.setId(DatabaseModel.getDatabaseModel().getLastInsertedId("item", "itemid"));
            menuItemNodeController.setMenuItem(menuItem);
            menuItemNodeController.setOnItemDeleteListener(this);
            menuItemNodeController.setMyNode(pane);
            menuItemNodeController.setParent(menuItemsFlowPane);
            menuItemsFlowPane.getChildren().add(menuItemsFlowPane.getChildren().size() -1 ,pane);

            addItemStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private OnMenuSectionDeleteListener onMenuSectionDeleteListener;

    public void setOnMenuSectionDeleteListener(OnMenuSectionDeleteListener onMenuSectionDeleteListener) {
        this.onMenuSectionDeleteListener = onMenuSectionDeleteListener;
    }

    public interface OnMenuSectionDeleteListener {
        void onMenuSectionDeletedListener(Pane section);
    }
}
