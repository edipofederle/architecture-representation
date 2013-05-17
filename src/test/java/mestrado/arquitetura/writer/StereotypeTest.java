package mestrado.arquitetura.writer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Variant;

import org.junit.Test;

public class StereotypeTest extends TestHelper {
	

	
	@Test
	public void shouldCreateClassWithSteretorypeMandatory() throws Exception{
		
		DocumentManager doc = givenADocument("edipo2", "simples");
		Operations op = new Operations(doc);
		
		String id = op.forClass().createClass("foo").build().get("id");
		
		Variant mandatory = givenAVariant("mandatory");
		
		op.forClass().addStereotype(id, mandatory);
		
		Architecture a = givenAArchitecture2("edipo2");
		
		Class klassFoo = a.findClassByName("foo");
		assertNotNull(klassFoo);
		
		assertFalse(klassFoo.isVariationPoint());
		assertEquals("[vb1, vb2]", klassFoo.getVariantType().getVariabilities().toString());
 	}

	private Variant givenAVariant(String name) {
		Variant mandatory = Variant.createVariant().withName(name)
				                   .andRootVp("rootVP")
				                   .andVariabilities("vb1", "vb2")
				                   .build();
		return mandatory;
	}
	
	@Test
	public void shouldCreateClassWithSteretorypeOptional() throws Exception{
		
		DocumentManager doc = givenADocument("ste2", "simples");
		Operations op = new Operations(doc);
		
		Variant optional = givenAVariant("optional");
		op.forClass().createClass("foo").withStereoype(optional).build();
		
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
		
		Variant optional = givenAVariant("optional");
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
		
		Variant mandatory = givenAVariant("mandatory");
		
		op.forClass().createClass("foo")
					.withStereoype(mandatory)
					.build().get("id");
		
		Architecture a = givenAArchitecture2("ste4");
		Class klassFoo = a.findClassByName("foo");
		
		assertNotNull(klassFoo);
		assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
		//assertTrue(klassFoo.isVariationPoint());
	}
	
//	@Test
//	public void shouldAddMandatoryAndVariationPointToClassInvertOrder() throws Exception{
//		DocumentManager doc = givenADocument("ste4", "simples");
//		Operations op = new Operations(doc);
//		
//		Variant mandatory = givenAVariant("mandatory");
//		/*
//		 * Inverte ordem dos estereotipos em relação ao caso de teste anterior. Esses testes servem para ver se alguma informação
//		 * Não esta sendo reescrita dentro da classe ElementBuilder.
//		*/ 
//		op.forClass().createClass("foo").withStereoype(VariantType.MANDATORY, VariantType.VARIATIONPOINT).build().get("id");
//		
//		Architecture a = givenAArchitecture2("ste4");
//		Class klassFoo = a.findClassByName("foo");
//		
//		assertNotNull(klassFoo);
//		assertEquals("mandatory", klassFoo.getVariantType().getVariantName());
//		assertTrue(klassFoo.isVariationPoint());
//	}
	

}