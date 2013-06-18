package arquitetura.api.touml;

import arquitetura.helpers.UtilResources;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Attribute {
	
	private String id;
	private String name;
	private VisibilityKind visibility;
	private Types.Type type;
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
	public String getVisibility() {
		return visibility.getName();
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
	public void setType(Types.Type type) {
		this.type = type;
	}
	
	public static Attribute create() {
		Attribute attr =  new Attribute();
		attr.setId(UtilResources.getRandonUUID());
		return attr;
		
	}
	private void setId(String randonUUID) {
		this.id = randonUUID;
	}

	public Attribute withName(String name) {
		this.name = name;
		return this;
	}

	public Attribute withVisibility(VisibilityKind visibility) {
		this.visibility = visibility;
		return this;
	}
	public Attribute withType(Types.Type type) {
		this.type = type;
		return this;
	}
	

}