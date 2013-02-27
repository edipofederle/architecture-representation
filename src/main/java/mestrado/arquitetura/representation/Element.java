package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe resposável por abstrair as propriedades comuns a todos os elementos
 * da arquitetura.
 *
 */
public abstract class Element {

	private String name;
	private Boolean isVariationPoint;
	private VariantType variantType;
	private final List<Concern> concerns = new ArrayList<Concern>();
	private Architecture architecture;
	private String typeElement;
	private Element parent;
	
	public Element(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String typeString, Element parent) {
		setArchitecture(architecture);
		setName(name);
		setIsVariationPoint(isVariationPoint);
		setVariantType(variantType);
		setTypeElement(typeString);
		setParent(parent);
	}

	private void setParent(Element parent) {
		this.parent = parent;
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
	 * Returns Parent Element. If there is no parent returns null.
	 * 
	 * @return {@link Element}
	 */
	public Element getParent(){
		return this.parent != null ? this.parent : null; // Eu nao queria retorna null :(
	}
	
}