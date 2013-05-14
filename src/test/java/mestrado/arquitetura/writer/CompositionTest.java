package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.junit.Test;

public class CompositionTest extends TestHelper {
	
	@Test
	public void shouldCreateComposition() throws Exception{
		DocumentManager doc = givenADocument("Composicao", "simples");
		
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("id");
		String foo = op.forClass().createClass("bar").build().get("id");
		
		String classUm =  op.forClass().createClass("ClasseUm").build().get("id");
		String classDois = op.forClass().createClass("ClasseDois").build().get("id");
		
		op.forComposition().createComposition().between(bar).and(foo).build();
		op.forComposition().createComposition().between(classDois).and(classUm).build();
		Architecture a = givenAArchitecture2("Composicao");
		
		assertEquals(2,a.getAllAssociations().size());
		
		AssociationRelationship composition = a.getAllAssociations().get(0);
		assertEquals(2,composition.getParticipants().size());
		
		assertEquals("Composition",composition.getParticipants().get(0).getAggregation());
		assertEquals("1",composition.getParticipants().get(0).getMultiplicity().toString());
	}
	
	@Test
	public void shouldCreateCompositionWithMultiplicy() throws Exception{
		DocumentManager doc = givenADocument("ComposicaoComMultiplicidade", "simples");
		Operations op = new Operations(doc);
		
		String classUm =  op.forClass().createClass("ClasseUm").build().get("id");
		String classDois = op.forClass().createClass("ClasseDois").build().get("id");
		
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
		DocumentManager doc = givenADocument("composicaoPacote", "simples");
		Operations op = new Operations(doc);
		
		String classUm =  op.forClass().createClass("ClasseUm").build().get("id");
		String classDois = op.forClass().createClass("ClasseDois").build().get("id");
		
		op.forPackage().createPacakge("meu.com.pacote").withClass(classUm).build();
		op.forComposition().createComposition().between(classDois).and(classUm).build();
		
		Architecture a = givenAArchitecture2("composicaoPacote");
		AssociationRelationship decom = a.getAllAssociations().get(0);
		assertNotNull(decom);
		
		assertEquals(2, decom.getParticipants().size());
		
	}
}
