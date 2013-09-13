package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;

public class AggregationTest extends TestHelper {
	
	private Element bar;
	private Element foo;
	
	
	@Before
	public void setUp() throws Exception{
		
		bar = Mockito.mock(Class.class);
		Mockito.when(bar.getName()).thenReturn("Bar");
		Mockito.when(bar.getId()).thenReturn("01010101001001001");
		
		foo = Mockito.mock(Class.class);
		Mockito.when(foo.getName()).thenReturn("Foo");
		Mockito.when(foo.getId()).thenReturn("199339390");
	}
	
	@Test
	public void shouldAggregationHaveAName() throws Exception{
		DocumentManager doc = givenADocument("AggregationWithName");
		Operations op = new Operations(doc,null);
		
		String barKlass =  op.forClass().createClass(foo).build().get("id");
		String fooKlass = op.forClass().createClass(bar).build().get("id");
		
		op.forAggregation().createRelation("My Aggregation").between(barKlass).and(fooKlass).build();
		
		Architecture a = givenAArchitecture2("AggregationWithName");
		AssociationRelationship ag = a.getAllAssociations().get(0);
		
		assertNotNull(ag);
		assertEquals("My Aggregation", ag.getName());
	}
	
	@Test
	public void shouldAggregationHaveDefaultNameIfBlank() throws Exception{
		DocumentManager doc = givenADocument("AggregationWithName2");
		Operations op = new Operations(doc,null);
		
		String barKlass =  op.forClass().createClass(foo).build().get("id");
		String fooKlass = op.forClass().createClass(bar).build().get("id");
		
		op.forAggregation().createRelation("").between(barKlass).and(fooKlass).build();
		
		Architecture a = givenAArchitecture2("AggregationWithName2");
		AssociationRelationship ag = a.getAllAssociations().get(0);
		
		assertNotNull(ag);
		assertEquals("shared", ag.getName());
	}
	
	@Test
	public void shouldCreateAggregationClassClass() throws Exception{
		DocumentManager doc = givenADocument("shared");
		Operations op = new Operations(doc,null);
		
		String barKlass =  op.forClass().createClass(foo).build().get("id");
		String fooKlass = op.forClass().createClass(bar).build().get("id");
		
		op.forAggregation().createRelation("Nome").between(barKlass).withMultiplicy("1..*").and(fooKlass).build();
		
		Architecture arch = givenAArchitecture2("shared");
		
		assertEquals(1, arch.getAllAssociations().size());
		AssociationRelationship a = arch.getAllAssociations().get(0);
		assertEquals("Aggregation", a.getParticipants().get(0).getAggregation());
		assertEquals("Foo", a.getParticipants().get(0).getCLSClass().getName());
		assertEquals("1..*", a.getParticipants().get(0).getMultiplicity().toString());
		
		assertEquals("none",a.getParticipants().get(1).getAggregation());
		assertEquals("1", a.getParticipants().get(1).getMultiplicity().toString());
	}
	
	@Test
	public void shouldCreateAggregationAssociationClassInsidePackageAndClassOutsidePackagee() throws Exception{
		
		DocumentManager doc = givenADocument("sharedClassPackageClass");
		Operations op = new Operations(doc,null);
		
		arquitetura.representation.Package models = Mockito.mock(arquitetura.representation.Package.class);
		Mockito.when(models.getName()).thenReturn("models");
		Mockito.when(models.getId()).thenReturn("6666666");
		
		String barKlass =  op.forClass().createClass(foo).build().get("id");

		op.forPackage().createPacakge(models).withClass(foo.getId()).build();
		String fooKlass = op.forClass().createClass(bar).build().get("id");
		
		op.forAggregation().createRelation("Nome").between(barKlass).withMultiplicy("1..*").and(fooKlass).build();
		
	}
	
}