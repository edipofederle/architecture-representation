package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Element;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * Builder responsável pelos atributos.
 * 
 * @author edipofederle
 *
 */
public class AttributeBuilder extends ElementBuilder<Attribute> {
	
	public AttributeBuilder(Architecture architecture) {
		super(architecture);
	}
	
	/**
	 * constrói um elemento do tipo atributo.
	 */
	@Override
	protected Attribute buildElement(NamedElement modelElement, Element parent) {
		Type attributeType = ((Property) modelElement).getType();
		String type = attributeType != null ? attributeType.getName() : "";
		
		return new Attribute(architecture, name, isVariationPoint, variantType, type, parent, modelElement.getNamespace().getQualifiedName(), false);
	}

}