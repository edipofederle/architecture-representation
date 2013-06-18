package mestrado.arquitetura.xmi2uml2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import arquitetura.exceptions.AttributeNotFoundException;
import arquitetura.exceptions.MethodNotFoundException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Method;
import arquitetura.xmi2uml2.Class;

public class ClassTest extends TestHelper{
	
	
	@Test
	public void testCreateAClass() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		Class klass = Class.createOnModel(model).withName("Person").withVisibility("PUBLIC_LITERAL");
		assertNotNull(klass);
		assertEquals("Person", klass.getName());
		assertEquals("public", klass.getVisibility());
	}
	
	@Test
	public void whenClassWitoutNameDefaultNameIsChoose() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		Class klass = Class.createOnModel(model).withVisibility("PUBLIC_LITERAL");
		assertEquals("DefaultName", klass.getName());
	}
	
	@Test
	public void whenClassWithoutVisibilityPublicIsDefault() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		Class klass = Class.createOnModel(model).withName("Dog");
		assertEquals("public", klass.getVisibility());		
	}
	
	@Test
	public void shouldHaveOneAttribute() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		Class klass = Class.createOnModel(model).withName("Dog").withAttributes("name:string");
		assertEquals(1, klass.getAttributes().size());
		assertEquals("string", klass.getAttributes().get(0).getType());
		assertEquals("name", klass.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldHaveTwoAttribute() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		Class klass = Class.createOnModel(model).withName("Dog1").withAttributes("name:string", "age:integer");
		assertEquals(2, klass.getAttributes().size());
		assertEquals("string", klass.getAttributes().get(0).getType());
		assertEquals("name", klass.getAttributes().get(0).getName());
		
		assertEquals("integer", klass.getAttributes().get(1).getType());
		assertEquals("age", klass.getAttributes().get(1).getName());
	}
	
	@Test
	public void shouldHaveOneMethodWithOneParamater() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "string");
		
		Class klass = Class.createOnModel(model).withName("Person")
				                                .withMethod("foo", params, "Integer");
		assertEquals("should have one method", 1, klass.getAllMethods().size());
		assertEquals("foo", klass.getAllMethods().get(0).getName());
		
		assertEquals("should have two parameter", 2, klass.getAllMethods().get(0).getOwnedParameters().size());
		assertEquals("name", klass.getAllMethods().get(0).getOwnedParameters().get(0).getName());
		assertEquals("String", klass.getAllMethods().get(0).getOwnedParameters().get(0).getType().getName());
		assertEquals("Integer", klass.getAllMethods().get(0).getType().getName());
	}
	
	
	@Test
	public void shouldHaveOneMethodWithTwoParamater() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("modelVazio");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "string");
		params.put("age", "integer");
		
		Class klass = Class.createOnModel(model).withName("Foo1")
				                                .withMethod("bar", params, "String");
		assertEquals("should have one method", 1, klass.getAllMethods().size());
		assertEquals("bar", klass.getAllMethods().get(0).getName());
		
		assertEquals("should have three parameter", 3, klass.getAllMethods().get(0).getOwnedParameters().size());
	
		assertEquals("bar", klass.getAllMethods().get(0).getName());
		assertEquals("Integer", klass.getAllMethods().get(0).getOwnedParameters().get(0).getType().getName());
		assertEquals("String", klass.getAllMethods().get(0).getOwnedParameters().get(1).getType().getName());
		
		assertEquals("age", klass.getAllMethods().get(0).getOwnedParameters().get(0).getName());
		assertEquals("name", klass.getAllMethods().get(0).getOwnedParameters().get(1).getName());
	}
	
//	@Test
//	public void shouldCreateAttribute() throws Exception{
//		
//		mestrado.arquitetura.representation.Class klass = a.findClassByName("Class1");
//		assertNotNull(klass);
//		assertEquals(0, klass.getAttributes().size());
//		
//		Attribute att = klass.createAttribute("name", "String");
//		String id = att.getId();
//		assertTrue(a.getAllIds().contains(id));
//		assertEquals("classes::Class1", att.getNamespace());
//		assertNotNull(att.getId());
//		
//		assertNotNull(att);
//		assertEquals(1, klass.getAttributes().size());
//	}
	
