/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base classifier.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * Class represents classifier in diagram.
 *
 * <p>
 *     Derived classes represent specific forms of classifiers
 *     (class, interface, attribute. etc.).
 * </p>
 */
public abstract class UMLClassifier extends Element {
	private final boolean isUserDefined;

	/**
	 * Create instance of class UMLClassifier.
	 * <p>
	 *     Instance is user defined (is a part of diagram).
	 * </p>
	 *
	 * @param name Classifier name.
	 */
	protected UMLClassifier(String name) {
		super(name);
		isUserDefined = true;
	}

	/**
	 * Create instance of class UMLClasifier.
	 *
	 * @param name          Classifier name.
	 * @param isUserDefined User defined (part of diagram).
	 */
	public UMLClassifier(String name, boolean isUserDefined) {
		super(name);
		this.isUserDefined = isUserDefined;
	}

	/**
	 * Abstract method for setting the default classifier name.
	 * <p>
	 *     Concrete variants are implemented in inhereted classes.
	 * </p>
	 */
	public abstract void setDefaultName();

	/**
	 * Determines whether the object represents a classifier
	 * that is modeled by the user in the diagram or not.
	 *
	 * @return If classifier is user defined (is a part of diagram),
	 *         return true. False otherwise.
	 */
	public boolean isUserDefined() {
		return this.isUserDefined;
	}

}
