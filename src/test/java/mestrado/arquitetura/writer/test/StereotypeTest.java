package mestrado.arquitetura.writer.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.uml2.uml.Stereotype;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.helpers.StereotypeHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.touml.BindingTime;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;
public class StereotypeTest extends TestHelper {
	
	
	private Element employee;
	private Element casa;
	
	@Before
	public void setUp() throws Exception{
		employee = Mockito.mock(Class.class);
		Mockito.when(employee.getName()).thenReturn("Employee");
		Mockito.when(employee.getId()).thenReturn("199339390");
		
		casa = Mockito.mock(Class.class);
		Mockito.when(casa.getName()).thenReturn("Casa");
		Mockito.when(casa.getId()).thenReturn("123123123123");
	}
	
	
	
	@Test
	public void testStereotypes() throws Exception{
		Architecture a = givenAArchitecture("edipo");
		
		assertEquals(1, a.getAllVariationPoints().size());
		assertEquals(2, a.getAllVariabilities().size());
		assertEquals(6, a.getAllVariants().size());
		
		Variability variabilidade1 = a.getAllVariabilities().get(1);
		Variability variabilidade0 = a.getAllVariabilities().get(0);
		
		assertEquals("variabilidade "+ variabilidade1.getName()  + " deve ter 2 variants", 2, variabilidade1.getVariants().size());
		assertEquals("variabilidade "+ variabilidade0.getName() + " deve ter 3 variants", 3, a.getAllVariabilities().get(0).getVariants().size());
		
		assertNotNull(a.getAllVariabilities().get(0).getVariationPoint());
		assertNotNull(a.getAllVariabilities().get(1).getVariationPoint());

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
		Operations op = new Operations(doc, null);
		
		String idEmployee = op.forClass().createClass(employee).build().get("id");
		
		
		String idFoo = op.forClass()
				         .createClass(casa).isVariationPoint("Bar1,Bar2,Bar3", "variability", BindingTime.DESIGN_TIME)
				         .build().get("id");
		
		Variant mandatory = givenAVariant("mandatory", idFoo, "mandatory");
		op.forClass().addStereotype(idFoo, mandatory);
		
		op.forGeneralization().between(idEmployee).and(idFoo).build();
		
		
		Architecture a = givenAArchitecture2("edipo2");
		
		Class klassFoo = a.findClassByName("Employee").get(0);
		assertNotNull(klassFoo);
		
		//assertTrue(klassFoo.isVariationPoint());
		//assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
 	}
	
	@Test
	public void shouldCreateClassWithSteretorypeMandatory() throws Exception{
		
		DocumentManager doc = givenADocument("somenteComMandatory");
		Operations op = new Operations(doc,null);
		
		String idEmployee = op.forClass().createClass(employee).build().get("id");
		
		
		String idFoo = op.forClass()
				         .createClass(casa)
				         .build().get("id");
		
		Variant mandatory = givenAVariant("mandatory", idFoo, "mandatory");
		op.forClass().addStereotype(idFoo, mandatory);
		
		op.forGeneralization().between(idEmployee).and(idFoo).build();
		
		
		Architecture a = givenAArchitecture2("somenteComMandatory");
		
		Class klassFoo = a.findClassByName("Employee").get(0);
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		//assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
 	}


	
	@Test
	public void shouldCreateClassWithSteretorypeOptional() throws Exception{
		
		DocumentManager doc = givenADocument("ste2");
		Operations op = new Operations(doc,null);
		
		
		String idFoo = op.forClass().createClass(casa).build().get("id");
		Variant optional = givenAVariant("optional", idFoo, "optional");
		op.forClass().addStereotype(idFoo, optional);
		
		Architecture a = givenAArchitecture2("ste2");
		
		Class klassFoo = a.findClassByName("Casa").get(0);
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		StereotypeHelper.getStereotypeByName(a.getModel(), "optional");
	}
	
	@Test
	public void shouldAddStereotypeToClass() throws Exception{
		DocumentManager doc = givenADocument("ste3");
		Operations op = new Operations(doc,null);
		
		String id = op.forClass().createClass(casa).build().get("id");
		
		Variant optional = givenAVariant("optional", id, "optional");
		op.forClass().addStereotype(id, optional);
		
		Architecture a = givenAArchitecture2("ste3");
		Class klassFoo = a.findClassByName("Casa").get(0);
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertNotNull(StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("Casa", a.getModel()), "optional"));
	}
	
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClass() throws Exception{
		DocumentManager doc = givenADocument("ste4");
		Operations op = new Operations(doc,null);
		
		String idClassFoo = op.forClass().createClass(casa).isVariationPoint("Bar1,Bar2,Bar3", "variability", BindingTime.DESIGN_TIME)
					.build().get("id");
		Variant mandatory = givenAVariant("mandatory", idClassFoo, "mandatory");
		op.forClass().addStereotype(idClassFoo, mandatory);
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("Casa").get(0);
		
		assertNotNull(klassFoo);
		
		assertNotNull(StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("Casa", a.getModel()), "mandatory"));
		assertNotNull(StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("Casa", a.getModel()), "variationPoint"));
		
	}
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClassInvertOrder() throws Exception{
		DocumentManager doc = givenADocument("ste5");
		Operations op = new Operations(doc,null);
		
		String idClass = op.forClass().createClass(casa).isVariationPoint("Bar1,Bar2,Bar3", "variability", BindingTime.DESIGN_TIME).build().get("id");
		Variant mandatory = givenAVariant("mandatory", idClass, "mandatory");
		/*
		 * Inverte ordem dos estereotipos em relação ao caso de teste anterior. Esses testes servem para ver se alguma informação
		 * Não esta sendo reescrita dentro da classe ElementBuilder.
		*/ 
		op.forClass().addStereotype(idClass, mandatory);
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("Casa").get(0);
		
		assertNotNull(klassFoo);
		
		Stereotype steMandatory = StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("Casa", a.getModel()), "mandatory");
		Stereotype steVariationPoint = StereotypeHelper.getStereotypeByName(modelHelper.getClassByName("Casa", a.getModel()), "variationPoint");
		
		assertNotNull(steMandatory);
		assertEquals("mandatory", steMandatory.getName());
		assertNotNull(steVariationPoint);
		assertEquals("variationPoint",steVariationPoint.getName());
	}
	
	@Test
	public void shouldAddConcernToClass() throws Exception{
		DocumentManager doc = givenADocument("addConcern");
		Operations op = new Operations(doc,null);
		
			op.forClass()
			   .createClass(casa)
			   .withConcern("persistence")
			   .build().get("id");
		
		Architecture a = givenAArchitecture2("addConcern");
		assertNotNull(a.getAllConcerns());
		assertEquals("deve ter um interesse", 1, a.getAllConcerns().size());
		assertEquals("interese deve ser Persistence", "persistence", a.getAllConcerns().entrySet().iterator().next().getValue().getName());
		
	}

}