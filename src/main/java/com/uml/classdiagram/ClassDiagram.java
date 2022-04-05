/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing class diagram.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.classdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class represents a class diagram.
 *
 * <p>
 *     Class diagram contains list of classes, relationships and defined data types.
 * </p>
 */
public class ClassDiagram extends Element {

	private List<UMLClass> diagramClasses;
	private List<UMLRelationship> diagramRelationships;
	private List<UMLDataType> diagramDataTypes;

	/**
	 * Create an instance of ClassDiagram.
	 * <p>
	 *     Standard UML data types are set.
	 * </p>
	 *
	 * @param name
	 */
	public ClassDiagram(String name) {
		super(name);
		this.diagramClasses = new ArrayList<UMLClass>();
		this.diagramRelationships = new ArrayList<UMLRelationship>();
		this.diagramDataTypes = defaultDiagramDataTypes();
	}

	/**
	 * Method for setting default UML primitive data types for class diagram.
	 *
	 * @return List with UML primitive data types.
	 */
	private ArrayList<UMLDataType> defaultDiagramDataTypes() {
		ArrayList<UMLDataType> defaultDataTypes = new ArrayList<UMLDataType>();

		defaultDataTypes.add(new UMLDataType("None"));
		defaultDataTypes.add(new UMLDataType("Boolean"));
		defaultDataTypes.add(new UMLDataType("Integer"));
		defaultDataTypes.add(new UMLDataType("UnlimitedNatural"));
		defaultDataTypes.add(new UMLDataType("String"));
		defaultDataTypes.add(new UMLDataType("Real"));

		return defaultDataTypes;
	}

	/**
	 * Creates an instance of the UML class and inserts it into the diagram.
	 * <p>
	 *     If a class with the same name already exists in the diagram, it does nothing.
	 * </p>
	 *
	 * @param name Class name
	 */
	public UMLClass createClass(String name) {
		for (UMLClass currentClass : diagramClasses) {
			if (currentClass.getName().equals(name)) {
				return null;
			}
		}

		UMLClass newClass = new UMLClass(name);
		diagramClasses.add(newClass);

		return newClass;
	}

	public UMLClass createDefaultClass() {
		UMLClass newClass = UMLClass.createDefault();
		diagramClasses.add(newClass);

		return newClass;
	}

	public boolean setNewClassName(String oldName, String newName) {
		for (UMLClass currentClass : diagramClasses) {
			if (currentClass.getName().equals(newName)) {
				return false;
			}
		}

		for (UMLClass currentClass : diagramClasses) {
			if (currentClass.getName().equals(oldName)) {
				currentClass.setName(newName);
				return true;
			}
		}

		return false;
	}

	public UMLInterface createInterface(String name) {
		for (UMLClass currentClass : diagramClasses) {
			if (currentClass.getName().equals(name)) {
				return null;
			}
		}

		UMLInterface newInterface = new UMLInterface(name);
		diagramClasses.add(newInterface);

		return newInterface;
	}

	public UMLInterface createDefaultInterface() {
		UMLInterface newInterface = UMLInterface.createDefault();
		diagramClasses.add(newInterface);

		return newInterface;
	}

	/**
	 * Find the data type by name in the diagram.
	 *
	 * <p>
	 *     If it does not exist, create an UMLDataType instance
	 *     representing a data type that is not captured in the diagram
	 * 	   (see UMLDataType.forName (java.lang.String));
	 * 	   This instance is included in the diagram structures, ie. that at next
	 * 	   this already created instance will be used in the search attempt.
	 * </p>
	 *
	 * @param dataTypeName
	 * @return
	 */
	public UMLDataType dataTypeForName(String dataTypeName) {
		for (UMLDataType dataType : diagramDataTypes) {
			if (dataType.getName().equals(dataTypeName)) {
				return dataType;
			}
		}

		UMLDataType newDataType = UMLDataType.forName(dataTypeName);
		diagramDataTypes.add(newDataType);

		return newDataType;
	}

	/**
	 * Find the class by name in the diagram.
	 *
	 * @param name Class name.
	 * @return     Class object if class was found in diagram. Null otherwise.
	 */
	public UMLClass findClass(String name) {
		for (UMLClass currentClass : diagramClasses) {
			if (currentClass.getName().equals(name)) {
				return currentClass;
			}
		}

		return null;
	}

	public List<UMLClass> getClasses() {
		return Collections.unmodifiableList(this.diagramClasses);
	}

	public UMLAssociation createAssociationRelationship(UMLClass from, UMLClass to) {
		UMLAssociation newAssociation = new UMLAssociation(from, to);
		this.diagramRelationships.add(newAssociation);

		return newAssociation;
	}

	public UMLAggregation createAggregationRelationship(UMLClass from, UMLClass to) {
		UMLAggregation newAggregation = new UMLAggregation(from, to);
		this.diagramRelationships.add(newAggregation);

		return newAggregation;
	}

	public UMLComposition createCompositionRelationship(UMLClass from, UMLClass to) {
		UMLComposition newComposition = new UMLComposition(from, to);
		this.diagramRelationships.add(newComposition);

		return newComposition;
	}

	public UMLInheritance createInheritanceRelationship(UMLClass from, UMLClass to) {
		UMLInheritance newInheritance = new UMLInheritance(from, to);
		this.diagramRelationships.add(newInheritance);

		return newInheritance;
	}

	public List<UMLRelationship> getRelationships() {
		return Collections.unmodifiableList(this.diagramRelationships);
	}
}
