package mestrado.arquitetura.parser.method;

import mestrado.arquitetura.helpers.UtilResources;

public class Attribute {
	
	private static String id;
	private String name;
	private VisibilityKind visibility;
	private Types type;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the visibility
	 */
	public VisibilityKind getVisibility() {
		return visibility;
	}
	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(VisibilityKind visibility) {
		this.visibility = visibility;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type.getName();
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Types type) {
		this.type = type;
	}
	
	public static Attribute create() {
		id = UtilResources.getRandonUUID();
		return new Attribute();
	}
	public Attribute withName(String name) {
		this.name = name;
		return this;
	}

	public Attribute withVisibility(VisibilityKind visibility) {
		this.visibility = visibility;
		return this;
	}
	public Attribute withType(Types type) {
		this.type = type;
		return this;
	}
	

}