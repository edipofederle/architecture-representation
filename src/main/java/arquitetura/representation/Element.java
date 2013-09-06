package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	private List<String> idsRelationships = new ArrayList<String>();
	
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
	
	public void addConcern(String concernName) {
		Concern concern = architecture.getOrCreateConcernByName(concernName);
		concerns.add(concern);
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
	 * @return the idsRelationships
	 */
	public List<String> getIdsRelationships() {
		return idsRelationships;
	}

	/**
	 * @param idsRelationships the idsRelationships to set
	 */
	public void setIdsRelationships(List<String> idsRelationships) {
		this.idsRelationships = idsRelationships;
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
	
	
	
}