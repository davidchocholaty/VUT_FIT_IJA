package com.uml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;

public class ClassUML {
    private Node view;

    public ClassUML(Pane rPane, double x, double y) throws IOException {
        FXMLLoader classAll = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        this.view = (Node)classAll.load();
        this.view.setLayoutX(x);
        this.view.setLayoutY(y);
        rPane.getChildren().add(this.view);
    }

    public Node getView() {
        return this.view;
    }
}
