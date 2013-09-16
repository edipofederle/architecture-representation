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
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;

public class CompositionTest extends TestHelper {
	
	private Element bar;
	private Element foo;
	private Element classeDois;
	private Element classeUm;
	
	@Before
	public void setUp() throws Exception{
		
		bar = Mockito.mock(Class.class);
		Mockito.when(bar.getName()).thenReturn("Bar");
		Mockito.when(bar.getId()).thenReturn("01010101001001001");
		
		foo = Mockito.mock(Class.class);
		Mockito.when(foo.getName()).thenReturn("Foo");
		Mockito.when(foo.getId()).thenReturn("199339390");
		
		
		classeUm = Mockito.mock(Class.class);
		Mockito.when(classeUm.getName()).thenReturn("ClasseUm");
		Mockito.when(classeUm.getId()).thenReturn("0101010101231201001001");
		
		classeDois = Mockito.mock(Class.class);
		Mockito.when(classeDois.getName()).thenReturn("ClasseDois");
		Mockito.when(classeDois.getId()).thenReturn("19933939023232");
	}
	
	@Test
	public void shouldCreateComposition() throws Exception{
		DocumentManager doc = givenADocument("Composicao");
		
		Operations op = new Operations(doc,null);
		
		String barKlass =  op.forClass().createClass(foo).build().get("id");
		String fooKlass = op.forClass().createClass(bar).build().get("id");
		
		op.forComposition().createComposition().between(barKlass).and(fooKlass).build();
		Architecture a = givenAArchitecture2("Composicao");
		
		assertEquals(1,a.getAllAssociations().size());
		
		AssociationRelationship composition = a.getAllAssociations().get(0);
		assertEquals(2,composition.getParticipants().size());
		
		assertEquals("Composition",composition.getParticipants().get(0).getAggregation());
		assertEquals("1",composition.getParticipants().get(0).getMultiplicity().toString());
	}
	
	@Test
	public void shouldCreateCompositionWithMultiplicy() throws Exception{
		DocumentManager doc = givenADocument("ComposicaoComMultiplicidade");
		Operations op = new Operations(doc,null);
		
		String classUm =  op.forClass().createClass(classeUm).build().get("id");
		String classDois = op.forClass().createClass(classeDois).build().get("id");
		
		op.forComposition().createComposition().between(classUm).withMultiplicy("1..*").and(classDois).withMultiplicy("0..1").build();
		
		Architecture a = givenAArchitecture2("ComposicaoComMultiplicidade");
		AssociationRelationship decom = a.getAllAssociations().get(0);
		assertNotNull(decom);
		
		assertEquals(2,decom.getParticipants().size());
		assertEquals("ClasseUm",decom.getParticipants().get(0).getCLSClass().getName());
		assertEquals("1..*",decom.getParticipants().get(0).getMultiplicity().toString());
		assertEquals("ClasseDois",decom.getParticipants().get(1).getCLSClass().getName());
		assertEquals("0..1",decom.getParticipants().get(1).getMultiplicity().toString());
	}
	
	@Test
	public void shouldCreateDependencyClassPackageClass() throws Exception{
		DocumentManager doc = givenADocument("composicaoPacote");
		Operations op = new Operations(doc, null);
		
		String classUm =  op.forClass().createClass(classeUm).build().get("id");
		String classDois = op.forClass().createClass(classeDois).build().get("id");
		
		Package pacote = Mockito.mock(Package.class);
		Mockito.when(pacote.getName()).thenReturn("meu.com.pacote");
		Mockito.when(pacote.getId()).thenReturn("12312312312");
		
		op.forPackage().createPacakge(pacote).withClass(classUm).build();
		op.forComposition().createComposition().between(classDois).and(classUm).build();
		
		Architecture a = givenAArchitecture2("composicaoPacote");
		AssociationRelationship decom = a.getAllAssociations().get(0);
		assertNotNull(decom);
		
		assertEquals(2, decom.getParticipants().size());
		
	}
}
