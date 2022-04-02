package com.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ClassUML extends Button {
    public Parent view;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();

    public ClassUML(double x, double y) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        Parent view = loader.load();
        setLayoutX(x);
        setLayoutY(y);
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));
        /*ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("add class name");
        MenuItem menuItem2 = new MenuItem("add parameter");
        MenuItem menuItem3 = new MenuItem("add method");
        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3);
        setContextMenu(contextMenu);
        Label name = new Label();
        Label parameter = new Label();
        Label method = new Label();
        getChildren().addAll(name, parameter, method);*/
    }

    public Parent getView() {
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }
}
