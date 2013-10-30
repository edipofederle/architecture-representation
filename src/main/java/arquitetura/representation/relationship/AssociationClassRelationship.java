package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import arquitetura.helpers.ElementsTypes;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Method;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationClassRelationship extends Relationship {

	public List<MemberEnd> memebersEnd = new ArrayList<MemberEnd>();
	private Element ownedEnd;
	private Class associationClass;
	private String idOwner;
	
	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param ends
	 * @param ownedEnd
	 * @param id - associationEnd
	 * @param idOwner - ex: pacote
	 * @param associationClass
	 */
	public AssociationClassRelationship(Architecture a, String name, List<MemberEnd> ends, Element ownedEnd, String id, String idOwner, Class associationClass) {
		super.setName(name);
		this.memebersEnd = ends;
		this.ownedEnd = ownedEnd;
		this.idOwner = idOwner;
		this.associationClass = associationClass;
		super.setId(id);
		super.setType(ElementsTypes.CLASS);
		}


	/**
	 * @return the attributes
	 */
	public List<Attribute> getAllAttributes() {
		return this.associationClass.getAllAttributes();
	}
	

	public List<MemberEnd> getMemebersEnd() {
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
	public List<Method> getAllMethods() {
		return this.associationClass.getAllMethods();
	}
	
	public List<Concern> getOwnConcerns() {
		return this.associationClass.getOwnConcerns();
	}
	
	public Collection<Concern> getAllConcerns() {
		Collection<Concern> concerns = new ArrayList<Concern>(getOwnConcerns());

		for (Method method : this.associationClass.getAllMethods())
			concerns.addAll(method.getAllConcerns());
		for (Attribute attribute : associationClass.getAllAttributes())
			concerns.addAll(attribute.getAllConcerns());
		
		return concerns;
	}

	/**
	 * @return the associationClass
	 */
	public Class getAssociationClass() {
		return associationClass;
	}
	
	

}