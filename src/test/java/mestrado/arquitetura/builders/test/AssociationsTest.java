package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.AssociationEnd;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.Relationship;

import org.junit.Test;

public class AssociationsTest extends TestHelper {
	
	@Test
	public void shouldHaveTwoAssociations() throws Exception {
		Architecture a = givenAArchitecture("association");
		assertEquals("Architecture should contain 4 associations", 4,	a.getAllAssociations().size());
	}
	
	@Test
	public void testAssociations() throws Exception {
		Architecture architecture2 = givenAArchitecture("association");

		List<Relationship> r = architecture2.getInterClassRelationships();
		assertNotNull(r);
		assertTrue(r.get(0) instanceof AssociationRelationship);

		AssociationRelationship association = (AssociationRelationship) r.get(0);
		List<AssociationEnd> participants = association.getParticipants();

		assertEquals(2, participants.size());

		assertNotNull(association.getParticipants());
		assertEquals("none", association.getParticipants().get(0).getAggregation());
		assertFalse(association.getParticipants().get(0).isNavigable());
		assertEquals("Class2", participants.get(0).getCLSClass().getName());

		assertEquals("none", association.getParticipants().get(1).getAggregation());
		assertTrue(association.getParticipants().get(1).isNavigable());
		assertEquals("Class1", participants.get(1).getCLSClass().getName());
	}
	
	@Test
	public void testAssociation2() throws Exception {
		Architecture architecture2 = givenAArchitecture("association");
		
		AssociationRelationship association = architecture2.getAllAssociations().get(1);
		List<AssociationEnd> participants = association.getParticipants();

		assertNotNull(association);

		assertEquals(2, participants.size());

		assertEquals("none", association.getParticipants().get(0).getAggregation());
		assertTrue(association.getParticipants().get(0).isNavigable());
		assertEquals("Class3", participants.get(0).getCLSClass().getName());

		assertEquals("none", association.getParticipants().get(1).getAggregation());
		assertFalse(association.getParticipants().get(1).isNavigable());
		assertEquals("Class4", participants.get(1).getCLSClass().getName());
	}
	

	@Test
	public void testMultiplicityAssociationRelationship() throws Exception {
		Architecture architecture2 = givenAArchitecture("association");
		AssociationRelationship association = architecture2.getAllAssociations().get(1);
		assertEquals("1", association.getParticipants().get(1).getMultiplicity().getLowerValue());
		assertEquals("1", association.getParticipants().get(1).getMultiplicity().getUpperValue());
		assertEquals("1", association.getParticipants().get(1).getMultiplicity().toString());
	}

	@Test
	public void testMultiplicityAssociationRelationship2() throws Exception {
		Architecture architecture2 = givenAArchitecture("association");
		AssociationRelationship association = architecture2.getAllAssociations().get(0);

		assertEquals("1", association.getParticipants().get(0).getMultiplicity().getLowerValue());
		assertEquals("1", association.getParticipants().get(0).getMultiplicity().getUpperValue());
		assertEquals("1", association.getParticipants().get(0).getMultiplicity().toString());
	}
	
	@Test
	public void shouldContainCompositeAssociation() throws Exception {
		Architecture architecture2 = givenAArchitecture("association");
		AssociationRelationship associationComposite = architecture2.getAllAssociations().get(2);
		List<AssociationEnd> participants = associationComposite.getParticipants();

		assertFalse(associationComposite.getParticipants().get(0).isNavigable());
		assertEquals("Class5", participants.get(0).getCLSClass().getName());

		assertEquals("Composition", associationComposite.getParticipants().get(1).getAggregation());
		assertFalse(associationComposite.getParticipants().get(1).isNavigable());
		assertEquals("Class6", participants.get(1).getCLSClass().getName());
		assertEquals("none", associationComposite.getParticipants().get(0).getAggregation());
		// TODO rever nome do metodo getAggregation para getTypeAssociation?

		assertEquals("0..*", associationComposite.getParticipants().get(0).getMultiplicity().toString());
		assertEquals("1", associationComposite.getParticipants().get(1).getMultiplicity().toString());
	}
	
	@Test
	public void shouldContainAggregationAssociation() throws Exception {
		Architecture architecture2 = givenAArchitecture("association");
		AssociationRelationship aggregation = architecture2.getAllAssociations().get(3);
		List<AssociationEnd> participants = aggregation.getParticipants();

		assertFalse(aggregation.getParticipants().get(0).isNavigable());
		assertEquals("Class7", participants.get(0).getCLSClass().getName());

		assertFalse(aggregation.getParticipants().get(1).isNavigable());
		assertEquals("Class8", participants.get(1).getCLSClass().getName());

		assertEquals("Aggregation", aggregation.getParticipants().get(0).getAggregation());
		assertFalse(aggregation.getParticipants().get(1).isNavigable());

		assertEquals("1", aggregation.getParticipants().get(1).getMultiplicity().toString());
		assertEquals("1..*", aggregation.getParticipants().get(0).getMultiplicity().toString());
	}
	
	@Test
	public void testAssociationWithThreeClasses() throws Exception {
		Architecture architecture3 = givenAArchitecture("complexAssociation");
		List<Relationship> r = architecture3.getInterClassRelationships();

		assertNotNull(architecture3);
		assertEquals("Should Contains Two Relationships", 2, r.size());
		assertEquals("Should Contains Three Classes", 3, architecture3.getAllClasses().size());

		AssociationRelationship association1 = architecture3.getAllAssociations().get(0);
		AssociationRelationship association2 = architecture3.getAllAssociations().get(1);

		assertNotNull(association1);
		assertNotNull(association2);

		assertEquals(2, association1.getParticipants().size());
		assertEquals(2, association2.getParticipants().size());

		List<AssociationEnd> a = association1.getParticipants();
		List<AssociationEnd> b = association2.getParticipants();
		Element klass3 = a.get(0).getCLSClass();
		Element klass2 = a.get(1).getCLSClass();

		Element klass1 = b.get(1).getCLSClass();
		Element kllass2a = b.get(0).getCLSClass();

		assertEquals("Class1", klass1.getName());
		assertEquals("Class2", kllass2a.getName());
		assertEquals("Class3", klass3.getName());
		assertEquals("Class2", klass2.getName());
		assertTrue(a.get(1).isNavigable());
		assertFalse(a.get(0).isNavigable());
	}
	
	
	@Test
	public void test() throws Exception{
		Architecture a = givenAArchitecture("associacoa1");
		
		assertEquals(1, a.getAllAssociations().size());
		
		AssociationRelationship as = a.getAllAssociations().get(0);
		
		assertEquals(2, as.getParticipants().size());
		assertEquals("Class2", as.getParticipants().get(0).getCLSClass().getName());
		assertEquals("1..*", as.getParticipants().get(0).getMultiplicity().toString());
		assertEquals("0..1", as.getParticipants().get(1).getMultiplicity().toString());
	}

}
