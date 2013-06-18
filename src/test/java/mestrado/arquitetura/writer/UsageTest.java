package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.api.touml.DocumentManager;
import arquitetura.api.touml.Operations;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.UsageRelationship;

public class UsageTest extends TestHelper {

	@Test
	public void shouldCreateUsageClassClass() throws Exception{
		DocumentManager doc = givenADocument("usageTeste1");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forUsage().createRelation("Usage #1").between(employee.get("id")).and(manager.get("id")).build();
	
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
		DocumentManager doc = givenADocument("usageTeste2");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forPackage().createPacakge("foo").withClass(employee.get("id"));
		
		op.forUsage().createRelation("Usage #2").between(employee.get("id")).and(manager.get("id")).build();
		
		Architecture a = givenAArchitecture2("usageTeste2");
		assertEquals(1,a.getAllUsage().size());
		
	}
	
	@Test
	public void shouldCreateUsageClassClassPackage() throws Exception{
		DocumentManager doc = givenADocument("usageTeste3");
		Operations op = new Operations(doc);
		
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forPackage().createPacakge("foo").withClass(employee.get("id"));
		
		op.forUsage().createRelation("Usage #2").between(manager.get("id")).and(employee.get("id")).build();
		
		Architecture a = givenAArchitecture2("usageTeste3");
		assertEquals(1, a.getAllUsage().size());
	}
	
	@Test
	public void shouldCreateUsagePackagePackage() throws Exception{
		DocumentManager doc = givenADocument("usageTeste4");
		Operations op = new Operations(doc);
		
		String p1 = op.forPackage().createPacakge("Pacote1").build().get("packageId");
		String p2 = op.forPackage().createPacakge("Pacote1").build().get("packageId");
		
		op.forUsage().createRelation("Dependencia com Nome").between(p1).and(p2).build();
		
		Architecture a = givenAArchitecture2("usageTeste4");
		assertEquals(1, a.getAllUsage().size());
	}
	
}
