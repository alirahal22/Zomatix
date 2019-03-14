package sample.Controller.Manager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.Authentication;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.Users.Employee;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerEmployeesController implements Initializable, ManagerEmployeeNodeController.OnEmployeeFireListener,
        AddEmployeeViewController.OnEmployeeAddListener {

    private Stage addEmployeeStage;

    @FXML
    public AnchorPane employeesAnchorPane;

    @FXML
    public ScrollPane employeesScrollPane;

    @FXML
    public FlowPane employeesFlowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DatabaseModel.getDatabaseModel().getManagerEmployees();

        AnchorPane.setBottomAnchor(employeesAnchorPane, 0.0);
        AnchorPane.setTopAnchor(employeesAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(employeesAnchorPane, 0.0);
        AnchorPane.setRightAnchor(employeesAnchorPane, 0.0);

        employeesFlowPane.prefWidthProperty().bind(employeesAnchorPane.widthProperty());
        employeesFlowPane.minHeightProperty().bind(employeesAnchorPane.heightProperty());

        employeesFlowPane.setPadding(new Insets(8, -50, 0, 15));
        employeesFlowPane.setHgap(15);
        employeesFlowPane.setVgap(11);

        try {
            for (Employee employee : DatabaseModel.getDatabaseModel().employeesArrayList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/manager_employee_node.fxml"));
                Pane newNode = loader.load();

                ManagerEmployeeNodeController managerEmployeeNodeController = loader.getController();
                managerEmployeeNodeController.setEmployee(employee);
                managerEmployeeNodeController.setMyNode(newNode);
                managerEmployeeNodeController.setOnEmployeeFireListener(this);

                employeesFlowPane.getChildren().add(newNode);
            }
            loadAddRestaurantButton();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        loadAddRestaurantButton();

    }

    public void loadAddRestaurantButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Manager/manager_add_restaurant_node.fxml"));
            Pane addButtonPane = loader.load();

            ImageView imageView = (ImageView) addButtonPane.getChildren().get(0);
            imageView.setOnMouseClicked( e -> addHandler());

            employeesFlowPane.getChildren().add(addButtonPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addHandler() {
        System.out.println("Adding Employee");
        addEmployeeStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/add_employee_view.fxml"));
            Pane root = loader.load();

            AddEmployeeViewController addEmployeeViewController = loader.getController();
            addEmployeeViewController.setOnEmployeeAddListener(this);

            Scene scene = new Scene(root, 1000,547);
            addEmployeeStage.setScene(scene);
            addEmployeeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEmployeeFiredListener(Employee employee, Pane node) {
        employeesFlowPane.getChildren().remove(node);
        DatabaseModel.getDatabaseModel().deleteEmployee(employee.getId());
    }

    @Override
    public void onEmployeeAddedListener(Employee employee) {
        addEmployeeStage.close();
        DatabaseModel.getDatabaseModel().createEmployee(employee);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/manager_employee_node.fxml"));
            Pane newNode = loader.load();

            ManagerEmployeeNodeController managerEmployeeNodeController = loader.getController();
            managerEmployeeNodeController.setEmployee(employee);
            managerEmployeeNodeController.setMyNode(newNode);
            managerEmployeeNodeController.setOnEmployeeFireListener(this);

            employeesFlowPane.getChildren().add(employeesFlowPane.getChildren().size() - 1, newNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Authentication.sendRegistrationMail(employee);

    }

    @Override
    public void onCancel() {
        addEmployeeStage.close();
    }
}
