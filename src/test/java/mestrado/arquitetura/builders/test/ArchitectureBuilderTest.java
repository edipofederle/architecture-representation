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
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Method;
import mestrado.arquitetura.representation.Package;

import org.junit.Before;
import org.junit.Test;


public class ArchitectureBuilderTest extends TestHelper {
	
	private Architecture architecture;
	private Package package1;
	
	@Before
	public void setUp() throws Exception{
		String uriToArchitecture = getUrlToModel("testArch");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		package1 = getPackageByName("Package1");
	}
	
	@Test
	public void shouldHaveTwoConcerns(){
		assertEquals("Architecture should have two conecern", 2, architecture.getConcerns().size());
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
		assertEquals("Architecture should have one package", 2, architecture.getPackages().size());
	}
	
	@Test
	public void shouldHaveCorrectNameForPackage(){
		assertContains(architecture.getPackages(), "Package1");
	}	
	
	@Test
	public void packageShouldContainTwoClasses(){
		hasClassesNames(package1, "Class1", "Class2", "Bar");
	}
	
	public Package getPackageByName(String name){
		List<Package> packges = architecture.getPackages();
		for (Package p : packges) {
			if(name.equalsIgnoreCase(p.getName()))
				return p;
		}
		return null;
	}
	
	@Test
	public void shouldHaveMandatoryStereotype(){
		Class class1 = package1.getClasses().get(0);
		assertEquals("mandatory", class1.getVariantType().toString());
	}
	
	@Test
	public void shouldHaveAClassBarWithOneAttribute(){
		 Class barKlass = package1.getClasses().get(2);
		 assertEquals("String",barKlass.getAttributes().get(0).getType());
		 assertEquals("name",barKlass.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldHaveOneMethod(){
		 Class class1 =package1.getClasses().get(0);
		 
		 assertEquals("Class1", class1.getName());
		 assertEquals(1, class1.getMethods().size());
		 assertEquals("foo", class1.getMethods().get(0).getName());
		 assertEquals("String", class1.getMethods().get(0).getReturnType());
		 assertEquals(2, class1.getMethods().get(0).getParameters().size());
		 assertEquals("name", class1.getMethods().get(0).getParameters().get(1).getName());
		 assertEquals("String", class1.getMethods().get(0).getParameters().get(1).getType());
		 assertEquals("description", class1.getMethods().get(0).getParameters().get(0).getName());
		 assertEquals("String", class1.getMethods().get(0).getParameters().get(0).getType());
	}
	
	@Test
	public void shouldHaveAEmptyStringTypeWhenNotTypeFoundForAttribute(){
		Class klassClass2 = package1.getClasses().get(1);
		assertEquals("", klassClass2.getAttributes().get(0).getType());
		assertEquals("age", klassClass2.getAttributes().get(0).getName());
	}
	
	@Test
	public void shouldClass2HaveTwoAttributes(){
		Class klassClass2 = package1.getClasses().get(1);
		assertEquals(2, klassClass2.getAttributes().size());
	}
	
	@Test
	public void shoulClassdBeAbastract(){
		Class klass = package1.getClasses().get(2);
		assertTrue("class should be abstract", klass.isAbstract());
	}
	
	@Test
	public void shouldClassNotBeAbstract(){
		Class klass = package1.getClasses().get(1);
		assertFalse("class should not be abstract", klass.isAbstract());
	}
	
	@Test
	public void shouldContainsAClassWithConcern(){
		List<Concern> concerns =  package1.getClasses().get(0).getConcerns();
		assertFalse(concerns.isEmpty());
		assertEquals("Persistence", concerns.get(0).getName());
	}
	
	@Test
	public void shouldContainTwoConcerns(){
		List<Concern> concerns = package1.getClasses().get(0).getConcerns();
		assertEquals(2, concerns.size());
		assertEquals("Persistence",  concerns.get(0).getName());
		assertEquals("sorting",  concerns.get(1).getName());
	}
	
	@Test
	public void testWithoutPackages() throws Exception{
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		assertNotNull(architecture);
		assertEquals(1, architecture.getClasses().size());
		assertEquals("Foo", architecture.getClasses().get(0).getName());
		
		assertEquals(1, architecture.getPackages().size());
		assertEquals("pacote1", architecture.getPackages().get(0).getName());
		Class klassBar = architecture.getPackages().get(0).getClasses().get(0);
		assertEquals("Bar", klassBar.getName());
	}

	@Test
	public void shoudlElementHaveAParentPackage() throws Exception{
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		Class klassBar = architecture.getPackages().get(0).getClasses().get(0);
		assertNotNull(klassBar.getParent());
		assertEquals("Class should belongs to package 'pacote1'", "pacote1", klassBar.getParent().getName());
	}
	
	@Test
	public void shouldAClassWithoutPackageBelongsToNONEParent() throws Exception{
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		Class fooKlass = architecture.getClasses().get(0);
		
		assertEquals(null, fooKlass.getParent());
	}
	
	@Test
	public void shouldMethodHaveAParentClass(){
		Class class1 = package1.getClasses().get(0);
		assertNotNull(class1.getMethods().get(0));
		assertNotNull(class1.getParent());
		assertEquals("Class1", class1.getMethods().get(0).getParent().getName());
	}
	
	@Test
	public void shouldAttributeHaveAParentClass(){
		Class klassClass2 = package1.getClasses().get(1);
		Element parentKlassClass2 = klassClass2.getAttributes().get(0).getParent();
		assertNotNull(parentKlassClass2);
		assertEquals("Class2", parentKlassClass2.getName());
	}
	
	@Test
	public void shouldMethodhaveAParrentClass(){
		Class class1 = package1.getClasses().get(0);
		Method fooMethod = class1.getMethods().get(0);
		assertNotNull(fooMethod.getParent());
		assertEquals("Class1", fooMethod.getParent().getName());
	}
	
	@Test
	public void shouldLoadPackagesInsidePackage(){
	  	assertEquals("Pacote2 should contains two packges", 2, getPackageByName("Pacote2").getNestedPackages().size());
	  	assertEquals("Pacote1DentroDoPacote2", getPackageByName("Pacote2").getNestedPackages().get(0).getName());
	}
	
	@Test
	public void shouldNestedPackagesHaveTwoClasses(){
		Package pkg = getPackageByName("Pacote2");
		Package p = pkg.getNestedPackages().get(0);
		assertNotNull(p);
		assertEquals(2,p.getClasses().size());
		hasClassesNames(p, "Person", "City");
	}
	
	@Test
	public void shouldNEstedPackage2HaveOneClass(){
		Package pkg = getPackageByName("Pacote2");
		Package pacote2DentrodoPacote2 = pkg.getNestedPackages().get(1);
		assertNotNull(pacote2DentrodoPacote2);
		assertEquals(1, pacote2DentrodoPacote2.getClasses().size());
		hasClassesNames(pacote2DentrodoPacote2, "Bar2");
	}
	
}