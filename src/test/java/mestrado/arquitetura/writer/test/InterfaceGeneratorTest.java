package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;

public class InterfaceGeneratorTest extends TestHelper {
	
	@Test
	public void shouldGenerateAInterface() throws Exception{
		DocumentManager doc = givenADocument("empty");
		Operations op = new Operations(doc, null);
		
		Interface inter = Mockito.mock(Interface.class);
		Mockito.when(inter.getName()).thenReturn("MyInterface");
		Mockito.when(inter.getId()).thenReturn("1231231231");
		
		op.forClass().createClass(inter).asInterface().build();
		
		Architecture a = givenAArchitecture2("empty");
		assertNotNull(a.getAllInterfaces());
		assertEquals(1, a.getAllInterfaces().size());
	}


}