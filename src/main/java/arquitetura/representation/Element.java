package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import arquitetura.representation.relationship.Relationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public abstract class Element {

	protected String id;
	private String name;
	private VariationPoint variationPoint;
	private Variant variant;
	private final List<Concern> concerns = new ArrayList<Concern>();
	private static Architecture architecture;
	private String typeElement;
	private String namespace;
	//private List<String> idsRelationships = new ArrayList<String>();
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

	protected void setName(String name) {
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
	
	@SuppressWarnings("static-access")
	private void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	/**
	 * Retorna apenas os interesses pertencentes a este elemento.<br />
	 * 
	 * @return List<{@link Concern}>
	 */
	public List<Concern> getOwnConcerns() {
		return concerns;
	}
	
	public boolean containsConcern(Concern concern){
		for (Concern conc : getOwnConcerns()) {
			if(conc.getName().equalsIgnoreCase(concern.getName()))
				return true;
		}
		return false;
	}
	
	public void addConcerns(List<String> concernsNames) {
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
	 * @return - true se incluir o interesse<br/>- false se não incluir o interesse
	 */
	public boolean addConcern(String concernName) {
		Concern concern = architecture.getOrCreateConcern(concernName);
		if(concern == null) return false;
		concerns.add(concern);
		return true;
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
	
	public static Architecture getArchitecture(){
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
		return relationships;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}