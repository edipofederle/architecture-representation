package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Class;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

public class ClassBuilder extends ElementBuilder<mestrado.arquitetura.representation.Class> {
	
	
	private AttributeBuilder attributeBuilder;

	public ClassBuilder(Architecture architecture) {
		super(architecture);
		attributeBuilder = new AttributeBuilder(architecture);
	}

	@Override
	protected mestrado.arquitetura.representation.Class buildElement(NamedElement modelElement) {
		boolean isAbstract = false;
		if(modelElement instanceof ClassImpl)
			isAbstract = ((org.eclipse.uml2.uml.Classifier)modelElement).isAbstract();
		mestrado.arquitetura.representation.Class klass = new Class(architecture, name, isVariationPoint, variantType, isAbstract);
		klass.getAttributes().addAll(getAttributes(modelElement));
		//class_.getMethods().addAll(getMethods(modelElement));
		//return klass;
		return klass;
	}

	private List<Attribute> getAttributes(NamedElement modelElement) {
		
		List<Attribute> attrs = new ArrayList<Attribute>();
		
		if(modelElement instanceof ClassImpl){
			EList<Property> attributes = ((Classifier) modelElement).getAllAttributes();
			for (Property property : attributes) {
				attrs.add(attributeBuilder.create(property));
			}
		}
		return attrs;
	}

}