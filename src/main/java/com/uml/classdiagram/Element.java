/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

/**
 * Class represents base named element.
 */
public abstract class Element {

	private String name;

	/**
	 * Create instance with specified name.
	 *
	 * @param name Element name.
	 */
	public Element(String name) {
		this.name = name;
	}

	/**
	 * Return element name.
	 *
	 * @return Element name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set new element name.
	 *
	 * @param name New element name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
