package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.AssociationEnd;
import mestrado.arquitetura.representation.relationship.Multiplicity;

import org.eclipse.uml2.uml.Property;

/**
 * Representa uma AssociationEnd numa associação.
 * 
 * @author edipofederle
 *
 */
public class AssociationEndBuilder {
	

	public AssociationEnd create(Property property, Element klass) {
		boolean isNavigable = property.isNavigable();
		String aggregation = property.getAggregation().getName();
		
		String upperValue = property.getUpperValue() == null ? "" : property.getUpperValue().stringValue();
		Multiplicity multiplicity = new  Multiplicity(property.getLowerValue().stringValue(), upperValue );
		return new AssociationEnd(klass, isNavigable, aggregation, multiplicity);
	}

}