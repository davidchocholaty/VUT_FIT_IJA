/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class represents an UML activation.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;

/**
 * Class represents the UML activation element created on a lifeline.
 */
public class ActivationSequenceUML extends Parent {
    private static final double RECTANGLE_WIDTH = 10;
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private Rectangle rectangle = new Rectangle();
    public ActivationSequenceUML(Double activation, double x, double y) {
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

    /**
     * Update activation coordinates.
     */
    private void update() {
        double x1 = getX1();
        double y1 = getY1();
        double y2 = getY2();

        rectangle.setX(x1 - (RECTANGLE_WIDTH/2));
        rectangle.setY(y1);
        rectangle.setWidth(RECTANGLE_WIDTH);
        rectangle.setHeight(y2-y1);

    }

    /**
     * Returns activation rectangle.
     *
     * @return Returns rectangle.
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Sets new rectangle that represents activation.
     *
     * @param rectangle New rectangle.
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Returns x coordinate.
     *
     * @return X coordinate.
     */
    public double getX1() {
        return x1.get();
    }

    /**
     * Returns x simple property coordinate.
     *
     * @return X simple property coordinate.
     */
    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    /**
     * Sets new x coordinate.
     *
     * @param x1 New x coordinate.
     */
    public void setX1(double x1) {
        this.x1.set(x1);
    }

    /**
     * Returns first y coordinate (start).
     *
     * @return Start y coordinate.
     */
    public double getY1() {
        return y1.get();
    }

    /**
     * Returns first y simple property coordinate.
     *
     * @return First y simple property coordinate.
     */
    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    /**
     * Sets new y start coordinate.
     *
     * @param y1 New y coordinate.
     */
    public void setY1(double y1) {
        this.y1.set(y1);
    }

    /**
     * Returns second y coordinate (end).
     *
     * @return End y coordinate.
     */
    public double getY2() {
        return y2.get();
    }

    /**
     * Returns second y simple property coordinate.
     *
     * @return Second y simple property coordinate.
     */
    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    /**
     * Sets new y end coordinate.
     *
     * @param y2 New x coordinate.
     */
    public void setY2(double y2) {
        this.y2.set(y2);
    }

}
