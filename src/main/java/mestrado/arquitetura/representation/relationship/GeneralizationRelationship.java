package mestrado.arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;

/**
 * 
 * @author edipofederle
 *
 */
public class GeneralizationRelationship extends Relationship {

	private Class parent;
	private Architecture architecture;
	private Class child;
	
	public GeneralizationRelationship(Class parentClass, Class childClass, Architecture architecture) {
		setParent(parentClass);
		setChild(childClass);
		this.architecture = architecture;
	}
	

	/**
	 * @return the child
	 */
	public Class getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Class child) {
		this.child = child;
	}



	public Class getParent() {
		return parent;
	}

	private void setParent(Class parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * Método que retorna todas as classes filhas para a parent class (general)
	 * 
	 * @return
	 */
	public List<Class> gelAllChildrenForGeneralClass() {
		List<GeneralizationRelationship> generalizations = architecture.getAllGeneralizations();
		List<Class> childreen = new ArrayList<Class>();
		
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