package mestrado.arquitetura.representation;


public class Attribute extends Element {

	private String type;

	public Attribute(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String type) {
		super(architecture, name, isVariationPoint, variantType);
		setType(type);
	}

	public Attribute(Architecture architecture, String name, String type) {
		this(architecture, name, false, VariantType.NONE, type);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}
}
