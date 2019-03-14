package sample.Model.Database;

import sample.Controller.Employee.EmployeeController;
import sample.Model.Database.DatabaseModel;

import java.util.TimerTask;

public class CheckOrderTask extends TimerTask {

    private EmployeeController employeeController;
    private int restaurantId;
    private int lastOrderId;

    public CheckOrderTask(EmployeeController employeeController, int restaurantId, int lastOrderId) {
        this.employeeController = employeeController;
        this.restaurantId = restaurantId;
        this.lastOrderId = lastOrderId;
    }

    @Override
    public void run() {
        int id = DatabaseModel.getDatabaseModel().checkNewOrder(restaurantId);
        if (lastOrderId < id) {
            lastOrderId = id;
            employeeController.getNewOrder(lastOrderId);
        }
    }
}
