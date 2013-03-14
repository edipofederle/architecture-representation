package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.builders.test.ArchitectureBuilderTest;

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

	/**
	 * Retorna todos os filhos de parent.<br /><br />
	 * 
	 * <b>Uso:</b><br>
	 * 
	 * Ver teste {@link ArchitectureBuilderTest#givenAParentClassShouldReturnAllChildren() }.
	 * 
	 * @return
	 */
//	public List<Class> getChildreen() {
//		List<InterClassRelationship> interRelation = architecture.getInterClassRelationships();
//		
//		List<GeneralizationInterClassRelationship> generalizations = new ArrayList<GeneralizationInterClassRelationship>();
//		
//		List<Class> returnList = new ArrayList<Class>();
//		
//		for (InterClassRelationship inter : interRelation)
//			if(inter instanceof GeneralizationInterClassRelationship)
//				generalizations.add(((GeneralizationInterClassRelationship)inter));
//		
//		for (GeneralizationInterClassRelationship generalization : generalizations)
//			if(generalization.getParent().equals(this.getParent()))
//				returnList.add(generalization.getChild());
//		
//		return returnList;
//	}

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