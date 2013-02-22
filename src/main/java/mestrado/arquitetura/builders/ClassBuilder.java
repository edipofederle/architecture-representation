package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Method;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

public class ClassBuilder extends ElementBuilder<mestrado.arquitetura.representation.Class> {
	
	private AttributeBuilder attributeBuilder;
	private MethodBuilder methodBuilder;
	private ModelHelper modelHelper;

	
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

	@Override
	protected mestrado.arquitetura.representation.Class buildElement(NamedElement modelElement) {
		mestrado.arquitetura.representation.Class klass = null; // TODO VER ISTO. 
		
		boolean isAbstract = false;
		if(modelElement instanceof ClassImpl){
			isAbstract = ((org.eclipse.uml2.uml.Classifier)modelElement).isAbstract();
		
		klass = new Class(architecture, name, isVariationPoint, variantType, isAbstract);
		klass.getAttributes().addAll(getAttributes(modelElement));
		klass.getMethods().addAll(getMethods(modelElement));
		}
		return klass;
	}

	private List<Attribute> getAttributes(NamedElement modelElement) {
		List<Attribute> attrs = new ArrayList<Attribute>();
		
		if(modelElement instanceof ClassImpl){
			EList<Property> attributes = ((Classifier) modelElement).getAllAttributes();
			for (Property property : attributes)
				attrs.add(attributeBuilder.create(property));
		}
		return attrs;
	}
	
	private List<Method> getMethods(NamedElement modelElement) {
		List<Method> methods = new ArrayList<Method>();
		List<Operation> elements = modelHelper.getAllMethods(modelElement);
		
		for (Operation classifier : elements)
			methods.add(methodBuilder.create(classifier));
			
		return methods;
	}

}