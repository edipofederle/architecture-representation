package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author edipofederle
 *
 */
public class GeneralizationInterClassRelationship extends InterClassRelationship {

	private Class parent;
	private List<Class> childreen = new ArrayList<Class>();
	
	public GeneralizationInterClassRelationship(Class parentClass, Class childClass, Architecture architecture) {
		setParent(parentClass);
		setChildreen(childClass);
	}

	public Class getParent() {
		return parent;
	}

	private void setParent(Class parent) {
		this.parent = parent;
	}

	public List<Class> getChildreen() {
		return childreen;
	}

	public void setChildreen(List<Class> childreen) {
		this.childreen = childreen;
	}

	public void setChildreen(Class childreen) {
		this.childreen.add(childreen);
	}

	public void replaceChild(Class newChild, Class childSubistitute){
		int index = childreen.indexOf(newChild);
		childreen.remove(index);
		childreen.add(childSubistitute);
	}
	
	public void replaceParent(Class parent){
		setParent(parent);
	}
	
}