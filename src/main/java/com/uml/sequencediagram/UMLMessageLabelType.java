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