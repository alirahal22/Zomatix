package sample.Controller.Manager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.RestaurantComponents.Composite.Ingredient;
import sample.Model.Objects.RestaurantComponents.Composite.MenuItem;
import sample.Model.Objects.RestaurantComponents.Composite.RestaurantLeaf;

import java.io.IOException;

public class AddItemController implements AddItemIngredientViewController.OnIngredientAddListener, AddItemIngredientNodeController.OnIngredientDeleteListener {

    Stage addIngredientStage;

    private MenuItem menuItem;
    private int categoryid;

    private OnItemSubmitListener onItemSubmitListener;

    public void setOnItemSubmitListener(OnItemSubmitListener onItemSubmitListener) {
        this.onItemSubmitListener = onItemSubmitListener;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        nameTextField.setText(menuItem.getName());
        descriptionTextArea.setText(menuItem.getDescription());
        priceTextField.setText("" + menuItem.getPrice());
        setUpIngredientsVBox();
    }

    public void setUpIngredientsVBox() {
        ingredientsVBox.getChildren().clear();
        for (RestaurantLeaf restaurantLeaf: menuItem.getAllChildren()) {
            try {
                Ingredient ingredient = (Ingredient) restaurantLeaf;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../../View/Manager/add_item_ingredient_node.fxml"));
                Pane root = loader.load();
                AddItemIngredientNodeController addItemIngredientNodeController = loader.getController();
                addItemIngredientNodeController.setIngredient(ingredient);
                System.out.println(ingredient.getId());
                addItemIngredientNodeController.setOnIngredientDeleteListener(this);
                addItemIngredientNodeController.setMyNode(root);
                ingredientsVBox.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public TextField nameTextField, priceTextField;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public Label submitFirstLabel;

    @FXML
    public VBox ingredientsVBox;

    @FXML
    public Button addIngredientButton;

    @FXML
    public void submitHandler() {
        if (menuItem == null) {
            menuItem = new MenuItem();
        }


        menuItem.setName(nameTextField.getText());
        menuItem.setCategoryid(this.categoryid);
        menuItem.setDescription(descriptionTextArea.getText());
        menuItem.setPrice(Float.parseFloat(priceTextField.getText()));

        onItemSubmitListener.onItemSubmittedListener(menuItem);


    }

    @FXML
    public void addIngredient() {
        addIngredientStage = new Stage();

        Pane root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/add_item_ingredient_view.fxml"));
            root = loader.load();
            AddItemIngredientViewController addItemIngredientViewController = loader.getController();
            addItemIngredientViewController.setOnIngredientAddListener(this);

            Scene scene = new Scene(root, 450, 150);
            addIngredientStage.setScene(scene);
            addIngredientStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editImageHandler() {

    }

    public void disableAddIngredients() {

        addIngredientButton.setDisable(true);
        submitFirstLabel.setVisible(true);

    }

    @Override
    public void onIngredientAddedListener(Ingredient ingredient) {
        System.out.println("Closing ingredient stage...");
        addIngredientStage.close();
        menuItem.getAllChildren().add(ingredient);
        setUpIngredientsVBox();
        DatabaseModel.getDatabaseModel().addIngredient(ingredient, menuItem.getId());
    }

    @Override
    public void onIngredientDeletedListener(Ingredient ingredient, Pane node) {
        ingredientsVBox.getChildren().remove(node);
        menuItem.getAllChildren().remove(ingredient);
        System.out.println("Deleting ingredient " + ingredient.getId());
        DatabaseModel.getDatabaseModel().deleteIngredient(ingredient.getId());
    }

    public interface OnItemSubmitListener {
        void onItemSubmittedListener(MenuItem menuItem);
    }


}
