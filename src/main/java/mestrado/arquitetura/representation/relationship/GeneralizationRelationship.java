package mestrado.arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class GeneralizationRelationship extends Relationship {

	private Element parent;
	private Architecture architecture;
	private Element child;
	
	public GeneralizationRelationship(Element parentClass, Element childClass, Architecture architecture, String id) {
		setParent(parentClass);
		setChild(childClass);
		this.architecture = architecture;
		setId(id);
		setTypeRelationship("generalization");
	}
	

	/**
	 * @return the child
	 */
	public Element getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Element child) {
		this.child = child;
	}



	public Element getParent() {
		return parent;
	}

	private void setParent(Element parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * Método que retorna todas as classes filhas para a parent class (general)
	 * 
	 * @return
	 */
	public List<Element> getAllChildrenForGeneralClass() {
		List<GeneralizationRelationship> generalizations = architecture.getAllGeneralizations();
		List<Element> childreen = new ArrayList<Element>();
		
		String general = this.parent.getName();
		
		for (GeneralizationRelationship generalization : generalizations)
			if(generalization.getParent().getName().equalsIgnoreCase(general))
				childreen.add(generalization.getChild());
		
		return childreen;

	}


	public void replaceChild(Class newChild){
		this.child = newChild;
	}
	
	public void replaceParent(Class parent){
		setParent(parent);
	}
	
}