package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.api.touml.Operations;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.junit.Test;

public class AggregationTest extends TestHelper {
	
	
	@Test
	public void shouldAggregationHaveAName() throws Exception{
		DocumentManager doc = givenADocument("AggregationWithName");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("id");
		String foo = op.forClass().createClass("bar").build().get("id");
		
		op.forAggregation().createRelation("My Aggregation").between(bar).and(foo).build();
		
		Architecture a = givenAArchitecture2("AggregationWithName");
		AssociationRelationship ag = a.getAllAssociations().get(0);
		
		assertNotNull(ag);
		assertEquals("My Aggregation", ag.getName());
	}
	
	@Test
	public void shouldAggregationHaveDefaultNameIfBlank() throws Exception{
		DocumentManager doc = givenADocument("AggregationWithName2");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("id");
		String foo = op.forClass().createClass("bar").build().get("id");
		
		op.forAggregation().createRelation("").between(bar).and(foo).build();
		
		Architecture a = givenAArchitecture2("AggregationWithName2");
		AssociationRelationship ag = a.getAllAssociations().get(0);
		
		assertNotNull(ag);
		assertEquals("shared", ag.getName());
	}
	
	@Test
	public void shouldCreateAggregationClassClass() throws Exception{
		DocumentManager doc = givenADocument("shared");
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
		
		DocumentManager doc = givenADocument("sharedClassPackageClass");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("id");
		op.forPackage().createPacakge("models").withClass(bar).build();
		String foo = op.forClass().createClass("bar").build().get("id");
		
		op.forAggregation().createRelation("Nome").between(bar).withMultiplicy("1..*").and(foo).build();
		
	}
	
}