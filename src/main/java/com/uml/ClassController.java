package com.uml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;

import java.util.Optional;

public class ClassController extends Parent{
    @FXML
    public Pane classBody;
    @FXML
    static MainController main;
    @FXML
    public Button inClassButton;
    public Label classArguments;
    public Label classMethods;
    public ContextMenu contextMenu;
    @FXML
    private Label className;

    public void setParentController(MainController parentController) {
        main = parentController;
    }

    public void initialize(){
        className.getStyleClass().add("classHead");
        classArguments.getStyleClass().add("classBody");
        classMethods.getStyleClass().add("classBody");
    }

    @FXML
    private void addClassNameAction(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("class name");
        dialog.setHeaderText("Enter class name:");
        dialog.setContentText("Class name:");

        Optional<String> name = dialog.showAndWait();

        System.out.println(name.get());

        if (MainController.diagram.setNewClassName(this.getId(), name.get())) {
            name.ifPresent(s -> this.className.setText(s));
            this.setId(name.toString());
            System.out.println("id: " + this.getId());
        } else {
            // TODO
            System.out.println("id: " + this.getId());
        }



    }


    public void addArgumentAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Argument");
        dialog.setHeaderText("Enter Argument:");
        dialog.setContentText("Argument name and type:");

        Optional<String> argument = dialog.showAndWait();
        if(this.classArguments.getText().equals("")){
            argument.ifPresent(s -> this.classArguments.setText(s));
        }else {
            argument.ifPresent(s -> this.classArguments.setText(this.classArguments.getText() + "\n" + s));
        }
    }

    public void addMethod(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Method");
        dialog.setHeaderText("Enter Argument:");
        dialog.setContentText("Method name and type:");

        Optional<String> method = dialog.showAndWait();
        if(this.classMethods.getText().equals("")){
            method.ifPresent(s -> this.classMethods.setText(s));
        }else {
            method.ifPresent(s -> this.classMethods.setText(this.classMethods.getText() + "\n" + s));
        }
    }
}
