package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Attribute;
import arquitetura.representation.Element;
import arquitetura.representation.Method;


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
	private List<Method> methods = new ArrayList<Method>();
	private String idOwner;

	/**
	 * 
	 * @param name
	 * @param ends
	 * @param ownedEnd
	 * @param id
	 * @param idOwner - Pacote
	 * @param props 
	 */
	public AssociationClassRelationship(String name, List<Element> ends, Element ownedEnd, String id, String idOwner, List<Attribute> props, List<Method> methods) {
		super();
		this.name = name;
		this.memebersEnd = ends;
		this.ownedEnd = ownedEnd;
		this.idOwner = idOwner;
		setId(id);
		this.attributes = props;
		this.methods = methods;
	}

	public String getName() {
		return name;
	}


	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
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

	/**
	 * Retorna o ID do pacote que a associationClass pertence.
	 * 
	 * @return String
	 */
	public String getPackageOwner() {
		return this.idOwner;
	}

	/**
	 * Retorna os métodos para associationClass
	 * 
	 * @return {@link Method}
	 */
	public List<Method> getMethods() {
		return this.methods;
	}

}