/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an interface model from UML.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * A class (its instances) represents an interface model from UML.
 * <p>
 *     Extends the UMLClass class, so it contains a list of attributes
 *     and operations (methods).
 * </p>
 */
public class UMLInterface extends UMLClass {
	private static int defaultInterfaceId = 1;

	/**
	 * Creates an instance representing a UML interface model.
	 *
	 * @param name Interface name
	 */
	public UMLInterface(String name) {
		super(name);
	}

	public static UMLInterface createDefault() {
		UMLInterface newInterface = new UMLInterface(null);
		newInterface.setDefaultName();

		return newInterface;
	}

	/**
	 * Set default interface name in format InterfaceN.
	 * <p>
	 *     For example first default interface name is "Interface".
	 *     Then the second one is Interface2, Interface3 and so on.
	 * </p>
	 */
	@Override
	public void setDefaultName() {
		if (defaultInterfaceId == 1) {
			super.setName("Interface");
		} else {
			super.setName("Interface" + String.valueOf(defaultInterfaceId));
		}

		defaultInterfaceId++;
	}

}
