package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.UsageInterClassRelationship;

import org.junit.Before;
import org.junit.Test;

public class RelationshipUsageClassPackageTest extends TestHelper {
	
	private Architecture architecture;
	private List<InterClassRelationship> relations;
	
	@Before
	public void setUp() throws Exception{
		String uriToArchitecture = getUrlToModel("usageClassPackage");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		relations = architecture.getInterClassRelationships();
	}
	
	@Test
	public void shouldLoadUsageInterClassPackage() throws Exception{
		assertNotNull(architecture);
		UsageInterClassRelationship relation = (UsageInterClassRelationship)relations.get(0);
		assertNotNull(relation);
		
		assertEquals("Class1", relation.getClient().getName());
		assertEquals("Package1", relation.getSupplier().getName());
	}
	
	
	@Test
	public void shouldLoadUsageInterPackageClass() throws Exception{
		String uriToArchitecture = getUrlToModel("PackageClassUsage");
		Architecture architecture2 = new ArchitectureBuilder().create(uriToArchitecture);
		List<InterClassRelationship> relations2 = architecture2.getInterClassRelationships();
		UsageInterClassRelationship relation2 = (UsageInterClassRelationship)relations2.get(0);
		assertNotNull(relations2);
		
		assertEquals("Package1", relation2.getClient().getName());
		assertEquals("Class1", relation2.getSupplier().getName());
	}

}
