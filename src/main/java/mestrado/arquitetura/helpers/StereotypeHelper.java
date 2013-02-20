package mestrado.arquitetura.helpers;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
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

	public static boolean hasConcern(NamedElement a) {
		try {
			if(a instanceof ClassImpl)
				if (searchForConcernsStereotypes(a) != null) return true;
			if (a instanceof StereotypeImpl) 
				if (((Classifier) a).getGeneralizations().get(0).getGeneral().getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
					return true;
		} catch (Exception e) {
			return hasStereotype(a, StereotypesTypes.CONCERN);
		}
		return false;
	}

	public static String getValueOfAttribute(Classifier element, Stereotype s, String attr) {
		return (String) element.getValue(s, attr);
	}

	/**
	 * Retorna o nome do concern, caso existir. Se element não possuir concern retorna ConcernNotFoundExpection.
	 * 
	 * @param c
	 * @return
	 * @throws ConcernNotFoundException 
	 */
	public static String getConcernName(NamedElement c) throws ConcernNotFoundException {
		if (hasConcern(c))
			if (searchForConcernsStereotypes(c) != null )
				return searchForConcernsStereotypes(c).getName();
		throw new ConcernNotFoundException("There is not concern in element " + c );
		
	}

	public static boolean isConcern2(NamedElement element) {
		EList<Stereotype> stes = element.getAppliedStereotypes();
		for (Stereotype stereotype : stes) {
			EList<Stereotype> subStereotype = stereotype.getApplicableStereotypes();
			for (Stereotype subste : subStereotype)
				if(StereotypesTypes.CONCERN.equalsIgnoreCase(subste.getName()))
					return true;
		}
		return false;
	}
	
	private static Stereotype searchForConcernsStereotypes(NamedElement element){
		EList<Stereotype> stes = element.getAppliedStereotypes();
		for (Stereotype stereotype : stes) {
			if(stereotype instanceof StereotypeImpl)
				if(!stereotype.getGeneralizations().isEmpty())
					if (stereotype.getGeneralizations().get(0).getGeneral()
						                     .getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
						return stereotype;
					
		}
		return null; //TODO FIX NO return null
	}

}