package mestrado.arquitetura.helpers;

import java.util.Iterator;

import mestrado.arquitetura.exceptions.ConcernNotFoundException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.StereotypeImpl;

/**
 * 
 * Helpers para trabalhar com estereótipos.
 * 
 * @author edipofederle
 *
 */
public class StereotypeHelper {
	
	/**
	 * 
	 * Verifica se um dado elemento (elt) contém o estereótipo (stereotypeName ).
	 * 
	 * @param elt
	 * @param stereotypeName
	 * @return boolean
	 */
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

	/**
	 * Verifica se um elemento é um ponto de variação.
	 * 
	 * @param element
	 * @return boolean
	 */
	public static boolean isVariationPoint(NamedElement element) {
		return hasStereotype(element, StereotypesTypes.VARIATION_POINT);
	}

	/**
	 * Verifica se um elemento é uma variabilidade.
	 * 
	 * @param element
	 * @return boolean
	 */
	public static boolean isVariability(NamedElement element) {
		EList<Comment> comments = ((Class) element).getPackage().getOwnedComments();
		
		for (Comment comment : comments) 
			for (Stereotype stereotype : comment.getAppliedStereotypes())
				if( stereotype.getName().equalsIgnoreCase("variability")) return true;
		
		return false;
	}
	
	/**
	 * Verifica se elemento possui interesse
	 * 
	 * @param element
	 * @return boolean
	 */
	public static boolean hasConcern(NamedElement element) {
		try {
			if(element instanceof ClassImpl)
				if (searchForConcernsStereotypes(element) != null) return true;
			if (element instanceof StereotypeImpl) 
				if (((Classifier) element).getGeneralizations().get(0).getGeneral().getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
					return true;
		} catch (Exception e) {
			return hasStereotype(element, StereotypesTypes.CONCERN);
		}
		return false;
	}
	
	/**
	 * Retorna o valorde um attributo de um dado estereótipo.
	 * 
	 * @param element
	 * @param s
	 * @param attr
	 * @return
	 */
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
	
	/**
	 * Busca por concern em todos os estereótipos aplicados em um elemento.
	 * 
	 * @param element
	 * @return {@link Stereotype}
	 */
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