package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.ClassOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.representation.Architecture;

import org.junit.Test;

public class DependencyTest extends TestHelper {
	
	@Test
	public void shouldCreateADependencyClassClass() throws Exception{
		DocumentManager doc = givenADocument("testeDependencia1", "simples");
		
		ClassOperations classOperations = new ClassOperations(doc);
		DependencyOperations dependencyOperations = new DependencyOperations(doc);
		
		Map<String, String> employee = classOperations.createClass("Employee").build();
		Map<String, String> manager = classOperations.createClass("Casa").build();
		
		dependencyOperations.createDependency()
							.between(employee.get("classId"))
							.and(manager.get("classId"))
							.build();
		
		Architecture a = givenAArchitecture2("testeDependencia1");
		assertNotNull(a);
		assertNotNull(a.getAllDependencies());
		assertEquals("", a.getAllDependencies().get(0).getName());
		assertEquals("Employee", a.getAllDependencies().get(0).getClient().getName());
		assertEquals("Casa", a.getAllDependencies().get(0).getSupplier().getName());
		
	}

}