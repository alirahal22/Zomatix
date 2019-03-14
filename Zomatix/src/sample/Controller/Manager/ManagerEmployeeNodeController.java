package sample.Controller.Manager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.JFXOkayCancelDialog;
import sample.Main;
import sample.Model.Objects.Users.Employee;

public class ManagerEmployeeNodeController {

    private Employee employee;
    private Pane myNode;

    private OnEmployeeFireListener onEmployeeFireListener;

    @FXML
    public Label usernameLabel, nameLabel, phoneLabel, emailLabel;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public void deleteHandler() {
        System.out.println("Logout");
        String headingText = "Logout?";
        String bodyText = "Are you sure you want to fire this employee?";
        String dialogBtnStyle = "";
        int dialogWidth = 300;
        int dialogHeight = 70;
        EventHandler<ActionEvent> okayAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onEmployeeFireListener.onEmployeeFiredListener(employee, myNode);
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

    public void setMyNode(Pane myNode) {
        this.myNode = myNode;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        usernameLabel.setText(employee.getUsername());
        nameLabel.setText(employee.getName());
        phoneLabel.setText(employee.getPhone());
        emailLabel.setText(employee.getEmail());
    }

    public void setOnEmployeeFireListener(OnEmployeeFireListener onEmployeeFireListener) {
        this.onEmployeeFireListener = onEmployeeFireListener;
    }

    public interface OnEmployeeFireListener {
        void onEmployeeFiredListener(Employee employee, Pane node);
    }
}
