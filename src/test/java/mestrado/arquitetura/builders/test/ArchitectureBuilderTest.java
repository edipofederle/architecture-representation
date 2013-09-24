package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Package;
import arquitetura.representation.Variability;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;

/**
 * 
 * @author edipofederle
 * 
 */
public class ArchitectureBuilderTest extends TestHelper {

	private Architecture architecture;

	private Package package1;

	@Before
	public void setUp() throws Exception {
		String uriToArchitecture = getUrlToModel("testArch");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		package1 = getPackageByName("Package1");
	}
	
	/**
	 * Baseado no arquivo de profile informado no arquivo de configuração.
	 * 
	 */
	@Test
	public void shouldHaveAListOfAllowedConcerns() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		assertNotNull(a.allowedConcerns());
	}

	@Test
	public void shouldHaveTwoConcerns() {
		assertEquals("Architecture should have two conecern", 2, architecture
				.getAllConcerns().size());
	}

	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull("Architecture should NOT be null", architecture);
	}

	@Test
	public void shouldHaveAName() throws Exception {
		assertEquals("Architecture name should be 'modelVariability",
				"testArch", architecture.getName());
	}

	@Test
	public void shouldHaveLoadPackages() throws Exception {
		assertNotNull(architecture.getAllPackages());
		assertEquals("Architecture should have two package", 4, architecture.getAllPackages().size());
	}

	@Test
	public void shouldHaveCorrectNameForPackage() {
		assertContains(architecture.getAllPackages(), "Package1");
	}

	@Test
	public void packageShouldContainTwoClasses() {
		hasClassesNames(package1, "Class1", "Class2", "Bar");
	}

	public Package getPackageByName(String name) {
		List<Package> packges = architecture.getAllPackages();
		for (Package p : packges) {
			if (name.equalsIgnoreCase(p.getName()))
				return p;
		}
		return null;
	}

	@Test
	public void shouldHaveMandatoryStereotype() {
		assertNotNull(architecture.getAllVariationPoints());
		System.out.println(architecture.getAllVariationPoints().get(0));
	}

	@Test
	public void shouldClassHaveNamespace() {
		Class class1 = (Class) package1.getClasses().get(0);
		assertNotNull(class1.getNamespace());
	}

	@Test
	public void shouldHaveAClassBarWithOneAttribute() {
		Class barKlass = (Class) package1.getClasses().get(2);
		assertEquals("String", barKlass.getAllAttributes().get(0).getType());
		assertEquals("name", barKlass.getAllAttributes().get(0).getName());
	}

	@Test
	public void shouldHaveOneMethod() {
		Class class1 = (Class) package1.getClasses().get(0);

		assertEquals("Class1", class1.getName());
		assertEquals(1, class1.getAllMethods().size());
		assertEquals("foo", class1.getAllMethods().get(0).getName());
		assertEquals("String", class1.getAllMethods().get(0).getReturnType());
		assertEquals(3, class1.getAllMethods().get(0).getParameters().size());
		assertEquals("name", class1.getAllMethods().get(0).getParameters().get(1)
				.getName());
		assertEquals("String", class1.getAllMethods().get(0).getParameters()
				.get(1).getType());
		assertEquals("Description", class1.getAllMethods().get(0).getParameters()
				.get(2).getName());
		assertEquals("String", class1.getAllMethods().get(0).getParameters()
				.get(2).getType());
	}

	@Test
	public void shouldHaveAEmptyStringTypeWhenNotTypeFoundForAttribute() {
		Class klassClass2 = (Class) package1.getClasses().get(1);
		assertEquals("", klassClass2.getAllAttributes().get(0).getType());
		assertEquals("age", klassClass2.getAllAttributes().get(0).getName());
	}

	@Test
	public void shouldClass2HaveTwoAttributes() {
		Class klassClass2 = (Class) package1.getClasses().get(1);
		assertEquals(2, klassClass2.getAllAttributes().size());
	}

	@Test
	public void shoulClassdBeAbastract() {
		Class klass = (Class) package1.getClasses().get(2);
		assertTrue("class should be abstract", klass.isAbstract());
	}

	@Test
	public void shouldClassNotBeAbstract() {
		Class klass = (Class) package1.getClasses().get(1);
		assertFalse("class should not be abstract", klass.isAbstract());
	}

	@Test
	public void shouldContainsAClassWithConcern() {
		List<Concern> concerns = package1.getClasses().get(0).getOwnConcerns();
		assertFalse(concerns.isEmpty());
		assertEquals("pong", concerns.get(1).getName());
	}

	@Test
	public void shouldContainTwoConcerns() {
		List<Concern> concerns = package1.getClasses().get(0).getOwnConcerns();
		assertEquals(2, concerns.size());
		assertEquals("pong", concerns.get(1).getName());
		assertEquals("play", concerns.get(0).getName());
	}

	@Test
	public void testWithoutPackages() throws Exception {
		String uriToArchitecture = getUrlToModel("semPacote");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
		assertNotNull(architecture);
		assertEquals(2, architecture.getAllClasses().size());
		assertEquals("Foo", architecture.getAllClasses().get(0).getName());

		assertEquals(1, architecture.getAllPackages().size());
		assertEquals("pacote1", architecture.getAllPackages().get(0).getName());
		Class klassBar = (Class) architecture.getAllPackages().get(0).getClasses().get(0);
		assertEquals("Bar", klassBar.getName());
	}


	@Test
	public void shouldLoadPackagesInsidePackage() {
		assertEquals("Pacote2 should contains two packges", 2,
				getPackageByName("Pacote2").getNestedPackages().size());
		assertEquals("Pacote1DentroDoPacote2", getPackageByName("Pacote2")
				.getNestedPackages().get(0).getName());
	}

	@Test
	public void shouldNestedPackagesHaveTwoClasses() {
		Package pkg = getPackageByName("Pacote2");
		Package p = pkg.getNestedPackages().get(0);
		assertNotNull(p);
		assertEquals(2, p.getClasses().size());
		hasClassesNames(p, "Person", "City");
	}

	@Test
	public void shouldNEstedPackage2HaveOneClass() {
		Package pkg = getPackageByName("Pacote2");
		Package pacote2DentrodoPacote2 = pkg.getNestedPackages().get(1);
		assertNotNull(pacote2DentrodoPacote2);
		assertEquals(1, pacote2DentrodoPacote2.getClasses().size());
		hasClassesNames(pacote2DentrodoPacote2, "Bar2");
	}

	@Test
	public void shouldHaveOneVariability() {
		assertEquals(1, architecture.getAllVariabilities().size());
	}


	@Test
	public void shouldVariabilityBelongToClass1() {
		Variability variability = architecture.getAllVariabilities().get(0);
		assertNotNull(variability);

		assertEquals("Class1", variability.getOwnerClass());
	}

	@Test
	public void shouldLoadInterElementDependency() throws Exception {
		String uriToArchitecture8 = getUrlToModel("dependency2");
		Architecture architecture8 = new ArchitectureBuilder()
				.create(uriToArchitecture8);

		DependencyRelationship dependencyInterElement = architecture8.getAllDependencies().get(0);

		assertNotNull(dependencyInterElement);
		
		assertEquals("Package1", dependencyInterElement.getClient().getName());

	}

	// AssociationClass

	@Test
	public void shouldLoadAssociationClassAssociation() throws Exception {
		String uriToArchitecture8 = getUrlToModel("associationClass");
		Architecture architecture8 = new ArchitectureBuilder().create(uriToArchitecture8);

		List<AssociationClassRelationship> associationClass = architecture8.getAllAssociationsClass();

		assertEquals(2, associationClass.get(0).getMemebersEnd().size());
		assertEquals("Employee", associationClass.get(0).getMemebersEnd().get(0).getName());
		assertEquals("Class1", associationClass.get(0).getMemebersEnd().get(1).getName());
//		assertEquals("Employee", associationClass.getOwnedEnd().getName());
//		assertTrue(associationClass.getOwnedEnd() instanceof Class);
		assertEquals("AssociationClass1", associationClass.get(0).getName());
	}

	/**
	 * @see <a href="http://d.pr/i/DLKa">Modelo usado no teste</a>
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLoadSimpleAssociationOnModelWithAssociationClassRelationship() throws Exception {

		String uriToArchitecture8 = getUrlToModel("associationClassAndAssociations");
		Architecture architecture8 = new ArchitectureBuilder().create(uriToArchitecture8);

		assertEquals(1, architecture8.getAllAssociationsRelationships().size());
		assertEquals(1, architecture8.getAllAssociationsClass().size());

		AssociationRelationship association =  architecture8.getAllAssociationsRelationships().get(0);

		assertNotNull(association);
		assertEquals(2, association.getParticipants().size());
		assertEquals("Employee", association.getParticipants().get(0).getCLSClass().getName());
		assertEquals("Class2", association.getParticipants().get(1).getCLSClass().getName());

	}


}