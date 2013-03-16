package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.RealizationInterClassRelationship;

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
		
		List<InterClassRelationship> relations = architecture6.getInterClassRelationships();
		RealizationInterClassRelationship realization = (RealizationInterClassRelationship) relations.get(0);
		
		assertNotNull(realization);
		
		assertEquals("Source Element should be Class2", "Class2", realization.getClientElement().getName());
		assertEquals("Specific Element should be Class1", "Class1", realization.getSupplierElement().getName());
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
		
		assertEquals("Client should be Class1", "Class1", a.getAllRealizations().get(0).getClientElement().getName());
		assertEquals("Supplier should be Package1", "Package1", a.getAllRealizations().get(0).getSupplierElement().getName());
	}
	
	
	/**
	 * @see <a href="http://d.pr/i/9gms">Modelo usado no teste (Imagem) </a>
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLoadRealizationPackageClass() throws Exception{
		Architecture a = givenAArchitecture("realizationPackageClass");
		
		assertNotNull(a.getAllRealizations());
		assertEquals(1, a.getInterClassRelationships().size());
		assertEquals(1, a.getAllRealizations().size());
		
		assertEquals("Client should be Package1", "Package1", a.getAllRealizations().get(0).getClientElement().getName());
		assertEquals("Supplier should be Class1", "Class1", a.getAllRealizations().get(0).getSupplierElement().getName());
	}
	

}