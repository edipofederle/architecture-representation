package arquitetura.representation.relationship;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arquitetura.helpers.ElementsTypes;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;

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
		super.setType(ElementsTypes.GENERALIZATION);
	}

	/**
	 * @return the child
	 */
	public Element getChild() {
		return child;
	}

	/**
	 * @param child
	 *            the child to set
	 */
	public void setChild(Element child) {
		this.child = child;
	}

	public Element getParent() {
		return parent;
	}

	public void setParent(Element parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * Método que retorna todas as classes filhas para a parent class (general)
	 * 
	 * @return
	 */
	public Set<Element> getAllChildrenForGeneralClass() {
		List<GeneralizationRelationship> generalizations = architecture.getAllGeneralizations();
		Set<Element> childreen = new HashSet<Element>();

		String general = this.parent.getName();

		for (GeneralizationRelationship generalization : generalizations)
			if (generalization.getParent().getName().equalsIgnoreCase(general))
				childreen.add(generalization.getChild());

		return Collections.unmodifiableSet(childreen);

	}

	public void replaceChild(Class newChild) {
		this.child = newChild;
	}

	public void replaceParent(Class parent) {
		setParent(parent);
	}

}