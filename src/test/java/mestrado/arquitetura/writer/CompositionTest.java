package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.CompositionOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.junit.Test;

public class CompositionTest extends TestHelper {
	
	@Test
	public void shouldCreateComposition() throws Exception{
		
		DocumentManager doc = givenADocument("Composicao", "simples");
		CompositionOperations cp = new CompositionOperations(doc);
		
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("classId");
		String foo = op.forClass().createClass("bar").build().get("classId");
		
		String classUm =  op.forClass().createClass("ClasseUm").build().get("classId");
		String classDois = op.forClass().createClass("ClasseDois").build().get("classId");
		
		cp.createComposition().between(bar).and(foo).build();
		cp.createComposition().between(classDois).and(classUm).build();
		Architecture a = givenAArchitecture2("Composicao");
		
		assertEquals(2,a.getAllAssociations().size());
		
		AssociationRelationship composition = a.getAllAssociations().get(0);
		assertEquals(2,composition.getParticipants().size());
		
		assertEquals("composite",composition.getParticipants().get(0).getAggregation());
		assertEquals("1",composition.getParticipants().get(0).getMultiplicity().toString());
		
	}
}
