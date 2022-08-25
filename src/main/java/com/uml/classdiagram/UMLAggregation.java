/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an aggregation UML relationship.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */


package com.uml.classdiagram;

/**
 * A class represents an aggregation relationship.
 */
public class UMLAggregation extends UMLInstanceLevel {

	/**
	 * Creates an UMLAggregation instance with start class and end class.
	 *
	 * @param from Start class.
	 * @param to End class.
	 */
	public UMLAggregation(UMLClass from, UMLClass to) {
		super(from, to);
	}

}
