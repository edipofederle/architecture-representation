package mestrado.arquitetura.representation;


public class RealizationInterClassRelationship extends InterClassRelationship {
	
	private String name;
	private Class sourceElement;
	private Class specificElement;
	
	
	public RealizationInterClassRelationship(Class sourceElement, Class specificElement, String name){
		setSpecificElement(specificElement);
		setSourceElement(sourceElement);
		setName(name);
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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