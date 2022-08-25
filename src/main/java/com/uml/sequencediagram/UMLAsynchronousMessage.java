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
 * A class represents an asynchronous message.
 */
public class UMLAsynchronousMessage extends UMLMessageOperationType {
	/**
	 * Creates an UMLAsynchronousMessage instance with start lifeline and end lifeline.
	 *
	 * @param fromLifeline Start lifeline.
	 * @param toLifeline   End lifeline.
	 */
	public UMLAsynchronousMessage(UMLLifeline fromLifeline, 
					              UMLLifeline toLifeline) {
		super(fromLifeline, toLifeline);
	}

}