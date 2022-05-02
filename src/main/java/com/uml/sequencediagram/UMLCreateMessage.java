package com.uml.sequencediagram;

public class UMLCreateMessage extends UMLMessageLabelType {

	public UMLCreateMessage(UMLLifeline fromLifeline, 
	                        UMLLifeline toLifeline, 
							String label) {
		super(fromLifeline, toLifeline, label);
	}

}