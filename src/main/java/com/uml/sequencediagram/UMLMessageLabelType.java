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
 * A class represents a label type message.
 * <p>
 *     A label type message contains basic label with plain text content.
 * </p>
 */
public abstract class UMLMessageLabelType extends UMLMessage {
	/**
	 * Creates an UMLMessageLabelType instance with start lifeline, end lifeline and label.
	 *
	 * @param fromLifeline Start lifeline.
	 * @param toLifeline   End lifeline.
	 * @param label        Lifeline label.
	 */
	public UMLMessageLabelType(UMLLifeline fromLifeline, 
	                           UMLLifeline toLifeline,
                               String label) {
		super(fromLifeline, toLifeline, label);        
	}

	/**
	 * Method sets new basic label of message.
	 *
	 * @param newLabel New label.
	 */
    public void setLabel(String newLabel) {
        super.setLabel(newLabel);
    }

}