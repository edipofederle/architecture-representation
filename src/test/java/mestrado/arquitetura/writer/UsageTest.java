package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;

import org.junit.Test;

public class UsageTest extends TestHelper {

	@Test
	public void shouldCreateUsage() throws Exception{
		DocumentManager doc = givenADocument("usageTeste1", "simples");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forUsage().createDependency("Usage #1").between(employee.get("classId")).and(manager.get("classId")).build();
	
		Architecture a = givenAArchitecture2("usageTeste1");
		assertNotNull(a.getAllUsage());
		assertEquals(1, a.getAllUsage().size());
		assertEquals(0, a.getAllDependencies().size());
	
	}
}
