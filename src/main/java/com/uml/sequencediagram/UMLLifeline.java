/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.sequencediagram;

import com.uml.classdiagram.UMLClass;

/**
 * A class (its instances) represents a lifeline model from UML.
 */
public class UMLLifeline {

	private long id;
	private static long nextId = 0;
	private double xCoordinate;
	private double yCoordinate;
	private UMLClass objectClass;
	private double height;

	/**
	 * Creates an instance representing an UML lifeline model.
	 *
	 * @param objectClass Class which instance lifeline represents.
	 * @param height Lifeline height on application frontend.
	 */
	public UMLLifeline(UMLClass objectClass, double height) {		
		this.objectClass = objectClass;
		this.height = height;
		this.xCoordinate = 0.0;
		this.yCoordinate = 0.0;
		this.id = nextId;
		nextId++;
	}

	/**
	 * Returns lifeline unique identifier.
	 *
	 * @return UMLLifeline identifier.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Get x coordinate of lifeline on frontend scene.
	 *
	 * @return Lifeline x coordinate.
	 */
	public double getXCoordinate() {
		return this.xCoordinate;
	}

	/**
	 * Set x coordinate of lifeline on frontend scene.
	 *
	 * @param xCoordinate Lifeline x coordinate.
	 */
	public void setXCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * Get y coordinate of lifeline on frontend scene.
	 *
	 * @return Lifeline y coordinate.
	 */
	public double getYCoordinate() {
		return this.yCoordinate;
	}

	/**
	 * Set y coordinate of lifeline on frontend scene.
	 *
	 * @param yCoordinate Lifeline y coordinate.
	 */
	public void setYCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * Return class which instance lifeline represents.
	 *
	 * @return Class diagram class.
	 */
	public UMLClass getObjectClass() {
		return this.objectClass;
	}

	/**
	 * Set new class which instance lifeline represents.
	 *
	 * @param objectClass New class instance.
	 */
	public void setObjectClass(UMLClass objectClass) {
		this.objectClass = objectClass;
	}

	/**
	 * Return actual lifeline height.
	 *
	 * @return Lifeline height.
	 */
	public double getHeight() {
		return this.height;
	}

	/**
	 * Set new lifeline height.
	 *
	 * @param height New lifeline height.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

}