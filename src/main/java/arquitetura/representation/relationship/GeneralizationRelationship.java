package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 * 
 */
public class GeneralizationRelationship extends Relationship {

	private Class parent;
	private Architecture architecture;
	private Class child;

	public GeneralizationRelationship(Class parentClass, Class childClass,
			Architecture architecture, String id) {
		setParent(parentClass);
		setChild(childClass);
		this.architecture = architecture;
		setId(id);
		setType("generalization");
	}

	/**
	 * @return the child
	 */
	public Class getChild() {
		return child;
	}

	/**
	 * @param child
	 *            the child to set
	 */
	public void setChild(Class child) {
		this.child = child;
	}

	public Class getParent() {
		return parent;
	}

	public void setParent(Class parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * Método que retorna todas as classes filhas para a parent class (general)
	 * 
	 * @return
	 */
	public List<Element> getAllChildrenForGeneralClass() {
		List<GeneralizationRelationship> generalizations = architecture
				.getAllGeneralizations();
		List<Element> childreen = new ArrayList<Element>();

		String general = this.parent.getName();

		for (GeneralizationRelationship generalization : generalizations)
			if (generalization.getParent().getName().equalsIgnoreCase(general))
				childreen.add(generalization.getChild());

		return childreen;

	}

	public void replaceChild(Class newChild) {
		this.child = newChild;
	}

	public void replaceParent(Class parent) {
		setParent(parent);
	}

}