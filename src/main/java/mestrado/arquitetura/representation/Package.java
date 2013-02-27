package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;


public class Package extends Element {

	//private final ClassesList classes = new ClassesList();
	private List<Element> elements = new ArrayList<Element>();
	
	public Package(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, Element parent) {
		super(architecture, name, isVariationPoint, variantType, "package", parent);
	}

	public List<Element> getElements() {
		return elements;
	}
	
	public List<Class> getClasses(){
		List<Class> paks = new ArrayList<Class>();
		for (Element element : elements) {
			if(element.getTypeElement().equals("klass"))
				paks.add(((Class)element));
		}
		return paks;
	}
	
	public List<Package> getNestedPackages(){
		List<Package> paks = new ArrayList<Package>();
		for (Element element : elements) {
			if(element.getTypeElement().equals("package"))
				paks.add(((Package)element));
		}
		return paks;
	}
}
