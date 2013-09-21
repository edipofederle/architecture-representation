package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Attribute;
import arquitetura.representation.Element;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationClassRelationship extends	Relationship {

	public String name;
	public List<Element> memebersEnd = new ArrayList<Element>();
	private Element ownedEnd;
	
	private List<Attribute> attributes = new ArrayList<Attribute>();

	public AssociationClassRelationship(String name, List<Element> ends, Element ownedEnd, String id) {
		super();
		this.name = name;
		this.memebersEnd = ends;
		this.ownedEnd = ownedEnd;
		setId(id);
//		for(Element element : memebersEnd)
//			setTypeRelationship(element.getId());
//		setTypeRelationship(ownedEnd.getId());
	}

	public String getName() {
		return name;
	}


	public List<Element> getMemebersEnd() {
		return memebersEnd;
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