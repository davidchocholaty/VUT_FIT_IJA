package com.uml.sequencediagram;

import com.uml.classdiagram.UMLOperation;
import java.util.List;

public abstract class UMLMessageOperationType extends UMLMessage {

	public UMLMessageOperationType(UMLLifeline fromLifeline, 
	                               UMLLifeline toLifeline) {
		super(fromLifeline, toLifeline, null);        
	}

    public boolean setOperation(String operation, String[] operationArguments) {
        List<UMLOperation> classOperations = super.getFromLifeline().getObjectClass().getOperations();

        for (UMLOperation currentOperation : classOperations) {
            if (currentOperation.getName().equals(operation)) {                
                if (currentOperation.getArguments().size() == operationArguments.length) {                    
                    StringBuilder label = new StringBuilder();

                    for (int i = 0; i < operationArguments.length; i++) {
                        label.append(operationArguments[i]);
                        label.append(", ");
                    }

                    String resultLabel = operation + "(" + label.substring(0, label.length() - 2) + ")";

                    super.setLabel(resultLabel);

                    return true;
                }
            }
        }

        return false;
    }

}