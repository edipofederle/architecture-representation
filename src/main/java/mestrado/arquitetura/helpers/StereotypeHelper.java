package mestrado.arquitetura.helpers;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.internal.impl.StereotypeImpl;

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
		EList<Stereotype> stes = a.getAppliedStereotypes();
		for (Stereotype stereotype : stes) {
			try {
				if(stereotype instanceof StereotypeImpl){
					if (((Classifier) stereotype).getGeneralizations().get(0)
							.getGeneral().getName()
							.equalsIgnoreCase(StereotypesTypes.CONCERN))
						return true;
				}
			} catch (Exception e) {
				return hasStereotype(a, StereotypesTypes.CONCERN);
			}
		}
		return false;
	}

	public static String getValueOfAttribute(Classifier element, Stereotype s, String attr) {
		return (String) element.getValue(s, attr);
	}

	/**
	 * Retorna o nome do concern, caso existir. Se element não possuir conern retorna ConcernNotFoundExpection.
	 * 
	 * @param c
	 * @return
	 */
	public static String getConcernName(NamedElement c) {
		if (isConcern(c)){
			EList<Stereotype> stes = c.getAppliedStereotypes();
			for (Stereotype stereotype : stes) {
				try {
					if(stereotype instanceof StereotypeImpl){
						if (((Classifier) stereotype).getGeneralizations().get(0).getGeneral().getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
							return stereotype.getName();
					}
				}catch (Exception e) {
					//TODO Log
				}
			}
		}
		return null;
		
	}

}