package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.representation.Element;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AbstractionRelationship extends Relationship{
	
	private Element client;
	private Element supplier;

	public AbstractionRelationship(Element client, Element supplier, String id) {
		setClient(client);
		setSupplier(supplier);
		setId(id);
		super.setType(ElementsTypes.ABSTRACTION);
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

}