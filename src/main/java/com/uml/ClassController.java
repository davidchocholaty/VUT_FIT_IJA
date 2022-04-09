package com.uml;

import com.uml.classdiagram.*;
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

        UMLClass cls = MainController.diagram.findClass(MainController.tmpNode.getView().getId());

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
        def.setPromptText("value1");

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

                UMLDataType dataType = MainController.diagram.dataTypeForName(type.getText());
                UMLAttribute attr = new UMLAttribute(name.getText(), dataType);

                String visibility = modifier.getValue().toString();

                if (visibility != null) {
                    switch (visibility) {
                        case "+":
                            attr.setVisibility(UMLVisibilityType.PUBLIC);
                            break;
                        case "-":
                            attr.setVisibility(UMLVisibilityType.PRIVATE);
                            break;
                        case "#":
                            attr.setVisibility(UMLVisibilityType.PROTECTED);
                            break;
                        case "~":
                            attr.setVisibility(UMLVisibilityType.PACKAGE);
                            break;
                    }
                }

                if(def.getText().equals("")){
                    attr.setDefaultValue(def.getText());
                }

                if(!cls.addAttribute(attr)) {
                    // TODO Adam - zadani stejneho argumentu - Warning
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

        /* Create method parameters backhand */
        UMLClass cls = MainController.diagram.findClass(MainController.tmpNode.getView().getId());

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
        TextField type = new TextField();
        type.setPromptText("type");

        grid.add(new Label("Modifier :"), 0, 0);
        grid.add(modifier, 0, 1);
        grid.add(new Label("Name :"), 1, 0);
        grid.add(name, 1, 1);
        grid.add(new Label("Parameters :"), 2, 0);
        grid.add(parameters, 2, 1);
        grid.add(new Label("return:"), 3, 0);
        grid.add(type, 3, 1);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if(this.classMethods.getText().equals("")){
                    this.classMethods.setText(this.classMethods.getText() + ((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((parameters.getText().equals("")) ? "()" : " ( " + parameters.getText() + " ) ") + ((type.getText().equals("")) ? "" : " : " + type.getText()));
                }else {
                    this.classArguments.setText(this.classArguments.getText() + "\n" + ((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((parameters.getText().equals("")) ? "()" : " ( " + parameters.getText() + " ) ") + ((type.getText().equals("")) ? "" : " : " + type.getText()));
                }

                UMLDataType retDataType = MainController.diagram.dataTypeForName(type.getText());
                List<UMLAttribute> classAttributes = new ArrayList<UMLAttribute>();

                /* Create method parameters */

                String methodParams = parameters.getText();
                /* Remove all whitespaces and non-visible characters */
                methodParams = methodParams.replaceAll("\\s+","");

                String[] splittedMethodParams = methodParams.split(",");

                for (String currentParam : splittedMethodParams) {
                    if (currentParam.equals("")) {
                        // TODO error - asi Warning - nespravny format parametru (carky mezi nimi)
                    }

                    String[] splittedParam = currentParam.split(":");

                    if (splittedParam.length != 2) {
                        // TODO error - asi Wargning nespravny format parametru (jednoho parametru)
                    } else if(splittedParam[0].equals("")) {
                        // TODO error - asi Warning - nezadane jmeno parametru
                    } else if (splittedParam[1].equals("")) {
                        // TODO error - asi Warning - nezadany datovy typ parametru
                    } else {
                        String paramName = splittedParam[0];
                        String paramDataType = splittedParam[1];

                        UMLDataType dataType = MainController.diagram.dataTypeForName(paramDataType);

                        UMLAttribute attr = new UMLAttribute(paramName, dataType);
                        classAttributes.add(attr);
                    }
                }

                UMLOperation operation = UMLOperation.create(name.getText(), retDataType, classAttributes.toArray(new UMLAttribute[classAttributes.size()]));

                String visibility = modifier.getValue().toString();

                if (visibility != null) {
                    switch (visibility) {
                        case "+":
                            operation.setVisibility(UMLVisibilityType.PUBLIC);
                            break;
                        case "-":
                            operation.setVisibility(UMLVisibilityType.PRIVATE);
                            break;
                        case "#":
                            operation.setVisibility(UMLVisibilityType.PROTECTED);
                            break;
                        case "~":
                            operation.setVisibility(UMLVisibilityType.PACKAGE);
                            break;
                    }
                }

                if(!cls.addOperation(operation)) {
                    // TODO Adam - zadani stejne metody - Warning
                }
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }
}
