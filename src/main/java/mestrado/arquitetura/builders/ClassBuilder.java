package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

public class ClassBuilder extends ElementBuilder<mestrado.arquitetura.representation.Class> {
	
	//AttributeBuilder attributeBuilder;

	public ClassBuilder(Architecture architecture) {
		super(architecture);
		//attributeBuilder = new AttributeBuilder(architecture);
	}

	@Override
	protected mestrado.arquitetura.representation.Class buildElement(NamedElement modelElement) {
		boolean isAbstract = false;
		if(modelElement instanceof ClassImpl)
			isAbstract = ((org.eclipse.uml2.uml.Classifier)modelElement).isAbstract();
		mestrado.arquitetura.representation.Class klass = new Class(architecture, name, isVariationPoint, variantType, isAbstract);
		//klass.getAttributes().addAll(getAttributes(modelElement));
		//class_.getMethods().addAll(getMethods(modelElement));
		return klass;
	}



}
