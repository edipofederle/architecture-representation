package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.touml.AssociationOperations;
import arquitetura.touml.ClassOperations;
import arquitetura.touml.DocumentManager;

public class AssociationTests  extends TestHelper {
	
	
	private Element employee;
	private Element casa;
	
	@Before
	public void setUp() throws Exception{
		employee = Mockito.mock(Class.class);
		Mockito.when(employee.getName()).thenReturn("Employee");
		Mockito.when(employee.getId()).thenReturn("199339390");
		
		casa = Mockito.mock(Class.class);
		Mockito.when(casa.getName()).thenReturn("Casa");
		Mockito.when(casa.getId()).thenReturn("123123123123");
	}
	
	@Test
	public void shouldCreateAssociation() throws Exception{
		DocumentManager doc = givenADocument("testeNovaAssociacao");
		AssociationOperations associationOperations = new AssociationOperations(doc);
		ClassOperations classOperations = new ClassOperations(doc,null);
		
		Map<String, String> employeeInfo = classOperations.createClass(employee).build();
		Map<String, String> casaInfo = classOperations.createClass(casa).build();
		
		associationOperations.createAssociation()
							 .betweenClass(employeeInfo.get("id")).withMultiplicy("1..*")
							 .andClass(casaInfo.get("id")).withMultiplicy("0..1")
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