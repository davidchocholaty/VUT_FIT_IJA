/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.sequencediagram;

import com.uml.classdiagram.UMLOperation;
import java.util.List;

/**
 * A class represents an operation type message.
 * <p>
 *     An operation type message contains label with method name and method parameters.
 * </p>
 */
public abstract class UMLMessageOperationType extends UMLMessage {
    /**
     * Creates an UMLMessageLabelType instance with start lifeline and end lifeline.
     * @param fromLifeline Start lifeline.
     * @param toLifeline End lifeline.
     */
	public UMLMessageOperationType(UMLLifeline fromLifeline, 
	                               UMLLifeline toLifeline) {
		super(fromLifeline, toLifeline, null);        
	}

    /**
     * Method sets new operation label of message with operation name and arguments.
     *
     * @param operation Operation name.
     * @param operationArguments Operation arguments.
     * @return True if operation name and arguments are valid and created in class diagram, false otherwise.
     */
    public boolean setOperation(String operation, String[] operationArguments) {
        List<UMLOperation> classOperations = super.getFromLifeline().getObjectClass().getOperations();

        if (operation.equals("<<create>>")) {
            return true;
        }

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