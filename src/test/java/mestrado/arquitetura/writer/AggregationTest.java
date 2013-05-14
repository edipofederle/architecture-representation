package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.junit.Test;

public class AggregationTest extends TestHelper {
	
	@Test
	public void shouldCreateAggregationClassClass() throws Exception{
		DocumentManager doc = givenADocument("shared", "simples");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("id");
		String foo = op.forClass().createClass("bar").build().get("id");
		
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
	
	@Test
	public void shouldCreateAggregationAssociationClassInsidePackageAndClassOutsidePackagee() throws Exception{
		
		DocumentManager doc = givenADocument("sharedClassPackageClass", "simples");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("id");
		op.forPackage().createPacakge("models").withClass(bar).build();
		String foo = op.forClass().createClass("bar").build().get("id");
		
		op.forAggregation().createRelation("Nome").between(bar).withMultiplicy("1..*").and(foo).build();
		
	}
	
}