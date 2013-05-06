package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.junit.Test;

public class UsageTest extends TestHelper {

	@Test
	public void shouldCreateUsageClassClass() throws Exception{
		DocumentManager doc = givenADocument("usageTeste1", "simples");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forUsage().createDependency("Usage #1").between(employee.get("classId")).and(manager.get("classId")).build();
	
		Architecture a = givenAArchitecture2("usageTeste1");
		assertNotNull(a.getAllUsage());
		assertEquals(1, a.getAllUsage().size());
		assertEquals(0, a.getAllDependencies().size());
		
		UsageRelationship usage = a.getAllUsage().get(0);
		assertEquals("Usage #1",usage.getName());
		
		assertEquals("Employee",usage.getClient().getName());
		assertEquals("Casa",usage.getSupplier().getName());
	}
	
	@Test
	public void shouldCreateUsageClassPackageClass() throws Exception{
		DocumentManager doc = givenADocument("usageTeste2", "simples");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forPackage().createPacakge("foo").withClass(employee.get("classId"));
		
		op.forUsage().createDependency("Usage #2").between(employee.get("classId")).and(manager.get("classId")).build();
		
		Architecture a = givenAArchitecture2("usageTeste2");
		assertEquals(1,a.getAllUsage().size());
		
	}
	
	@Test
	public void shouldCreateUsageClassClassPackage() throws Exception{
		DocumentManager doc = givenADocument("usageTeste3", "simples");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forPackage().createPacakge("foo").withClass(employee.get("classId"));
		
		op.forUsage().createDependency("Usage #2").between(manager.get("classId")).and(employee.get("classId")).build();
		
		Architecture a = givenAArchitecture2("usageTeste3");
		assertEquals(1, a.getAllUsage().size());
	}
	
	@Test
	public void shouldCreateUsagePackagePackage() throws Exception{
		DocumentManager doc = givenADocument("usageTeste4", "simples");
		Operations op = new Operations(doc);
		
		String p1 = op.forPackage().createPacakge("Pacote1").build().get("packageId");
		String p2 = op.forPackage().createPacakge("Pacote1").build().get("packageId");
		
		op.forUsage().createDependency("Dependencia com Nome").between(p1).and(p2).build();
		
		Architecture a = givenAArchitecture2("usageTeste4");
		assertEquals(1, a.getAllUsage().size());
	}
	
}
