/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing class controller for frontend.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import com.uml.classdiagram.*;
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

/**
 * Class represents controller for classes on frontend.
 */
public class ClassController extends Parent{
    @FXML
    public Pane classBody;
    private MainController main;
    @FXML
    public Button inClassButton;
    public Pane classArguments;
    public double classArgumentPosition = 0.0;
    public Pane classMethods;
    public double classMethodsPosition = 0.0;
    public ContextMenu contextMenu;
    public MenuItem Name;
    public MenuItem Arguments;
    public MenuItem Methods;
    @FXML
    public Label className;

    /**
     * Set parent controller.
     *
     * @param parentController New parent controller.
     */
    public void setParentController(MainController parentController) {
        this.main = parentController;
    }

    /**
     * Initialize class frontend block for class name,
     * class arguments and class operations.
     */
    public void initialize(){
        className.getStyleClass().add("classHead");
        classArguments.getStyleClass().add("classBody");
        classMethods.getStyleClass().add("classBody");
    }

    /**
     * Add class name to frontend class block.
     *
     * @param event Action event.
     */
    @FXML
    private void addClassNameAction(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("class name");
        dialog.setHeaderText("Enter class name:");
        dialog.setContentText("Class name:");

        Optional<String> name = dialog.showAndWait();

        name.ifPresent(s -> {
            if (main.diagram.setNewClassName(MainController.tmpNode.getView().getId(),s)) {
                this.className.setText(s);
                MainController.tmpNode.getView().setId(s);
            } else {
                warning("This name was used!");
                addClassNameAction(event);
            }
        });

    }

    /**
     * Add class attribute into class frontend block.
     *
     * @param actionEvent Action event.
     */
    public void addArgumentAction(ActionEvent actionEvent) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Argument");
        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        UMLClass cls = main.diagram.findClass(MainController.tmpNode.getView().getId());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox modifier = new ComboBox();
        modifier.getItems().addAll("", "+", "-", "#", "~");
        TextField name = new TextField();
        name.setPromptText("Name");
        TextField type = new TextField();
        type.setPromptText("type");
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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {

                UMLDataType dataType = main.diagram.dataTypeForName(type.getText());
                UMLAttribute attr = new UMLAttribute(name.getText(), dataType);

                if(modifier.getValue() != null) {
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
                }

                if(!def.getText().equals("")){
                    attr.setDefaultValue(def.getText());
                }

                if(!cls.addAttribute(attr)) {
                    warning("This argument was entered already");
                    addArgumentAction(actionEvent);
                    return null;
                }
                Label label = new Label();
                label.setText(((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((type.getText().equals("")) ? "" : " : " + type.getText()) + ((def.getText().equals("")) ? "" : " = " + def.getText()));
                classArguments.getChildren().add(label);
                label.setLayoutY(classArgumentPosition);
                classArgumentPosition += 16;
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

    }

    /**
     * Add method into frontend class block.
     *
     * @param actionEvent Action event.
     */
    public void addMethod(ActionEvent actionEvent) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Method");
        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        /* Create method parameters backhand */
        UMLClass cls = main.diagram.findClass(MainController.tmpNode.getView().getId());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox modifier = new ComboBox();
        modifier.getItems().addAll("", "+", "-", "#", "~");
        TextField name = new TextField();
        name.setPromptText("Name");
        TextField parameters = new TextField();
        parameters.setPromptText("names: types, next");
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

                UMLDataType retDataType = main.diagram.dataTypeForName(type.getText());
                List<UMLAttribute> classAttributes = new ArrayList<UMLAttribute>();

                /* Create method parameters */

                String methodParams = parameters.getText();
                /* Remove all whitespaces and non-visible characters */
                methodParams = methodParams.replaceAll("\\s+","");

                String[] splittedMethodParams = methodParams.split(",");

                for (String currentParam : splittedMethodParams) {
                    if (currentParam.equals("")) {
                        warning("bad parameter format. You need to write the comma between");
                        addMethod(actionEvent);
                        return null;
                    }

                    String[] splittedParam = currentParam.split(":");

                    if (splittedParam.length != 2) {
                        warning("Bad parameter format.");
                        addMethod(actionEvent);
                        return null;
                    } else if(splittedParam[0].equals("")) {
                        warning("You need to write parameter name.");
                        addMethod(actionEvent);
                        return null;
                    } else if (splittedParam[1].equals("")) {
                        warning("You need to write parameter data type.");
                        addMethod(actionEvent);
                        return null;
                    } else {
                        String paramName = splittedParam[0];
                        String paramDataType = splittedParam[1];

                        UMLDataType dataType = main.diagram.dataTypeForName(paramDataType);

                        UMLAttribute attr = new UMLAttribute(paramName, dataType);
                        classAttributes.add(attr);
                    }
                }

                UMLOperation operation = UMLOperation.create(name.getText(), retDataType, classAttributes.toArray(new UMLAttribute[classAttributes.size()]));

                if(modifier.getValue() != null) {
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
                }

                if(!cls.addOperation(operation)) {
                    warning("This operation name was entered already");
                    addMethod(actionEvent);
                    return null;
                }

                Label label = new Label();
                label.setText(((modifier.getValue() == null) ? "" : modifier.getValue()) + " " + name.getText() + ((parameters.getText().equals("")) ? "()" : " ( " + parameters.getText() + " ) ") + ((type.getText().equals("")) ? "" : " : " + type.getText()));
                classMethods.getChildren().add(label);
                label.setLayoutY(classMethodsPosition);
                classMethodsPosition += 16;
                if(main.diagram.isOverriddenMethod(cls, operation)){
                    label.getStyleClass().add("overriden");
                }
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }

    /**
     * Show warning window with error message.
     *
     * @param message Error message.
     */
    public void warning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void setAbstract(ActionEvent actionEvent) {
        className.getStyleClass().add("abstractClass");
        UMLClass cls = main.diagram.findClass(MainController.tmpNode.getView().getId());
        cls.setAbstract(true);
    }

    public void setInterface(ActionEvent actionEvent) {
        className.getStyleClass().add("interfaceClass");
        UMLClass cls = main.diagram.findClass(MainController.tmpNode.getView().getId());
        cls.setInterface(true);
    }
}
