package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle
 *
 */
public class AbstractionRelationship extends InterClassRelationship{
	
	private Element parent;
	private Element child;

	public AbstractionRelationship(Element interfacee, Element packagee) {
		setParent(interfacee);
		setChild(packagee);
		if(packagee instanceof Package)
			((Package) packagee).addImplementedInterface(interfacee);
	}

	private void setChild(Element packagee) {
		this.child = packagee;
	}

	public Element getParent() {
		return parent;
	}

	public void setParent(Element parent) {
		this.parent = parent;
	}

	public Element getChild() {
		return child;
	}

}