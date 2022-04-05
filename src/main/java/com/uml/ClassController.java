package com.uml;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
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
    public MenuItem Name;
    public MenuItem Arguments;
    public MenuItem Methods;
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

        System.out.println(MainController.tmpNode.getView().getId());

        name.ifPresent(s -> {
            if (MainController.diagram.setNewClassName(MainController.tmpNode.getView().getId(),s)) {
                this.className.setText(s);
                MainController.tmpNode.getView().setId(s);
                System.out.println("id: " + MainController.tmpNode.getView().getId());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("This name was used!");

                alert.showAndWait();
                addClassNameAction(event);
                System.out.println("id: " + MainController.tmpNode.getView().getId());
            }
        });

    }


    public void addArgumentAction(ActionEvent actionEvent) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Argument");
        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox modifier = new ComboBox();
        modifier.getItems().addAll("", "+", "-", "#", "~");
        TextField name = new TextField();
        name.setPromptText("Name");
        TextField type = new TextField();
        type.setPromptText("Type");
        TextField def = new TextField();
        type.setPromptText("value1");

        grid.add(new Label("Modifier :"), 0, 0);
        grid.add(modifier, 0, 1);
        grid.add(new Label("Name :"), 1, 0);
        grid.add(name, 1, 1);
        grid.add(new Label("Type :"), 2, 0);
        grid.add(type, 2, 1);
        grid.add(new Label("Default:"), 3, 0);
        grid.add(def, 3, 1);


        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if(this.classArguments.getText().equals("")){
                    this.classArguments.setText(this.classArguments.getText() + ((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((type.getText().equals("")) ? "" : " : " + type.getText()) + ((def.getText().equals("")) ? "" : " = " + def.getText()));
                }else {
                    this.classArguments.setText(this.classArguments.getText() + "\n" + ((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((type.getText().equals("")) ? "" : " : " + type.getText()) + ((def.getText().equals("")) ? "" : " = " + def.getText()));
                }
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }

    public void addMethod(ActionEvent actionEvent) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Method");
        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox modifier = new ComboBox();
        modifier.getItems().addAll("", "+", "-", "#", "~");
        TextField name = new TextField();
        name.setPromptText("Name");
        TextField parameters = new TextField();
        parameters.setPromptText("names types");
        TextField ret = new TextField();
        ret.setPromptText("names types");

        grid.add(new Label("Modifier :"), 0, 0);
        grid.add(modifier, 0, 1);
        grid.add(new Label("Name :"), 1, 0);
        grid.add(name, 1, 1);
        grid.add(new Label("Type :"), 2, 0);
        grid.add(parameters, 2, 1);
        grid.add(new Label("return:"), 3, 0);
        grid.add(ret, 3, 1);


        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if(this.classMethods.getText().equals("")){
                    this.classMethods.setText(this.classMethods.getText() + ((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((parameters.getText().equals("")) ? "()" : " ( " + parameters.getText() + " ) ") + ((ret.getText().equals("")) ? "" : " : " + ret.getText()));
                }else {
                    this.classArguments.setText(this.classArguments.getText() + "\n" + ((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((parameters.getText().equals("")) ? "()" : " ( " + parameters.getText() + " ) ") + ((ret.getText().equals("")) ? "" : " : " + ret.getText()));
                }
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }
}
