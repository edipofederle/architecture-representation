package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;

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
		
		assertEquals(2,architecture.getAllClasses().get(0).getOwnConcerns().size());
		assertEquals("action",architecture.getAllClasses().get(0).getOwnConcerns().get(0).getName());
		assertEquals("bowling",architecture.getAllClasses().get(0).getOwnConcerns().get(1).getName());
	}
	
	@Test
	public void teste() throws Exception{
		String uriToArchitecture = getUrlToModel("pacotesTesteRe/Needless");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
		System.out.println(architecture.getAllRealizations().get(0).getName());
		System.out.println("Client:"+architecture.getAllRealizations().get(0).getClient().getName());
		System.out.println("Supplier:"+architecture.getAllRealizations().get(0).getSupplier().getName());
		
		System.out.println("\n");
		System.out.println(architecture.getAllDependencies().get(0).getName());
		System.out.println("Cliente:"+architecture.getAllDependencies().get(0).getClient().getName());
		System.out.println("Supplier:"+architecture.getAllDependencies().get(0).getSupplier().getName());
		
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
		Package pkg = new Package(arch, "Pacote", null, "","id");
		arch.getElements().add(pkg);
		
		assertEquals(1, arch.getAllPackages().size());
		assertEquals("Pacote", arch.getAllPackages().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoPackages(){
		assertEquals(Collections.emptyList(), arch.getAllPackages());
	}
	
	@Test
	public void shouldReturnAllClasses(){
		Class klass = new Class(arch, "Klass", null, false, "namespace","id");
		arch.getElements().add(klass);
		
		assertEquals(1, arch.getAllClasses().size());
		assertEquals("Klass", arch.getAllClasses().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoClasses(){
		assertEquals(Collections.emptyList(), arch.getAllClasses());
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
		arch.getElements().add(new Class(arch, "Klass",  null, false,  "namespace", "id"));
		Element klass = arch.findElementByName("klass","class");
		
		assertNotNull(klass);
		assertEquals("Klass", klass.getName());
	}
	
	@Test
	public void shouldReturnElementPackageByName(){
		arch.getElements().add( new Package(arch, "Pacote", "id"));
		Element pkg = arch.findElementByName("Pacote", "package");
		
		assertNotNull(pkg);
		assertEquals("Pacote", pkg.getName());
	}
	
	@Test
	public void shouldReturnAllInterfaces(){
		arch.getElements().add(new Class(arch, "Klass1",  null, false,  "namespace", "id"));
		arch.getElements().add(new Interface(arch, "Interface1", null, "namesapce","id"));
		arch.getElements().add(new Interface(arch, "Interface2", null, "namesapce","id"));
		
		assertEquals(2, arch.getAllInterfaces().size());
		assertEquals(1, arch.getAllClasses().size());
		assertContains(arch.getAllInterfaces(), "Interface1", "Interface2");
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoInterfaces(){
		assertEquals(Collections.emptyList(), arch.getAllInterfaces());
	}
	
	@Test
	public void shouldReturnAllGeneralizations() throws Exception{
		assertEquals(3, architecture.getInterClassRelationships().size());
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
		assertEquals(2,a.getAllClasses().size());
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
		
		assertEquals(1,package1.getAllClassIdsForThisPackage().size());
		
	}
	
	@Test
	public void shouldRemoveAssociationRelationship() throws Exception{
		Architecture a = givenAArchitecture("association");
		AssociationRelationship as = a.getAllAssociationsRelationships().get(0);
		assertEquals("Architecture should contain 2 associations", 2, a.getAllAssociationsRelationships().size());
		assertEquals(8, a.getAllClasses().size());
		a.operationsOverRelationship().removeAssociationRelationship(as);
		assertEquals("Architecture should contain 3 associations", 1, a.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAssociationRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("association");
		assertEquals("Architecture should contain 2 associations", 2,	a.getAllAssociationsRelationships().size());
		a.operationsOverRelationship().removeAssociationRelationship(null);
		assertEquals("Architecture should contain 2 associations", 2,	a.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void shouldRemoveDependecyRelationship() throws Exception{
		Architecture a = givenAArchitecture("dependency");
		DependencyRelationship dp = a.getAllDependencies().get(0);
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
		UsageRelationship usage = a.getAllUsage().get(0);
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
		AssociationClassRelationship associationClass = a.getAllAssociationsClass().get(0);
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
		GeneralizationRelationship g = a.getAllGeneralizations().get(0);
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
		AbstractionRelationship ab = a.getAllAbstractions().get(0);
		assertEquals("Architecture should contain 1 Abstraction", 1,	a.getAllAbstractions().size());
		a.forAbstraction().remove(ab);
		assertEquals("Architecture should contain 0 Abstraction", 0,	a.getAllAbstractions().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAbstractionRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		assertEquals("Architecture should contain 1 Abstraction", 1,	a.getAllAbstractions().size());
		a.forAbstraction().remove(null);
		assertEquals("Architecture should contain 1 Abstraction", 1,	a.getAllAbstractions().size());
	}
	
	@Test
	public void shouldCreateAPackage(){
		assertEquals(1, architecture.getAllPackages().size());
		assertEquals(7, architecture.getAllIds().size());
		
		Package packageTest = architecture.createPackage("myPackage");
		
		assertEquals("generico::myPackage", packageTest.getNamespace());
		
		assertEquals(8, architecture.getAllIds().size());
		
		assertNotNull(packageTest.getId());
		
		assertEquals(2,architecture.getAllPackages().size());
		assertSame(packageTest, architecture.getAllPackages().get(1));
	}
	
	@Test
	public void shouldRemoveAPackage() throws PackageNotFound{
		assertEquals(1, architecture.getAllPackages().size());
		
		Package p = architecture.findPackageByName("Package1");
		assertNotNull(p);
		
		assertEquals(7, architecture.getAllIds().size());
		
		architecture.removePackage(p);
		assertEquals(0,architecture.getAllPackages().size());
		
		assertEquals(5, architecture.getAllIds().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove1() throws Exception{
		Architecture a = givenAArchitecture("testePackageRemove");
		
		assertEquals(1,a.getAllClasses().size());
		assertEquals(0, a.getAllInterfaces().size());
		assertEquals(1, a.getAllClasses().size());
		
		assertEquals(2, a.getAllIds().size());
		
		a.removePackage(a.getAllPackages().get(0));
		
		assertEquals(0, a.getAllClasses().size());
		assertEquals(0, a.getAllClasses().size());
		
		assertEquals(0, a.getAllIds().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove2() throws Exception{
		Architecture a = givenAArchitecture("removePacoteTeste2");
		
		assertEquals(2, a.getAllClasses().size());
		assertEquals(1, a.getAllAssociationsRelationships().size());
		assertEquals(4, a.getAllIds().size());
		
		a.removePackage(a.getAllPackages().get(0));
		
		assertEquals(1, a.getAllClasses().size());
		assertEquals(0, a.getAllAssociationsRelationships().size());
		assertEquals(1, a.getAllIds().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove3() throws Exception{
		Architecture a = givenAArchitecture("removePacote3");
		
		assertEquals(3,a.getAllClasses().size());
		assertEquals(2, a.getAllAssociationsRelationships().size());
		
		assertEquals(2, a.getAllAssociationsRelationships().get(1).getParticipants().size());
		
		a.removePackage(a.getAllPackages().get(0));
		
		assertEquals(1, a.getAllClasses().size());
		assertEquals(0, a.getAllAssociationsRelationships().size());
		
	}
	
	
	@Test
	public void shouldCreateInterface() throws InterfaceNotFound{
		assertEquals(0, architecture.getAllInterfaces().size());
		assertEquals(7, architecture.getAllIds().size());
		Interface interfacee = architecture.createInterface("myInterface");

		assertEquals(8, architecture.getAllIds().size());
		assertEquals("generico::myInterface", interfacee.getNamespace());
		
		assertNotNull(interfacee);
		assertNotNull(interfacee.getId());
		assertEquals(1, architecture.getAllInterfaces().size());
		assertEquals(interfacee, architecture.findInterfaceByName("myInterface"));
	}
	
	@Test
	public void shouldRemoveInterface(){
		Interface interfacee = architecture.createInterface("myInterface");
		
		assertEquals(1, architecture.getAllInterfaces().size());
		
		architecture.removeInterface(interfacee); 
		
		assertEquals(0, architecture.getAllInterfaces().size());
	}
	
	@Test
	public void shouldCreateClass() throws ClassNotFound{
		assertEquals(3, architecture.getAllClasses().size());
		
		assertEquals(7, architecture.getAllIds().size());
		
		Class klass = architecture.createClass("Bar");
		assertEquals("generico::Bar", klass.getNamespace());
		
		assertEquals(4, architecture.getAllClasses().size());
		
		assertEquals(klass, architecture.findClassByName("Bar").get(0));
		assertEquals(8, architecture.getAllIds().size());
	}
	
	@Test
	public void shouldRemoveClass(){
		assertEquals(3, architecture.getAllClasses().size());
		Class klass = architecture.getAllClasses().get(0);
		
		architecture.removeClass(klass);
		assertEquals(2, architecture.getAllClasses().size());
	}
	
	@Test
	public void shouldArchitecuteHaveAListContainAllElementsId(){
		assertEquals(7, architecture.getAllIds().size());	
	}
	
}