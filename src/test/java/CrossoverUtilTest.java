import jmetal.operators.crossover.CrossoverUtils;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;


public class CrossoverUtilTest extends TestHelper {
	
	@Test
	public void test() throws Exception {
		CrossoverUtils crossoverutil = new CrossoverUtils();
		Architecture offspring = givenAArchitecture("offspring");
		Concern feature = offspring.getConcernByName("brickles");
		crossoverutil.removeArchitecturalElementsRealizingFeature(feature, offspring, "allLevels");
	}
}