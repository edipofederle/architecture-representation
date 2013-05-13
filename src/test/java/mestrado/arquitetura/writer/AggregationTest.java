package mestrado.arquitetura.writer;

import static org.junit.Assert.*;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.junit.Test;

public class AggregationTest extends TestHelper {
	
	@Test
	public void shouldCreateAggregationClass() throws Exception{
		DocumentManager doc = givenADocument("shared", "simples");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("classId");
		String foo = op.forClass().createClass("bar").build().get("classId");
		
		op.forAggregation().createRelation("Nome").between(bar).withMultiplicy("1..*").and(foo).build();
		
		Architecture arch = givenAArchitecture2("shared");
		
		assertEquals(1, arch.getAllAssociations().size());
		AssociationRelationship a = arch.getAllAssociations().get(0);
		assertEquals("Aggregation", a.getParticipants().get(0).getAggregation());
		assertEquals("foo", a.getParticipants().get(0).getCLSClass().getName());
		assertEquals("1..*", a.getParticipants().get(0).getMultiplicity().toString());
		
		assertEquals("none",a.getParticipants().get(1).getAggregation());
		assertEquals("1", a.getParticipants().get(1).getMultiplicity().toString());
		
	}

}