package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mestrado.arquitetura.helpers.ModelElementHelper;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.VariantType;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;


/**
 * Classe abstrata que abstrai elemetnos comuns aos builders.
 * 
 * @author edipofederle
 *
 * @param <T>
 */
public abstract class ElementBuilder<T extends mestrado.arquitetura.representation.Element> {

	protected String name;
	protected Boolean isVariationPoint;
	protected VariantType variantType;
	protected List<String> concerns;
	protected final Architecture architecture;
	private final HashMap<String, T> createdElements = new HashMap<String, T>();

	public ElementBuilder(Architecture architecture) {
		this.architecture = architecture;
	}
	
	protected abstract T buildElement(NamedElement modelElement, Element parent);
	
	public T create(NamedElement modelElement, Element parent) {
		initialize();
		inspectStereotypes(modelElement);
		name = modelElement.getName();
		T element = buildElement(modelElement, parent);
		element.addConcerns(concerns);
		createdElements.put(getXmiId(modelElement), element);
		return element;
	}
	
	private void inspectStereotypes(NamedElement modelElement) {
		List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
		for (Stereotype stereotype : allStereotypes) {
			verifyVariationPoint(stereotype);
			verifyVariant(stereotype);	
			//verifyFeature(stereotype); // TODO Verificar
			verifyConcern(stereotype);
		}
	}
	
	private void verifyConcern(Stereotype stereotype) {
		if (StereotypeHelper.hasConcern(stereotype))
			 concerns.add(stereotype.getName());
	}
	
	private void verifyVariant(Stereotype stereotype) {
		VariantType type = VariantType.getByName(stereotype.getName());
		if (type != VariantType.NONE)
			variantType = type;
	}
	private void verifyVariationPoint(Stereotype stereotype) {
		isVariationPoint = StereotypeHelper.isVariationPoint(stereotype);
	}
	
	private void initialize() {
		name = "";
		isVariationPoint = false;
		variantType = VariantType.NONE;
		concerns = new ArrayList<String>();
	}
	
	public T getElementByXMIID(Integer xmiid) {
		return createdElements.get(xmiid);
	}
	
	/**
	 * Returna o atributo xmi:id como uma <b>String</b> para um dado eObject.
	 * Retrona <b>null</b> (por enquanto) caso xmiResources for null.
	 * 
	 * @param eObject
	 * @return <b>String</b>
	 */
	private static String getXmiId (EObject eObject) {
		Resource xmiResource = eObject.eResource();
		if (xmiResource == null ) {
			return null; //TODO verificar isto. Não retornar NULL.
		} else {
			return ((XMLResource) xmiResource).getID(eObject);
		}
	}
	
}