package com.uml.sequencediagram;

import com.uml.classdiagram.UMLClass;
import com.uml.customexception.InvalidOperationLabel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequenceDiagram extends Element {

	private List<UMLMessage> diagramMessages;
	private List<UMLLifeline> diagramLifelines;

	public SequenceDiagram(String name) {
		super(name);
		this.diagramMessages = new ArrayList<UMLMessage>();
		this.diagramLifelines = new ArrayList<UMLLifeline>();
	}

	public UMLLifeline createLifeline(UMLClass classObject, double height) {
		UMLLifeline newLifeline = new UMLLifeline(classObject, height);
		diagramLifelines.add(newLifeline);

		return newLifeline;
	}

	public boolean deleteLifeline(UMLLifeline lifeline) {
		int idx;

		if ((idx = diagramLifelines.indexOf(lifeline)) != -1) {
			diagramLifelines.remove(idx);
			return true;
		}

		return false;
	}

	public void deleteAllLifelineMessages(UMLLifeline lifeline) {
		int idx;
		List<UMLMessage> messagesToDelete = new ArrayList<UMLMessage>();		

		for (UMLMessage currentMessage : diagramMessages) {
			if (currentMessage.getFromLifeline().equals(lifeline) ||
			    currentMessage.getToLifeline().equals(lifeline)) {
				messagesToDelete.add(currentMessage);
			}
		}

		for (UMLMessage currentMessage : messagesToDelete) {
			idx = diagramMessages.indexOf(currentMessage);
			diagramMessages.remove(idx);
		}
	}	

	public List<UMLLifeline> getLifelines() {
		return Collections.unmodifiableList(this.diagramLifelines);
	}

	public UMLSynchronousMessage createSynchronousMessage(UMLLifeline fromLifeline,
	                                                      UMLLifeline toLifeline,
														  String operation,
														  String[] operationArguments) throws InvalidOperationLabel {
		UMLSynchronousMessage newSyncMessage = new UMLSynchronousMessage(fromLifeline, toLifeline);

		boolean exitSuccess = newSyncMessage.setOperation(operation, operationArguments);

		if (!exitSuccess) {
			throw new InvalidOperationLabel("Invalid synchronous message operation.");
		}

		this.diagramMessages.add(newSyncMessage);

		return newSyncMessage;
	}

	public UMLAsynchronousMessage createAsynchronousMessage(UMLLifeline fromLifeline,
	                                                        UMLLifeline toLifeline,
														    String operation,
															String[] operationArguments) throws InvalidOperationLabel {
		UMLAsynchronousMessage newAsyncMessage = new UMLAsynchronousMessage(fromLifeline, toLifeline);
		
		boolean exitSuccess = newAsyncMessage.setOperation(operation, operationArguments);

		if (!exitSuccess) {
			throw new InvalidOperationLabel("Invalid asynchronous message operation.");
		}
		
		this.diagramMessages.add(newAsyncMessage);

		return newAsyncMessage;
	}

	public UMLSelfMessage createSelfMessage(UMLLifeline lifeline,
	                                        String operation,
											String[] operationArguments) throws InvalidOperationLabel {
		UMLSelfMessage newSelfMessage = new UMLSelfMessage(lifeline);
		
		boolean exitSuccess = newSelfMessage.setOperation(operation, operationArguments);

		if (!exitSuccess) {
			throw new InvalidOperationLabel("Invalid self message operation.");
		}
		
		this.diagramMessages.add(newSelfMessage);

		return newSelfMessage;
	}

	public UMLCreateMessage createCreateMessage(UMLLifeline fromLifeline,
												UMLLifeline toLifeline,
												String[] operationArguments) throws InvalidOperationLabel {
		UMLCreateMessage newCreateMessage = new UMLCreateMessage(fromLifeline, toLifeline);

		boolean exitSuccess = newCreateMessage.setOperation(null, operationArguments);

		if (!exitSuccess) {
			throw new InvalidOperationLabel("Invalid create message operation arguments.");
		}

		this.diagramMessages.add(newCreateMessage);

		return newCreateMessage;
	}

	public UMLReturnMessage createReturnMessage(UMLLifeline fromLifeline,
	                                            UMLLifeline toLifeline,
												String label) {
		UMLReturnMessage newReturnMessage = new UMLReturnMessage(fromLifeline, toLifeline, label);
		this.diagramMessages.add(newReturnMessage);

		return newReturnMessage;
	}

	public UMLDestroyMessage createDestroyMessage(UMLLifeline fromLifeline,
	                                              UMLLifeline toLifeline,
											      String label) {
		UMLDestroyMessage newDestroyMessage = new UMLDestroyMessage(fromLifeline, toLifeline, label);
		this.diagramMessages.add(newDestroyMessage);

		return newDestroyMessage;
	}

	public List<UMLMessage> getMessages() {
		return Collections.unmodifiableList(this.diagramMessages);
	}

}