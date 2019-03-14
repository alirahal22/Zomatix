package sample.Model.Visitor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import sample.Controller.Customer.MainController;
import sample.Controller.Employee.EmployeeController;
import sample.Controller.Manager.ManagerMainController;
import sample.Model.Database.DatabaseModel;
import sample.Model.Objects.Users.Customer;
import sample.Model.Objects.Users.Employee;
import sample.Model.Objects.Users.Manager;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import javax.management.Notification;
import java.io.IOException;

public class LoginVisitor implements UsersVisitor {
    @Override
    public void visit(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Employee/employee_dashboard.fxml"));
            Pane root = loader.load();
            EmployeeController employeeController = loader.getController();
            employeeController.setActiveEmployee(employee);

            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root, 1200, 960));
            mainStage.setTitle("Zomatix");
            mainStage.setMinHeight(940);
            mainStage.setMinWidth(900);
            mainStage.getScene().getStylesheets().add("sample/style.css");
            mainStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Manager manager) {
        try {

            DatabaseModel.getDatabaseModel().setActiveManager(manager);
            DatabaseModel.getDatabaseModel().getManagerRestaurants();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Manager/manager_main.fxml"));
            Pane root = loader.load();
            ManagerMainController managerMainController = loader.getController();
            Stage mainStage = new Stage();

            //pass the new stage and the currentCustomer for the controller
            managerMainController.setMainStage(mainStage);
            managerMainController.setActiveManager(manager);

            mainStage.setScene(new Scene(root, 1275, 720));
            mainStage.setTitle("Zomatix");
            mainStage.setMinHeight(702);
            mainStage.setMinWidth(1050);
            mainStage.getScene().getStylesheets().add("sample/style.css");
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Customer customer) {
        try {

            customer.getOrder().setCustomerid(customer.getId());
            DatabaseModel.getDatabaseModel().setActiveCustomer(customer);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../View/Customer/main.fxml"));
            Pane root = loader.load();
            MainController mainController = loader.getController();
            Stage mainStage = new Stage();

            //pass the new stage and the currentCustomer for the controller
            mainController.setMainStage(mainStage);
            mainController.setActiveCustomer(customer);

            mainStage.setScene(new Scene(root, 1275, 720));
            mainStage.setTitle("Zomatix");
            mainStage.setMinHeight(702);
            mainStage.setMinWidth(1050);
            mainStage.getScene().getStylesheets().add("sample/style.css");
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
