package mestrado.arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Element;

public class AssociationClassRelationship extends	Relationship {

	public String name;
	public List<Element> memebersEnd = new ArrayList<Element>();
	private Element ownedEnd;

	public AssociationClassRelationship(String name, List<Element> ends, Element ownedEnd, String id) {
		super();
		this.name = name;
		this.memebersEnd = ends;
		this.ownedEnd = ownedEnd;
		setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Element> getMemebersEnd() {
		return memebersEnd;
	}

	public void setMemebersEnd(List<Element> memebersEnd) {
		this.memebersEnd = memebersEnd;
	}

	/**
	 * Retorna  {@link Element } dona da AssociationClass.
	 * 
	 * @return
	 */
	public Element getOwnedEnd() {
		return ownedEnd;
	}

}