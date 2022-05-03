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
 * A class represents a message that has start UMLLifeline (fromLifeline) and end UMLLifeline (toLifeline).
 * <p>
 *     For identifying message it has and unique identifier in class diagram.
 * </p>
 */
public abstract class UMLMessage extends Element {

	private long id;
	private static long nextId = 0;
	private double yCoordinate;
	private UMLLifeline fromLifeline;
	private UMLLifeline toLifeline;

	/**
	 * Creates an UMLMessage instance with start lifeline and end lifeline.
	 *
	 * @param fromLifeline Start lifeline.
	 * @param toLifeline End lifeline.
	 * @param label Lifeline label (basic label or operation label).
	 */
	public UMLMessage(UMLLifeline fromLifeline, 
					  UMLLifeline toLifeline,
					  String label) {
		super(label);	
		this.fromLifeline = fromLifeline;
		this.toLifeline = toLifeline;
		this.yCoordinate = 0.0;
		this.id = nextId;
		nextId++;
	}

	/**
	 * Returns message unique identifier.
	 *
	 * @return UMLMessage identifier.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Set new message label.
	 * <p>
	 *     New label can be of plain text type or operation type.
	 * </p>
	 *
	 * @param newLabel New message label.
	 */
	protected void setLabel(String newLabel) {
		super.setName(newLabel);
	}

	/**
	 * Return current label of message.
	 *
	 * @return Current label.
	 */
	public String getLabel() {
		return super.getName();
	}

	/**
	 * Get y coordinate of message on frontend scene.
	 *
	 * @return Message y coordinate.
	 */
	public double getYCoordinate() {
		return this.yCoordinate;
	}

	/**
	 * Set y coordinate of message on frontend scene.
	 *
	 * @param yCoordinate Message y coordinate.
	 */
	public void setYCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * Returns the start lifeline.
	 *
	 * @return Start lifeline.
	 */
	public UMLLifeline getFromLifeline() {
		return this.fromLifeline;
	}

	/**
	 * Change start lifeline.
	 *
	 * @param fromLifeline New start lifeline.
	 */
	public void setFromLifeline(UMLLifeline fromLifeline) {
		this.fromLifeline = fromLifeline;
	}

	/**
	 * Returns the end lifeline.
	 *
	 * @return End lifeline.
	 */
	public UMLLifeline getToLifeline() {
		return this.toLifeline;
	}

	/**
	 * Change end lifeline.
	 *
	 * @param toLifeline New end lifeline.
	 */
	public void setToLifeline(UMLLifeline toLifeline) {
		this.toLifeline = toLifeline;
	}
}