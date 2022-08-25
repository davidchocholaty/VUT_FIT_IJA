/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an association UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * A class represents an association relationship.
 */
public class UMLAssociation extends UMLInstanceLevel {

	/**
	 * Creates an UMLAssociation instance with start class and end class.
	 *
	 * @param from Start class.
	 * @param to End class.
	 */
	public UMLAssociation(UMLClass from, UMLClass to) {
		super(from, to);
	}

}
