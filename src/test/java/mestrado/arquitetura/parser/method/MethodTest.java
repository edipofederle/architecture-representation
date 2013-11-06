package mestrado.arquitetura.parser.method;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.test.HelperTest;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.touml.Argument;
import arquitetura.touml.Method;
import arquitetura.touml.Types;
import arquitetura.touml.VisibilityKind;

public class MethodTest extends HelperTest {

	@Test
	public void methodTest(){
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.INTEGER, "in"));
		Method foo = Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER)
							 .abstractMethod().build();
		
		assertEquals("foo", foo.getName());
		assertEquals("public", foo.getVisibility());
		assertEquals("Integer", foo.getReturnMethod());
		assertEquals(1, foo.getArguments().size());
		assertEquals("true", foo.isAbstract());
		
		Argument arg1 = foo.getArguments().get(0);
		assertEquals("name", arg1.getName());
		assertEquals("Integer", arg1.getType().getName());
		
	}
	
	@Test
	public void methodTest2(){
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("a", Types.INTEGER, "in"));
		Method foo = Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER)
							 .build();
		
		assertEquals("foo", foo.getName());
		assertEquals("public", foo.getVisibility());
		assertEquals("Integer", foo.getReturnMethod());
		assertEquals(1, foo.getArguments().size());
		assertEquals("false", foo.isAbstract());
		
	}
	
	
	@Test
	public void testOperationsGeneralization() throws Exception{
		Architecture a = givenAArchitecture("operationsTests");
		
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		arquitetura.representation.Class klass2 = a.findClassByName("Class2").get(0);
		arquitetura.representation.Class klass3 = a.findClassByName("Class3").get(0);
		
		assertEquals(1,klass1.getAllMethods().size());
		assertEquals(1,klass2.getAllMethods().size());
		assertEquals(2,klass3.getAllMethods().size());
	}
	
	
	@Test
	public void testOperationsGeneralizationInterface() throws Exception{
		Architecture a = givenAArchitecture("operationsGeneralizationInterface/interfaceGeneralizationInterface");
		
		arquitetura.representation.Interface klass1 = a.findInterfaceByName("Class1"); // TODO _ implementar para retornar uma lista
		arquitetura.representation.Interface klass2 = a.findInterfaceByName("Class2");
		
		assertEquals(1, klass2.getOperations().size());
	}
}
