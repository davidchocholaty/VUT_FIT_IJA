/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class represents destroy object symbol.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;

/**
 * Class represents destroying object symbol which is represented as a cross.
 */
public class Cross extends Group {
    private static final double CROSS_WIDTH = 5;
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final Polyline crossA = new Polyline();
    private final Polyline crossB = new Polyline();

    /**
     * Creates new cross instance.
     *
     * @param layoutX Cross x coordinate.
     * @param y Cross y coordinate.
     */
    public Cross(double layoutX, double y) {
        this.x1.set(layoutX);
        this.y1.set(y);
        getChildren().addAll(crossA, crossB);

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.y1}){
            s.addListener((l,o,n) -> update() );
        }

        update();
    }

    /**
     * Update cross x and y coordinates.
     */
    private void update() {
        double x1 = getX1();
        double y1 = getY1();
        crossA.getPoints().setAll(x1 + CROSS_WIDTH, y1 - CROSS_WIDTH, x1 - CROSS_WIDTH, y1 + CROSS_WIDTH);
        crossB.getPoints().setAll(x1 + CROSS_WIDTH, y1 + CROSS_WIDTH, x1 - CROSS_WIDTH, y1 - CROSS_WIDTH);
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
     * Returns y coordinate.
     *
     * @return Y coordinate.
     */
    public double getY1() {
        return y1.get();
    }

    /**
     * Returns y simple property coordinate.
     *
     * @return Y simple property coordinate.
     */
    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    /**
     * Sets new y coordinate.
     *
     * @param y1 New y coordinate.
     */
    public void setY1(double y1) {
        this.y1.set(y1);
    }
}
