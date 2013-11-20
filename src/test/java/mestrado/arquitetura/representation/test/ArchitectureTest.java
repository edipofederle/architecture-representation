package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;

public class ArchitectureTest extends TestHelper {
	
	private Architecture arch;
	private Architecture architecture;
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/generico.png">Modelo usado para architecture (Imagem)</a>
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception{
		arch = new Architecture("arquitetura");
		
		String uriToArchitecture = getUrlToModel("generico");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
	}
	
	@Test
	public void shouldReturnConcernsForClass() throws Exception{
		String uriToArchitecture = getUrlToModel("f/Needless");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
		assertEquals(2, architecture.findClassByName("Class1").get(0).getOwnConcerns().size());
		assertEquals("action", architecture.findClassByName("Class1").get(0).getOwnConcerns().get(0).getName());
		assertEquals("bowling", architecture.findClassByName("Class1").get(0).getOwnConcerns().get(1).getName());
	}
	
	@Test
	public void teste() throws Exception{
		String uriToArchitecture = getUrlToModel("pacotesTesteRe/Needless");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
		System.out.println(architecture.getAllRealizations().iterator().next().getName());
		System.out.println("Client:"+architecture.getAllRealizations().iterator().next().getClient().getName());
		System.out.println("Supplier:"+architecture.getAllRealizations().iterator().next().getSupplier().getName());
		
		System.out.println("\n");
		System.out.println(architecture.getAllDependencies().iterator().next().getName());
		System.out.println("Cliente:"+architecture.getAllDependencies().iterator().next().getClient().getName());
		System.out.println("Supplier:"+architecture.getAllDependencies().iterator().next().getSupplier().getName());
		
	}
	
	@Test
	public void shouldArchitectureHaveAName(){
		assertEquals("arquitetura", arch.getName());
	}
	
	@Test
	public void shouldArchitectureHaveAEmptyNameWhenNull(){
		arch.setName(null);
		assertEquals("", arch.getName());
	}
	
