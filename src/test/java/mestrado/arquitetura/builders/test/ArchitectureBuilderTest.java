package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
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
		assertEquals(3, architecture.getPackages().get(0).getClasses().size());
		assertEquals("Class1", architecture.getPackages().get(0).getClasses().get(0).getName());
		assertEquals("Class2", architecture.getPackages().get(0).getClasses().get(1).getName());
		assertEquals("Bar", architecture.getPackages().get(0).getClasses().get(2).getName());
	}
	
	@Test
	public void shouldHaveMandatoryStereotype(){
		Class class1 = architecture.getPackages().get(0).getClasses().get(0);
		assertEquals("mandatory",class1.getVariantType().toString());
	}
	
	@Test
	public void shouldHaveAClassBarWithOneAttribute(){
		 Class barKlass = architecture.getPackages().get(0).getClasses().get(2);
		 assertEquals("String",barKlass.getAttributes().get(0).getType());
		 assertEquals("name",barKlass.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldHaveAEmptyStringTypeWhenNotTypeFoundForAttribute(){
		Class klassClass2 = architecture.getPackages().get(0).getClasses().get(1);
		assertEquals("", klassClass2.getAttributes().get(0).getType());
		assertEquals("age", klassClass2.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldClass2HaveTwoAttributes(){
		Class klassClass2 = architecture.getPackages().get(0).getClasses().get(1);
		assertEquals(2, klassClass2.getAttributes().size());
	}
	
	
	@Test
	public void classShouldBeAbastract(){
		Class klass = architecture.getPackages().get(0).getClasses().get(2);
		assertTrue("class should be abstract", klass.isAbstract());
	}
	
	@Test
	public void classShoiuldNotBeAbstract(){
		Class klass = architecture.getPackages().get(0).getClasses().get(1);
		assertFalse("class should not be abstract", klass.isAbstract());
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