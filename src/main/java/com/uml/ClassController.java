package com.uml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class ClassController extends Parent{
    @FXML
    public AnchorPane classBody;
    @FXML
    static MainController main;
    static Node button1;
    @FXML
    public Button inClassButton;
    @FXML
    private Label className;
    static int count = 0;

    public void initialize(){
        inClassButton.setOnDragDetected(e -> main.dDetect(e, classBody));
        inClassButton.setOnMouseDragged(e -> main.drag(e, classBody));
    }
    public void setParentController(MainController parentController) {
        main = parentController;
    }

    @FXML
    private void addClassNameAction(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("class name");
        dialog.setHeaderText("Enter class name:");
        dialog.setContentText("Class name:");

        Optional<String> name = dialog.showAndWait();
        name.ifPresent(s -> this.className.setText(s));
    }

    public void classClick(MouseEvent mouseEvent) {
        if(main.isArrowAct()){
            if(count == 0){
                button1 = classBody;
                count++;
            }else if(count == 1){
                main.arrow = main.createAndAddArrow(button1, classBody);
                count = 0;
            }
        }
    }
}
