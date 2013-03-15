package mestrado.arquitetura.representation;

/**
 * 
 * Representa uma relação de 'Usage' entre um classe e um Pacote.
 * 
 * @author edipofederle
 *
 */
public class UsageInterClassPackageRelationship extends InterClassRelationship{
	
	private String name;
	private Element supplier;
	private Element client;
	
	
	/**
	 * @param name
	 * @param supplier
	 * @param client
	 * 
	 */
	public UsageInterClassPackageRelationship(String name, Element supplier, Element client) {
		super();
		this.supplier = supplier;
		this.client = client;
		this.name = name;
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
	public void setSupplier(Element supplier) {
		this.supplier = supplier;
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
	public void setClient(Element client) {
		this.client = client;
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
	

}
