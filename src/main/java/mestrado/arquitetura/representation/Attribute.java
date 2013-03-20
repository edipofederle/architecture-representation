package mestrado.arquitetura.representation;

/**
 * Representa um atributo.
 * 
 * @author edipofederle
 *
 */
public class Attribute extends Element {

	private String type;

	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param isVariationPoint
	 * @param variantType
	 * @param type
	 * @param parent
	 * @param namesapce
	 * @param id
	 */
	public Attribute(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, String type,  Element parent, String namesapce, String id) {
		super(architecture, name, isVariationPoint, variantType, "attribute", parent, namesapce, id);
		setType(type);
	}

	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param type
	 * @param parent
	 * @param namespace
	 * @param id
	 */
	public Attribute(Architecture architecture, String name, String type, Element parent, String namespace, String id) {
		this(architecture, name, false, VariantType.NONE, type, parent, namespace, id);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}
}