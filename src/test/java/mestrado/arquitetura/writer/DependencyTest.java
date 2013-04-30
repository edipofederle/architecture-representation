package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.ClassOperations;
import mestrado.arquitetura.parser.DependencyOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.PackageOperation;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;

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
	
	@Test
	public void shouldCreateADependencyClassPackage() throws Exception{
		DocumentManager doc = givenADocument("testeDependenciClassPackage", "simples");
		PackageOperation packageOperation = new  PackageOperation(doc);
		ClassOperations classOperations = new ClassOperations(doc);
		DependencyOperations dependencyOperations = new DependencyOperations(doc);
		
		Map<String, String> id = packageOperation.createPacakge("controllers").build();
		Map<String, String> employee = classOperations.createClass("Employee").build();
		
		dependencyOperations.createDependency()
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
		PackageOperation packageOperation = new  PackageOperation(doc);
		DependencyOperations dependencyOperations = new DependencyOperations(doc);
		
		Map<String, String> controllers = packageOperation.createPacakge("controllers").build();
		Map<String, String> models = packageOperation.createPacakge("models").build();
		
		System.out.println(controllers.get("packageId"));
		System.out.println(models.get("packageId"));
		
		dependencyOperations.createDependency()
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
		ClassOperations classOperations = new ClassOperations(doc);
		DependencyOperations dependencyOperations = new DependencyOperations(doc);
		
		String post = classOperations.createClass("Post").build().get("classId");
		String comment = classOperations.createClass("Comment").build().get("classId");
		String user = classOperations.createClass("User").build().get("classId");
		String category = classOperations.createClass("Category").build().get("classId");
		
		dependencyOperations.createDependency().between(post).and(comment).build();
		dependencyOperations.createDependency().between(post).and(user).build();
		dependencyOperations.createDependency().between(post).and(category).build();
		
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
		ClassOperations classOperations = new ClassOperations(doc);
		DependencyOperations dependencyOperations = new DependencyOperations(doc);
		
		String post = classOperations.createClass("Post").build().get("classId");
		String comment = classOperations.createClass("Comment").build().get("classId");
		String user = classOperations.createClass("User").build().get("classId");
		String category = classOperations.createClass("Category").build().get("classId");
		
		dependencyOperations.createDependency().between(user).and(comment).build();
		dependencyOperations.createDependency().between(post).and(comment).build();
		dependencyOperations.createDependency().between(category).and(comment).build();
		
		Architecture a = givenAArchitecture2("dependenciaMultipla2");
		
		assertEquals(3, a.getAllDependencies().get(0).getAllClientsForSupplierClass().size());
		assertEquals(1, a.getAllDependencies().get(0).getAllSuppliersForClientClass().size());
		
		
	}
	

}