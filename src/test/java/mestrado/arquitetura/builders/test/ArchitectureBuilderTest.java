package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Concern;

import org.junit.Before;
import org.junit.Test;

public class ArchitectureBuilderTest extends TestHelper {
	
	Architecture architecture;
	
	@Before
	public void setUp() throws Exception{
		String uriToArchitecture = getUrlToModel("testArch");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
	}
	
	@Test
	public void shouldNotBeNull() throws Exception{
		assertNotNull("Architecture should NOT be null", architecture);
	}
	
	@Test
	public void shouldHaveAName() throws Exception{
		assertEquals("Architecture name should be 'modelVariability", "testArch", architecture.getName());
	}
	
	@Test
	public void shouldHaveLoadPackages() throws Exception{
		assertNotNull(architecture.getPackages());
		assertEquals(1, architecture.getPackages().size());
	}
	
	@Test
	public void shouldHaveCorrectNameForPackage(){
		assertEquals("Package1", architecture.getPackages().get(0).getName());
	}
	
	@Test
	public void packageShouldContainTwoClasses(){
		assertEquals(2, architecture.getPackages().get(0).getClasses().size());
		assertEquals("Class1", architecture.getPackages().get(0).getClasses().get(0).getName());
		assertEquals("Class2", architecture.getPackages().get(0).getClasses().get(1).getName());
	}
	
	@Test
	public void shouldContainsAClassWithConcern(){
		List<Concern> concerns = architecture.getPackages().get(0).getClasses().get(0).getConcerns();
		assertFalse(concerns.isEmpty());
		assertEquals("Persistence", concerns.get(0).getName());
	}
	
	@Test
	public void shouldContainTwoConcerns(){
		List<Concern> concerns = architecture.getPackages().get(0).getClasses().get(0).getConcerns();
		assertEquals(2, concerns.size());
		assertEquals("Persistence",  concerns.get(0).getName());
		assertEquals("sorting",  concerns.get(1).getName());
	}
	
}