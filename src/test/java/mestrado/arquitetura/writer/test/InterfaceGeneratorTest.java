package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import main.GenerateArchitecture;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
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
	
	@Test
	public void whenIAddInterfaceToPackagePackageShouldCotainsTheInterface() throws Exception{
		Architecture a = givenAArchitecture("package");
		
		assertEquals(1, a.getAllPackages().size());
		assertEquals(2, a.getAllPackages().get(0).getElements().size());
		
		Interface inter = new Interface(a, "MyInterface");
		a.getAllPackages().get(0).getElements().add(inter);
		
		GenerateArchitecture generate = new GenerateArchitecture();
		generate.generate(a, "saidaPacoteComInterface");
		
		Architecture gerada = givenAArchitecture2("saidaPacoteComInterface");
		assertEquals(1,gerada.getAllInterfaces().size());
		assertEquals(1,gerada.getAllPackages().size());
		assertEquals(3, gerada.getAllPackages().get(0).getElements().size());
		
	}
	
	
	@Test
	public void shouldGenerateAInterfaceWithConcern() throws Exception{
		DocumentManager doc = givenADocument("IntefaceConcern");
		Operations op = new Operations(doc, null);
		
		Interface inter = Mockito.mock(Interface.class);
		Mockito.when(inter.getName()).thenReturn("MyInterface");
		Mockito.when(inter.getId()).thenReturn("1231231231");
		
		Concern persistence = Mockito.mock(Concern.class);
		Mockito.when(persistence.getName()).thenReturn("persistence");
		
		arquitetura.touml.Method m = arquitetura.touml.Method.create()
				  .withId(UtilResources.getRandonUUID())
				  .withName("foo").abstractMethod()
				  .build();
		
		
		op.forClass().createClass(inter).asInterface().withMethod(m).build();
		op.forConcerns().withConcern(persistence, inter.getId());
		
		Architecture a = givenAArchitecture2("IntefaceConcern");
		assertNotNull(a.getAllInterfaces());
		assertEquals(1, a.getAllInterfaces().size());
		
		GenerateArchitecture generate = new GenerateArchitecture();
		generate.generate(a, "regenerateIntefaceConcern");
		
		
		Architecture regenerateIntefaceConcern = givenAArchitecture2("regenerateIntefaceConcern");
		Interface interf = regenerateIntefaceConcern.findInterfaceByName("MyInterface");
		
		assertEquals(1,interf.getOwnConcerns().size());
		assertEquals("persistence",interf.getOwnConcerns().get(0).getName());
		
		arquitetura.representation.Method operationFoo = interf.getOperations().get(0);
		assertEquals("foo", operationFoo.getName());	
		assertTrue(operationFoo.getOwnConcerns().isEmpty());
		
	}


}