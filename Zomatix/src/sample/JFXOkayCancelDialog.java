package sample;

import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class JFXOkayCancelDialog {

    private String headingText;
    private String bodyText;
    private String dialogBtnStyle;
    private String okayText;
    private String cancelText;
    private int dialogWidth;
    private int dialogHeight;
    private EventHandler<ActionEvent> okayAction;
    private EventHandler<ActionEvent> cancelAction;
    private Pane pane;

    public JFXOkayCancelDialog(String headingText, String bodyText, String dialogBtnStyle, int dialogWidth,
                               int dialogHeight, EventHandler<ActionEvent> okayAction, EventHandler<ActionEvent> cancelAction, Pane pane,
                               String okayText, String cancelText) {
        this.headingText = headingText;
        this.bodyText = bodyText;
        this.dialogBtnStyle = dialogBtnStyle;
        this.dialogWidth = dialogWidth;
        this.dialogHeight = dialogHeight;
        this.okayAction = okayAction;
        this.cancelAction = cancelAction;
        this.pane = pane;
        this.okayText = okayText;
        this.cancelText = cancelText;
    }

    public JFXOkayCancelDialog(String headingText, String bodyText, String dialogBtnStyle, int dialogWidth,
                               int dialogHeight, EventHandler<ActionEvent> okayAction, EventHandler<ActionEvent> cancelAction, Pane pane,
                               ResourceBundle bundle) {
        this.headingText = headingText;
        this.bodyText = bodyText;
        this.dialogBtnStyle = dialogBtnStyle;
        this.dialogWidth = dialogWidth;
        this.dialogHeight = dialogHeight;
        this.okayAction = okayAction;
        this.cancelAction = cancelAction;
        this.pane = pane;
        //okayText = bundle.getString("okayBtnText");
        //cancelText = bundle.getString("cancelBtnText");
    }

    public void show() {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(headingText));
        content.setBody(new Text(bodyText));
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton okayBtn = new JFXButton(okayText);
        okayBtn.addEventHandler(ActionEvent.ACTION, (e)-> {
            dialog.close();
        });
        okayBtn.addEventHandler(ActionEvent.ACTION, okayAction);
        okayBtn.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        okayBtn.setPrefHeight(32);
        okayBtn.setStyle(dialogBtnStyle);
        JFXButton cancelBtn = new JFXButton(cancelText);
        cancelBtn.addEventHandler(ActionEvent.ACTION, (e)-> {
            dialog.close();
        });
        cancelBtn.addEventHandler(ActionEvent.ACTION, cancelAction);
        cancelBtn.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        cancelBtn.setPrefHeight(32);
        cancelBtn.setStyle(dialogBtnStyle);
        content.setActions(cancelBtn, okayBtn);
        content.setPrefSize(dialogWidth, dialogHeight);
        pane.getChildren().add(stackPane);
        AnchorPane.setTopAnchor(stackPane, (pane.getHeight()-content.getPrefHeight())/2);
        AnchorPane.setLeftAnchor(stackPane, (pane.getWidth()-content.getPrefWidth())/2);
        dialog.show();
    }

    public String getOkayText() {
        return okayText;
    }

    public void setOkayText(String okayText) {
        this.okayText = okayText;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public EventHandler<ActionEvent> getOkayAction() {
        return okayAction;
    }

    public void setOkayAction(EventHandler<ActionEvent> okayAction) {
        this.okayAction = okayAction;
    }

    public EventHandler<ActionEvent> getCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(EventHandler<ActionEvent> cancelAction) {
        this.cancelAction = cancelAction;
    }

}