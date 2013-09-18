package arquitetura.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

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
		element.addConcerns(concerns);
		createdElements.put(XmiHelper.getXmiId(modelElement), element);
		addIdToListOfElements(modelElement);
		return element;
	}

	private void addIdToListOfElements(NamedElement modelElement) {
		if(!architecture.getAllIds().contains(XmiHelper.getXmiId(modelElement)))
			architecture.getAllIds().add(XmiHelper.getXmiId(modelElement));
	}
	
	private void inspectStereotypes(NamedElement modelElement) {
		List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
		for (Stereotype stereotype : allStereotypes) {
//			try {
//				verifyVariant(stereotype, modelElement);
//			} catch (Exception e) {}
//			
			verifyConcern(stereotype);
		}
	}
	
	private void verifyConcern(Stereotype stereotype) {
		if (StereotypeHelper.hasConcern(stereotype))
			 concerns.add(stereotype.getName());
	}
	
//	private void verifyVariant(Stereotype stereotype, NamedElement modelElement) {
//		VariantType type = VariantType.getByName(stereotype.getName());
//		if ((type != null) && (!stereotype.getName().equalsIgnoreCase(VariantType.VARIATIONPOINT.toString()))){
//			
//			variantType = Variant.createVariant().withName(stereotype.getName())
//								   .andRootVp(StereotypeHelper.getValueOfAttribute(modelElement, stereotype, "rootVP"))
//								   .build();
//			
//		}
//	}

//	private String[] getVariabilities(Stereotype stereotype, NamedElement modelElement) {
//		EList<Property> stereotypeAttrs = stereotype.getAllAttributes();
//		
//		for (Property property : stereotypeAttrs) {
//			if("variabilities".equalsIgnoreCase(property.getName())){
//				return StereotypeHelper.getValueOfAttribute(modelElement, stereotype, "variabilities").split(",");
//			}
//		}
//		return new String[] {};
//	}
	
	private void initialize() {
		name = "";
		isVariationPoint = false;
		variantType = null;
		concerns = new ArrayList<String>();
	}
	
}