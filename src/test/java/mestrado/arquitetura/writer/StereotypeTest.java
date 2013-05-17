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
import mestrado.arquitetura.representation.VariantType;

import org.junit.Test;

public class StereotypeTest extends TestHelper {
	
	
	@Test
	public void test() throws Exception{
		Architecture a = givenAArchitecture2("edipo");
		
		Class klass = a.findClassByName("foo");
		assertEquals("mandatory", klass.getVariantType().getVariantType().toString());
		assertEquals("ROOTVP", klass.getVariantType().getRootVP());
		assertEquals("[teste, demo, casa]", klass.getVariantType().getVariabilities().toString());
		assertEquals("3",a.getVariabilities().get(0).getMaxSelection());
	}
	
	@Test
	public void shouldCreateClassWithSteretorypeMandatory() throws Exception{
		
		DocumentManager doc = givenADocument("edipo2", "simples");
		Operations op = new Operations(doc);
		
		String id = op.forClass().createClass("foo").build().get("id");
		op.forClass().addStereotype(id, "mandatory");
		
		Architecture a = givenAArchitecture2("edipo2");
		
		Class klassFoo = a.findClassByName("foo");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
 	}
	
	@Test
	public void shouldCreateClassWithSteretorypeOptional() throws Exception{
		
		DocumentManager doc = givenADocument("ste2", "simples");
		Operations op = new Operations(doc);
		
		op.forClass().createClass("foo").withStereoype(VariantType.OPTIONAL).build();
		
		Architecture a = givenAArchitecture2("ste2");
		
		Class klassFoo = a.findClassByName("foo");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertEquals("optional", klassFoo.getVariantType().getVariantType().toString());
	}
	
	@Test
	public void shouldAddStereotypeToClass() throws Exception{
		DocumentManager doc = givenADocument("ste3", "simples");
		Operations op = new Operations(doc);
		
		String id = op.forClass().createClass("foo").build().get("id");
		
		op.forClass().addStereotype(id, "optional");
		
		Architecture a = givenAArchitecture2("ste3");
		Class klassFoo = a.findClassByName("foo");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertEquals("optional", klassFoo.getVariantType().getVariantType().toString());
	}
	
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClass() throws Exception{
		DocumentManager doc = givenADocument("ste4", "simples");
		Operations op = new Operations(doc);
		
		op.forClass().createClass("foo").withStereoype(VariantType.VARIATIONPOINT, VariantType.MANDATORY).build().get("id");
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo");
		
		assertNotNull(klassFoo);
		assertEquals("mandatory", klassFoo.getVariantType().getVariantType().toString());
		assertTrue(klassFoo.isVariationPoint());
	}
	
	@Test
	public void shouldAddMandatoryAndVariationPointToClassInvertOrder() throws Exception{
		DocumentManager doc = givenADocument("ste4", "simples");
		Operations op = new Operations(doc);
		
		/*
		 * Inverte ordem dos estereotipos em relação ao caso de teste anterior. Esses testes servem para ver se alguma informação
		 * Não esta sendo reescrita dentro da classe ElementBuilder.
		*/ 
		op.forClass().createClass("foo").withStereoype(VariantType.MANDATORY, VariantType.VARIATIONPOINT).build().get("id");
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo");
		
		assertNotNull(klassFoo);
		assertEquals("mandatory", klassFoo.getVariantType().getVariantType().toString());
		assertTrue(klassFoo.isVariationPoint());
	}
	

}