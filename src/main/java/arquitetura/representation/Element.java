package arquitetura.representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.relationship.Relationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public abstract class Element implements Serializable {

	private static final long serialVersionUID = 4736685073967472613L;
	
	protected String id;
	private String name;
	private VariationPoint variationPoint;
	private Variant variant;
	private Set<Concern> concerns = new HashSet<Concern>();
	private Architecture architecture;
	private String typeElement;
	private String namespace;
	private List<Relationship> relationships = new ArrayList<Relationship>();
	
	public Element(Architecture architecture, String name, Variant variant, String typeElement, String namespace, String id) {
		setArchitecture(architecture);
		setId(id);
		setName(name);
		setVariant(variant);
		setTypeElement(typeElement);
		setNamespace(namespace);
	}
	
	public abstract Collection<Concern> getAllConcerns();

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	private void setId(String id) {
		this.id = id;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	private void setTypeElement(String typeElement) {
		this.typeElement = typeElement;
	}
	
	public String getTypeElement(){
		return this.typeElement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public Boolean isVariationPoint() {
		return this.getVariationPoint() != null;
	}

	public Variant getVariant() {
		return variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	private void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	/**
	 * Retorna apenas os interesses pertencentes a este elemento.<br />
	 * 
	 * @return List<{@link Concern}>
	 */
	public Set<Concern> getOwnConcerns() {
		return concerns;
	}
	
	public boolean containsConcern(Concern concern){
		for (Concern conc : getOwnConcerns()) {
			if(conc.getName().equalsIgnoreCase(concern.getName()))
				return true;
		}
		return false;
	}
	
	public void addConcerns(List<String> concernsNames) throws ConcernNotFoundException {
		for (String name : concernsNames) 
			addConcern(name);
	}
	
	/**
	 * Adiciona um interesse a classe.<br/>
	 * 
	 * Somente irá incluir o interesse se o mesmo estive presente no arquivo de perfil que contêm os interesses</br>
	 * 
	 * 
	 * @param concernName
	 * @throws ConcernNotFoundException 
	 * 
	 */
	public void addConcern(String concernName) throws ConcernNotFoundException {
		Concern concern = architecture.getOrCreateConcern(concernName);
		concerns.add(concern);
	}
	
	public void removeConcern(String concernName){
		concerns.remove(architecture.getConcernByName(concernName));
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}
	
	public Architecture getArchitecture(){
		return architecture;
	}

	/**
	 * @return the variationPoint
	 */
	public VariationPoint getVariationPoint() {
		return variationPoint;
	}

	/**
	 * @param variationPoint the variationPoint to set
	 */
	public void setVariationPoint(VariationPoint variationPoint) {
		this.variationPoint = variationPoint;
	}
	
	public List<Relationship> getRelationships() {
		return Collections.unmodifiableList(relationships);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	 @Override
	 public boolean equals(Object obj) {
	        if (this == obj)
	                return true;
	        if (obj == null)
	                return false;
	        if (!(obj instanceof Element))
	                return false;
	                
	        Element other = (Element) obj;
	        return (
	                this.getName().equals(other.getName()) &&
	                this.getNamespace().equals(other.getNamespace())
	        );
	 }

	public void addRelationship(Relationship relationship) {
		this.relationships.add(relationship);
	}
	
	public void removeRelationship(Relationship relationship) {
		this.relationships.remove(relationship);
	}
	
	

	
}