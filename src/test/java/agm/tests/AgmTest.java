package agm.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.flyweights.VariabilityFlyweight;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;

public class AgmTest extends TestHelper {
	
	@Test
	public void teste_1() throws Exception{
		String uriToArchitecture = getUrlToModel("AGM_TESTS/agm");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
//		System.out.println("Interfaces:"+ architecture.getAllInterfaces());
//		System.out.println("Classes:"+architecture.getAllClasses());
//		System.out.println(architecture.getAllConcerns());
//		System.out.println(architecture.getAllPackages());
		
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		Class player = architecture.findClassByName("Player").get(0);
		
		assertNotNull(movableSprites);
		assertTrue(movableSprites.isVariationPoint());
		
		//System.out.println(movableSprites.getVariationPoint().getVariabilities().get(0).getName());
		
		for (Variability v : VariabilityFlyweight.getInstance().getVariabilities()) {
			System.out.println("Nome:"+v.getName() + "| ClassOwner:"+v.getOwnerClass());
			for(Variant variant : v.getVariants()){
				System.out.println("\t"+variant.getName());
			}
			 
		}
	
	}

}
