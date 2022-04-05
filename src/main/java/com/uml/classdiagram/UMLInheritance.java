/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an inheritance UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */


package com.uml.classdiagram;

/**
 * A class represents an inheritance relationship.
 */
public class UMLInheritance extends UMLClassLevel {

	/**
	 * Creates an UMLInheritance instance with start class and end class.
	 *
	 * @param from Start class.
	 * @param to   End class.
	 */
	public UMLInheritance(UMLClass from, UMLClass to) {
		super(from, to);
	}

}
