package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.test.HelperTest;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.UsageInterClassRelationship;

import org.junit.Test;

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
		assertEquals(1, a.getInterClassRelationships().size());
		
		 UsageInterClassRelationship u = a.getAllUsage().get(0);
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

		List<InterClassRelationship> relations2 = architecture8.getInterClassRelationships();

		assertNotNull(relations2);
		assertEquals(2, relations2.size());

		UsageInterClassRelationship usage = (UsageInterClassRelationship) relations2.get(0);

		assertNotNull(usage);
		assertEquals("Usage1", usage.getName());
		assertEquals("Class1", usage.getClient().getName());
		assertEquals("Class2", usage.getSupplier().getName());
	}
	
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/Relacionamentos%20Testes/usageRelationshipTests/Screen%20Shot%202013-03-16%20at%203.17.51%20PM%201.png">Modelo usado no teste (Image)</a>
	 * @throws Exception
	 */
	@Test
	public void teste() throws Exception{
		String uriToArchitecture8 = getUrlToModel("usage3");
		Architecture architecture8 = new ArchitectureBuilder().create(uriToArchitecture8);

		assertNotNull(architecture8);

		List<InterClassRelationship> relations2 = architecture8.getInterClassRelationships();
		assertEquals(1,relations2.size());
		
		
		 UsageInterClassRelationship u = architecture8.getAllUsage().get(0);
		 assertEquals("Class1", u.getClient().getName());
		 assertEquals("model::Package2", u.getClient().getNamespace());
		 
		 assertEquals("Class1", u.getSupplier().getName());
		 assertEquals("model::Package1::Package1", u.getSupplier().getNamespace());
	}

}
