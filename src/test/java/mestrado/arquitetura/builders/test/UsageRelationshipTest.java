package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.helpers.test.HelperTest;

import org.junit.Test;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;

public class UsageRelationshipTest extends HelperTest {
	
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/Relacionamentos%20Testes/usageRelationshipTests/usage1.png">Modelo usado no teste (Imagem) </a>
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLoadUsageClassInsidePackage() throws Exception{
		Architecture a = givenAArchitecture("usageClassInsidePackage");
		
		assertNotNull(a.getAllUsage());
		assertEquals(1, a.getAllUsage().size());
		assertEquals(1, a.getAllRelationships().size());
		
		 UsageRelationship u = a.getAllUsage().get(0);
		 assertEquals("Class2", u.getClient().getName());
		 assertEquals("model::Package1", u.getClient().getNamespace());
		 
		 assertEquals("Class1", u.getSupplier().getName());
	}
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/Relacionamentos%20Testes/usageRelationshipTests/usageClassClass.png">Modelo usado no teste (Image)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldReturnAllUsageClass() throws Exception{
		String uriToArchitecture = getUrlToModel("classUsageClass");
		Architecture a = new ArchitectureBuilder().create(uriToArchitecture);
		assertEquals(2, a.getAllUsage().size());
	}
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/Relacionamentos%20Testes/usageRelationshipTests/usageClassClass.png">Modelo usado no teste (Image)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldLoadUsageClassBetweenClass() throws Exception {
		String uriToArchitecture8 = getUrlToModel("classUsageClass");
		Architecture architecture8 = new ArchitectureBuilder().create(uriToArchitecture8);

		assertNotNull(architecture8);

		List<Relationship> relations2 = architecture8.getAllRelationships();

		assertNotNull(relations2);
		assertEquals(2, relations2.size());

		UsageRelationship usage = (UsageRelationship) relations2.get(0);

		assertNotNull(usage);
		assertEquals("Usage1", usage.getName());
		assertEquals("Class1", usage.getClient().getName());
		assertEquals("Class2", usage.getSupplier().getName());
	}
	
	
	@Test
	public void shouldLoadUsageClassesInsidePakages() throws Exception{
		Architecture architecture8 = givenAArchitecture("usage3");
		assertNotNull(architecture8);

		List<Relationship> relations2 = architecture8.getAllRelationships();
		assertEquals(1, relations2.size());
		
		
		 UsageRelationship u = architecture8.getAllUsage().get(0);
		 assertEquals("Class1", u.getClient().getName());
		 assertEquals("model::Package2", u.getClient().getNamespace());
		 
		 assertEquals("Class1", u.getSupplier().getName());
		 assertEquals("model::Package1", u.getSupplier().getNamespace());
	}
	
	
	/**
	 * @see <a href="https://dl.dropboxusercontent.com/u/6730822/de/usageRelationshipTests/classUsagePackage.png">Modelo usado no teste (Image)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldLoadUsageInterClassPackage() throws Exception{
		Architecture a = givenAArchitecture("usageClassPackage");
		List<Relationship> relations = a.getAllRelationships();
		assertNotNull(a);
		UsageRelationship relation = (UsageRelationship)relations.get(0);
		assertNotNull(relation);
		
		assertEquals("Class1", relation.getClient().getName());
		assertEquals("Package1", relation.getSupplier().getName());
	}
	
	
	/**
	 * @see <a href="https://dl.dropboxusercontent.com/u/6730822/de/usage2.png">Modelo usado no teste (Image)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldLoadUsageInterPackageClass() throws Exception{
		String uriToArchitecture = getUrlToModel("PackageClassUsage");
		Architecture architecture2 = new ArchitectureBuilder().create(uriToArchitecture);
		List<Relationship> relations2 = architecture2.getAllRelationships();
		UsageRelationship relation2 = (UsageRelationship)relations2.get(0);
		assertNotNull(relations2);
		
		assertEquals("Package1", relation2.getClient().getName());
		assertEquals("Class1", relation2.getSupplier().getName());
	}
	
	@Test
	public void shouldLoadUsagePackagePackage() throws Exception{
		Architecture a = givenAArchitecture("UsagePackagePackage");
		
		assertEquals(1, a.getAllUsage().size());
		
		assertEquals("Package1", a.getAllUsage().get(0).getClient().getName());
	}
	
}