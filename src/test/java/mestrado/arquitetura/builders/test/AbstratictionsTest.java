package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AbstractionRelationship;

public class AbstratictionsTest  extends TestHelper{
	
	
	@Test
	public void shouldLoadAbstractionInterElement() throws Exception {
		Architecture architecture7 = givenAArchitecture("abstractionInterElement");

		assertNotNull(architecture7);

		AbstractionRelationship abstractionInterElement = architecture7.getAllAbstractions().get(0);

		assertNotNull(abstractionInterElement);
		assertEquals("Client should be myInterfaceSupplier", "myInterfaceClient",	abstractionInterElement.getClient().getName());
		assertEquals("Supplier should be Package1Supplier", "Package1Supplier", abstractionInterElement.getSupplier().getName());
	}

}
