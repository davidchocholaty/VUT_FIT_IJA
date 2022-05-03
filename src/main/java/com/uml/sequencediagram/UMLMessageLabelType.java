/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.sequencediagram;

public abstract class UMLMessageLabelType extends UMLMessage {

	public UMLMessageLabelType(UMLLifeline fromLifeline, 
	                           UMLLifeline toLifeline,
                               String label) {
		super(fromLifeline, toLifeline, label);        
	}

    public void setLabel(String newLabel) {
        super.setLabel(newLabel);
    }

}