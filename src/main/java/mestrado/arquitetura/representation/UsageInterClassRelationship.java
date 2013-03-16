package mestrado.arquitetura.representation;

public class UsageInterClassRelationship extends InterClassRelationship {
	
	private String name;
	private Element supplier;
	private Element client;
	
	public UsageInterClassRelationship(String name, Element supplier, Element client) {
		super();
		this.name = name;
		this.supplier = supplier;
		this.client = client;
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
	public void setClient(Class client) {
		this.client = client;
	}
	
}
