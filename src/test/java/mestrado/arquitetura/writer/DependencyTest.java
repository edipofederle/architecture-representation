package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.api.touml.DocumentManager;
import arquitetura.api.touml.Operations;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.DependencyRelationship;

public class DependencyTest extends TestHelper {
	
	@Test
	public void shouldCreateADependencyClassClass() throws Exception{
		DocumentManager doc = givenADocument("testeDependencia1");
		Operations op = new Operations(doc);
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forDependency().createRelation("Dependency #12")
							.between(employee.get("id"))
							.and(manager.get("id"))
							.build();
		
		Architecture a = givenAArchitecture2("testeDependencia1");
		assertNotNull(a);
		assertNotNull(a.getAllDependencies());
		assertEquals("Dependency #12", a.getAllDependencies().get(0).getName());
		assertEquals("Employee", a.getAllDependencies().get(0).getClient().getName());
		assertEquals("Casa", a.getAllDependencies().get(0).getSupplier().getName());
	}

	
	@Test
	public void shouldCreateADependencyClassPackage() throws Exception{
		DocumentManager doc = givenADocument("testeDependenciClassPackage");
		Operations op = new Operations(doc);
		
		Map<String, String> id = op.forPackage().createPacakge("controllers").build();
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		
		op.forDependency().createRelation("Dependency #12")
							.between(employee.get("id"))
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
		DocumentManager doc = givenADocument("testeDependenciPackagePackage");
		Operations op = new Operations(doc);
		
		Map<String, String> controllers = op.forPackage().createPacakge("controllers").build();
		Map<String, String> models = op.forPackage().createPacakge("models").build();
		
		System.out.println(controllers.get("packageId"));
		System.out.println(models.get("packageId"));
		
		op.forDependency().createRelation("Dependency #12")
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
		DocumentManager doc = givenADocument("dependenciaMultipla");
		Operations op = new Operations(doc);
		
		String post = op.forClass().createClass("Post").build().get("id");
		String comment = op.forClass().createClass("Comment").build().get("id");
		String user = op.forClass().createClass("User").build().get("id");
		String category = op.forClass().createClass("Category").build().get("id");
		
		op.forDependency().createRelation("Dependency #1").between(post).and(comment).build();
		op.forDependency().createRelation("Dependency #3").between(post).and(user).build();
		op.forDependency().createRelation("Dependency #2").between(post).and(category).build();
		
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
		DocumentManager doc = givenADocument("dependenciaMultipla2");
		Operations op = new Operations(doc);
		
		String post = op.forClass().createClass("Post").build().get("id");
		String comment = op.forClass().createClass("Comment").build().get("id");
		String user = op.forClass().createClass("User").build().get("id");
		String category = op.forClass().createClass("Category").build().get("id");
		
		op.forDependency().createRelation("Dependency #1").between(user).and(comment).build();
		op.forDependency().createRelation("Dependency #2").between(post).and(comment).build();
		op.forDependency().createRelation("Dependency #3").between(category).and(comment).build();
		
		Architecture a = givenAArchitecture2("dependenciaMultipla2");
		
		assertEquals(3, a.getAllDependencies().get(0).getAllClientsForSupplierClass().size());
		assertEquals(1, a.getAllDependencies().get(0).getAllSuppliersForClientClass().size());
	}
	
	@Test
	public void shouldCreateDependencyClassClassPackage() throws Exception{
		DocumentManager doc = givenADocument("dependenciaClassClassPackage");
		Operations op = new Operations(doc);
		
		String klassId = op.forClass().createClass("bar").build().get("id");
		String fooId = op.forClass().createClass("Foo").build().get("id");
		op.forPackage().createPacakge("Controllers").withClass(klassId).build();
		
		op.forDependency().createRelation("Dependency #12").between(klassId).and(fooId).build();

		Architecture a = givenAArchitecture2("dependenciaClassClassPackage");
		
		assertTrue(modelContainId("dependenciaClassClassPackage", klassId));
		assertTrue(modelContainId("dependenciaClassClassPackage", fooId));
		
		assertEquals(1, a.getAllDependencies().size());
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		assertEquals("bar",dependency.getClient().getName());
		assertEquals("Foo",dependency.getSupplier().getName());
	}
	
	@Test
	public void createDependencyPacakgeClassClass() throws Exception{
		DocumentManager doc = givenADocument("dependencyPacakgeClassClass");
		Operations op = new Operations(doc);
		
		String klassId = op.forClass().createClass("bar").build().get("id");
		String fooId = op.forClass().createClass("Foo").build().get("id");
		//op.forPackage().createPacakge("Controllers").withClass(klassId).build();
		
		op.forDependency().createRelation("Dependency #12").between(fooId).and(klassId).build();

		Architecture a = givenAArchitecture2("dependencyPacakgeClassClass");
		
		assertTrue(modelContainId("dependencyPacakgeClassClass", klassId));
		assertTrue(modelContainId("dependencyPacakgeClassClass", fooId));
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		assertEquals("Foo",dependency.getClient().getName());
		assertEquals("bar",dependency.getSupplier().getName());
	}
	
	@Test
	public void whenDependencyNotHaveANameSetDefault() throws Exception{
		DocumentManager doc = givenADocument("dependencySemNome");
		Operations op = new Operations(doc);
		
		String klassId = op.forClass().createClass("bar").build().get("id");
		String fooId = op.forClass().createClass("Foo").build().get("id");
		
		op.forDependency().createRelation("").between(fooId).and(klassId).build();
		op.forDependency().createRelation(null).between(fooId).and(klassId).build();

		Architecture a = givenAArchitecture2("dependencySemNome");
		
		assertEquals("dependency", a.getAllDependencies().get(0).getName());
		assertEquals("dependency", a.getAllDependencies().get(1).getName());
	}
	
	

}