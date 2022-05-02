package com.uml.sequencediagram;

import com.uml.classdiagram.UMLClass;

public class UMLLifeline {

	private long id;
	private static long nextId = 0;
	private double xCoordinate;
	private UMLClass objectClass;
	private double height;

	public UMLLifeline(UMLClass objectClass, double height) {		
		this.objectClass = objectClass;
		this.height = height;
		this.xCoordinate = 0.0;
		this.id = nextId;
		nextId++;
	}

	public long getId() {
		return this.id;
	}

	public double getXCoordinate() {
		return this.xCoordinate;
	}

	public void setXCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public UMLClass getObjectClass() {
		return this.objectClass;
	}

	public void setObjectClass(UMLClass objectClass) throws CloneNotSupportedException {		
		this.objectClass = objectClass;
	}

	public double getHeight() {
		return this.height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}