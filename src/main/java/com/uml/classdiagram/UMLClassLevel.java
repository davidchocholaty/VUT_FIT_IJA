/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing a class level UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */


package com.uml.classdiagram;

/**
 * A class represents a class level relationship.
 */
public class UMLClassLevel extends UMLRelationship {

	/**
	 * Create an UMLClassLevel instance with start class and end class.
	 * @param from Start class.
	 * @param to   End class.
	 */
	public UMLClassLevel(UMLClass from, UMLClass to) {
		super(from, to);
	}

}
