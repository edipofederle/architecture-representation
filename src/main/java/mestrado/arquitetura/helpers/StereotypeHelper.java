package mestrado.arquitetura.helpers;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public class StereotypeHelper {
	
	public static boolean hasStereotype(Element elt, String stereotypeName) {
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

	public static boolean isVariationPoint(Classifier a) {
		return hasStereotype(a, StereotypesTypes.VARIATION_POINT);
	}

	public static boolean isVariability(Classifier klass) {
		EList<Comment> comments = klass.getPackage().getOwnedComments();
		
		for (Comment comment : comments) 
			for (Stereotype stereotype : comment.getAppliedStereotypes())
				if( stereotype.getName().equalsIgnoreCase("variability")) return true;
		
		return false;
	}

	public static boolean isConcern(Classifier a) {
		return hasStereotype(a, StereotypesTypes.CONCERN);
	}

}