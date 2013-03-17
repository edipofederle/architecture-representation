package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.AbstractionRelationship;
import mestrado.arquitetura.representation.Architecture;

import org.junit.Test;

public class AbstratictionsTest  extends TestHelper{
	
	
	@Test
	public void shouldLoadAbstractionInterElement() throws Exception {
		Architecture architecture7 = givenAArchitecture("abstractionInterElement");

		assertNotNull(architecture7);

		AbstractionRelationship abstractionInterElement = architecture7.getAllAbstractions().get(0);

		assertNotNull(abstractionInterElement);
		assertEquals("Supplier should be Package1Supplier", "Package1Supplier",
				abstractionInterElement.getChild().getName());
		assertEquals("Supplier should be myInterfaceClient", "myInterfaceClient", abstractionInterElement.getParent().getName());
		assertTrue(abstractionInterElement.getParent().isInterface());
	}

}
