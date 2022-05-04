/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing main controller of frontend application part.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import com.uml.classdiagram.*;
import com.uml.sequencediagram.*;
import com.uml.customexception.*;
import com.uml.filehandler.IJAXMLParser;
import com.uml.filehandler.SaveHandler;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Class represents application frontend main controller.
 */
public class MainController extends Parent {
    @FXML
    public ClassController childController;
    public ToggleButton compositionButton;
    public ToggleButton realizationButton;
    public ToggleButton aggregationButton;
    public TabPane tabPane;
    private Node view;
    public Pane rPane;
    private Node button;
    public Arrow arrow;
    public ToggleButton classButton;
    public ToggleButton arrowButton;
    public boolean arrowAct;
    static String arrowID;
    private boolean classAct = false;
    static int count = 0;
    static ClassUML tmpNode;
    static Arrow tmpArrow;
    static ToggleGroup group = new ToggleGroup();
    static  int tabIndex = 1;

    public ClassDiagram diagram = new ClassDiagram("Class diagram");

    /**
     * Initialize main controller.
     */
    public void initialize() {
        group.getToggles().addAll(compositionButton, realizationButton, aggregationButton, classButton, arrowButton);
    }

    /**
     * Set active class.
     *
     * @param mouseEvent Event of mouse.
     */
    public void classActive(MouseEvent mouseEvent) {
        if (classButton.isSelected()) {
            this.classAct = true;
            this.arrowAct = false;
            count = 0;
        } else {
            this.classAct = false;
        }
    }

    /**
     * Get button.
     *
     * @return Returns button.
     */
    public Node getButton() {
        return button;
    }

    /**
     * Set button.
     *
     * @param button New button.
     */
    public void setButton(Node button) {
        this.button = button;
    }

    /**
     * Get arrowAct.
     *
     * @return Returns arrowAct.
     */
    public boolean isArrowAct() {
        return arrowAct;
    }

    /**
     * Set arrowAct.
     *
     * @param arrowAct Returns arrowAct.
     */
    public void setArrowAct(boolean arrowAct) {
        this.arrowAct = arrowAct;
    }

    /**
     * Set active arrow.
     *
     * @param mouseEvent Event of mouse.
     */
    public void arrowActive(MouseEvent mouseEvent) {
        if (arrowButton.isSelected()) {
            classAct = false;
            arrowAct = true;
            count = 0;
            arrowID = "arrow";
        } else if (realizationButton.isSelected()) {
            classAct = false;
            arrowAct = true;
            count = 0;
            arrowID = "realization";
        } else if (aggregationButton.isSelected()) {
            classAct = false;
            arrowAct = true;
            count = 0;
            arrowID = "aggregation";
        } else if (compositionButton.isSelected()) {
            classAct = false;
            arrowAct = true;
            count = 0;
            arrowID = "composition";
        } else {
            arrowAct = false;
        }
    }

    /**
     * Creates new ClassUML instance.
     *
     * @param mouseEvent Event of mouse.
     * @return New ClassUML instance.
     * @throws IOException FXMLLoader error.
     */
    public ClassUML createElement(MouseEvent mouseEvent) throws IOException {

        ClassUML el = new ClassUML(mouseEvent.getX(), mouseEvent.getY(), this);

        el.getView().setOnDragDetected(e -> dDetect(e, el));
        el.getView().setOnMouseDragged(e -> drag(e, el));
        el.getView().setOnMousePressed(e -> classClick(e, el));

        return el;
    }

    /**
     * Creates new ClassUML instance.
     * <p>
     *     Used for creating new classes when loading class diagram from file.
     * </p>
     *
     * @param x Class x coordinate.
     * @param y Class y coordinate.
     * @param name Class name.
     * @return New ClassUML instance.
     * @throws IOException FXMLLoader error.
     */
    public ClassUML createElement(double x, double y, String name) throws IOException {
        ClassUML el = new ClassUML(x, y, name, this);
        el.getController().className.setText(name);

        el.getView().setOnDragDetected(e -> dDetect(e, el));
        el.getView().setOnMouseDragged(e -> drag(e, el));
        el.getView().setOnMousePressed(e -> classClick(e, el));

        return el;
    }

