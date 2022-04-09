/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an instance level UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * A class represents an instance level relationship.
 * <p>
 *     An instance level relationship can contains multiplicity types at both ends
 *     of relationship and roles strings as well.
 * </p>
 */
public abstract class UMLInstanceLevel extends UMLRelationship {

	private UMLMultiplicityType fromMultiplicity;
	private UMLMultiplicityType toMultiplicity;
	private String fromRole;
	private String toRole;

	/**
	 * Creates an UMLInstanceLevel instance with start class and end class.
	 *
	 * @param from Start class.
	 * @param to   End class.
	 */
	public UMLInstanceLevel(UMLClass from, UMLClass to) {
		super(from, to);
		this.fromMultiplicity = UMLMultiplicityType.UNSPECIFIED;
		this.toMultiplicity = UMLMultiplicityType.UNSPECIFIED;
		this.fromRole = "";
		this.toRole = "";
	}

	/**
	 * Returns the multiplicity type of the starting class.
	 * @return Starting class multiplicity type.
	 */
	public UMLMultiplicityType getFromMultiplicity() {
		return this.fromMultiplicity;
	}

	/**
	 * Set the multiplicity type of the starting class.
	 *
	 * @param fromMultiplicity Starting class multiplicity type.
	 */
	public void setFromMultiplicity(UMLMultiplicityType fromMultiplicity) {
		this.fromMultiplicity = fromMultiplicity;
	}

	/**
	 * Returns the multiplicity type of the ending class.
	 * @return Ending class multiplicity type.
	 */
	public UMLMultiplicityType getToMultiplicity() {
		return this.toMultiplicity;
	}

	/**
	 * Set the multiplicity type of the ending class.
	 *
	 * @param toMultiplicity Ending class multiplicity type.
	 */
	public void setToMultiplicity(UMLMultiplicityType toMultiplicity) {
		this.toMultiplicity = toMultiplicity;
	}

	/**
	 * Returns the role of the starting class.
	 * @return Starting class role.
	 */
	public String getFromRole() {
		return this.fromRole;
	}

	/**
	 * Set the role of the starting class.
	 *
	 * @param fromRole Starting class role.
	 */
	public void setFromRole(String fromRole) {
		this.fromRole = fromRole;
	}

	/**
	 * Returns the role of the ending class.
	 * @return Ending class role.
	 */
	public String getToRole() {
		return this.toRole;
	}

	/**
	 * Set the role of the ending class.
	 *
	 * @param toRole Ending class role.
	 */
	public void setToRole(String toRole) {
		this.toRole = toRole;
	}

}
