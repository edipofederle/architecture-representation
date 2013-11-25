package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class RealizationRelationship extends Relationship {
	
	private Element client;
	private Element supplier;
	
	
	public RealizationRelationship(Element client, Element supplier, String name, String id){
		setClient(client);
		setSupplier(supplier);
		setId(id);
		super.setName(name);
		super.setType(ElementsTypes.REALIZATION);
		
		if((client instanceof Package) && (supplier instanceof Interface)){
			((Package) client).addImplementedInterface(supplier);
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result
				+ ((supplier == null) ? 0 : supplier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RealizationRelationship other = (RealizationRelationship) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (supplier == null) {
			if (other.supplier != null)
				return false;
		} else if (!supplier.equals(other.supplier))
			return false;
		return true;
	}
	
}