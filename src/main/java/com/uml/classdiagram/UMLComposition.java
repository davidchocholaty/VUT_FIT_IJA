/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing a composition UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * A class represents a composition relationship.
 */
public class UMLComposition extends UMLInstanceLevel {

	/**
	 * Creates an UMLComposition instance with start class and end class.
	 *
	 * @param from Start class.
	 * @param to   End class.
	 */
	public UMLComposition(UMLClass from, UMLClass to) {
		super(from, to);
	}

}
