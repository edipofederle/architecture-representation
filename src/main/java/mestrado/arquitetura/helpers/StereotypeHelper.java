package mestrado.arquitetura.helpers;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

public class StereotypeHelper {
	
	public static boolean hasStereotype(NamedElement elt, String stereotypeName) {
		boolean has = false;

		if (elt != null) {
			Iterator<Stereotype> i = elt.getAppliedStereotypes().iterator();
			Stereotype currentStereotype;
			while (i.hasNext() && !has) {
				currentStereotype = (Stereotype) i.next();
				if (currentStereotype.getName().equalsIgnoreCase(stereotypeName))
					has = true;
			}
		}
		return has;
	}

	public static boolean isVariationPoint(NamedElement a) {
		return hasStereotype(a, StereotypesTypes.VARIATION_POINT);
	}

	public static boolean isVariability(NamedElement klass) {
		EList<Comment> comments = ((Class) klass).getPackage().getOwnedComments();
		
		for (Comment comment : comments) 
			for (Stereotype stereotype : comment.getAppliedStereotypes())
				if( stereotype.getName().equalsIgnoreCase("variability")) return true;
		
		return false;
	}

	public static boolean isConcern(NamedElement a) {
		if(a instanceof Stereotype)
			return StereotypesTypes.CONCERN.equalsIgnoreCase(a.getName());
		return hasStereotype(a, StereotypesTypes.CONCERN);
	}

}