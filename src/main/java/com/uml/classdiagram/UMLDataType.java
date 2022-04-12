/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an data type.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * A class (its instances) represents a data type.
 * <p>
 *     Data type is user defined.
 * </p>
 */
public class UMLDataType extends UMLClassifier {

	/**
	 * Creates an instance representing a data type.
	 *
	 * @param dataType Data type
	 */
	public UMLDataType(String dataType) {
		super(dataType);
	}

	/**
	 * Creates an instance representing a data type.
	 *
	 * @param dataType Data type
	 */
	public UMLDataType(String dataType, boolean isUserDefined) {
		super(dataType, isUserDefined);
	}

	/**
	 * Implementation of parent abstract class.
	 *
	 * <p>
	 *     It is not necessary to specifically implement this method for the current class.
	 * </p>
	 */
	public void setDefaultName(){}

	/**
	 * The factory method for creating an UMLDataType instance for the specified data type.
	 * An instance represents a data type that is not modeled in the diagram.
	 *
	 * @param dataType Data type.
	 * @return         Created data type.
	 */
	public static UMLDataType forName(String dataType) {
		return new UMLDataType(dataType, false);
	}

	/**
	 * Returns a string representing the data type.
	 *
	 * @return String representing the data type.
	 */
	@Override
	public String toString() {
		return super.getName();
	}
}
