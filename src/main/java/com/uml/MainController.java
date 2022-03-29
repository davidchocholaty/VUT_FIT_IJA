package com.uml;

import com.dlsc.formsfx.model.structure.Element;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    public void initialize() {
        childController.setParentController(this);
    }

    public void classActive(MouseEvent mouseEvent) {
        if(classButton.isSelected()){
            classAct = true;
            arrowAct = false;
        }else{
            classAct = false;
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
        if(arrowButton.isSelected()){
            classAct = false;
            arrowAct = true;
        }else{
            arrowAct = false;
        }
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
        if(classAct){
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            ClassUML el = new ClassUML(rPane, x, y);

            el.getView().translateXProperty().bind(classButton.widthProperty().divide(-2));
            el.getView().translateYProperty().bind(classButton.heightProperty().divide(-2));
        }
    }

    public void dDetect(MouseEvent e, Node inClassButton) {
        inClassButton.toFront();
    }

    public void drag(MouseEvent event, Node el) {
        el.setLayoutX(el.getTranslateX() + event.getX() + el.getLayoutX());
        el.setLayoutY(el.getTranslateY() + event.getY() + el.getLayoutY());
    }


    public Arrow createAndAddArrow(Node n1, Node n2){
        Arrow arrow = new Arrow(n1.getLayoutX(), n1.getLayoutY(), n2.getLayoutX(), n2.getLayoutY());
        arrow.x1Property().bind(n1.layoutXProperty());
        arrow.y1Property().bind(n1.layoutYProperty());
        arrow.x2Property().bind(n2.layoutXProperty());
        arrow.y2Property().bind(n2.layoutYProperty());

        this.rPane.getChildren().add(arrow);
        return arrow;
    }

    public void diagramDragDetected(MouseEvent mouseEvent) {
        Node n = (Node)mouseEvent.getSource();
        System.out.println(n);
    }
}
