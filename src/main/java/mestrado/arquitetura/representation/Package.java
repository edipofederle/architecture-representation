package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class Package extends Element {

	private final List<Class> classes = new ArrayList<Class>();
	
	public Package(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType) {
		super(architecture, name, isVariationPoint, variantType);
	}

	public List<Class> getClasses() {
		return classes;
	}
}
