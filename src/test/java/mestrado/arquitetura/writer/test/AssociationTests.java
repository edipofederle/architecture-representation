package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.touml.AssociationOperations;
import arquitetura.touml.ClassOperations;
import arquitetura.touml.DocumentManager;

public class AssociationTests  extends TestHelper {
	
	@Test
	public void shouldCreateAssociation() throws Exception{
		DocumentManager doc = givenADocument("testeNovaAssociacao");
		AssociationOperations associationOperations = new AssociationOperations(doc);
		ClassOperations classOperations = new ClassOperations(doc);
		
		Map<String, String> employee = classOperations.createClass("Employee").build();
		Map<String, String> casa = classOperations.createClass("Casa").build();
		
		associationOperations.createAssociation()
							 .betweenClass(employee.get("id")).withMultiplicy("1..*")
							 .andClass(casa.get("id")).withMultiplicy("0..1")
							 .build();
		
		Architecture a = givenAArchitecture2("testeNovaAssociacao");
		
		assertEquals(1,a.getAllAssociations().size());
		AssociationRelationship association = a.getAllAssociations().get(0);
		assertNotNull(association);
		assertEquals(2, association.getParticipants().size());
		
		assertEquals("Casa", association.getParticipants().get(0).getCLSClass().getName());
		assertEquals("0..1", association.getParticipants().get(0).getMultiplicity().toString());
		
		assertEquals("Employee", association.getParticipants().get(1).getCLSClass().getName());
		assertEquals("1..*", association.getParticipants().get(1).getMultiplicity().toString());
	}
}