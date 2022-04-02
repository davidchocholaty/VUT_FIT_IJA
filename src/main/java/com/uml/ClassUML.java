package com.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ClassUML extends Button{
    public Node view;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();

    public ClassUML(double x, double y) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        Button loaded = loader.load();
        setView(loaded);
        getView().setLayoutX(x);
        getView().setLayoutY(y);
        getView().translateXProperty().bind(loaded.widthProperty().divide(-2));
        getView().translateYProperty().bind(loaded.heightProperty().divide(-2));
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
