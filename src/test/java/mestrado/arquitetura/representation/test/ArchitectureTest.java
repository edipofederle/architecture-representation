package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ClassNotFound;
import mestrado.arquitetura.exceptions.InterfaceNotFound;
import mestrado.arquitetura.exceptions.PackageNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Concern;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Interface;
import mestrado.arquitetura.representation.Package;
import mestrado.arquitetura.representation.Variability;
import mestrado.arquitetura.representation.VariantType;
import mestrado.arquitetura.representation.relationship.AbstractionRelationship;
import mestrado.arquitetura.representation.relationship.AssociationClassRelationship;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.junit.Before;
import org.junit.Test;

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
	public void shouldArchitectureHaveAName(){
		assertEquals("arquitetura", arch.getName());
	}
	
	@Test
	public void shouldArchitectureHaveAEmptyNameWhenNull(){
		arch.setName(null);
		assertEquals("", arch.getName());
	}
	
	@Test
	public void shouldCreateConcernWhenNotExists(){
		assertEquals("core", arch.getOrCreateConcernByName("core").getName());
		assertEquals(1,arch.getConcerns().size());
	}
	
	@Test
	public void shouldNotCreateConcernWhenExists(){
		assertEquals(0, arch.getConcerns().size());
		assertEquals("core", arch.getOrCreateConcernByName("core").getName());
		assertEquals(1, arch.getConcerns().size());
		
		Concern concern = arch.getOrCreateConcernByName("core");
		assertSame(arch.getConcerns().get("core"), concern);
	}
	
	@Test
	public void shouldReturnAllPackages(){
		Package pkg = new Package(arch, "Pacote", false, VariantType.MANDATORY, null, "","id");
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
		Class klass = new Class(arch, "Klass", false,  VariantType.MANDATORY, false, null, "", "namespace","id");
		arch.getElements().add(klass);
		
		assertEquals(1, arch.getAllClasses().size());
		assertEquals("Klass", arch.getAllClasses().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoClasses(){
		assertEquals(Collections.emptyList(), arch.getAllClasses());
	}
	
	@Test
	public void shouldReturnVariabilities(){
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("name", "var");
		Variability v = new Variability("var", "1", "1", false, attributes, null);
		arch.getVariabilities().add(v);
		
		assertNotNull(arch.getVariabilities().get(0));
		assertEquals("var", arch.getVariabilities().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoVariabilities(){
		assertEquals(Collections.emptyList(), arch.getVariabilities());
	}
	
	@Test
	public void shouldReturnNullWhenElementNotFound(){
		assertNull(arch.findElementByName("KlassNotFound"));
	}
	
	@Test
	public void shouldReturnElementClassByName(){
		arch.getElements().add(new Class(arch, "Klass", false,  VariantType.MANDATORY, false, null, "", "namespace", "id"));
		Element klass = arch.findElementByName("klass");
		
		assertNotNull(klass);
		assertEquals("Klass", klass.getName());
	}
	
	@Test
	public void shouldReturnElementPackageByName(){
		arch.getElements().add( new Package(arch, "Pacote", false, VariantType.MANDATORY, null, "", "id"));
		Element pkg = arch.findElementByName("Pacote");
		
		assertNotNull(pkg);
		assertEquals("Pacote", pkg.getName());
	}
	
	@Test
	public void shouldReturnAllInterfaces(){
		arch.getElements().add(new Class(arch, "Klass1", false,  VariantType.MANDATORY, false, null, "", "namespace", "id"));
		arch.getElements().add(new Interface(arch, "Interface1", false, VariantType.MANDATORY, null, "namesapce","id"));
		arch.getElements().add(new Interface(arch, "Interface2", false, VariantType.MANDATORY, null, "namesapce","id"));
		
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
		assertEquals(1, architecture.getAllAssociations().size());
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
		assertEquals("Class3", architecture.findElementByName("CLass3").getName());
	}
	
	@Test(expected=ClassNotFound.class)
	public void shouldRaiseClassNotFoundExceptionWhenClassNotFound() throws ClassNotFound{
		architecture.findClassByName("noClass");
	}
	
	@Test
	public void shouldFindInterfaceByName() throws Exception{
		Architecture a = givenAArchitecture("classes");
		assertNotNull(a.findInterfaceByName("interface"));
		assertEquals("Interface", a.findInterfaceByName("interface").getName());
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
	public void shouldRemoveAssociationRelationship() throws Exception{
		Architecture a = givenAArchitecture("association");
		AssociationRelationship as = a.getAllAssociations().get(0);
		assertEquals("Architecture should contain 4 associations", 4, a.getAllAssociations().size());
		assertEquals(8, a.getAllClasses().size());
		a.removeAssociationRelationship(as);
		assertEquals("Architecture should contain 3 associations", 3, a.getAllAssociations().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAssociationRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("association");
		assertEquals("Architecture should contain 4 associations", 4,	a.getAllAssociations().size());
		a.removeAssociationRelationship(null);
		assertEquals("Architecture should contain 4 associations", 4,	a.getAllAssociations().size());
	}
	
	@Test
	public void shouldRemoveDependecyRelationship() throws Exception{
		Architecture a = givenAArchitecture("dependency");
		DependencyRelationship dp = a.getAllDependencies().get(0);
		assertEquals("Architecture should contain 5 dependency", 5,	a.getAllDependencies().size());
		a.removeDependencyRelationship(dp);
		assertEquals("Architecture should contain 4 dependency", 4,	a.getAllDependencies().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveDependencyRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("dependency");
		assertEquals("Architecture should contain 5 dependency", 5,	a.getAllDependencies().size());
		a.removeDependencyRelationship(null);
		assertEquals("Architecture should contain 4 dependency", 5,	a.getAllDependencies().size());
	}
	
	@Test
	public void shouldRemoveUsageRelationship() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		UsageRelationship usage = a.getAllUsage().get(0);
		assertEquals("Architecture should contain 1 usage", 1,	a.getAllUsage().size());
		a.removeUsageRelationship(usage);
		assertEquals("Architecture should contain 0 usage", 0,	a.getAllUsage().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveUsageRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		assertEquals("Architecture should contain 1 usage", 1,	a.getAllUsage().size());
		a.removeUsageRelationship(null);
		assertEquals("Architecture should contain 0 usage", 1,	a.getAllUsage().size());
	}
	
	@Test
	public void shouldRemoveAssociationClassRelationship() throws Exception{
		Architecture a = givenAArchitecture("associationClass");
		AssociationClassRelationship associationClass = a.getAllAssociationsClass().get(0);
		assertEquals("Architecture should contain 1 AssociationClass", 1,	a.getAllAssociationsClass().size());
		a.removeAssociationClass(associationClass);
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
		a.removeGeneralizationRelationship(g);
		assertEquals("Architecture should contain 2 generalization", 2,	a.getAllGeneralizations().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveGeneralizationRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("generalizationArch");
		assertEquals("Architecture should contain 3 generalization", 3,	a.getAllGeneralizations().size());
		a.removeGeneralizationRelationship(null);
		assertEquals("Architecture should contain 2 generalization", 3,	a.getAllGeneralizations().size());
	}
	
	@Test
	public void shouldRemoveAbstractionRelationship() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		AbstractionRelationship ab = a.getAllAbstractions().get(0);
		assertEquals("Architecture should contain 1 Abstraction", 1,	a.getAllAbstractions().size());
		a.removeAbstractionRelationship(ab);
		assertEquals("Architecture should contain 0 Abstraction", 0,	a.getAllAbstractions().size());
	}
	
	@Test
	public void shouldNotChangeListWhenTryRemoveAbstractionRelationshipNotExist() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		assertEquals("Architecture should contain 1 Abstraction", 1,	a.getAllAbstractions().size());
		a.removeAbstractionRelationship(null);
		assertEquals("Architecture should contain 1 Abstraction", 1,	a.getAllAbstractions().size());
	}
	
	@Test
	public void shouldAddConcerns(){
		List<Package> packages = architecture.getAllPackages();
		assertEquals("Number of concerns should be 0", 0, architecture.getConcerns().size());
		
		packages.get(0).addConcern("concern1");
		assertNotNull(architecture.getOrCreateConcernByName("concern1"));
		assertEquals("Number of concerns should be 1", 1, architecture.getConcerns().size());
		
		architecture.getAllClasses().get(0).addConcern("teste1");
		architecture.getAllClasses().get(1).addConcern("teste2");
		
		assertEquals("Number of concerns should be 3", 3 , architecture.getConcerns().size());
		assertNotNull(architecture.getOrCreateConcernByName("teste1"));
		assertNotNull(architecture.getOrCreateConcernByName("teste2"));
	}
	
	@Test
	public void shouldCreateAPackage(){
		assertEquals(1,architecture.getAllPackages().size());
		assertEquals(5, architecture.getAllIds().size());
		
		Package packageTest = architecture.createPackage("myPackage");
		
		assertEquals(5, architecture.getAllIds().size());
		
		assertNotNull(packageTest.getId());
		
		assertEquals(2,architecture.getAllPackages().size());
		assertSame(packageTest, architecture.getAllPackages().get(1));
	}
	
	@Test
	public void shouldRemoveAPackage() throws PackageNotFound{
		assertEquals(1,architecture.getAllPackages().size());
		
		Package p = architecture.findPackageByName("Package1");
		assertNotNull(p);
		
		assertEquals(5, architecture.getAllIds().size());
		
		architecture.removePackage(p);
		assertEquals(0,architecture.getAllPackages().size());
		
		assertEquals(4, architecture.getAllIds().size());
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
		
		//assertEquals(0, a.getAllIds().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove2() throws Exception{
		Architecture a = givenAArchitecture("removePacoteTeste2");
		
		assertEquals(2, a.getAllClasses().size());
		assertEquals(1, a.getAllAssociations().size());
		
		a.removePackage(a.getAllPackages().get(0));
		
		assertEquals(1, a.getAllClasses().size());
		assertEquals(0, a.getAllAssociations().size());
	}
	
	@Test
	public void shouldRemoveAllElementRelatedWithPackageWhenPackageRemove3() throws Exception{
		Architecture a = givenAArchitecture("removePacote3");
		
		assertEquals(3,a.getAllClasses().size());
		assertEquals(2, a.getAllAssociations().size());
		
		assertEquals(2, a.getAllAssociations().get(1).getParticipants().size());
		
		a.removePackage(a.getAllPackages().get(0));
		
		assertEquals(1, a.getAllClasses().size());
		assertEquals(0, a.getAllAssociations().size());
		
	}
	
	
	@Test
	public void shouldCreateInterface() throws InterfaceNotFound{
		assertEquals(0, architecture.getAllInterfaces().size());
		Interface interfacee = architecture.createInterface("myInterface");
		
		assertNotNull(interfacee);
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
		
		Class klass = architecture.createClass("Bar");
		
		assertEquals(4, architecture.getAllClasses().size());
		
		assertEquals(klass, architecture.findClassByName("Bar"));
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
		assertEquals(5, architecture.getAllIds().size());	
	}
	
}