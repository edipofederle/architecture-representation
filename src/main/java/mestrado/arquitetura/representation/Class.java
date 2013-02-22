package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class Class extends Element {
	
	private boolean isAbstract;
	private final List<Attribute> attributes = new ArrayList<Attribute>();
	private final List<Method> methods = new ArrayList<Method>();
	
	public Class(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, boolean isAbstract) {
		super(architecture, name, isVariationPoint, variantType);
		setAbstract(isAbstract);
	}

	
	public void setAttribute(Attribute attr){
		this.attributes.add(attr);
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public List<Method> getMethods() {
		return methods;
	}

	
}