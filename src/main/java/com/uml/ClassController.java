package com.uml;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.Optional;

public class ClassController extends Parent{
    @FXML
    public Pane classBody;
    @FXML
    static MainController main;
    @FXML
    public Button inClassButton;
    @FXML
    private Label className;

    public void initialize(){
        inClassButton.setOnDragDetected(e -> main.dDetect(e, main.elem));
        inClassButton.setOnMouseDragged(e -> main.drag(e, main.elem));
        inClassButton.setOnMousePressed(e-> main.classClick(e, main.elem));
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

}
