package mestrado.arquitetura.representation;

import java.util.List;

public class GeneralizationInterClassRelationship extends InterClassRelationship {

	private Class parent;
	private Class child;
	private List<Class> childreen;
	
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
	

	public List<Class> getChildreen() {
		return childreen;
	}

	public void setChildreen(List<Class> childreen) {
		this.childreen = childreen;
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