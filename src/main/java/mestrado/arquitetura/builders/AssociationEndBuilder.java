package mestrado.arquitetura.builders;

import mestrado.arquitetura.representation.AssociationEnd;
import mestrado.arquitetura.representation.Class;

import org.eclipse.uml2.uml.Property;

public class AssociationEndBuilder {
	

	public AssociationEnd create(Property property, Class klass) {
		boolean isNavigable = property.isNavigable();
		String aggregation = property.getAggregation().getName();
		return new AssociationEnd(klass, isNavigable, aggregation);
	}

}
