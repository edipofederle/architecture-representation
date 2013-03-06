package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.AssociationEnd;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Multiplicity;

import org.eclipse.uml2.uml.Property;

public class AssociationEndBuilder {
	

	public AssociationEnd create(Property property, Class klass) {
		boolean isNavigable = property.isNavigable();
		String aggregation = property.getAggregation().getName();
		
		Multiplicity multiplicity = new  Multiplicity(property.getLowerValue().stringValue(), property.getUpperValue().stringValue());
		return new AssociationEnd(klass, isNavigable, aggregation, multiplicity);
	}

}