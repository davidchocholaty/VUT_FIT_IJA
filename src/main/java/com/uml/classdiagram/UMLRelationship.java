/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */


package com.uml.classdiagram;

/**
 * A class represents a relationship that has start UMLClass (from) and end UMLClass (to).
 * <p>
 *     For identifying relationship it has unique identifier in class diagram.
 * </p>
 */
public abstract class UMLRelationship extends Element {

	private UMLClass from;
	private UMLClass to;
	private static long nextId = 0;
	private final long id;

	/**
	 * Creates an UMLRelationship instance with start class and end class.
	 *
	 * <p>
	 *     Created instance has unique identifier in class diagram.
	 * </p>
	 *
	 * @param from Start class.
	 * @param to   End class.
	 */
	public UMLRelationship(UMLClass from, UMLClass to) {
		super(null);
		this.from = from;
		this.to = to;
		this.id = nextId;
		nextId++;
	}

	/**
	 * Returns the start class.
	 *
	 * @return Start class.
	 */
	public UMLClass getFrom() {
		return this.from;
	}

	/**
	 * Change start class.
	 *
	 * @param from Start class.
	 */
	public void setFrom(UMLClass from) {
		this.from = from;
	}

	/**
	 * Returns the end class.
	 *
	 * @return End class.
	 */
	public UMLClass getTo() {
		return this.to;
	}

	/**
	 * Change end class.
	 *
	 * @param to End class.
	 */
	public void setTo(UMLClass to) {
		this.to = to;
	}

	/**
	 * Returns relationship unique identifier.
	 *
	 * @return UMLRelationship identifier.
	 */
	public long getId() {
		return this.id;
	}

}
