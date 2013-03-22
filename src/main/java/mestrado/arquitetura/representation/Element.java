package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe resposável por abstrair as propriedades comuns a todos os elementos
 * da arquitetura.
 * 
 * @author edipofederle
 */
public abstract class Element {

	private String id;
	private String name;
	private Boolean isVariationPoint;
	private VariantType variantType;
	private final List<Concern> concerns = new ArrayList<Concern>();
	private static Architecture architecture;
	private String typeElement;
	private String namespace;
	private List<String> idsRelationships = new ArrayList<String>();
	
	public Element(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String typeElement, String namespace, String id) {
		setArchitecture(architecture);
		setId(id);
		setName(name);
		setIsVariationPoint(isVariationPoint);
		setVariantType(variantType);
		setTypeElement(typeElement);
		setNamespace(namespace);
	}

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
		return isVariationPoint;
	}

	private void setIsVariationPoint(Boolean isVariationPoint) {
		this.isVariationPoint = isVariationPoint;
	}
	

	public VariantType getVariantType() {
		return variantType;
	}

	public void setVariantType(VariantType variantType) {
		this.variantType = variantType;
	}

	public Boolean getIsVariationPoint() {
		return isVariationPoint;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@SuppressWarnings("static-access")
	private void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public List<Concern> getConcerns() {
		return concerns;
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
	
	
	
}