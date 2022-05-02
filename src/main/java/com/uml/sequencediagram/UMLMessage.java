package com.uml.sequencediagram;

public abstract class UMLMessage extends Element {

	private long id;
	private static long nextId = 0;
	private double yCoordinate;
	private UMLLifeline fromLifeline;
	private UMLLifeline toLifeline;	

	public UMLMessage(UMLLifeline fromLifeline, 
					  UMLLifeline toLifeline,
					  String label) {
		super(label);	
		this.fromLifeline = fromLifeline;
		this.toLifeline = toLifeline;
		this.yCoordinate = 0.0;
		this.id = nextId;
		nextId++;
	}

	public long getId() {
		return this.id;
	}

	protected void setLabel(String newLabel) {
		super.setName(newLabel);
	}

	public String getLabel() {
		return super.getName();
	}

	public void setYCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public double getYCoordinate() {
		return this.yCoordinate;
	}	

	public void setFromLifeline(UMLLifeline fromLifeline) {
		this.fromLifeline = fromLifeline;
	}

	public UMLLifeline getFromLifeline() {
		return this.fromLifeline;
	}	

	public UMLLifeline getToLifeline() {
		return this.toLifeline;
	}

	public void setToLifeline(UMLLifeline toLifeline) {
		this.toLifeline = toLifeline;
	}
}