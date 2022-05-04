package com.uml;

import com.uml.classdiagram.UMLClass;
import com.uml.sequencediagram.UMLLifeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.io.IOException;

public class SequenceUML extends Parent {
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();

    private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private Node view;
    private Node line;
    private final Polyline dashedLine = new Polyline();
    private final Polyline clickLine = new Polyline();
    private final double LINE_SCALER = 20;
    public final Glow glow = new Glow(0.8);
    public ObservableList<Node> edges = FXCollections.observableArrayList();

    public UMLLifeline lifeline;

    public Polyline getClickLine() {
        return clickLine;
    }

    private void setView(Button sequence) {
        this.view = sequence;
    }

    public Node getView(){
        return this.view;
    }

    public SequenceUML(double x, double height, double y, Pane rpane, String name) throws IOException {
        System.out.println(height);
        this.x1.set(x);
        this.y1.set(height);
        this.x2.set(x);
        if(y == 0.0){
            this.y2.set(rpane.getHeight());
        }else{
            this.y2.set(y);
        }
        FXMLLoader loader = new FXMLLoader(App.class.getResource("sequence_uml.fxml"));
        Button sequence = loader.load();
        setView(sequence);
        sequence.setText(name);
        getView().setLayoutX(x);
        getView().setLayoutY(height);
        getView().translateXProperty().bind(sequence.widthProperty().divide(-2));
        getView().translateYProperty().bind(sequence.heightProperty().divide(-2));
        clickLine.setStrokeWidth(24);
        clickLine.setStroke(Color.rgb(255, 255, 255, 0.01));

        clickLine.setOnMouseEntered(e -> dashedLine.setEffect(glow));
        clickLine.setOnMouseExited(e -> dashedLine.setEffect(null));
        getChildren().addAll(dashedLine, clickLine, sequence);
        getView().getStyleClass().add("sequence");

        x1.bind(getView().layoutXProperty());
        y1.bind(getView().layoutYProperty());
        x2.bind(getView().layoutXProperty());

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.x2, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        update();
    }

    private void update(){
        double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
        double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

        double x1 = start[0];
        double y1 = start[1];
        double x2 = start[0];
        double y2 = end[1];

        dashedLine.getPoints().setAll(x1, y1, x2, y2);
        clickLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getStrokeDashArray().addAll(2d, 5d);
    }

    private double[] scale(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
                x1 + Math.cos(theta) * LINE_SCALER,
                y1 + Math.sin(theta) * LINE_SCALER
        };
    }

    public double getY2() {
        return y2.get();
    }

    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2.set(y2);
    }
}
