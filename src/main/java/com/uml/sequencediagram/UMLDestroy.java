/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.sequencediagram;

/**
 * A class represents an object destroying.
 */
public class UMLDestroy {

    private long id;
    private static long nextId = 0;
    private UMLLifeline lifeline;
    private double yCoordinate;

    /**
     * Creates an UMLDestroy instance with lifeline and y coordinate.
     *
     * @param lifeline Lifeline on which is destroying created.
     * @param yCoordinate Destroying symbol y coordinate on frontend scene.
     */
    public UMLDestroy(UMLLifeline lifeline, double yCoordinate) {
        this.lifeline = lifeline;
        this.yCoordinate = yCoordinate;
        this.id = nextId;
        nextId++;
    }

    /**
     * Returns object destroying symbol unique identifier.
     *
     * @return UMLDestory identifier.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Return object destroying lifeline.
     *
     * @return Actual lifeline.
     */
    public UMLLifeline getLifeline() {
        return this.lifeline;
    }

    /**
     * Set new lifeline for destroying an object.
     *
     * @param lifeline New lifeline.
     */
    public void setLifeline(UMLLifeline lifeline) {
        this.lifeline = lifeline;
    }

    /**
     * Get y coordinate of destroying symbol on frontend scene.
     *
     * @return Destroying symbol y coordinate.
     */
    public double getYCoordinate() {
        return this.yCoordinate;
    }

    /**
     * Set y coordinate of destroying symbol on frontend scene.
     *
     * @param yCoordinate Destroying symbol y coordinate.
     */
    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

}
