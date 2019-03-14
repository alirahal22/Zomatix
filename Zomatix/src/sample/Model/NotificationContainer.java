package sample.Model;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class NotificationContainer {
    public static void orderAccepted() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Order Accepted");
        tray.setMessage("Your order was Accepted");
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(5));
    }

    public static void orderRejected() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Order Rejected");
        tray.setMessage("Your order was rejected");
        tray.setNotificationType(NotificationType.WARNING);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndWait();
    }

    public static void orderDelivered() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Started Delivery");
        tray.setMessage("Your order is on its way");
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(5));
    }

    public static void orderCompleted() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Order Completed");
        tray.setMessage("Hope you enjoyed your food");
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(5));
    }

    public static void orderCancelled() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Order Cancelled");
        tray.setMessage("Your order was cancelled");
        tray.setNotificationType(NotificationType.NOTICE);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(5));
    }
}
