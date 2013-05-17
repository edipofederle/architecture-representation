package mestrado.arquitetura.representation;

/**
 * Representa um atributo.
 * 
 * @author edipofederle
 *
 */
public class Attribute extends Element {

	private String type;
	private String visibilityKind;

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
	public Attribute(Architecture architecture, String name, String visibilityKind, boolean isVariationPoint, Variant variantType, String type, String namesapce, String id) {
		super(architecture, name, isVariationPoint, variantType, "attribute", namesapce, id);
		setType(type);
		setVisibilityKind(visibilityKind);
	}

	private void setVisibilityKind(String visibilityKind) {
		this.visibilityKind = visibilityKind;
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
	public Attribute(Architecture architecture, String name, String visibilityKind, String type, String namespace, String id) {
		this(architecture, name, visibilityKind, false, null, type, namespace, id);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	public String getVisibility() {
		return this.visibilityKind;
		
	}
}