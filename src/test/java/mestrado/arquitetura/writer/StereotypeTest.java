package mestrado.arquitetura.writer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.uml2.uml.Stereotype;
import org.junit.Test;

import arquitetura.helpers.StereotypeHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.touml.BindingTime;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;
public class StereotypeTest extends TestHelper {
	
	
	@Test
	public void testStereotypes() throws Exception{
		Architecture a = givenAArchitecture("edipo");
		
		assertEquals(1, a.getAllVariationPoints().size());
		assertEquals(2, a.getAllVariabilities().size());
		assertEquals(6, a.getAllVariants().size());
		
		assertEquals("variabilidade 2 deve ter 2 variants", 3, a.getAllVariabilities().get(1).getVariants().size());
		assertEquals("variabilidade 1 deve ter 4 variants", 4, a.getAllVariabilities().get(0).getVariants().size());
		
		assertEquals(1,a.getAllVariabilities().get(0).getVariationPoints().size());
		assertEquals(1,a.getAllVariabilities().get(1).getVariationPoints().size());

		assertTrue(a.findClassByName("GameMenu").get(0).isVariationPoint());
		assertEquals("Ponto de variação deve ter 5 variants", 5, a.findClassByName("GameMenu").get(0).getVariationPoint().getVariants().size());
		
		List<Variant> listVariants = a.findClassByName("GameMenu").get(0).getVariationPoint().getVariants();
		List<String> variantNames = new ArrayList<String>();
		for (Variant string : listVariants) {
			variantNames.add(string.getName());
		}
		assertEquals("[BricklesGameMenu, PongGameMenu, BowlingGameMenu, Class1, Class2]", variantNames.toString());
	}
	
	@Test
	public void timer() throws Exception{
		Architecture a = givenAArchitecture("times");
		
		List<Variability> variabilities = a.getAllVariabilities();
		
		assertEquals(7, a.getAllVariants().size());
		assertEquals(2 ,a.getAllVariationPoints().size());
		assertEquals(2, variabilities.size());
		
		assertEquals(1, a.findClassByName("Class7").get(0).getVariant().getVariabilities().size());
		
		
		
//		System.out.println(k1.getVariant().getVariantType());
//		
//		System.out.println("Variabilidade "+variabilities.get(1).getName());
//		for(VariationPoint variationPoint : a.getAllVariantionsPoints()){
//			System.out.println("\tVariationPoint: "+ variationPoint.getVariationPointElement().getName() + ":" + variationPoint.getVariabilities().size());
//			System.out.println("\t"+ variationPoint.getVariationPointElement().getVariant().getVariantType()); //TODO VER sobre isso
//		}
//		System.out.println("Variantes");
//		for(Variant variant : a.getAllVariants()){
//			System.out.println("\t"+variant.getName() + ":" + variant.getVariantType() + ":" + variant.getVariabilities().size());
//		}
//		
//		System.out.println(variabilities.get(0).getName()+":"+variabilities.get(0).getVariationPoints().get(0).getVariationPointElement());
//		System.out.println(variabilities.get(0).getName()+":"+variabilities.get(0).getVariationPoints().get(1).getVariationPointElement());
//		System.out.println("\n");
//		
//		System.out.println(variabilities.get(1).getName()+":"+variabilities.get(1).getVariationPoints().get(0).getVariationPointElement());
//		System.out.println(variabilities.get(1).getName()+":"+variabilities.get(1).getVariationPoints().get(1).getVariationPointElement());
//		System.out.println("\n");
//		
//		System.out.println(variabilities.get(1).getName()+":"+variabilities.get(1).getVariationPoints().get(0).getVariants());
//		System.out.println(variabilities.get(1).getName()+":"+variabilities.get(1).getVariationPoints().get(1).getVariants());
//		System.out.println("Variants da Variabilidade 1");
//		
//		for(Variant variant : variabilities.get(1).getVariants()){
//			System.out.println(variant.getName());
//		}
		
	//	assertEquals(variabilities.get(0).getVariationPoints().get(0).getVariationPointElement(), variabilities.get(1).getVariationPoints().get(0).getVariationPointElement());
	}
	
