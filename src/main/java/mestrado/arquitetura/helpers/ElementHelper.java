package mestrado.arquitetura.helpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageableElement;

public abstract class ElementHelper {

	@SuppressWarnings("unchecked")
	protected static <T> List<Classifier> getAllElementsByType(
			PackageableElement element, Class<T> type) {
		EList<Element> ownedElements = element.getOwnedElements();
		List<T> elements = new ArrayList<T>();
		for (Element e : ownedElements) {
			if (e.getClass().equals(type))
				elements.add((T) e);
		}
		return (List<Classifier>) elements;
	}

}
