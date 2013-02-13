package mestrado.arquitetura.xmi2uml2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.helpers.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.xmi2uml2.Class;

import org.eclipse.uml2.uml.Package;
import org.junit.Test;

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
	
}