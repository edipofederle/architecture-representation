package mestrado.arquitetura.writer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Variability;
import mestrado.arquitetura.representation.Variant;

import org.junit.Test;

public class StereotypeTest extends TestHelper {
	
	
	@Test
	public void testStereotypes() throws Exception{
		Architecture a = givenAArchitecture("edipo");
		
		Variability variability1 = a.getAllVariabilities().get(0);
		Variability variability2 = a.getAllVariabilities().get(1);
		
		assertEquals("arquitetura deve ter duas variabilidades", 2, a.getAllVariabilities().size());
		assertEquals("variabilidade 1 deve ter 3 variants", 3, variability1.getVariants().size());
		assertEquals("variabilidade 2 deve ter 2 variants", 2, variability2.getVariants().size());
		
		assertEquals(1,a.getAllVariabilities().get(0).getVariationPoints().size());
		
		assertTrue(a.findClassByName("GameMenu").isVariationPoint());
		assertEquals("Ponto de variação deve ter 5 variants", 5, a.findClassByName("GameMenu").getVariants().size());
		assertEquals("[BricklesGameMenu, PongGameMenu, BowlingGameMenu, Class1, Class2]",
				    (a.findClassByName("GameMenu").getVariants().toString()));
		
		variability1.getVariationPoints().get(0).getVariants();
		
	}

	
	@Test
	public void shouldCreateClassWithSteretorypeMandatoryAndVariationPoint() throws Exception{
		
		DocumentManager doc = givenADocument("edipo2", "simples");
		Operations op = new Operations(doc);
		
		String idMenuGameClass = op.forClass().createClass("TicTacToe").build().get("id");
		
		
		String idFoo = op.forClass()
				         .createClass("MenuGame").isVariationPoint()
				         .build().get("id");
		
		Variant mandatory = givenAVariant("mandatory", idFoo);
		op.forClass().addStereotype(idFoo, mandatory);
		
		op.forGeneralization().between(idMenuGameClass).and(idFoo).build();
		
		
		Architecture a = givenAArchitecture2("edipo2");
		
		Class klassFoo = a.findClassByName("MenuGame");
		assertNotNull(klassFoo);
		
		assertTrue(klassFoo.isVariationPoint());
		assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
 	}
	
	@Test
	public void shouldCreateClassWithSteretorypeMandatory() throws Exception{
		
		DocumentManager doc = givenADocument("somenteComMandatory", "simples");
		Operations op = new Operations(doc);
		
		String idMenuGameClass = op.forClass().createClass("TicTacToe").build().get("id");
		
		
		String idFoo = op.forClass()
				         .createClass("MenuGame")
				         .build().get("id");
		
		Variant mandatory = givenAVariant("mandatory", idFoo);
		op.forClass().addStereotype(idFoo, mandatory);
		
		op.forGeneralization().between(idMenuGameClass).and(idFoo).build();
		
		
		Architecture a = givenAArchitecture2("somenteComMandatory");
		
		Class klassFoo = a.findClassByName("MenuGame");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
 	}


	
	@Test
	public void shouldCreateClassWithSteretorypeOptional() throws Exception{
		
		DocumentManager doc = givenADocument("ste2", "simples");
		Operations op = new Operations(doc);
		
		
		String idFoo = op.forClass().createClass("foo").build().get("id");
		Variant optional = givenAVariant("optional", idFoo);
		op.forClass().addStereotype(idFoo, optional);
		
		Architecture a = givenAArchitecture2("ste2");
		
		Class klassFoo = a.findClassByName("foo");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertEquals("optional", klassFoo.getVariantType().getVariantName());
	}
	
	@Test
	public void shouldAddStereotypeToClass() throws Exception{
		DocumentManager doc = givenADocument("ste3", "simples");
		Operations op = new Operations(doc);
		
		String id = op.forClass().createClass("foo").build().get("id");
		
		Variant optional = givenAVariant("optional", id);
		op.forClass().addStereotype(id, optional);
		
		Architecture a = givenAArchitecture2("ste3");
		Class klassFoo = a.findClassByName("foo");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertEquals("optional", klassFoo.getVariantType().getVariantName());
	}
	
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClass() throws Exception{
		DocumentManager doc = givenADocument("ste4", "simples");
		Operations op = new Operations(doc);
		
		
		String idClassFoo = op.forClass().createClass("foo").isVariationPoint()
					.build().get("id");
		Variant mandatory = givenAVariant("mandatory", idClassFoo);
		op.forClass().addStereotype(idClassFoo, mandatory);
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo");
		
		assertNotNull(klassFoo);
		assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
		assertTrue(klassFoo.isVariationPoint());
	}
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClassInvertOrder() throws Exception{
		DocumentManager doc = givenADocument("ste5", "simples");
		Operations op = new Operations(doc);
		
		String idClass = op.forClass().createClass("foo").isVariationPoint().build().get("id");
		Variant mandatory = givenAVariant("mandatory", idClass);
		/*
		 * Inverte ordem dos estereotipos em relação ao caso de teste anterior. Esses testes servem para ver se alguma informação
		 * Não esta sendo reescrita dentro da classe ElementBuilder.
		*/ 
		op.forClass().addStereotype(idClass, mandatory);
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo");
		
		assertNotNull(klassFoo);
		assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
		assertTrue(klassFoo.isVariationPoint());
	}
	
	@Test
	public void shouldAddConcernToClass() throws Exception{
		DocumentManager doc = givenADocument("addConcern", "simples");
		Operations op = new Operations(doc);
		
			op.forClass()
			   .createClass("foo")
			   .withConcern("Persistence")
			   .build().get("id");
		
		Architecture a = givenAArchitecture2("addConcern");
		assertNotNull(a.getAllConcerns());
		assertEquals("deve ter um interesse", 1, a.getAllConcerns().size());
		assertEquals("interese deve ser Persistence", "Persistence", a.getAllConcerns().entrySet().iterator().next().getValue().getName());
		
		
		
		
	}
	

}