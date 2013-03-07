package mestrado.arquitetura.representation;

public class GeneralizationInterClassRelationship extends InterClassRelationship {

	private Class parent;
	private Class child;
	
	public GeneralizationInterClassRelationship(Class parentClass, Class childClass) {
		setParent(parentClass);
		setChild(childClass);
	}

	public Class getParent() {
		return parent;
	}

	private void setParent(Class parent) {
		this.parent = parent;
	}

	public Class getChild() {
		return child;
	}

	private void setChild(Class child) {
		this.child = child;
	}
	
	public void replaceChild(Class child){
		setChild(child);
	}
	
	public void replaceParent(Class parent){
		setParent(parent);
	}
}