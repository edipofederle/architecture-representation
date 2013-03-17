package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle
 *
 */
public class RealizationInterClassRelationship extends InterClassRelationship {
	
	private String name;
	private Element client;
	private Element supplier;
	
	
	public RealizationInterClassRelationship(Element client, Element supplier, String name){
		setClient(client);
		setSupplier(supplier);
		setName(name);
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
	private void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the client
	 */
	public Element getClient() {
		return client;
	}


	/**
	 * @param client the client to set
	 */
	private void setClient(Element client) {
		this.client = client;
	}


	/**
	 * @return the supplier
	 */
	public Element getSupplier() {
		return supplier;
	}


	/**
	 * @param supplier the supplier to set
	 */
	private void setSupplier(Element supplier) {
		this.supplier = supplier;
	}


	
}