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
 * A class represents an activation symbol of UML sequence diagram.
 */
public class UMLActivation {

    private long id;
    private static long nextId = 0;
    private UMLLifeline lifeline;
    private double firstYCoordinate;
    private double secondYCoordinate;

    /**
     * Creates an UMLActivation instance with lifeline and both y coordinates.
     *
     * @param lifeline Lifeline on which is activation created.
     * @param firstYCoordinate First y coordinate of activation (where activation starts).
     * @param secondYCoordinate Second y coordinate of activation (where activation ends).
     */
    public UMLActivation(UMLLifeline lifeline,
                         double firstYCoordinate,
                         double secondYCoordinate) {
        this.lifeline = lifeline;
        this.firstYCoordinate = firstYCoordinate;
        this.secondYCoordinate = secondYCoordinate;
        this.id = nextId;
        nextId++;
    }

    /**
     * Returns activation unique identifier.
     *
     * @return UMLActivation identifier.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Return activation lifeline.
     *
     * @return Actual lifeline.
     */
    public UMLLifeline getLifeline() {
        return this.lifeline;
    }

    /**
     * Set new lifeline for activation.
     *
     * @param lifeline New lifeline.
     */
    public void setLifeline(UMLLifeline lifeline) {
        this.lifeline = lifeline;
    }

    /**
     * Get y coordinate of activation start on frontend scene.
     *
     * @return Activation start y coordinate.
     */
    public double getFirstYCoordinate() {
        return this.firstYCoordinate;
    }

    /**
     * Set y coordinate of activation start on frontend scene.
     *
     * @param yCoordinate New activation start y coordinate.
     */
    public void setFirstYCoordinate(double yCoordinate) {
        this.firstYCoordinate = yCoordinate;
    }

    /**
     * Get y coordinate of activation end on frontend scene.
     *
     * @return Activation end y coordinate.
     */
    public double getSecondYCoordinate() {
        return this.secondYCoordinate;
    }

    /**
     * Set y coordinate of activation end on frontend scene.
     *
     * @param yCoordinate New activation end y coordinate.
     */
    public void setSecondYCoordinate(double yCoordinate) {
        this.firstYCoordinate = yCoordinate;
    }

}
