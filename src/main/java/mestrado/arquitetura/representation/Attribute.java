package mestrado.arquitetura.representation;

/**
 * Representa um atributo.
 * 
 * @author edipofederle
 *
 */
public class Attribute extends Element {

	private String type;

	public Attribute(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String type,  Element parent) {
		super(architecture, name, isVariationPoint, variantType, "attribute", parent);
		setType(type);
	}

	public Attribute(Architecture architecture, String name, String type, Element parent) {
		this(architecture, name, false, VariantType.NONE, type, parent);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}
}