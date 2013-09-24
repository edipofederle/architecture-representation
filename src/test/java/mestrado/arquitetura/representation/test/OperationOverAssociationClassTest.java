package mestrado.arquitetura.representation.test;

import static org.junit.Assert.*;
import main.GenerateArchitecture;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;

public class OperationOverAssociationClassTest extends TestHelper {

	@Test
	public void removeAssociation() throws Exception {
		Architecture a = givenAArchitecture("association");
		a.removeAssociationRelationship(a.getAllAssociationsRelationships().get(0));
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "removendoAssociacao");
		
		Architecture genereted = givenAArchitecture2("removendoAssociacao");
		assertEquals(1, genereted.getAllAssociationsRelationships().size());
		
	}
}
