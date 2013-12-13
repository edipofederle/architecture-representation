import static org.junit.Assert.assertEquals;
import jmetal.operators.crossover.CrossoverOperations;
import main.GenerateArchitecture;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Package;


public class CrossoverTests extends TestHelper {

	Architecture parent;
	Architecture offspring;
	GenerateArchitecture generate;
	
	@Before
	public void setUp() throws Exception{
		parent = givenAArchitecture("parent");
		offspring = givenAArchitecture("offspring");
		generate = new GenerateArchitecture();
	}
	
	@Test
	public void testAddAttributesRealizingFeatureToOffspring() throws Exception {
		Class classComp = parent.findClassByName("Class2").get(0);
		Concern concern = parent.getConcernByName("pause");
		assertEquals(0, offspring.findClassByName("Class2").get(0).getAllAttributes().size());
		
		CrossoverOperations.addAttributesRealizingFeatureToOffspring(concern, classComp, null, offspring);
		
		assertEquals(1, offspring.findClassByName("Class2").get(0).getAllAttributes().size());
	}
	
	@Test
	public void testAddAttributesRealizingFeatureToOffspring2() throws Exception {
		
		Class classComp = parent.findClassByName("Foo").get(0); //Classe somente existe em parent
		Concern concern = parent.getConcernByName("brickles");
		
		Package parentPackage = parent.findPackageByName("Package1");
		
		CrossoverOperations.addAttributesRealizingFeatureToOffspring(concern, classComp, parentPackage, offspring);
		
		assertEquals(1, offspring.findClassByName("Foo").get(0).getAllAttributes().size());
		
		generate.generate(offspring, "offsprintAttributesRealizingFeature");
	}
	
	@Test
	public void testAddAttributesRealizingFeatureToOffspring3() throws Exception {
		Class classComp = parent.findClassByName("Foo").get(0); //Classe somente existe em parent
		Concern concern = parent.getConcernByName("brickles");
		
		Package parentPackage = parent.findPackageByName("Package2");
		
		CrossoverOperations.addAttributesRealizingFeatureToOffspring(concern, classComp, parentPackage, offspring);
		
		assertEquals(1, offspring.findClassByName("Foo").get(0).getAllAttributes().size());
		
		generate.generate(offspring, "offsprintAttributesRealizingFeature2");
	} 
	
	@Test
	public void testMoveHierarchyToSameComponent() throws Exception {
		Architecture parent = givenAArchitecture("parent2");
		Architecture offspring = givenAArchitecture("offspring2");
		
		Class classComp = parent.findClassByName("Class1").get(0);
		
		Package targetComp = offspring.findPackageByName("Package1");
		Package sourceComp = parent.findPackageByName("Package1");
		Concern concern = parent.getConcernByName("brickles");
		
		CrossoverOperations.moveHierarchyToSameComponent(classComp, targetComp, sourceComp, offspring, parent, concern);
		
		generate.generate(offspring, "MoveHierarchyToSameComponent");
	}
	
	@Test
	public void testMoveHierarchyToSameComponent2() throws Exception {
		Architecture parent = givenAArchitecture("parent2");
		Architecture offspring = givenAArchitecture("offspring2");
		
		Class classComp = parent.findClassByName("Class1").get(0);
		
		Package targetComp = offspring.createPackage("Novo");
		Package sourceComp = parent.findPackageByName("Package1");
		Concern concern = parent.getConcernByName("brickles");
		
		CrossoverOperations.moveHierarchyToSameComponent(classComp, targetComp, sourceComp, offspring, parent, concern);
		
		generate.generate(offspring, "MoveHierarchyToSameComponent2");
	}
	
	@Test
	public void testAddInterfacesToOffspring() throws Exception {
		
		Architecture parent = givenAArchitecture("parent2");
		Architecture offspring = givenAArchitecture("offspring2");
		Concern concern = parent.getConcernByName("brickles");
		
		Package comp = parent.findPackageByName("Package1");
		Package newComp = offspring.createPackage("NovoPacote");
		
		CrossoverOperations.addInterfacesToOffspring(concern, comp, newComp, offspring);
		
		generate.generate(offspring, "testAddInterfacesToOffspring");
		Architecture gerada = givenAArchitecture2("testAddInterfacesToOffspring");
		assertEquals(3, gerada.getAllPackages().size());
		assertEquals(1, gerada.getAllDependencies().size());
		assertEquals(1, gerada.getAllRealizations().size());
	}
	
	
} 
