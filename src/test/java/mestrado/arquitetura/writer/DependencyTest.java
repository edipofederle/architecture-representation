package mestrado.arquitetura.writer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;

import org.junit.Test;

public class DependencyTest extends TestHelper {
	
	@Test
	public void shouldCreateADependencyClassClass() throws Exception{
		DocumentManager doc = givenADocument("testeDependencia1", "simples");
		
		Operations op = new Operations(doc);
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forDependency().createDependency()
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
	
	@Test
	public void shouldCreateADependencyClassPackage() throws Exception{
		DocumentManager doc = givenADocument("testeDependenciClassPackage", "simples");
		Operations op = new Operations(doc);
		
		Map<String, String> id = op.forPackage().createPacakge("controllers").build();
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		
		op.forDependency().createDependency()
							.between(employee.get("classId"))
							.and(id.get("packageId"))
							.build();
		
		Architecture a = givenAArchitecture2("testeDependenciClassPackage");
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		
		assertNotNull(a);
		assertEquals(1, a.getAllPackages().size());
		assertNotNull(a.getAllDependencies().size());
		assertEquals("Employee", dependency.getClient().getName());
		assertEquals("controllers", dependency.getSupplier().getName());
	}
	
	@Test
	public void shouldCreateDependencyPackageClass() throws Exception{
		DocumentManager doc = givenADocument("testeDependenciPackagePackage", "simples");
		Operations op = new Operations(doc);
		
		Map<String, String> controllers = op.forPackage().createPacakge("controllers").build();
		Map<String, String> models = op.forPackage().createPacakge("models").build();
		
		System.out.println(controllers.get("packageId"));
		System.out.println(models.get("packageId"));
		
		op.forDependency().createDependency()
							.between(controllers.get("packageId"))
							.and(models.get("packageId"))
							.build();
		
		Architecture a = givenAArchitecture2("testeDependenciPackagePackage");
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		
		assertNotNull(a);
		assertEquals(2, a.getAllPackages().size());
		assertNotNull(a.getAllDependencies().size());
		assertEquals("controllers", dependency.getClient().getName());
		assertEquals("models", dependency.getSupplier().getName());
	}
	
	@Test
	public void shouldCreateDependencyWithMultiplesSuppliers() throws Exception{
		DocumentManager doc = givenADocument("dependenciaMultipla", "simples");
		Operations op = new Operations(doc);
		
		String post = op.forClass().createClass("Post").build().get("classId");
		String comment = op.forClass().createClass("Comment").build().get("classId");
		String user = op.forClass().createClass("User").build().get("classId");
		String category = op.forClass().createClass("Category").build().get("classId");
		
		op.forDependency().createDependency().between(post).and(comment).build();
		op.forDependency().createDependency().between(post).and(user).build();
		op.forDependency().createDependency().between(post).and(category).build();
		
		Architecture a = givenAArchitecture2("dependenciaMultipla");
		
		assertEquals(3, a.getAllDependencies().size());
		assertEquals("Post", a.getAllDependencies().get(0).getClient().getName());
		assertEquals("Post", a.getAllDependencies().get(1).getClient().getName());
		assertEquals("Post", a.getAllDependencies().get(2).getClient().getName());
		
		assertEquals(3, a.getAllDependencies().get(0).getAllSuppliersForClientClass().size());
		assertEquals("Comment", a.getAllDependencies().get(0).getAllSuppliersForClientClass().get(0).getName());
		assertEquals("User", a.getAllDependencies().get(0).getAllSuppliersForClientClass().get(1).getName());
		assertEquals("Category", a.getAllDependencies().get(0).getAllSuppliersForClientClass().get(2).getName());
	}
	
	@Test
	public void shouldCreeateDependencyWithMultipleCleints() throws Exception{
		DocumentManager doc = givenADocument("dependenciaMultipla2", "simples");
		Operations op = new Operations(doc);
		
		String post = op.forClass().createClass("Post").build().get("classId");
		String comment = op.forClass().createClass("Comment").build().get("classId");
		String user = op.forClass().createClass("User").build().get("classId");
		String category = op.forClass().createClass("Category").build().get("classId");
		
		op.forDependency().createDependency().between(user).and(comment).build();
		op.forDependency().createDependency().between(post).and(comment).build();
		op.forDependency().createDependency().between(category).and(comment).build();
		
		Architecture a = givenAArchitecture2("dependenciaMultipla2");
		
		assertEquals(3, a.getAllDependencies().get(0).getAllClientsForSupplierClass().size());
		assertEquals(1, a.getAllDependencies().get(0).getAllSuppliersForClientClass().size());
	}
	
	@Test
	public void shouldCreateDependencyClassClassPackage() throws Exception{
		DocumentManager doc = givenADocument("dependenciaClassClassPackage", "simples");
		Operations op = new Operations(doc);
		
		String klassId = op.forClass().createClass("bar").build().get("classId");
		String fooId = op.forClass().createClass("Foo").build().get("classId");
		op.forPackage().createPacakge("Controllers").withClass(klassId).build();
		
		op.forDependency().createDependency().between(klassId).and(fooId).build();

		Architecture a = givenAArchitecture2("dependenciaClassClassPackage");
		
		assertTrue(modelContainId("dependenciaClassClassPackage", klassId));
		assertTrue(modelContainId("dependenciaClassClassPackage", fooId));
		
		assertEquals(1, a.getAllDependencies().size());
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		assertEquals("bar",dependency.getClient().getName());
		assertEquals("Foo",dependency.getSupplier().getName());
	}
	
	@Test
	public void CreateDependencyPacakgeClassClass() throws Exception{
		DocumentManager doc = givenADocument("dependencyPacakgeClassClass", "simples");
		Operations op = new Operations(doc);
		
		String klassId = op.forClass().createClass("bar").build().get("classId");
		String fooId = op.forClass().createClass("Foo").build().get("classId");
		op.forPackage().createPacakge("Controllers").withClass(klassId).build();
		
		op.forDependency().createDependency().between(fooId).and(klassId).build();

		Architecture a = givenAArchitecture2("dependencyPacakgeClassClass");
		
		assertTrue(modelContainId("dependencyPacakgeClassClass", klassId));
		assertTrue(modelContainId("dependencyPacakgeClassClass", fooId));
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		assertEquals("Foo",dependency.getClient().getName());
		assertEquals("bar",dependency.getSupplier().getName());
	}

}