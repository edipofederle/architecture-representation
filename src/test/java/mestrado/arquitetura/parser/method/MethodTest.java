package mestrado.arquitetura.parser.method;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

import arquitetura.api.touml.Argument;
import arquitetura.api.touml.Method;
import arquitetura.api.touml.Types;
import arquitetura.api.touml.VisibilityKind;

public class MethodTest {

	@Test
	public void methodTest(){
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.INTEGER_WRAPPER));
		Method foo = Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER_WRAPPER)
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
		arguments.add(Argument.create("a", Types.INTEGER_WRAPPER));
		Method foo = Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER_WRAPPER)
							 .build();
		
		assertEquals("foo", foo.getName());
		assertEquals("public", foo.getVisibility());
		assertEquals("Integer", foo.getReturnMethod());
		assertEquals(1, foo.getArguments().size());
		assertEquals("false", foo.isAbstract());
		
	}
}
