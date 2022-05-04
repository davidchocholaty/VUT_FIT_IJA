package com.uml;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;

public class ActivationSequenceUML extends Parent {
    private static final double RECTANGLE_WIDTH = 10;
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private Rectangle rectangle = new Rectangle();
    public ActivationSequenceUML(Double activation, double x, double y, SequenceUML sq) {
        this.x1.set(x);
        this.y1.set(activation);
        this.y2.set(y);
        getChildren().add(rectangle);

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        rectangle.getStyleClass().add("activation");

        update();
    }

    private void update() {
        double x1 = getX1();
        double y1 = getY1();
        double y2 = getY2();

        rectangle.setX(x1 - (RECTANGLE_WIDTH/2));
        rectangle.setY(y1);
        rectangle.setWidth(RECTANGLE_WIDTH);
        rectangle.setHeight(y2-y1);

    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
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
