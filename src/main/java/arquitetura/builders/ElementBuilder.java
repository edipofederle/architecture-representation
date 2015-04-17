package arquitetura.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.ModelElementHelper;
import arquitetura.helpers.StereotypeHelper;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Variant;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 * @param <T>
 */
public abstract class ElementBuilder<T extends arquitetura.representation.Element> {

	protected String name;
	protected Boolean isVariationPoint;
	protected Variant variantType;
	protected List<String> concerns;
        //Inicio - Thaina 12/14 - Aspecto
        protected List<String> aspects;
        //Fim - Thaina 12/14 - Aspecto
	protected final Architecture architecture;
	private final HashMap<String, T> createdElements = new HashMap<String, T>();
	
	public ElementBuilder(Architecture architecture) {
		this.architecture = architecture;
	}
	
	protected abstract T buildElement(NamedElement modelElement);
	
	/**
	 * Cria um novo elemento arquitetural. 
	 * 
	 * @param modelElement
	 * @return
	 */
	public T create(NamedElement modelElement) {
		initialize();
		inspectStereotypes(modelElement);
		name = modelElement.getName();
		T element = buildElement(modelElement);
		try {
			element.addConcerns(concerns);
                        //Inicio - Thaina 12/14 - Aspecto
                        element.addAspects(aspects);
                        //Fim - Thaina 12/14 - Aspecto
		} catch (ConcernNotFoundException e) {
			e.printStackTrace();
		}
		createdElements.put(XmiHelper.getXmiId(modelElement), element);
		return element;
	}

	private void inspectStereotypes(NamedElement modelElement) {
		List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
		for (Stereotype stereotype : allStereotypes) {
			verifyConcern(stereotype);
                        verifyAspect(stereotype);
		}
	}
        
        //Inicio - Thaina 12/14 - Aspectos
        private void verifyAspect(Stereotype stereotype) {
		if (StereotypeHelper.hasAspect(stereotype)){
			 aspects.add(stereotype.getName());
                }
	}
        //Fim - Thaina 12/14 - Aspectos
	
	private void verifyConcern(Stereotype stereotype) {
		if (StereotypeHelper.hasConcern(stereotype)){
			 concerns.add(stereotype.getName());
                }
	}
	
	private void initialize() {
		name = "";
		isVariationPoint = false;
		variantType = null;
		concerns = new ArrayList<String>();
                //Inicio - Thaina 12/14 - Aspecto
                aspects = new ArrayList<String>();
                //Fim - Thaina 12/14 - Aspecto
	}
	
}