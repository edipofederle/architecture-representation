package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;


public class AttributeBuilder extends ElementBuilder<Attribute> {
	
	public AttributeBuilder(Architecture architecture) {
		super(architecture);
	}

	@Override
	protected Attribute buildElement(NamedElement modelElement) {
		Type attributeType = ((Property) modelElement).getType();
		String type = attributeType != null ? attributeType.getName() : "";
		return new Attribute(architecture, name, isVariationPoint, variantType, type);
	}

}
