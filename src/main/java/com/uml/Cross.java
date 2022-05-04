package com.uml;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class Cross extends Group {
    private static final double CROSS_WIDTH = 5;
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final Polyline crossA = new Polyline();
    private final Polyline crossB = new Polyline();
    public Cross(double layoutX, double y) {
        this.x1.set(layoutX);
        this.y1.set(y);
        getChildren().addAll(crossA, crossB);

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.y1}){
            s.addListener((l,o,n) -> update() );
        }

        update();
    }

    private void update() {
        double x1 = getX1();
        double y1 = getY1();
        crossA.getPoints().setAll(x1 + CROSS_WIDTH, y1 - CROSS_WIDTH, x1 - CROSS_WIDTH, y1 + CROSS_WIDTH);
        crossB.getPoints().setAll(x1 + CROSS_WIDTH, y1 + CROSS_WIDTH, x1 - CROSS_WIDTH, y1 - CROSS_WIDTH);
    }

    public double getX1() {
        return x1.get();
    }

    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public double getY1() {
        return y1.get();
    }

    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1.set(y1);
    }
}
