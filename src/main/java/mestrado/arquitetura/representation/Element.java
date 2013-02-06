package mestrado.arquitetura.representation;

/**
 * Classe resposável por abstrair as propriedades comuns a todos os elementos
 * da arquitetura.
 *
 */
public abstract class Element {

	private String name;
	private Boolean isVariationPoint;
	
	public Element(String name, boolean isVariationPoint) {
		setName(name);
		setIsVariationPoint(isVariationPoint);
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

	@Override
	public String toString() {
		return getName();
	}
	

}