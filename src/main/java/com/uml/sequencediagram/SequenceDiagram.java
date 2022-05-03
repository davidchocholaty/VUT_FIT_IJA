/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.sequencediagram;

import com.uml.classdiagram.UMLClass;
import com.uml.customexception.InvalidOperationLabel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class represents a sequence diagram.
 *
 * <p>
 *     Sequence diagram contains list of messages and lifelines.
 * </p>
 */
public class SequenceDiagram extends Element {

	private List<UMLMessage> diagramMessages;
	private List<UMLLifeline> diagramLifelines;

	/**
	 * Create an instance of SequenceDiagram.
	 *
	 * @param name Sequence diagram name.
	 */
	public SequenceDiagram(String name) {
		super(name);
		this.diagramMessages = new ArrayList<UMLMessage>();
		this.diagramLifelines = new ArrayList<UMLLifeline>();
	}

	/**
	 * Creates an instance of the UML lifeline and inserts it into the diagram.
	 *
	 * @param classObject Class which instance the current lifeline represents.
	 * @param height Height of lifeline on application frontend.
	 * @return Return new UMLLifeline instance.
	 */
	public UMLLifeline createLifeline(UMLClass classObject, double height) {
		UMLLifeline newLifeline = new UMLLifeline(classObject, height);
		diagramLifelines.add(newLifeline);

		return newLifeline;
	}

	/**
	 * Delete lifeline from sequence diagram if exists.
	 *
	 * @param lifeline Lifeline to be deleted.
	 * @return Status of operation (true/false);
	 */
	public boolean deleteLifeline(UMLLifeline lifeline) {
		int idx;

		if ((idx = diagramLifelines.indexOf(lifeline)) != -1) {
			diagramLifelines.remove(idx);
			return true;
		}

		return false;
	}

	/**
	 * Delete all messages which use the lifeline to be deleted.
	 * <p>
	 *     The lifeline is in from or to attribute of message.
	 * </p>
	 *
	 * @param lifeline Lifeline which messages should be deleted.
	 */
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

	/**
	 * Method for obtaining diagram lifelines.
	 *
	 * @return Unmodifiable list with diagram lifelines.
	 */
	public List<UMLLifeline> getLifelines() {
		return Collections.unmodifiableList(this.diagramLifelines);
	}

	/**
	 * Creates an instance of the UML synchronous message and inserts it into the diagram.
	 *
	 * @param fromLifeline Lifeline where the message starts.
	 * @param toLifeline Lifeline where the message ends.
	 * @param operation Method name.
	 * @param operationArguments Method parameters.
	 * @return New instance of an UMLSynchronousMessage.
	 * @throws InvalidOperationLabel Invalid operation name or operation arguments.
	 */
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

	/**
	 * Creates an instance of the UML asynchronous message and inserts it into the diagram.
	 *
	 * @param fromLifeline Lifeline where the message starts.
	 * @param toLifeline Lifeline where the message ends.
	 * @param operation Method name.
	 * @param operationArguments Method parameters.
	 * @return New instance of an UMLAsynchronousMessage.
	 * @throws InvalidOperationLabel Invalid operation name or operation arguments.
	 */
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

	/**
	 * Creates an instance of the UML self message and inserts it into the diagram.
	 *
	 * @param lifeline Lifeline where the message starts and ends.
	 * @param operation Method name.
	 * @param operationArguments Method parameters.
	 * @return New instance of an UMLSelfMessage.
	 * @throws InvalidOperationLabel Invalid operation name or operation arguments.
	 */
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

	/**
	 * Creates an instance of the UML create message and inserts it into the diagram.
	 *
	 * @param fromLifeline Lifeline where the message starts.
	 * @param toLifeline Lifeline where the message ends.
	 * @param operationArguments Method parameters.
	 * @return New instance of an UMLCreateMessage.
	 * @throws InvalidOperationLabel Invalid operation arguments.
	 */
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

	/**
	 * Creates an instance of the UML return message and inserts it into the diagram.
	 *
	 * @param fromLifeline Lifeline where the message starts.
	 * @param toLifeline Lifeline where the message ends.
	 * @param label Lifeline label.
	 * @return New instance of an UMLReturnMessage.
	 */
	public UMLReturnMessage createReturnMessage(UMLLifeline fromLifeline,
	                                            UMLLifeline toLifeline,
												String label) {
		UMLReturnMessage newReturnMessage = new UMLReturnMessage(fromLifeline, toLifeline, label);
		this.diagramMessages.add(newReturnMessage);

		return newReturnMessage;
	}

	/**
	 * Creates an instance of the UML destroy message and inserts it into the diagram.
	 *
	 * @param fromLifeline Lifeline where the message starts.
	 * @param toLifeline Lifeline where the message ends.
	 * @param label Lifeline label.
	 * @return New instance of an UMLDestroyMessage.
	 */
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