    /**
     * Add new ClassUML element.
     *
     * @param el ClassUML element.
     */
    public void addElement(ClassUML el){
        rPane.getChildren().add(el.getView());
    }

    /**
     * Add new element.
     *
     * @param mouseEvent Event of mouse.
     * @throws IOException FXMLLoader error.
     */
    public void addElement(MouseEvent mouseEvent) throws IOException {
        if (classAct) {
            rPane.getChildren().add(createElement(mouseEvent).getView());
        }
    }

    /**
     * Add attribute into class.
     * <p>
     *     This method is used for adding new attribute into class
     *     while loading class diagram from file.
     * </p>
     *
     * @param uml Frontend UML class.
     * @param name Class name.
     * @param dataType Attribute data type.
     * @param modifier Attribute modifier (visibility).
     * @param value Attribute default value.
     */
    public void addAttribute(ClassUML uml,String name, String dataType, String modifier, String value){
        ClassController controller = uml.getController();

        UMLClass cls = this.diagram.findClass(uml.getView().getId());
        UMLDataType type = this.diagram.dataTypeForName(dataType);
        UMLAttribute attr = new UMLAttribute(name, type);

        if (!modifier.equals("")) {
            switch (modifier) {
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

        if(!value.equals("")){
            attr.setDefaultValue(value);
        }

        cls.addAttribute(attr);

        if(controller.classArguments.getText().equals("")){
            controller.classArguments.setText(controller.classArguments.getText() + ((modifier.equals("")) ? "" : modifier) + " " + name + ((dataType.equals("")) ? "" : " : " + dataType) + ((value.equals("")) ? "" : " = " + value));
        }else {
            controller.classArguments.setText(controller.classArguments.getText() + "\n" + ((modifier.equals("")) ? "" : modifier) + " " + name + ((dataType.equals("")) ? "" : " : " + dataType) + ((value.equals("")) ? "" : " = " + value));
        }
    }

    /**
     * Add operation into class.
     * <p>
     *     This method is used for adding new operation into class
     *     while loading class diagram from file.
     * </p>
     *
     * @param uml Frontend UML class.
     * @param name Class name.
     * @param parameters Operation parameters.
     * @param ret Operation return data type.
     * @param modifier Operation modifier (visibility).
     */
    public void addOperation(ClassUML uml, String name, String parameters, String ret, String modifier){
        ClassController controller = uml.getController();

        UMLClass cls = this.diagram.findClass(uml.getView().getId());
        UMLDataType retDataType = this.diagram.dataTypeForName(ret);
        List<UMLAttribute> classAttributes = new ArrayList<UMLAttribute>();

        /* Create method parameters */

        String methodParams = parameters;
        /* Remove all whitespaces and non-visible characters */
        methodParams = methodParams.replaceAll("\\s+","");

        String[] splittedMethodParams = methodParams.split(",");

        for (String currentParam : splittedMethodParams) {

            String[] splittedParam = currentParam.split(":");

            String paramName = splittedParam[0];
            String paramDataType = splittedParam[1];

            UMLDataType dataType = this.diagram.dataTypeForName(paramDataType);

            UMLAttribute attr = new UMLAttribute(paramName, dataType);
            classAttributes.add(attr);
        }

        UMLOperation operation = UMLOperation.create(name, retDataType, classAttributes.toArray(new UMLAttribute[classAttributes.size()]));

        if (!modifier.equals("")) {
            switch (modifier) {
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

        cls.addOperation(operation);

        if(controller.classMethods.getText().equals("")){
            controller.classMethods.setText(controller.classMethods.getText() + ((modifier.equals("")) ? "" : modifier) + " " + name + ((parameters.equals("")) ? "()" : " ( " + parameters + " ) ") + ((ret.equals("")) ? "" : " : " + ret));
        }else {
            controller.classMethods.setText(controller.classMethods.getText() + "\n" + ((modifier.equals("")) ? "" : modifier) + " " + name + ((parameters.equals("")) ? "()" : " ( " + parameters + " ) ") + ((ret.equals("")) ? "" : " : " + ret));
        }
    }

    /**
     * Insert the class currently being worked on in the foreground.
     *
     * @param e Event of mouse.
     * @param inClassButton Current class.
     */
    public void dDetect(MouseEvent e, ClassUML inClassButton) {
        inClassButton.toFront();
    }

    /**
     * Moving with class on frontend.
     *
     * @param event Event of mouse.
     * @param el Current class.
     */
    public void drag(MouseEvent event, ClassUML el) {
        double x = el.getView().getTranslateX() + event.getX() + el.getView().getLayoutX();
        double y = el.getView().getTranslateY() + event.getY() + el.getView().getLayoutY();
        el.getView().setLayoutX(x);
        el.getView().setLayoutY(y);

        UMLClass cls = diagram.findClass(el.getView().getId());
        cls.setXCoordinate(x);
        cls.setYCoordinate(y);
    }

    /**
     * Creates an instance level relationship.
     *
     * @param from From class.
     * @param to To class.
     * @param fromText From multiplicity.
     * @param toText To multiplicity.
     * @param relType Type of relationship (association, aggregation, composition).
     */
    public void createAndAddRelationship(ClassUML from, ClassUML to, String fromText, String toText, String relType){
        Arrow arrow = new Arrow(from, to, this, fromText, toText, relType);
        arrow.x1Property().bind(from.getView().layoutXProperty());
        arrow.y1Property().bind(from.getView().layoutYProperty());
        arrow.x2Property().bind(to.getView().layoutXProperty());
        arrow.y2Property().bind(to.getView().layoutYProperty());

        arrow.setOnMouseClicked(e -> arrowClicked(e, arrow));

        from.edges.add(arrow);
        to.edges.add(arrow);

        rPane.getChildren().add(arrow);
    }

    /**
     * Creates a class level relationship (inheritance).
     *
     * @param from From class.
     * @param to To class.
     * @param relType Type of relationship (inheritance).
     */
    public void createAndAddRelationship(ClassUML from, ClassUML to, String relType){
        Arrow arrow = new Arrow(from, to, this, relType);
        arrow.x1Property().bind(from.getView().layoutXProperty());
        arrow.y1Property().bind(from.getView().layoutYProperty());
        arrow.x2Property().bind(to.getView().layoutXProperty());
        arrow.y2Property().bind(to.getView().layoutYProperty());

        arrow.setOnMouseClicked(e -> arrowClicked(e, arrow));

        from.edges.add(arrow);
        to.edges.add(arrow);

        rPane.getChildren().add(arrow);
    }

    /**
     * Creates an arrow.
     *
     * @param n1 From class.
     * @param n2 To class.
     * @param arrowID Arrow identifier.
     * @return Returns new arrow instance.
     */
    public Arrow createAndAddArrow(ClassUML n1, ClassUML n2, String arrowID) {
        Arrow arrow = new Arrow(n1.getView().getLayoutX(), n1.getView().getLayoutY(), n2.getView().getLayoutX(), n2.getView().getLayoutY(), arrowID, n1, n2, this);
        arrow.x1Property().bind(n1.getView().layoutXProperty());
        arrow.y1Property().bind(n1.getView().layoutYProperty());
        arrow.x2Property().bind(n2.getView().layoutXProperty());
        arrow.y2Property().bind(n2.getView().layoutYProperty());

        arrow.setOnMouseClicked(e -> arrowClicked(e, arrow));

        n1.edges.add(arrow);
        n2.edges.add(arrow);

        return arrow;
    }

    /**
     * Set clicked arrow.
     *
     * @param e Event of mouse.
     * @param arrow Current arrow.
     */
    private void arrowClicked(MouseEvent e, Arrow arrow) {
        tmpArrow = arrow;
    }

    /**
     * Detect drag in class gram.
     *
     * @param mouseEvent Event of mouse.
     */
    public void diagramDragDetected(MouseEvent mouseEvent) {
        Node n = (Node) mouseEvent.getSource();
    }

    public void classClick(MouseEvent e, ClassUML classBody) {
        if (isArrowAct()) {
            if (count == 0) {
                tmpNode = classBody;
                count++;
            } else if (count == 1) {
                rPane.getChildren().add(createAndAddArrow(tmpNode, classBody, arrowID));
                count = 0;
            }
        } else {
            tmpNode = classBody;
        }
    }

    /**
     * Records the execution of the element delete action
     *
     * @param event Event.
     */
    public void handleKeyEvents(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE && tmpNode.getView() != null) {
            UMLClass cls = diagram.findClass(tmpNode.getView().getId());
            diagram.deleteAllClassRelationships(cls);

            for (Arrow a : tmpNode.edges) {
                rPane.getChildren().remove(a);
            }

            rPane.getChildren().remove(tmpNode.getView());

            diagram.deleteClass(cls);
        }
    }

    /**
     * Saving class diagram into file.
     *
     * @param mouseEvent Event of mouse.
     */
    public void saveProject(MouseEvent mouseEvent) {
        boolean saveLoad = false;
        String path = getFilePathWindow(saveLoad);

        if(path != null){
            SaveHandler saveHandler = new SaveHandler(diagram);
            try{
                saveHandler.save(path);
            } catch (ParserConfigurationException | FileNotFoundException | TransformerException e) {
                childController.warning("Invalid file path.");
            }
        }
    }

    /**
     * Loading class from file.
     *
     * @param mouseEvent Event of mouse.
     */
    public void loadProject(MouseEvent mouseEvent){
        boolean saveLoad = true;
        String path  = getFilePathWindow(saveLoad);
        if(path != null){
            IJAXMLParser parser = new IJAXMLParser(path);
            try {
                parser.parse();
            } catch (ParserConfigurationException | IllegalFileExtension | IOException | SAXException e) {
                childController.warning("Invalid file path.");
            } catch (IllegalFileFormat e) {
                childController.warning("Invalid file syntax.");
            }
        }
    }

    /**
     * Creates a window for choosing file for saving or loading class diagram.
     *
     * @param saveLoad Saving or loading mode.
     * @return File path.
     */
    private String getFilePathWindow(boolean saveLoad){
        Window window = rPane.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ija xml files (*.ijaxml, *.ixl)", "*.ijaxml", "*.ixl");
        fileChooser.getExtensionFilters().add(extFilter);

        File file;

        if (saveLoad) {
            file = fileChooser.showOpenDialog(window);
        } else {
            file = fileChooser.showSaveDialog(window);
        }
        if(file != null){
            return file.getAbsolutePath();
        }else{
            return null;
        }
    }

    /**
     * Creates a window with user help message.
     *
     * @param mouseEvent Event of mouse.
     */
    public void helpWindow(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("help.fxml"));
        try {
            Node node = fxmlLoader.load();
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Help");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            dialog.getDialogPane().setContent(node);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addTab(Event event) {
        try{
            //TODO jmeno sequence diagram
            String tabText = String.format("Sequence diagram %d", tabIndex++);
            Tab tab = new Tab(tabText);
            tab.setId(tabText);
            FXMLLoader loader = new FXMLLoader(App.class.getResource("sequencePage.fxml"));
            SequenceController sequenceController = new SequenceController();
            loader.setController(sequenceController);
            tab.setContent((Node) loader.load());
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);

            /* Backend */
            sequenceController.sequenceDiagram = new SequenceDiagram(tabText);
            sequenceController.classDiagram = this.diagram;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // TODO
    public SequenceController addTabLoad(String name) {
        return null;
    }
}
