package com.uml;

import com.uml.classdiagram.UMLClass;
import com.uml.classdiagram.UMLRelationship;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

import com.uml.classdiagram.ClassDiagram;

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

    private void contentAction(ActionEvent e, ClassUML el) {
        System.out.println("tady");
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
        if (classAct) {
            rPane.getChildren().add(createElement(mouseEvent).getView());
        }
    }

    public void dDetect(MouseEvent e, ClassUML inClassButton) {
        inClassButton.toFront();
    }

    public void drag(MouseEvent event, ClassUML el) {
        el.getView().setLayoutX(el.getView().getTranslateX() + event.getX() + el.getView().getLayoutX());
        el.getView().setLayoutY(el.getView().getTranslateY() + event.getY() + el.getView().getLayoutY());
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
            /* deleteAllClassRelationships(UMLClass) */
            /* TODO David doimplementovat */
            /*diagram.deleteAllClassRelationships(cls);*/

            for (Arrow a : tmpNode.edges) {
                rPane.getChildren().remove(a);
            }

            rPane.getChildren().remove(tmpNode.getView());
            /* TODO David doimplementovat */
            /*diagram.deleteClass(cls);*/
        }
    }
}
