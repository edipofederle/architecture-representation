package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.helpers.test.HelperTest;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.representation.Class;
import arquitetura.representation.relationship.GeneralizationRelationship;


public class GeneralizationRelationshipTest extends HelperTest {
	
	@Test
	public void generalizationBetweenInterfaceAndClass() throws Exception{
		String path = "herancaInterfaceClass/";
		Architecture arch = givenAArchitecture(path+"herancaInterfaceClass");
		
		List<GeneralizationRelationship> generalization = arch.getAllGeneralizations();
		assertEquals(1, generalization.size());
		
		assertTrue(generalization.get(0).getChild() instanceof Interface);
		assertTrue(generalization.get(0).getParent() instanceof Class);
	
	}
	
}
