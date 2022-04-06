/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an attribute with name and type.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * A class represents an attribute that has its name and type.
 * It is derived (extended) from the Element class. Attribute type
 * is represented by the UMLClassifier class. Used as a UML class attribute
 * or operation argument.
 */
public class UMLAttribute extends Element {

	private UMLDataType type;
	private UMLVisibilityType visibility;
	private static int defaultAttrId = 1;
	private String defaultValue;
	/**
	 * Creates an attribute instance.
	 *
	 * @param name Attribute name.
	 * @param type Attribute type.
	 */
	public UMLAttribute(String name, UMLDataType type) {
		super(name);
		this.type = type;
		this.visibility = UMLVisibilityType.UNSPECIFIED;
		this.defaultValue = "";
	}

	/**
	 * Return attribute data type.
	 *
	 * @return Attribute data type
	 */
	public UMLDataType getType() {
		return this.type;
	}

	/**
	 * Change attribute data type.
	 *
	 * @param type Attribute new data type.
	 */
	public void setType(UMLDataType type) {
		this.type = type;
	}

	/**
	 * Return attribute visibility.
	 *
	 * @return Attribute visibility.
	 */
	public UMLVisibilityType getVisibility() {
		return this.visibility;
	}

	/**
	 * Set attribute visibility.
	 *
	 * @param visibility Attribute visibility.
	 */
	public void setVisibility(UMLVisibilityType visibility) {
		this.visibility = visibility;
	}

	public static UMLAttribute createDefault() {
		UMLAttribute newAttribute = new UMLAttribute(null, UMLDataType.forName("None"));
		newAttribute.setDefaultName();

		return newAttribute;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Set default attribute name in format attributeN.
	 * <p>
	 *     For example first default attribute name is "attribute".
	 *     Then the second one is attribute2, attribute3 and so on.
	 * </p>
	 */
	public void setDefaultName() {
		if (defaultAttrId == 1) {
			super.setName("attribute");
		} else {
			super.setName("attribute" + String.valueOf(defaultAttrId));
		}

		defaultAttrId++;
	}

	/**
	 * Returns a string representing the state of the attribute in the form "name : type".
	 *
	 * @return String representing the attribute.
	 */
	@Override
	public String toString() {
		return super.getName() + " : " + type.toString();
	}
}
