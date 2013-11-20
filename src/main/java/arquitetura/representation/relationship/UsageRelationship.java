package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.representation.Element;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class UsageRelationship extends Relationship {
	
	private String name;
	private Element supplier;
	private Element client;
	
	public UsageRelationship(String name, Element supplier, Element client, String id) {
		super();
		this.name = name;
		this.supplier = supplier;
		this.client = client;
		setId(id);
		super.setType(ElementsTypes.USAGE);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Element getSupplier() {
		return supplier;
	}
	public void setSupplier(Element supplier) {
		this.supplier = supplier;
	}
	public Element getClient() {
		return client;
	}
	public void setClient(Element client) {
		this.client = client;
	}
	
}
