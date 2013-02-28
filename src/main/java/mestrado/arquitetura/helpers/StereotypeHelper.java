package mestrado.arquitetura.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mestrado.arquitetura.exceptions.ConcernNotFoundException;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl;
import org.eclipse.uml2.uml.internal.impl.StereotypeImpl;

/**
 * 
 * Helpers para trabalhar com estereótipos.
 * 
 * @author edipofederle
 *
 */
public class StereotypeHelper extends TestHelper {
	
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
	public static Comment getCommentVariability(NamedElement element) {
		EList<Comment> comments = ((Class) element).getPackage().getOwnedComments();
		
		for (Comment comment : comments) 
			for (Stereotype stereotype : comment.getAppliedStereotypes())
				if (stereotype.getName().equalsIgnoreCase("variability")) return comment;
		
		return null;
	}
	
	
	/**
	 * Verifica se um elemento é uma variabilidade.
	 * 
	 * @param element
	 * @return boolean
	 */
	public static boolean isVariability(NamedElement element) {
		return getCommentVariability(element) != null ? true : false;
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
	 * Retorna o valor de um attributo de um dado estereótipo e EnumerationLiteral.
	 * 	 * @param <T>
	 * 
	 * @param element
	 * @param stereotype
	 * @param attrName name
	 * @return
	 */
	public static String getValueOfAttribute(Element element, Stereotype variability, String attrName) {
		
		if(element.getValue(variability, attrName) instanceof EnumerationLiteralImpl){
			EnumerationLiteral e = (EnumerationLiteral) element.getValue(variability, attrName);
			return e.getName();
		}
		
		return (String) element.getValue(variability, attrName).toString();
		
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
		
		return null;
	}

	public static Map<String, String> getVariabilityAttributes(NamedElement klass) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		Comment commentVariability  = getCommentVariability(klass);
		if(commentVariability != null){
			
			Stereotype variability = getStereotypeByName(klass, "variability");
			Map<String, String> variabilityProps  = new HashMap<String, String>();
			
			String name = getValueOfAttribute(commentVariability, variability, "name");
			String bidingTime = getValueOfAttribute(commentVariability, variability, "bindingTime");
			String maxSelection = getValueOfAttribute(commentVariability, variability, "maxSelection");
			String minSelection = getValueOfAttribute(commentVariability, variability, "minSelection");
			String variants = getValueOfAttribute(commentVariability, variability, "variants");
			String allowAddingVar = getValueOfAttribute(commentVariability, variability, "allowAddingVar");
			
			variabilityProps.put("name", name);
			variabilityProps.put("bindingTime", bidingTime);
			variabilityProps.put("maxSelection", maxSelection);
			variabilityProps.put("minSelection", minSelection);
			variabilityProps.put("variants", variants);
			variabilityProps.put("allowAddingVar", allowAddingVar);
			
			return variabilityProps;
		}
		
		return Collections.emptyMap();
	}

	private static Stereotype getStereotypeByName(NamedElement element, String stereotypeName) {
		List<Stereotype> stereotypes = ModelElementHelper.getAllStereotypes(element);
		for (Stereotype stereotype : stereotypes) {
			if(stereotypeName.equalsIgnoreCase(stereotype.getName()))
				return stereotype;
		}
		
		return null;
	}

}