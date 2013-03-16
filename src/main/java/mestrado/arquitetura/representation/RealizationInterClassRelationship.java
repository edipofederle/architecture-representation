package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle
 *
 */
public class RealizationInterClassRelationship extends InterClassRelationship {
	
	private String name;
	private Element clientElement;
	private Element supplierElement;
	
	
	public RealizationInterClassRelationship(Element clientElement, Element supplierElement, String name){
		setClientElement(clientElement);
		setSupplierElement(supplierElement);
		setName(name);
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void setClientElement(Element clientElement) {
		this.clientElement = clientElement;
	}

	public Element setSupplierElement() {
		return clientElement;
	}

	/**
	 * @return the supplierElement
	 */
	public Element getSupplierElement() {
		return supplierElement;
	}

	/**
	 * @param supplierElement the supplierElement to set
	 */
	public void setSupplierElement(Element supplierElement) {
		this.supplierElement = supplierElement;
	}

	/**
	 * @return the clientElement
	 */
	public Element getClientElement() {
		return clientElement;
	}
	
	
}