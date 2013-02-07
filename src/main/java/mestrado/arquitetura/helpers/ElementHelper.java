package mestrado.arquitetura.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLPackage;

public abstract class ElementHelper {
	
	
	private static Map<String, EClass> types = new HashMap<String, EClass>();

	static {
		types.put("class", UMLPackage.Literals.CLASS);
		types.put("interface", UMLPackage.Literals.INTERFACE);
		types.put("association", UMLPackage.Literals.ASSOCIATION);
		types.put("dependency", UMLPackage.Literals.DEPENDENCY);
		types.put("comment", UMLPackage.Literals.COMMENT);
		types.put("property", UMLPackage.Literals.PROPERTY);
		types.put("operation", UMLPackage.Literals.OPERATION);
		types.put("package", UMLPackage.Literals.PACKAGE);
	}
		
	@SuppressWarnings("unchecked")
	protected static <T> List<Classifier> getAllElementsByType(PackageableElement element, String type) {
		EList<Element> ownedElements = element.getOwnedElements();
		List<T> elements = new ArrayList<T>();
		for (Element e : ownedElements) {
			if (e.eClass().equals(getLiteralType(type)))
				elements.add((T) e);
		}
		return (List<Classifier>) elements;
	}
	
	private static EClass getLiteralType(String type){
		return types.get(type);
	}

}
