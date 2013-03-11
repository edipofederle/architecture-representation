package mestrado.arquitetura.representation;

/**
 * 
 * @author edipofederle
 *
 */
public class AbstractionInterElementRelationship extends InterElementRelationship{
	
	private Class parent;
	private Package child;

	public AbstractionInterElementRelationship(Class interfacee, Package packagee) {
		setParent(interfacee);
		setChild(packagee);
		packagee.addImplementedInterface(interfacee);
	}

	private void setChild(Package packagee) {
		this.child = packagee;
	}

	public Class getParent() {
		return parent;
	}

	public void setParent(Class parent) {
		this.parent = parent;
	}

	public Package getChild() {
		return child;
	}

}