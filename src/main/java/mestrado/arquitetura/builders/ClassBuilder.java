package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelElementHelper;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Method;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

/**
 * Builder resposável por criar element do tipo Classe.
 * 
 * @author edipofederle
 *
 */
public class ClassBuilder extends ElementBuilder<mestrado.arquitetura.representation.Class> {
	
	private AttributeBuilder attributeBuilder;
	private MethodBuilder methodBuilder;
	private ModelHelper modelHelper;

	/**
	 * Recebe como paramêtro {@link Architecture}. <br/>
	 * Initializa helper {@link ModelHelper}
	 * 
	 * @param architecture
	 */
	public ClassBuilder(Architecture architecture) {
		super(architecture);
		attributeBuilder = new AttributeBuilder(architecture);
		methodBuilder = new MethodBuilder(architecture);
		
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constrói um elemento do tipo {@link Class}.
	 */
	@Override
	protected mestrado.arquitetura.representation.Class buildElement(NamedElement modelElement, Element parent) {
		mestrado.arquitetura.representation.Class klass = null; // TODO VER ISTO. 
		
		boolean isAbstract = false;
		boolean isInterface = false;
		
		if(modelElement instanceof ClassImpl)
			isAbstract = ((org.eclipse.uml2.uml.Classifier)modelElement).isAbstract();
		
		isInterface = ModelElementHelper.isInterface(modelElement);
		
		klass = new Class(architecture, name, isVariationPoint, variantType, isAbstract, parent,isInterface);
		klass.getAttributes().addAll(getAttributes(modelElement, klass));
		klass.getMethods().addAll(getMethods(modelElement, klass));
		
		return klass;
	}
	
	/**
	 * Retorna todos atributos de uma Class.
	 * @param modelElement
	 * @return List
	 */
	private List<Attribute> getAttributes(NamedElement modelElement, Class parent) {
		List<Attribute> attrs = new ArrayList<Attribute>();
		
		if(modelElement instanceof ClassImpl){
			//EList<Property> attributes = ((Classifier) modelElement).getAllAttributes();
			List<Property> attributes = modelHelper.getAllAttributesForAClass(modelElement);
			for (Property property : attributes)
				attrs.add(attributeBuilder.create(property, parent));
		}
		
		return attrs;
	}
	
	/**
	 * Retorna todos os método de uma classe
	 * 
	 * @param modelElement
	 * @return List
	 */
	private List<Method> getMethods(NamedElement modelElement, Class parent) {
		List<Method> methods = new ArrayList<Method>();
		List<Operation> elements = modelHelper.getAllMethods(modelElement);
		
		for (Operation classifier : elements)
			methods.add(methodBuilder.create(classifier, parent));
			
		return methods;
	}

}