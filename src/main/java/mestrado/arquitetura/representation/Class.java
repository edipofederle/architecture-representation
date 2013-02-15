package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class Class extends Element {
	
	private boolean isAbstract;
	
	public Class(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, boolean isAbstract) {
		super(architecture, name, isVariationPoint, variantType);
		setAbstract(isAbstract);
	}

	private final List<String> attributes = new ArrayList<String>();

	
	public void setAttribute(String attr){
		this.attributes.add(attr);
	}
	
	public List<String> getAttributes() {
		return attributes;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	
}