package mestrado.arquitetura.representation;

public class UsageInterClassRelationship extends InterClassRelationship {
	
	private String name;
	private Class supplier;
	private Class client;
	
	public UsageInterClassRelationship(String name, Class supplier, Class client) {
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
	public Class getSupplier() {
		return supplier;
	}
	public void setSupplier(Class supplier) {
		this.supplier = supplier;
	}
	public Class getClient() {
		return client;
	}
	public void setClient(Class client) {
		this.client = client;
	}
	
}