	@Test
	public void shouldCreateClassWithSteretorypeMandatoryAndVariationPoint() throws Exception{
		
		DocumentManager doc = givenADocument("edipo2");
		Operations op = new Operations(doc);
		
		String idMenuGameClass = op.forClass().createClass("TicTacToe").build().get("id");
		
		
		String idFoo = op.forClass()
				         .createClass("MenuGame").isVariationPoint("Bar1,Bar2,Bar3", "variability", BindingTime.DESIGN_TIME)
				         .build().get("id");
		
		Variant mandatory = givenAVariant("mandatory", idFoo, "mandatory");
		op.forClass().addStereotype(idFoo, mandatory);
		
		op.forGeneralization().between(idMenuGameClass).and(idFoo).build();
		
		
		Architecture a = givenAArchitecture2("edipo2");
		
		Class klassFoo = a.findClassByName("MenuGame").get(0);
		assertNotNull(klassFoo);
		
		//assertTrue(klassFoo.isVariationPoint());
		//assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
 	}
	
	@Test
	public void shouldCreateClassWithSteretorypeMandatory() throws Exception{
		
		DocumentManager doc = givenADocument("somenteComMandatory");
		Operations op = new Operations(doc);
		
		String idMenuGameClass = op.forClass().createClass("TicTacToe").build().get("id");
		
		
		String idFoo = op.forClass()
				         .createClass("MenuGame")
				         .build().get("id");
		
		Variant mandatory = givenAVariant("mandatory", idFoo, "mandatory");
		op.forClass().addStereotype(idFoo, mandatory);
		
		op.forGeneralization().between(idMenuGameClass).and(idFoo).build();
		
		
		Architecture a = givenAArchitecture2("somenteComMandatory");
		
		Class klassFoo = a.findClassByName("MenuGame").get(0);
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		//assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
 	}


	
	@Test
	public void shouldCreateClassWithSteretorypeOptional() throws Exception{
		
		DocumentManager doc = givenADocument("ste2");
		Operations op = new Operations(doc);
		
		
		String idFoo = op.forClass().createClass("foo").build().get("id");
		Variant optional = givenAVariant("optional", idFoo, "optional");
		op.forClass().addStereotype(idFoo, optional);
		
		Architecture a = givenAArchitecture2("ste2");
		
		Class klassFoo = a.findClassByName("foo").get(0);
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		StereotypeHelper.getStereotypeByName(a.getModel(), "optional");
	}
	
	@Test
	public void shouldAddStereotypeToClass() throws Exception{
		DocumentManager doc = givenADocument("ste3");
		Operations op = new Operations(doc);
		
		String id = op.forClass().createClass("foo").build().get("id");
		
		Variant optional = givenAVariant("optional", id, "optional");
		op.forClass().addStereotype(id, optional);
		
		Architecture a = givenAArchitecture2("ste3");
		Class klassFoo = a.findClassByName("foo").get(0);
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertNotNull(StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("foo", a.getModel()), "optional"));
	}
	
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClass() throws Exception{
		DocumentManager doc = givenADocument("ste4");
		Operations op = new Operations(doc);
		
		String idClassFoo = op.forClass().createClass("foo").isVariationPoint("Bar1,Bar2,Bar3", "variability", BindingTime.DESIGN_TIME)
					.build().get("id");
		Variant mandatory = givenAVariant("mandatory", idClassFoo, "mandatory");
		op.forClass().addStereotype(idClassFoo, mandatory);
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo").get(0);
		
		assertNotNull(klassFoo);
		
		assertNotNull(StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("foo", a.getModel()), "mandatory"));
		assertNotNull(StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("foo", a.getModel()), "variationPoint"));
		
	}
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClassInvertOrder() throws Exception{
		DocumentManager doc = givenADocument("ste5");
		Operations op = new Operations(doc);
		
		String idClass = op.forClass().createClass("foo").isVariationPoint("Bar1,Bar2,Bar3", "variability", BindingTime.DESIGN_TIME).build().get("id");
		Variant mandatory = givenAVariant("mandatory", idClass, "mandatory");
		/*
		 * Inverte ordem dos estereotipos em relação ao caso de teste anterior. Esses testes servem para ver se alguma informação
		 * Não esta sendo reescrita dentro da classe ElementBuilder.
		*/ 
		op.forClass().addStereotype(idClass, mandatory);
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo").get(0);
		
		assertNotNull(klassFoo);
		
		Stereotype steMandatory = StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("foo", a.getModel()), "mandatory");
		Stereotype steVariationPoint = StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("foo", a.getModel()), "variationPoint");
		
		assertNotNull(steMandatory);
		assertEquals("mandatory", steMandatory.getName());
		assertNotNull(steVariationPoint);
		assertEquals("variationPoint",steVariationPoint.getName());
	}
	
	@Test
	public void shouldAddConcernToClass() throws Exception{
		DocumentManager doc = givenADocument("addConcern");
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