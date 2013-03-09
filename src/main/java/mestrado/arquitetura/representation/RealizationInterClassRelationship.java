package mestrado.arquitetura.representation;

import mestrado.arquitetura.builders.ClassBuilder;

public class RealizationInterClassRelationship extends InterClassRelationship {
	
	private String name;
	private Class sourceElement;
	private Class specificElement;
	
	private ClassBuilder classBuilder;
	
	public RealizationInterClassRelationship(ClassBuilder classBuilder, Class sourceElement, Class specificElement){
		this.classBuilder = classBuilder;
		setSpecificElement(specificElement);
		setSourceElement(sourceElement);
	}

	private void setSourceElement(Class sourceElement) {
		this.sourceElement = sourceElement;
	}

	private void setSpecificElement(Class specificElement) {
		this.specificElement = specificElement;
	}

	public Class getSourceElement() {
		return sourceElement;
	}

	public Class getSpecificElement() {
		return specificElement;
	}
	
}