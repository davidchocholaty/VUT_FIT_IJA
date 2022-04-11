package com.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;


import java.io.IOException;

import com.uml.classdiagram.UMLClass;

public class ClassUML extends Button{
    public Node view;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();
    private ClassController controller;

    public ClassUML(double x, double y, MainController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        Button loaded = loader.load();
        setView(loaded);
        getView().setLayoutX(x);
        getView().setLayoutY(y);
        getView().translateXProperty().bind(loaded.widthProperty().divide(-2));
        getView().translateYProperty().bind(loaded.heightProperty().divide(-2));


        UMLClass cls = controller.diagram.createDefaultClass();
        cls.setXCoordinate(x);
        cls.setYCoordinate(y);

        loaded.setId(cls.getName());

    }

    public ClassUML(double x, double y, String name, MainController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        Button loaded = loader.load();
        setView(loaded);
        getView().setLayoutX(x);
        getView().setLayoutY(y);
        getView().translateXProperty().bind(loaded.widthProperty().divide(-2));
        getView().translateYProperty().bind(loaded.heightProperty().divide(-2));

        this.controller = loader.getController();

        controller.diagram.createClass(name);

        loaded.setId(name);
    }

    public ClassController getController(){
        return this.controller;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    public Node getThis(){
        return (Node) this.getChildren();
    }
}
