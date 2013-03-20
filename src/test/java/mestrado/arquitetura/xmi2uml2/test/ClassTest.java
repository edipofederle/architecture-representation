package mestrado.arquitetura.xmi2uml2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.exceptions.AttributeNotFoundException;
import mestrado.arquitetura.exceptions.MethodNotFoundException;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Attribute;
import mestrado.arquitetura.representation.Method;
import mestrado.arquitetura.xmi2uml2.Class;

import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;

public class ClassTest extends TestHelper{
	
	private Architecture a;
	
	@Before
	public void setUp() throws Exception{
		a = givenAArchitecture("classes");
	}
	
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
	
	@Test
	public void shouldCreateAttribute() throws Exception{
		
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Class1");
		assertNotNull(klass);
		assertEquals(0, klass.getAttributes().size());
		
		Attribute att = klass.createAttribute("name", "String");
		
		assertNotNull(att);
		assertEquals(1, klass.getAttributes().size());
	}
	
	@Test
	public void shouldRemoveAttribute() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		assertNotNull(klass);
		assertEquals("Person", klass.getName());
		Attribute att = klass.createAttribute("name", "String");
		assertEquals(2, klass.getAttributes().size());
		
		klass.removeAttribute(att);
		
		assertEquals(1, klass.getAttributes().size());
	}
	
	@Test
	public void shouldMoveAttributeFromOneClassToOther() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		mestrado.arquitetura.representation.Class klass8 = a.findClassByName("Class8");
		Attribute att = klass.findAttributeByName("name");
		
		assertEquals(0, klass8.getAttributes().size());
		assertEquals(1, klass.getAttributes().size());
		
		klass.moveAttributeToClass(att, klass8 );
		
		assertEquals(1, klass8.getAttributes().size());
		assertEquals(0, klass.getAttributes().size());
	}
	
	@Test
	public void shouldFindAttributeOnClass() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		Attribute att = klass.findAttributeByName("name");
		assertNotNull(att);
		assertEquals("name", att.getName());
	}
	
	@Test
	public void shouldCreateMethod() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		
		assertEquals(1,klass.getAllMethods().size());
		
		klass.createMethod("bar", "String", false);
		
		assertEquals(2,klass.getAllMethods().size());
	}
	
	@Test
	public void shouldNotCreateMethodWhenExists() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		
		assertEquals(1, klass.getAllMethods().size());
		
		klass.createMethod("foo", "", false);
		
		assertEquals(1, klass.getAllMethods().size());
	}
	
	
	@Test
	public void shouldNotCreateMethodWhenExists2() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		
		assertEquals(1, klass.getAllMethods().size());
		
		klass.createMethod("bar", "String", false);
		klass.createMethod("bar", "String", false);
		
		assertEquals(2, klass.getAllMethods().size());
	}
	
	@Test
	public void shouldFindMethodByName() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		
		assertEquals(1, klass.getAllMethods().size());
		Method foo = klass.findMethodByName("foo");
		assertNotNull(foo);
	}
	
	@Test
	public void shouldRemoveMethod() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		Method foo = klass.findMethodByName("foo");
		assertEquals(1, klass.getAllMethods().size());
		
		klass.removeMethod(foo);
		
		assertEquals(0, klass.getAllMethods().size());
		
	}
	
	@Test
	public void shouldMoveMethodToClass() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		
		mestrado.arquitetura.representation.Class klass8 = a.findClassByName("Class8");
		Method foo = klass.findMethodByName("foo");
		
		assertEquals(1, klass.getAllMethods().size());
		assertEquals(0, klass8.getAllMethods().size());
		
		klass.moveMethodToClass(foo, klass8);
		
		assertEquals(0, klass.getAllMethods().size());
		assertEquals(1, klass8.getAllMethods().size());
	}
	
	@Test(expected=MethodNotFoundException.class)
	public void shouldRaiseMethodNotFoundExceptionWhenMethodNotFound() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		Method foo = klass.findMethodByName("metodoSemNome");
		assertNull(foo);
	}
	
	@Test(expected=AttributeNotFoundException.class)
	public void shouldRaiseAttributeNotFoundWhenAttributeNotFound() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		klass.findAttributeByName("algumaCoisa");
	}
	
	@Test
	public void shouldGetAllAbstractMethods() throws Exception{
		Architecture a = givenAArchitecture("methods");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Class1");
		
		assertEquals(2,klass.getAllMethods().size());
		assertEquals(1, klass.getAllAbstractMethods().size());
	}
	
	@Test
	public void shouldReturnAllRelationships() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		
		assertEquals(1,klass.getRelationships().size());
	}
	
	@Test
	public void shouldRemoveIdFromListOfElementsWhenElementDeleted() throws Exception{
		Architecture a = givenAArchitecture("ExtendedPO2");
		mestrado.arquitetura.representation.Class klass = a.findClassByName("Person");
		Method foo = klass.findMethodByName("foo");
	
		assertNotNull(a);
	
		assertEquals(16, a.getAllIds().size());
		
		klass.removeMethod(foo);
		
		assertEquals(15, a.getAllIds().size());
	}
	
}