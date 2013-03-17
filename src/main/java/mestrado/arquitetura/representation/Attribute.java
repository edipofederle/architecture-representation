package mestrado.arquitetura.representation;

/**
 * Representa um atributo.
 * 
 * @author edipofederle
 *
 */
public class Attribute extends Element {

	private String type;

	public Attribute(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String type,  Element parent, String namesapce, boolean interfacee) {
		super(architecture, name, isVariationPoint, variantType, "attribute", parent, namesapce, interfacee);
		setType(type);
	}

	public Attribute(Architecture architecture, String name, String type, Element parent, String namespace, boolean interfacee) {
		this(architecture, name, false, VariantType.NONE, type, parent, namespace, interfacee);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}
}