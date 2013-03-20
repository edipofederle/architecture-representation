package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Method;

import org.junit.Test;

public class InterfaceBuilderTest extends TestHelper {
	
	@Test
	public void shouldLoadInterface() throws Exception{
		Architecture a = givenAArchitecture("interface");
		assertEquals(1, a.getAllInterfaces().size());
		
		List<Method> operations = a.getAllInterfaces().get(0).getOperations();
		
		assertEquals(1, operations.size());
		assertEquals("Operation1", operations.get(0).getName());
		assertEquals("String", operations.get(0).getReturnType());
		assertEquals(2, operations.get(0).getParameters().size());
		assertEquals("name", operations.get(0).getParameters().get(0).getName());
		assertEquals("fullName", operations.get(0).getParameters().get(1).getName());
	}

}