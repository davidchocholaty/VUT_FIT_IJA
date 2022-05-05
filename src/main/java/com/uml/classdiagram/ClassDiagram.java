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
	 * @param name Class diagram name.
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
	 * @param name Class name.
	 * @return If class name is valid, return new UMLClass instance. Otherwise, return null.
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

	/**
	 * Creates an instance of the UML class with default name and inserts it into the diagram.
	 *
	 * @return New default UMLClass instance.
	 */
	public UMLClass createDefaultClass() {
		UMLClass newClass = UMLClass.createDefault();
		diagramClasses.add(newClass);

		return newClass;
	}

	/**
	 * Set new name for class in diagram.
	 * <p>
	 *		Change class name only, if class with old name exists in diagram and
	 *		class with new name doesn't exist in diagram.
	 * </p>
	 *
	 * @param oldName Diagram class name.
	 * @param newName New name for diagram class.
	 * @return Return status of operation (true/false).
	 */
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

	/**
	 * Delete class from diagram if exists.
	 *
	 * @param cls Class to be deleted.
	 * @return Status of operation (true/false).
	 */
	public boolean deleteClass(UMLClass cls) {
		int idx;

		if ((idx = diagramClasses.indexOf(cls)) != -1) {
			diagramClasses.remove(idx);
			return true;
		}

		return false;
	}

	/**
	 * Delete all relationships which use the class to be deleted.
	 * <p>
	 *     The class is in from or to attribute of relationship.
	 * </p>
	 *
	 * @param cls Class which relationships should be deleted.
	 */
	public void deleteAllClassRelationships(UMLClass cls) {
		int idx;
		List<UMLRelationship> relationshipsToDelete = new ArrayList<UMLRelationship>();

		for (UMLRelationship currentRel : diagramRelationships) {
			/* If parameter class is from class in current relationship -> delete relationship */
			if (currentRel.getFrom().getName().equals(cls.getName()) ||
					currentRel.getTo().getName().equals(cls.getName())) {
				relationshipsToDelete.add(currentRel);
			}
		}

		for (UMLRelationship currentRel : relationshipsToDelete) {
			idx = diagramRelationships.indexOf(currentRel);
			diagramRelationships.remove(idx);
		}
	}

	/**
	 * Check if concrete method in concrete operation is overridden.
	 * <p>
	 *     The method checks all parent classes via inheritance relationship.
	 * </p>
	 *
	 * @param operationClass Class that contains searched method.
	 * @param operation Searched method.
	 * @return True if searched method is overridden, false otherwise.
	 */
	public boolean isOverriddenMethod(UMLClass operationClass, UMLOperation operation) {
		List<UMLAttribute> currentOperationArguments;
		List<UMLAttribute> operationArguments;
		boolean isOverridden;
		boolean identicalArguments;

		operationArguments = operation.getArguments();

		/* List through all relationships. */
		for (UMLRelationship currentRelationship : diagramRelationships) {
			/* If the operationClass is TO class of current relationship */
			/* and the type of relationship is inheritance. */
			if (currentRelationship.getFrom().equals(operationClass) &&
					currentRelationship instanceof UMLInheritance) {
				/* List through class operations. */
				for (UMLOperation currentOperation : currentRelationship.getTo().getOperations()) {
					if (currentOperation.getName().equals(operation.getName()) &&
							currentOperation.getType().equals(operation.getType())) {
						/* Operation names and return types are identical. */
						currentOperationArguments = currentOperation.getArguments();

						if (currentOperationArguments.size() == operationArguments.size()) {
							/* Count of parameters is the same. */
							identicalArguments = true;

							for (int i = 0; i < currentOperationArguments.size(); i++) {
								/* Types of arguments are not the same. */
								if (!currentOperationArguments.get(i).getType().equals(operationArguments.get(i).getType())) {
									identicalArguments = false;
									break;
								}
							}

							if (identicalArguments) {
								return true;
							}
						}
					}
				}

				/* Overridden method was not found in current class. */
				/* Check the parent classes to see if this is their method. */
				isOverridden = isOverriddenMethod(currentRelationship.getTo(), operation);

				if (isOverridden) {
					return true;
				}
			}
		}

		return false;
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
	 * @param dataTypeName Name of data type.
	 * @return Return new data type or data type defined in diagram.
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
	 * @return Class object if class was found in diagram. Null otherwise.
	 */
	public UMLClass findClass(String name) {
		for (UMLClass currentClass : diagramClasses) {
			if (currentClass.getName().equals(name)) {
				return currentClass;
			}
		}

		return null;
	}

	/**
	 * Method for obtaining diagram classes.
	 *
	 * @return Unmodifiable list with diagram classes.
	 */
	public List<UMLClass> getClasses() {
		return Collections.unmodifiableList(this.diagramClasses);
	}

	/**
	 * Method for obtaining diagram classes names.
	 *
	 * @return Unmodifiable list with diagram classes names.
	 */
	public List<String> getClassesNames() {
		List<String> classesNames = new ArrayList<String>();

		for (UMLClass currentClass : this.diagramClasses) {
			classesNames.add(currentClass.getName());
		}

		return Collections.unmodifiableList(classesNames);
	}

	/**
	 * Creates an instance of the UML association and inserts it into the diagram.
	 *
	 * @param from Class where relationship starts.
	 * @param to Class where relationship ends.
	 * @return New instance of an UMLAssociation.
	 */
	public UMLAssociation createAssociationRelationship(UMLClass from, UMLClass to) {
		UMLAssociation newAssociation = new UMLAssociation(from, to);
		this.diagramRelationships.add(newAssociation);

		return newAssociation;
	}

	/**
	 * Creates an instance of the UML aggregation and inserts it into the diagram.
	 *
	 * @param from Class where relationship starts.
	 * @param to Class where relationship ends.
	 * @return New instance of an UMLAggregation.
	 */
	public UMLAggregation createAggregationRelationship(UMLClass from, UMLClass to) {
		UMLAggregation newAggregation = new UMLAggregation(from, to);
		this.diagramRelationships.add(newAggregation);

		return newAggregation;
	}

	/**
	 * Creates an instance of the UML composition and inserts it into the diagram.
	 *
	 * @param from Class where relationship starts.
	 * @param to Class where relationship ends.
	 * @return New instance of an UMLComposition.
	 */
	public UMLComposition createCompositionRelationship(UMLClass from, UMLClass to) {
		UMLComposition newComposition = new UMLComposition(from, to);
		this.diagramRelationships.add(newComposition);

		return newComposition;
	}

	/**
	 * Creates an instance of the UML inheritance and inserts it into the diagram.
	 *
	 * @param from Class where relationship starts.
	 * @param to Class where relationship ends.
	 * @return New instance of an UMLInheritance.
	 */
	public UMLInheritance createInheritanceRelationship(UMLClass from, UMLClass to) {
		UMLInheritance newInheritance = new UMLInheritance(from, to);
		this.diagramRelationships.add(newInheritance);

		return newInheritance;
	}

	/**
	 * Method for obtaining diagram relationships.
	 *
	 * @return Unmodifiable list with diagram relationships.
	 */
	public List<UMLRelationship> getRelationships() {
		return Collections.unmodifiableList(this.diagramRelationships);
	}
}
