/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an operation with name, return data type and arguments.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */


package com.uml.classdiagram;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class represents an operation that has its name, the return type
 * and argument list. It is derived (extended) from the UMLAttribute class,
 * from which it recieves the name and return type. The argument is represented
 * with class UMLAttribute. Used as part of the UML class classifier or interface.
 */
public class UMLOperation extends UMLAttribute {

	private final List<UMLAttribute> operationArguments;
	private static int defaultOperationId = 1;

	/**
	 * Constructor for creating an operation with a given name and return type.
	 *
	 * @param name Operation name.
	 * @param type Return type.
	 */
	public UMLOperation(String name, UMLDataType type) {
		super(name, type);
		this.operationArguments = new ArrayList<UMLAttribute>();
	}

	/**
	 * Adds a new argument to the argument list.
	 * <p>
	 * 		The argument is inserted at the end of the list. If an argument with the same name already exists
	 * 		in the list, it will not perform the operation.
	 * </p>
	 *
	 * @param arg New argument.
	 * @return    True if new argument was added. False otherwise.
	 */
	public boolean addArgument(UMLAttribute arg) {
		for (UMLAttribute attr : operationArguments) {
			if (attr.getName().equals(arg.getName())) {
				return false;
			}
		}

		operationArguments.add(arg);

		return true;
	}

	/**
	 * Factory method for creating an instance of an operation.
	 *
	 * @param name Operation name.
	 * @param type Operation return type.
	 * @param args List of operation arguments.
	 * @return     Object which represents operation in UML diagram.
	 */
	public static UMLOperation create(String name, UMLDataType type, UMLAttribute... args) {
		UMLOperation newOperation = new UMLOperation(name, type);

		for (UMLAttribute attr : args) {
			newOperation.addArgument(attr);
		}

		return newOperation;
	}

	/**
	 * Returns an unmodifiable argument list.
	 *
	 * @return Unmodifiable argument list.
	 */
	public List<UMLAttribute> getArguments() {
		return Collections.unmodifiableList(operationArguments);
	}

	/**
	 * Delete an argument from argument list.
	 *
	 * @param arg Deleted argument.
	 * @return    True if operation was successful. False otherwise.
	 */
	public boolean deleteArgument(UMLAttribute arg) {
		int idx;

		if ((idx = operationArguments.indexOf(arg)) != -1) {
			operationArguments.remove(idx);
			return true;
		}

		return false;
	}

	public static UMLOperation createDefault() {
		UMLOperation newOperation = new UMLOperation(null, UMLDataType.forName("None"));
		newOperation.setDefaultName();

		return newOperation;
	}

	public static UMLOperation createDefault(UMLAttribute... args) {
		UMLOperation newOperation = create(null, UMLDataType.forName("None"), args);
		newOperation.setDefaultName();

		return newOperation;
	}

	/**
	 * Set default operation name in format operationN.
	 * <p>
	 *     For example first default operation name is "operation".
	 *     Then the second one is operation2, operation3 and so on.
	 * </p>
	 */
	@Override
	public void setDefaultName() {
		if (defaultOperationId == 1) {
			super.setName("operation");
		} else {
			super.setName("operation" + String.valueOf(defaultOperationId));
		}

		defaultOperationId++;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append(this.getName());
		str.append("(");

		if(this.operationArguments.size() != 0) {
			this.operationArguments.forEach(attr -> {str.append(attr); str.append(", ");});
			str.setLength(str.length() - 2); // delete last ", "
		}

		str.append(")");

		String returnType = this.getType().toString();

		if(!returnType.equals("None")) {
			str.append(" : ");
			str.append(this.getType().toString());
		}

		return str.toString();
	}
}