	@Test
	public void shouldReturnAllPackages(){
		new Package(arch, "Pacote");
		
		assertEquals(1, arch.getAllPackages().size());
		assertEquals("Pacote", arch.getAllPackages().iterator().next().getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoPackages(){
		assertTrue(arch.getAllPackages().isEmpty());
	}
	
	@Test
	public void shouldReturnAllClasses() throws ClassNotFound{
		new Class(arch, "Klass", false);
		
		assertEquals(1, arch.getClasses().size());
		assertEquals("Klass", arch.findClassByName("Klass").get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoClasses(){
		assertTrue(arch.getClasses().isEmpty());
	}

	@Test
	public void shouldReturnEmptyListWhenNoVariabilities(){
		assertEquals(Collections.emptyList(), arch.getAllVariabilities());
	}
	
	@Test
	public void shouldReturnNullWhenElementNotFound(){
		assertNull(arch.findElementByName("KlassNotFound","class"));
	}
	
	@Test
	public void shouldReturnElementClassByName(){
		new Class(arch, "Klass", false);
		Element klass = arch.findElementByName("klass","class");
		
		assertNotNull(klass);
		assertEquals("Klass", klass.getName());
	}
	
	@Test
	public void shouldReturnElementPackageByName(){
		assertEquals(0, arch.getAllPackages().size());
		
		new Package(arch, "Pacote");
		Element pkg = arch.findElementByName("Pacote", "package");
		
		assertNotNull(pkg);
		assertEquals("Pacote", pkg.getName());
		assertEquals(1, arch.getAllPackages().size());
	}
	
	@Test
	public void shouldReturnAllInterfaces() throws InterfaceNotFound{
		new Class(arch, "Klass1", false);
		new Interface(arch, "Interface1");
		new Interface(arch, "Interface2");
		
		assertEquals(2, arch.getInterfaces().size());
		assertEquals(1, arch.getClasses().size());
		assertNotNull(arch.findInterfaceByName("Interface1"));
		assertNotNull(arch.findInterfaceByName("Interface2"));
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoInterfaces(){
		assertTrue(arch.getInterfaces().isEmpty());
	}
	
	@Test
	public void shouldReturnAllGeneralizations() throws Exception{
		assertEquals(3, architecture.getAllRelationships().size());
		assertNotNull(architecture.getAllGeneralizations());
		assertEquals(1, architecture.getAllGeneralizations().size());
	}
	
	
	@Test
	public void shouldReturnAllAssociations(){
		assertEquals(1, architecture.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void shouldReturnAllUsageClassPackage(){
		assertEquals(1, architecture.getAllUsage().size());
	}
	
	@Test
	public void shouldReturnAllDependency() throws Exception{
		String uriToArchitecture = getUrlToModel("dependency");
		Architecture a = new ArchitectureBuilder().create(uriToArchitecture);
		assertEquals(5, a.getAllDependencies().size());
	}
	
	@Test
	public void shouldReturnVisibilityForAttribute() throws Exception{
		String uriToArchitecture =  getUrlToModel("testeCreateClassWithAttribute");
		Architecture a = new ArchitectureBuilder().create(uriToArchitecture);
		Class klass = a.findClassByName("Class43").get(0);
		assertEquals("public",klass.findAttributeByName("age").getVisibility());
	}
	
	@Test
	public void shouldReturnAllDependencyClassPackage() throws Exception{
		String uriToArchitecture = getUrlToModel("dependency");
		Architecture a = new ArchitectureBuilder().create(uriToArchitecture);
		assertEquals(5, a.getAllDependencies().size());
	}
	
	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/d5.png"> Modelo usado no teste (imagem)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldLoadDependencyPackageClass() throws Exception{
		String uriToArchitecture = getUrlToModel("dependency2");
		Architecture a = new ArchitectureBuilder().create(uriToArchitecture);
		assertEquals(1, a.getAllDependencies().size());
	}
	
	@Test
	public void shouldFindClassByName() throws Exception{
		assertNotNull(architecture.findClassByName("Class3"));
		assertEquals("Class3", architecture.findElementByName("CLass3","class").getName());
	}
	
	@Test
	public void shouldFindClassById() throws Exception{
		Architecture a = givenAArchitecture("classesComMesmoNome");
		Class c = a.findClassById("_Tln8YMlrEeKhSNDKJvPCfQ");
		
		assertNotNull(c);
		assertEquals("model::Package1",c.getNamespace());
		
		Class c2 = a.findClassById("_Q4UBkMlrEeKhSNDKJvPCfQ");
		assertNotNull(c2);
		assertEquals("model",c2.getNamespace());
	}
	
	@Test
	public void shouldReturnsListOfClassesWhenModelContainsClassesWithSameName() throws Exception{
		targetDirExport =  "src/test/java/resources/";
		Architecture a = givenAArchitecture2("classesComMesmoNome");
		List<Class> classes = a.findClassByName("Class1");
		
		assertEquals(2,classes.size());
		assertEquals("Class1", classes.get(0).getName());
		assertEquals("Class1", classes.get(1).getName());
		targetDirExport = ReaderConfig.getDirExportTarget();
	}
	
	@Test(expected=ClassNotFound.class)
	public void shouldRaiseClassNotFoundExceptionWhenClassNotFound() throws ClassNotFound{
		architecture.findClassByName("noClass");
	}
	
	@Test
	public void shouldFindInterfaceByName() throws Exception{
		Architecture a = givenAArchitecture("classes");
		assertNotNull(a.findInterfaceByName("Interface"));
		assertEquals("Interface", a.findInterfaceByName("Interface").getName());
	}
	
	@Test(expected=InterfaceNotFound.class)
	public void shouldRaiseInterfaceNotFoundExceptionWhenInterfaceNotFound() throws InterfaceNotFound{
		architecture.findInterfaceByName("noInterface");
	}
	
	@Test
	public void shouldFindPackageByName() throws PackageNotFound{
		Package pkg = architecture.findPackageByName("Package1");
		assertNotNull(pkg);
		assertEquals("Package1", pkg.getName());
	}
	
	@Test(expected=PackageNotFound.class)
	public void shouldRaisePackageNotFoundExceptionWhenPackageNotFound() throws PackageNotFound{
		architecture.findPackageByName("noPackage");
	}
	
	@Test
	public void shouldMoveClassToPackage() throws Exception{
		Architecture a = givenAArchitecture("classPackage");
		Class klass1 = a.findClassByName("Class1").get(0);
		Package package1 = a.findPackageByName("Package1");
		
		a.moveClassToPackage(klass1,package1);
		
	}
	
	@Test
	public void shouldRemoveAssociationRelationship() throws Exception{
		Architecture a = givenAArchitecture("associations/associationWithMultiplicity");
		AssociationRelationship as = a.getAllAssociationsRelationships().iterator().next();
		assertEquals("Architecture should contain 2 associations", 1, a.getAllAssociationsRelationships().size());
		assertEquals(2, a.getClasses().size());
		a.removeRelationship(as);
		assertEquals("Architecture should contain 0 associations", 0, a.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAssociationRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("associations/associationWithMultiplicity");
		assertEquals("Architecture should contain 2 associations", 1,	a.getAllAssociationsRelationships().size());
		a.operationsOverRelationship().removeAssociationRelationship(null);
		assertEquals("Architecture should contain 2 associations", 1,	a.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void shouldRemoveDependecyRelationship() throws Exception{
		Architecture a = givenAArchitecture("dependency");
		DependencyRelationship dp = a.getAllDependencies().iterator().next();
		assertEquals("Architecture should contain 5 dependency", 5,	a.getAllDependencies().size());
		a.operationsOverRelationship().removeDependencyRelationship(dp);
		assertEquals("Architecture should contain 4 dependency", 4,	a.getAllDependencies().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveDependencyRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("dependency");
		assertEquals("Architecture should contain 5 dependency", 5,	a.getAllDependencies().size());
		a.operationsOverRelationship().removeDependencyRelationship(null);
		assertEquals("Architecture should contain 4 dependency", 5,	a.getAllDependencies().size());
	}
	
	@Test
	public void shouldRemoveUsageRelationship() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		UsageRelationship usage = a.getAllUsage().iterator().next();
		assertEquals("Architecture should contain 1 usage", 1,	a.getAllUsage().size());
		a.forUsage().remove(usage);
		assertEquals("Architecture should contain 0 usage", 0,	a.getAllUsage().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveUsageRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		assertEquals("Architecture should contain 1 usage", 1,	a.getAllUsage().size());
		a.forUsage().remove(null);
		assertEquals("Architecture should contain 0 usage", 1,	a.getAllUsage().size());
	}
	
	@Test
	public void shouldRemoveAssociationClassRelationship() throws Exception{
		Architecture a = givenAArchitecture("associationClass");
		AssociationClassRelationship associationClass = a.getAllAssociationsClass().iterator().next();
		assertEquals("Architecture should contain 1 AssociationClass", 1,	a.getAllAssociationsClass().size());
		a.operationsOverRelationship().removeAssociationClass(associationClass);
		assertEquals("Architecture should contain 0 AssociationClass", 0,	a.getAllAssociationsClass().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAssociationClassRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("associationClass");
		assertEquals("Architecture should contain 1 AssociationClass", 1,	a.getAllAssociationsClass().size());
		assertEquals("Architecture should contain 0 AssociationClass", 1,	a.getAllAssociationsClass().size());
	}
	
	@Test
	public void shouldRemoveGeneralizationRelationship() throws Exception{
		Architecture a = givenAArchitecture("generalizationArch");
		GeneralizationRelationship g = a.getAllGeneralizations().iterator().next();
		assertEquals("Architecture should contain 3 generalization", 3,	a.getAllGeneralizations().size());
		a.operationsOverRelationship().removeGeneralizationRelationship(g);
		assertEquals("Architecture should contain 2 generalization", 2,	a.getAllGeneralizations().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveGeneralizationRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("generalizationArch");
		assertEquals("Architecture should contain 3 generalization", 3,	a.getAllGeneralizations().size());
		a.operationsOverRelationship().removeGeneralizationRelationship(null);
		assertEquals("Architecture should contain 2 generalization", 3,	a.getAllGeneralizations().size());
	}
	
	@Test
	public void shouldRemoveAbstractionRelationship() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		AbstractionRelationship ab = a.getAllAbstractions().iterator().next();
		assertEquals("Architecture should contain 1 Abstraction", 2,	a.getAllAbstractions().size());
		a.forAbstraction().remove(ab);
		assertEquals("Architecture should contain 0 Abstraction", 1,	a.getAllAbstractions().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAbstractionRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		assertEquals("Architecture should contain 1 Abstraction", 2, a.getAllAbstractions().size());
		a.forAbstraction().remove(null);
		assertEquals("Architecture should contain 1 Abstraction", 2, a.getAllAbstractions().size());
	}
	
	@Test
	public void shouldCreateAPackage() throws PackageNotFound{
		Package packageTest = architecture.createPackage("myPackage");
		assertEquals("generico::myPackage", packageTest.getNamespace());
		
		assertNotNull(packageTest.getId());
		assertEquals(2, architecture.getAllPackages().size());
		assertSame(packageTest, architecture.findPackageByName("myPackage"));
	}
	
	@Test
	public void shouldRemoveAPackage() throws PackageNotFound{
		Package p = architecture.findPackageByName("Package1");
		assertNotNull(p);
		architecture.removePackage(p);
		assertEquals(0,architecture.getAllPackages().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove1() throws Exception{
		Architecture a = givenAArchitecture("testePackageRemove");
		
		Set<Class> allClasses = new HashSet<Class>();
		for(Package p : a.getAllPackages())
			allClasses.addAll(p.getClasses());
		
		Set<Interface> allInterfaces = new HashSet<Interface>();
		for(Package p : a.getAllPackages())
			allInterfaces.addAll(p.getAllInterfaces());
		
		assertEquals(1, allClasses.size());
		assertEquals(0, allInterfaces.size());
		
		
		a.removePackage(a.getAllPackages().iterator().next());
		
		assertEquals(0, a.getClasses().size());
		assertEquals(0, a.getClasses().size());
		assertTrue(a.getAllPackages().isEmpty());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove2() throws Exception{
		Architecture a = givenAArchitecture("removePacoteTeste2");
		
		Set<Class> allClasses = new HashSet<Class>();
		for(Package p : a.getAllPackages())
			allClasses.addAll(p.getClasses());
		
		allClasses.addAll(a.getClasses());
		
		
		assertEquals(2, allClasses.size());
		assertEquals(1, a.getAllAssociationsRelationships().size());
		
		a.removePackage(a.getAllPackages().iterator().next());
		
		assertEquals(1, a.getClasses().size());
		assertEquals(0, a.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove3() throws Exception{
		Architecture a = givenAArchitecture("removePacote3");
		
		Set<Class> allClasses = new HashSet<Class>();
		for(Package p : a.getAllPackages())
			allClasses.addAll(p.getClasses());
		
		allClasses.addAll(a.getClasses());
		
		assertEquals("Deve ter tres clases", 3, allClasses.size());
		assertEquals("Deve ter 2 associações",2, a.getAllAssociationsRelationships().size());
		
		for(AssociationRelationship as : a.getAllAssociationsRelationships())
			assertEquals(2, as.getParticipants().size());
		
		a.removePackage(a.getAllPackages().iterator().next());
		
		Set<Class> allClasses2 = new HashSet<Class>();
		for(Package p : a.getAllPackages())
			allClasses2.addAll(p.getClasses());
		
		allClasses2.addAll(a.getClasses());
		
		assertEquals("Deve ter uma classe", 1,allClasses2.size());
		assertEquals(0, a.getAllAssociationsRelationships().size());
		
	}
	
	
	@Test
	public void shouldCreateInterface() throws InterfaceNotFound{
		assertEquals(0, architecture.getInterfaces().size());
		Interface interfacee = architecture.createInterface("myInterface");

		assertEquals("generico::myInterface", interfacee.getNamespace());
		
		assertNotNull(interfacee);
		assertNotNull(interfacee.getId());
		assertEquals(1, architecture.getInterfaces().size());
		assertEquals(interfacee, architecture.findInterfaceByName("myInterface"));
	}
	
	@Test
	public void shouldRemoveInterface(){
		Interface interfacee = architecture.createInterface("myInterface");
		
		assertEquals(1, architecture.getInterfaces().size());
		
		architecture.removeInterface(interfacee); 
		
		assertEquals(0, architecture.getInterfaces().size());
	}
	
	@Test
	public void shouldCreateClass() throws ClassNotFound{
		assertEquals(3, architecture.getClasses().size());
		
		
		Class klass = architecture.createClass("Bar", true);
		assertEquals("generico::Bar", klass.getNamespace());
		
		assertEquals(4, architecture.getClasses().size());
		
		assertEquals(klass, architecture.findClassByName("Bar").get(0));
		assertTrue(architecture.findClassByName("Bar").get(0).isAbstract());
	}
	
	@Test
	public void shouldRemoveClass() throws ClassNotFound{
		assertEquals(3, architecture.getClasses().size());
		Class klass = architecture.findClassByName("Class1").get(0);
		
		architecture.removeClass(klass);
		assertEquals(2, architecture.getClasses().size());
	}
	
	@Test
	public void shouldFindPackageOfClass() throws Exception{
		Architecture arch = givenAArchitecture("classPacote");
		Class klass = arch.findClassByName("Class1").get(0);
		
		assertEquals("Class1",klass.getName());
		
		assertEquals("Package1", arch.findPackageOfClass(klass).getName());
	}
	
	@Test(expected=PackageNotFound.class)
	public void shouldThrowsPackageNotFound() throws Exception{
		Architecture arch = givenAArchitecture("classPacote");
		
		Class klass = Mockito.mock(Class.class);
		Mockito.when(klass.getName()).thenReturn("XPTO");
		
		arch.findPackageOfClass(klass);
	}
	
	@Test
	public void shouldReturnAllRelationshipsOfPackage() throws Exception{
		Architecture a = givenAArchitecture("PackageClassUsage");
		
		Package p = a.getAllPackages().iterator().next();
		List<Relationship> relationships = p.getRelationships();
		
		assertEquals(1, relationships.size());
		assertTrue(relationships.get(0) instanceof UsageRelationship);
	}
	
	@Test
	public void shouldReturnEmptyListWhenNotFoundRelationships() throws Exception{
		Architecture a = givenAArchitecture("Package");
		
		assertTrue(a.getAllPackages().iterator().next().getRelationships().isEmpty());
	}
	
	@Test
	public void testClone() throws Exception{
		Architecture a = givenAArchitecture("agmfinal/agm");
		Architecture cloneA = a.deepClone();
		
		assertNotNull(cloneA);
		assertNotSame(a, cloneA);
	}
	
	@Test
	public void testGetAllElements() throws Exception{
		Architecture a = givenAArchitecture("recurPackages");
		
		assertEquals(7, a.getElements().size());
		assertEquals("Deve ter 3 pacotes", 3, a.getAllPackages().size());
		assertContains(a.getAllPackages(), "Package1", "Package2", "Package3");
		
		assertEquals("Deve ter 4 classes", 4, a.getAllClasses().size());
	}
}