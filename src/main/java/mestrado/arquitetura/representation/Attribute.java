package mestrado.arquitetura.representation;

/**
 * Representa um atributo.
 * 
 * @author edipofederle
 *
 */
public class Attribute extends Element {

	private String type;

	public Attribute(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String type,  Element parent, String namesapce) {
		super(architecture, name, isVariationPoint, variantType, "attribute", parent, namesapce);
		setType(type);
	}

	public Attribute(Architecture architecture, String name, String type, Element parent, String namespace) {
		this(architecture, name, false, VariantType.NONE, type, parent, namespace);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}
}