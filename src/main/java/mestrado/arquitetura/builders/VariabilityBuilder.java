package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Variability;
import mestrado.arquitetura.representation.VariationPoint;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;

/**
 * 
 * @author edipofederle
 *
 */
public class VariabilityBuilder {
	
	private Architecture architecture;
	
	private final HashMap<String, Variability> variabilities = new HashMap<String, Variability>();
		
	public VariabilityBuilder(Architecture architecture) throws ModelNotFoundException, ModelIncompleteException {
		this.architecture = architecture;
	}

	public List<Variability> create(Classifier klass) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {

		Variability variability  = null;
		List<Comment> commentVariability  = StereotypeHelper.getCommentVariability(klass);
		List<Variability> variabilitiesList = new ArrayList<Variability>();
		Element variationPointElement = null;
		
		for (Comment comment : commentVariability) {
			Map<String, String> variabilityAttributes = StereotypeHelper.getVariabilityAttributes(klass, comment);
			List<Element> variants = new ArrayList<Element>();
			if(variabilities.get(variabilityAttributes.get("name")) != null){
				variabilitiesList.add(variabilities.get(variabilityAttributes.get("name")));
			}else{
			
				if(variabilityAttributes != null) { 
					variationPointElement = architecture.findElementByName(klass.getName()); // Busca Classe ja na representacao
					
					variability = new Variability(variabilityAttributes.get("name"),
							                      variabilityAttributes.get("minSelection"),
							                      variabilityAttributes.get("maxSelection"),
							                      allowAddingVar(variabilityAttributes),
							                      variabilityAttributes, klass.getName());
					
					variabilities.put(variability.getName(), variability);
					String[] variantsElements = variabilityAttributes.get("variants").split(",");
					
					for (String variantElement : variantsElements) {
						Element element = architecture.findElementByName(variantElement.trim());
						if (element != null) variants.add(element);
					}
					
					
				}
			}
			variability.addVariationPoint(new VariationPoint(variationPointElement, variants));
			variabilitiesList.add(variability);
		}
			
		
		return variabilitiesList;
	}

	private boolean allowAddingVar(Map<String, String> a) {
		return "true".equalsIgnoreCase(a.get("allowAddingVar")) ? true : false;
	}

}