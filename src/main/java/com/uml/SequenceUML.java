/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing UML lifeline on frontend application part.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import com.uml.sequencediagram.UMLLifeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.io.IOException;

/**
 * Class represents UML lifeline used on application frontend.
 */
public class SequenceUML extends Parent {
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();

    private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private Button view;
    public final Polyline dashedLine = new Polyline();
    private final Polyline clickLine = new Polyline();
    private final double LINE_SCALER = 20;
    public final Glow glow = new Glow(0.8);
    public ObservableList<Node> edges = FXCollections.observableArrayList();

    public UMLLifeline lifeline;

    /**
     * Obtaining click line.
     * @return Click line.
     */
    public Polyline getClickLine() {
        return clickLine;
    }

    /**
     * Set new view to lifeline.
     *
     * @param sequence New view.
     */
    private void setView(Button sequence) {
        this.view = sequence;
    }

    /**
     * Returns actual lifeline view.
     *
     * @return Actual view.
     */
    public Button getView(){
        return this.view;
    }

    /**
     * Creates new SequenceUML instance.
     * <p>
     *     SequenceUML instance represents new lifeline on application frontend.
     * </p>
     *
     * @param x X coordinate.
     * @param height Lifeline height.
     * @param y Y coordinate lifeline indent.
     * @param rpane Pane.
     * @param name Lifeline name.
     * @throws IOException Error occurrence while setting lifeline name.
     */
    public SequenceUML(double x, double height, double y, Pane rpane, String name) throws IOException {
        this.x1.set(x);
        this.y1.set(height);
        this.x2.set(x);

        if(y == 0.0){
            this.y2.set(rpane.getScene().getHeight());
        }else{
            this.y2.set(y);
        }
        this.name.set(name);

        FXMLLoader loader = new FXMLLoader(App.class.getResource("sequence_uml.fxml"));
        Button sequence = loader.load();
        setView(sequence);
        sequence.setText(this.name.get());
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

        this.name.addListener((l,o,n) -> update());

        update();
    }

    /**
     * Calculates new position on frontend scene on update.
     */
    private void update(){
        double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
        double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

        double x1 = start[0];
        double y1 = start[1];
        double x2 = start[0];
        double y2 = end[1];

        getView().setText(this.name.get());

        dashedLine.getPoints().setAll(x1, y1, x2, y2);
        clickLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getStrokeDashArray().addAll(2d, 5d);
    }

    /**
     * Calculates scale from coordinates.
     *
     * @param x1 First x coordinate.
     * @param y1 First y coordinate.
     * @param x2 Second x coordinate.
     * @param y2 Second y coordinate.
     * @return Calculated scale.
     */
    private double[] scale(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
                x1 + Math.cos(theta) * LINE_SCALER,
                y1 + Math.sin(theta) * LINE_SCALER
        };
    }

    /**
     * Returns y2 coordinate.
     *
     * @return Y2 coordinate.
     */
    public double getY2() {
        return y2.get();
    }

    /**
     * Returns y2 property.
     *
     * @return Y2 property.
     */
    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    /**
     * Set new y2 coordinate.
     *
     * @param y2 New y2 coordinate.
     */
    public void setY2(double y2) {
        this.y2.set(y2);
    }

    /**
     * Obtaining lifeline name.
     *
     * @return Name.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Obtaining lifeline name property.
     *
     * @return Name property.
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Set new lifeline name.
     *
     * @param name New name.
     */
    public void setName(String name) {
        this.name.set(name);
    }
}
