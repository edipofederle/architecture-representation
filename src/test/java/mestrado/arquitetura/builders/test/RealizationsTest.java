

package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Relationship;
import mestrado.arquitetura.representation.RealizationRelationship;

import org.junit.Test;

public class RealizationsTest extends TestHelper {
	
	/**
	 * @see <a href="http://d.pr/i/RvoH">Modelo usado no teste (Imagem) </a>
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLoadRealizations() throws Exception{
		Architecture architecture6 = givenAArchitecture("realization");
		assertNotNull(architecture6);
		
		List<Relationship> relations = architecture6.getInterClassRelationships();
		RealizationRelationship realization = (RealizationRelationship) relations.get(0);
		
		assertNotNull(realization);
		
		assertEquals("Source Element should be Class2", "Class2", realization.getClient().getName());
		assertEquals("Specific Element should be Class1", "Class1", realization.getSupplier().getName());
	}
	
	
	/**
	 * @see <a href="http://d.pr/i/9gms">Modelo usado no teste (Imagem) </a>
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLoadRealizationClassPackage() throws Exception{
		Architecture a = givenAArchitecture("realizationClassPackage");
		
		assertNotNull(a.getAllRealizations());
		assertEquals(1, a.getAllRealizations().size());
		
		assertEquals("Client should be Class1", "Class1", a.getAllRealizations().get(0).getClient().getName());
		assertEquals("Supplier should be Package1", "Package1", a.getAllRealizations().get(0).getSupplier().getName());
	}
	
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/Relacionamentos%20Testes/realizations/realization4.png">Modelo usado no teste (Imagem) </a>
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLoadRealizationPackageClass() throws Exception{
		Architecture a = givenAArchitecture("realizationPackageClass");
		
		assertNotNull(a.getAllRealizations());
		assertEquals(1, a.getInterClassRelationships().size());
		assertEquals(1, a.getAllRealizations().size());
		
		assertEquals("Client should be Package1", "Package1", a.getAllRealizations().get(0).getClient().getName());
		assertEquals("Supplier should be Class1", "Class1", a.getAllRealizations().get(0).getSupplier().getName());
	}
	
	@Test
	public void shouldLoadRealizationPackagePackage() throws Exception{
		Architecture a = givenAArchitecture("realizationPackagePackage");
		
		assertNotNull(a);
		assertEquals(1, a.getAllRealizations().size());
		
		assertEquals("Package2", a.getAllRealizations().get(0).getClient().getName());
		assertEquals("Package1", a.getAllRealizations().get(0).getSupplier().getName());
		
	}
	
	
	
	

}