//	@Test @Ignore
//	public void shouldRemoveAttribute() throws Exception{
//		Architecture a = givenAArchitecture("ExtendedPO2");
//		
//		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
//		assertNotNull(klass);
//		assertEquals("Person", klass.getName());
//		//Attribute att = klass.createAttribute("name", "String");
//		assertEquals(2, klass.getAttributes().size());
//		
//		String id = att.getId();
//		assertTrue(a.getAllIds().contains(id));
//		klass.removeAttribute(att);
//		
//		assertEquals(1, klass.getAttributes().size());
//		assertFalse(a.getAllIds().contains(id));
//	}
//	
	@Test
	public void shouldMoveAttributeFromOneClassToOther() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		arquitetura.representation.Class klass8 = a.findClassByName("Class8").get(0);
		Attribute att = klass.findAttributeByName("name");
		
		assertEquals(0, klass8.getAllAttributes().size());
		assertEquals(1, klass.getAllAttributes().size());
		
		klass.moveAttributeToClass(att, klass8 );
		
		assertEquals(1, klass8.getAllAttributes().size());
		assertEquals(0, klass.getAllAttributes().size());
	}
	
	@Test
	public void shouldChangeNamespaceWhenMoveAttribute() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		arquitetura.representation.Class klass8 = a.findClassByName("Class8").get(0);
		Attribute att = klass.findAttributeByName("name");
		assertEquals("ExtendedPO2::Person", att.getNamespace());
		klass.moveAttributeToClass(att, klass8 );
		assertEquals("ExtendedPO2::Class8", att.getNamespace());
	}
	
	@Test
	public void shouldFindAttributeOnClass() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		Attribute att = klass.findAttributeByName("name");
		assertNotNull(att);
		assertEquals("name", att.getName());
	}
	
	@Test
	public void shouldCreateMethod() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		
		assertEquals(1, klass.getAllMethods().size());
		
		assertEquals("ExtendedPO2", klass.getNamespace());
		
		Method m = klass.createMethod("bar", "String", false, null);
		String id = m.getId();
		
		assertEquals("ExtendedPO2::Person", m.getNamespace());
		assertNotNull(id);
		assertTrue(a.getAllIds().contains(id));
		assertEquals(2,klass.getAllMethods().size());
	}
	
	@Test
	public void shouldNotCreateMethodWhenExists() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		
		assertEquals(1, klass.getAllMethods().size());
		
		klass.createMethod("foo", "", false, null);
		
		assertEquals(1, klass.getAllMethods().size());
	}
	
	
	@Test
	public void shouldNotCreateMethodWhenExists2() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		
		assertEquals(1, klass.getAllMethods().size());
		
		klass.createMethod("bar", "String", false, null);
		klass.createMethod("bar", "String", false, null);
		
		assertEquals(2, klass.getAllMethods().size());
	}
	
	
	@Test
	public void shouldFindMethodByName() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		
		assertEquals(1, klass.getAllMethods().size());
		Method foo = klass.findMethodByName("foo");
		assertNotNull(foo);
	}
	
	@Test
	public void shouldRemoveMethod() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		Method foo = klass.findMethodByName("foo");
		assertEquals(1, klass.getAllMethods().size());
		
		klass.removeMethod(foo);
		
		assertEquals(0, klass.getAllMethods().size());
	}
	
	@Test
	public void shouldMoveMethodToClass() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		
		arquitetura.representation.Class klass8 = a.findClassByName("Class8").get(0);
		Method foo = klass.findMethodByName("foo");
		
		assertEquals("ExtendedPO2", klass.getNamespace());
		
		assertEquals(1, klass.getAllMethods().size());
		assertEquals(0, klass8.getAllMethods().size());
		
		klass.moveMethodToClass(foo, klass8);
		
		assertEquals(0, klass.getAllMethods().size());
		assertEquals(1, klass8.getAllMethods().size());
	}
	
	@Test
	public void shouldChangeNamespaceWhenMoveMethod() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		arquitetura.representation.Class klass8 = a.findClassByName("Class8").get(0);
		Method foo = klass.findMethodByName("foo");
		
		assertEquals("ExtendedPO2::Person", foo.getNamespace());
		
		klass.moveMethodToClass(foo, klass8);
		
		assertEquals("ExtendedPO2::Class8", foo.getNamespace());
		
	}
	
	@Test(expected=MethodNotFoundException.class)
	public void shouldRaiseMethodNotFoundExceptionWhenMethodNotFound() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		Method foo = klass.findMethodByName("metodoSemNome");
		assertNull(foo);
	}
	
	@Test(expected=AttributeNotFoundException.class)
	public void shouldRaiseAttributeNotFoundWhenAttributeNotFound() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		klass.findAttributeByName("algumaCoisa");
	}
	
	@Test
	public void shouldGetAllAbstractMethods() throws Exception{
		Architecture a = givenAArchitecture("methods");
		arquitetura.representation.Class klass = a.findClassByName("Class1").get(0);
		
		assertEquals(2,klass.getAllMethods().size());
		assertEquals(1, klass.getAllAbstractMethods().size());
	}
	
	@Test
	public void shouldReturnAllRelationships() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		
		assertEquals(1,klass.getRelationships().size());
	}
	
	@Test
	public void shouldRemoveIdFromListOfElementsWhenElementDeleted() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		arquitetura.representation.Class klass = a.findClassByName("Person").get(0);
		Method foo = klass.findMethodByName("foo");
	
		assertNotNull(a);
	
		assertEquals(16, a.getAllIds().size());
		
		klass.removeMethod(foo);
		
		assertEquals(15, a.getAllIds().size());
	}
	
}