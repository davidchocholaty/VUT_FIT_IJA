package com.uml.sequencediagram;

public class UMLSelfMessage extends UMLMessageOperationType {

	public UMLSelfMessage(UMLLifeline lifeline) {
		super(lifeline, lifeline);
	}

}