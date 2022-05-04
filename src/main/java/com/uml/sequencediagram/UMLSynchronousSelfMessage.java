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
 * A class represents a synchronous self message.
 */
public class UMLSynchronousSelfMessage extends UMLMessageOperationType {
	/**
	 * Creates an UMLSynchronousSelfMessage instance with start and end lifeline.
	 *
	 * @param lifeline Start and end lifeline.
	 */
	public UMLSynchronousSelfMessage(UMLLifeline lifeline) {
		super(lifeline, lifeline);
	}

}