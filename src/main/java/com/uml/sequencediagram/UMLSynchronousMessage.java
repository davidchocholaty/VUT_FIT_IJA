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
 * A class represents an synchronous message.
 */
public class UMLSynchronousMessage extends UMLMessageOperationType {
	/**
	 * Creates an UMLSynchronousMessage instance with start lifeline and end lifeline.
	 *
	 * @param fromLifeline Start lifeline.
	 * @param toLifeline   End lifeline.
	 */
	public UMLSynchronousMessage(UMLLifeline fromLifeline, 
	                             UMLLifeline toLifeline) {
		super(fromLifeline, toLifeline);
	}

}