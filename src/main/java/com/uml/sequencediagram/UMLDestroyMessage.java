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
 * A class represents a destroy message.
 */
public class UMLDestroyMessage extends UMLMessageLabelType {
	/**
	 * Creates an UMLDestroyMessage instance with start lifeline, end lifeline and label.
	 *
	 * @param fromLifeline Start lifeline.
	 * @param toLifeline   End lifeline.
	 * @param label        Lifeline label.
	 */
	public UMLDestroyMessage(UMLLifeline fromLifeline, 
							 UMLLifeline toLifeline, 
							 String label) {
		super(fromLifeline, toLifeline, label);
	}

}