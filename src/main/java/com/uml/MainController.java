package com.uml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController extends Parent {
    @FXML
    public ClassController childController;
    private Node view;
    public Pane rPane;
    private Node button;
    public Arrow arrow;
    public ToggleButton classButton;
    public ToggleButton arrowButton;
    public boolean arrowAct;
    private boolean classAct = false;
    static int count = 0;
    static ClassUML tmpNode;
    public ClassUML elem;

    public void initialize() {
        childController.setParentController(this);
    }

    public void classActive(MouseEvent mouseEvent) {
        if (classButton.isSelected()) {
            this.classAct = true;
            this.arrowAct = false;
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
        } else {
            arrowAct = false;
        }
    }

    public ClassUML createElement(MouseEvent mouseEvent) throws IOException {

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        ClassUML el = new ClassUML(mouseEvent.getX(), mouseEvent.getY());

        System.out.println(el);

        el.setOnDragDetected(e -> dDetect(e, el));
        el.setOnMouseDragged(e -> drag(e, el));
        el.setOnMousePressed(e -> classClick(e, el));

        return el;
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
        if (classAct) {
            rPane.getChildren().add(createElement(mouseEvent).getView());
        }
    }

    public void dDetect(MouseEvent e, Button inClassButton) {
        inClassButton.toFront();
    }

    public void drag(MouseEvent event, Button el) {
        el.setLayoutX(el.getTranslateX() + event.getX() + el.getLayoutX());
        el.setLayoutY(el.getTranslateY() + event.getY() + el.getLayoutY());
    }


    public Arrow createAndAddArrow(ClassUML el1, ClassUML el2) {
        Node n1 = (Node) el1;
        Node n2 = (Node) el2;
        Arrow arrow = new Arrow(n1.getLayoutX(), n1.getLayoutY(), n2.getLayoutX(), n2.getLayoutY());
        arrow.x1Property().bind(n1.layoutXProperty());
        arrow.y1Property().bind(n1.layoutYProperty());
        arrow.x2Property().bind(n2.layoutXProperty());
        arrow.y2Property().bind(n2.layoutYProperty());

        el1.edges.add(arrow);
        el2.edges.add(arrow);
        this.rPane.getChildren().add(arrow);
        return arrow;
    }

    public void diagramDragDetected(MouseEvent mouseEvent) {
        Node n = (Node) mouseEvent.getSource();
    }

    public void classClick(MouseEvent e, ClassUML classBody) {
        System.out.println(classBody);
        if (isArrowAct()) {
            if (count == 0) {
                tmpNode = classBody;
                count++;
            } else if (count == 1) {
                arrow = createAndAddArrow(tmpNode, classBody);
                count = 0;
            }
        } else {
            tmpNode = classBody;
        }
    }

    public void handleKeyEvents(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE && tmpNode != null) {
            rPane.getChildren().remove(tmpNode);
        }
    }
}
