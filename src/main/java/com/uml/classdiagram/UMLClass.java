/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing a class model from UML.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class (its instances) represents a class model from UML.
 * <p>
 *     Extends the UMLClassifier class. Contains a list of attributes and operations (methods).
 *     Class can be abstract.
 * </p>
 */
public class UMLClass extends UMLClassifier {
	private boolean isAbstract;
	private double xCoordinate;
	private double yCoordinate;
	private UMLVisibilityType visibility;
	private final List<UMLAttribute> classAttributes;
	private final List<UMLOperation> classOperations;
	private static int defaultClassId = 1;

	/**
	 * Creates an instance representing an UML class model.
	 * <p>
	 *     Class is not abstract.
	 * </p>
	 *
	 * @param name Class name
	 */
	public UMLClass(String name) {
		super(name);
		this.isAbstract = false;
		this.visibility = UMLVisibilityType.UNSPECIFIED;
		this.classAttributes = new ArrayList<UMLAttribute>();
		this.classOperations = new ArrayList<UMLOperation>();
		this.xCoordinate = 0.0;
		this.yCoordinate = 0.0;
	}

	/**
	 * Test whether the object represents an abstract class model.
	 *
	 * @return If the class is abstract, it return true. Otherwise it returns false.
	 */
	public boolean isAbstract() {
		return this.isAbstract;
	}

	/**
	 * Changes the object information to see if it represents an abstract class.
	 * @param isAbstract Whether it is an abstract class or not.
	 */
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	/**
	 * Add attribute to class.
	 * <p>
	 *     Attribute is added to class if attribute with same name wasn't found in class.
	 * </p>
	 *
	 * @param attr Attribute to be added.
	 * @return Status of operation (true/false).
	 */
	public boolean addAttribute(UMLAttribute attr) {
		for (UMLAttribute listAttr : classAttributes) {
			if (listAttr.getName().equals(attr.getName())) {
				return false;
			}
		}

		classAttributes.add(attr);

		return true;
	}

	/**
	 * Set x coordinate of class on frontend scene.
	 *
	 * @param xCoordinate Class x coordinate.
	 */
	public void setXCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * Set y coordinate of class on frontend scene.
	 *
	 * @param yCoordinate Class y coordinate.
	 */
	public void setYCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * Get x coordinate of class on frontend scene.
	 *
	 * @return Class x coordinate.
	 */
	public double getXCoordinate() {
		return this.xCoordinate;
	}

	/**
	 * Get y coordinate of class on frontend scene.
	 *
	 * @return Class y coordinate.
	 */
	public double getYCoordinate() {
		return this.yCoordinate;
	}

	/**
	 * Delete class attribute.
	 *
	 * @param attr Class attribute to be deleted.
	 * @return Status of operation. True if attribute was found and deleted, false otherwise.
	 */
	public boolean deleteAttribute(UMLAttribute attr) {
		int idx;

		if ((idx = classAttributes.indexOf(attr)) != -1) {
			classAttributes.remove(idx);
			return true;
		}

		return false;
	}

	/**
	 * Return unmodifiable list of class attributes.
	 *
	 * @return Unmodifiable list of class attributes.
	 */
	public List<UMLAttribute> getAttributes() {
		return Collections.unmodifiableList(classAttributes);
	}

	/**
	 * Returns the position of the attribute in the attributes list.
	 * <p>
	 *     The position is indexed from the value 0.
	 *     If the class does not contain the given attribute, it returns -1.
	 * </p>
	 *
	 * @param attr Searched attribute.
	 * @return Position of attribute.
	 */
	public int getAttrPosition(UMLAttribute attr) {
		return this.classAttributes.indexOf(attr);
	}

	/**
	 * Moves the position of the attribute to the newly specified one.
	 *
	 * <p>
	 *     The position is indexed from the value 0. If the class does not contain the given attribute, it does nothing
	 *     and returns -1. When moving to the pos position, all items are created (attributes)
	 *     from position pos (inclusive) move one position to the right.
	 * </p>
	 *
	 * @param attr Moved attribute.
	 * @param pos New attribute position.
	 * @return Operation success or failure.
	 */
	public int moveAttrAtPosition(UMLAttribute attr, int pos) {
		if (classAttributes.remove(attr)) {
			classAttributes.add(pos, attr);
			return 0;
		}

		return -1;
	}

	/**
	 * Add operation to class.
	 * <p>
	 *     Operation is added to class if an identical operation wasn't found in class.
	 * </p>
	 *
	 * @param oper Operation to be added.
	 * @return Exit status (true/false).
	 */
	public boolean addOperation(UMLOperation oper) {
		for (UMLOperation listOper : classOperations) {
			if (listOper.getName().equals(oper.getName())) {
				return false;
			}
		}

		classOperations.add(oper);

		return true;
	}

	/**
	 * Delete class operation.
	 *
	 * @param oper Class operation to be deleted.
	 * @return Exit status. True if class operation was found and deleted, false otherwise.
	 */
	public boolean deleteOperation(UMLOperation oper) {
		int idx;

		if ((idx = classOperations.indexOf(oper)) != -1) {
			classOperations.remove(idx);
			return true;
		}

		return false;
	}

	/**
	 * Return unmodifiable list of class operations.
	 *
	 * @return Unmodifiable list of class operations.
	 */
	public List<UMLOperation> getOperations() {
		return Collections.unmodifiableList(classOperations);
	}

	/**
	 * Returns the position of the operation in the operations list.
	 * <p>
	 *     The position is indexed from the value 0.
	 *     If the class does not contain the given operation, it returns -1.
	 * </p>
	 *
	 * @param operation Searched operation.
	 * @return Position of operation.
	 */
	public int getOperationPosition(UMLOperation operation) {
		return this.classOperations.indexOf(operation);
	}

	/**
	 * Moves the position of the operation to the newly specified one.
	 *
	 * <p>
	 *     The position is indexed from the value 0. If the class does not contain the given operation, it does nothing
	 *     and returns -1. When moving to the pos position, all items are created (operations)
	 *     from position pos (inclusive) move one position to the right.
	 * </p>
	 *
	 * @param operation Moved operation.
	 * @param pos New operation position.
	 * @return Operation success or failure.
	 */
	public int moveOperationAtPosition(UMLOperation operation, int pos) {
		if (classOperations.remove(operation)) {
			classOperations.add(pos, operation);
			return 0;
		}

		return -1;
	}

	/**
	 * Return class visibility.
	 *
	 * @return Class visibility.
	 */
	public UMLVisibilityType getVisibility() {
		return this.visibility;
	}

	/**
	 * Change the class visibility.
	 * @param visibility Class visibility.
	 */
	public void setVisibility(UMLVisibilityType visibility) {
		this.visibility = visibility;
	}

	public static UMLClass createDefault() {
		UMLClass newClass = new UMLClass(null);
		newClass.setDefaultName();

		return newClass;
	}

	/**
	 * Set default class name in format ClassN.
	 * <p>
	 *     For example first default class name is "Class".
	 *     Then the second one is Class2, Class3 and so on.
	 * </p>
	 */
	public void setDefaultName() {
		if (defaultClassId == 1) {
			super.setName("Class");
		} else {
			super.setName("Class" + String.valueOf(defaultClassId));
		}

		defaultClassId++;
	}

}
