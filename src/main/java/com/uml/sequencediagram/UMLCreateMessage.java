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
 * A class represents a create message.
 */
public class UMLCreateMessage extends UMLMessageOperationType {
	/**
	 * Creates an UMLCreateMessage instance with start lifeline and end lifeline.
	 *
	 * @param fromLifeline Start lifeline.
	 * @param toLifeline   End lifeline.
	 */
	public UMLCreateMessage(UMLLifeline fromLifeline, 
	                        UMLLifeline toLifeline) {
		super(fromLifeline, toLifeline);
	}

}