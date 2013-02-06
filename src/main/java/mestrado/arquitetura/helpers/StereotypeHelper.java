package mestrado.arquitetura.helpers;

import java.util.Iterator;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public class StereotypeHelper {
	
	public static boolean hasStereotype(Element elt, String stereotypeName) {
		boolean has = false;

		if (elt != null) {
			@SuppressWarnings("rawtypes")
			Iterator i = elt.getAppliedStereotypes().iterator();
			Stereotype currentStereotype;
			while (i.hasNext() && !has) {
				currentStereotype = (Stereotype) i.next();
				if (currentStereotype.getName().equalsIgnoreCase(stereotypeName))
					has = true;
			}
		}
		return has;
	}


}
