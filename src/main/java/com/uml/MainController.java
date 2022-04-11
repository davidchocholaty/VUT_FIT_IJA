package com.uml;

import com.uml.classdiagram.UMLClass;
import com.uml.filehandler.CustomException;
import com.uml.filehandler.IJAXMLParser;
import com.uml.filehandler.SaveHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.uml.classdiagram.ClassDiagram;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class MainController extends Parent {
    @FXML
    public ClassController childController;
    public ToggleButton compositionButton;
    public ToggleButton realizationButton;
    public ToggleButton aggregationButton;
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

    /*************************************/
    static ClassDiagram diagram = new ClassDiagram("Class diagram");
    /*************************************/

    public void initialize() {
        group.getToggles().addAll(compositionButton, realizationButton, aggregationButton, classButton, arrowButton);
    }

    public void classActive(MouseEvent mouseEvent) {
        if (classButton.isSelected()) {
            this.classAct = true;
            this.arrowAct = false;
            count = 0;
        } else {
            this.classAct = false;
        }
    }

    public Node getButton() {
        return button;
    }

    public void setButton(Node button) {
        this.button = button;
    }

    public boolean isArrowAct() {
        return arrowAct;
    }

    public void setArrowAct(boolean arrowAct) {
        this.arrowAct = arrowAct;
    }

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

    public ClassUML createElement(MouseEvent mouseEvent) throws IOException {

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        ClassUML el = new ClassUML(mouseEvent.getX(), mouseEvent.getY());

        el.getView().setOnDragDetected(e -> dDetect(e, el));
        el.getView().setOnMouseDragged(e -> drag(e, el));
        el.getView().setOnMousePressed(e -> classClick(e, el));

        return el;
    }

    public ClassUML createElement(double x, double y, String name) throws IOException {
        ClassUML el = new ClassUML(x, y, name);
        childController.className.setText(name);

        el.getView().setOnDragDetected(e -> dDetect(e, el));
        el.getView().setOnMouseDragged(e -> drag(e, el));
        el.getView().setOnMousePressed(e -> classClick(e, el));

        return el;
    }

    public void addElement(ClassUML el){
        rPane.getChildren().add(el.getView());
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
        if (classAct) {
            rPane.getChildren().add(createElement(mouseEvent).getView());
        }
    }

    public void addAttribute(String name, String dataType, String modifier, String value){
        if(childController.classArguments.getText().equals("")){
            childController.classArguments.setText(childController.classArguments.getText() + ((modifier.equals("")) ? "" : modifier) + " " + name + ((dataType.equals("")) ? "" : " : " + dataType) + ((value.equals("")) ? "" : " = " + value));
        }else {
            childController.classArguments.setText(childController.classArguments.getText() + "\n" + ((modifier.equals("")) ? "" : modifier) + " " + name + ((dataType.equals("")) ? "" : " : " + dataType) + ((value.equals("")) ? "" : " = " + value));
        }
    }

    public void addOperation(String name, String parameters, String ret, String modifier){

    }

    public void dDetect(MouseEvent e, ClassUML inClassButton) {
        inClassButton.toFront();
    }

    public void drag(MouseEvent event, ClassUML el) {
        double x = el.getView().getTranslateX() + event.getX() + el.getView().getLayoutX();
        double y = el.getView().getTranslateY() + event.getY() + el.getView().getLayoutY();
        el.getView().setLayoutX(x);
        el.getView().setLayoutY(y);

        UMLClass cls = diagram.findClass(el.getView().getId());
        cls.setXCoordinate(x);
        cls.setYCoordinate(y);
    }


    public Arrow createAndAddArrow(ClassUML n1, ClassUML n2, String arrowID) {
        Arrow arrow = new Arrow(n1.getView().getLayoutX(), n1.getView().getLayoutY(), n2.getView().getLayoutX(), n2.getView().getLayoutY(), arrowID, n1, n2);
        arrow.x1Property().bind(n1.getView().layoutXProperty());
        arrow.y1Property().bind(n1.getView().layoutYProperty());
        arrow.x2Property().bind(n2.getView().layoutXProperty());
        arrow.y2Property().bind(n2.getView().layoutYProperty());

        arrow.setOnMouseClicked(e -> arrowClicked(e, arrow));

        n1.edges.add(arrow);
        n2.edges.add(arrow);

        return arrow;
    }

    private void arrowClicked(MouseEvent e, Arrow arrow) {
        System.out.println(arrow);
        tmpArrow = arrow;
    }

    public void diagramDragDetected(MouseEvent mouseEvent) {
        Node n = (Node) mouseEvent.getSource();
    }

    public void classClick(MouseEvent e, ClassUML classBody) {
        System.out.println(classBody.getView().getId());
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

    public void saveProject(MouseEvent mouseEvent) {
        boolean saveLoad = false;
        String path = getFilePathWindow(saveLoad);

        SaveHandler saveHandler = new SaveHandler(diagram);
        try{
            saveHandler.saveClassDiagram(path);
        } catch (ParserConfigurationException | FileNotFoundException | TransformerException e) {
            childController.warning("Invalid file path.");
        }
    }

    public void loadProject(MouseEvent mouseEvent){
        boolean saveLoad = true;
        String path  = getFilePathWindow(saveLoad);
        IJAXMLParser parser = new IJAXMLParser(path);
        try {
            parser.parse();
        } catch (ParserConfigurationException | CustomException.IllegalFileExtension | IOException | SAXException e) {
            childController.warning("Invalid file path.");
        } catch (CustomException.IllegalFileFormat e) {
            childController.warning("Invalid file syntax.");
        }
    }

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

        return file.getAbsolutePath();
    }
}
