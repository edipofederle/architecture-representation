package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.Class;
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
	

	public AssociationEnd create(Property property, Class klass) {
		boolean isNavigable = property.isNavigable();
		String aggregation = property.getAggregation().getName();
		
		Multiplicity multiplicity = new  Multiplicity(property.getLowerValue().stringValue(), property.getUpperValue().stringValue());
		return new AssociationEnd(klass, isNavigable, aggregation, multiplicity);
	}

}