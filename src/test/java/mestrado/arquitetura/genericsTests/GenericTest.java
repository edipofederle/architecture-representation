package mestrado.arquitetura.genericsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.DependencyInterClassRelationship;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.junit.Test;

/**
 * 
 * @author edipofederle
 *
 */
public class GenericTest extends TestHelper {
	
	
	@Test
	public void shouldApplyStereotypePersistense() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("interface");
		
		NamedElement klass = modelHelper.getAllClasses(model).get(0);
		Profile profileConcern = (Profile) givenAModel("perfilConcerns.profile");
		model.applyProfile(profileConcern);
		assertNotNull(klass);
		assertEquals("myInterface", klass.getName());
		
		assertFalse("Nao deve possuir concern", StereotypeHelper.hasConcern(klass));
		
		
		Stereotype concern = profileConcern.getOwnedStereotype("Persistence");
		assertEquals("Persistence", concern.getName());
		assertNotNull(concern);
		
		klass.applyStereotype(concern);
		
		assertTrue("Deve possuir concern", StereotypeHelper.hasConcern(klass));
	}
	
	
	@Test
	public void shouldLoadDependencyInterClassWitoutPackageAndClassWithPackage() throws Exception{
		String uriToArchitecture = getUrlToModel("classPacote");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
		assertNotNull(architecture);
		assertEquals(2, architecture.getClasses().size());
		assertEquals(1, architecture.getInterClassRelationships().size());
		
		DependencyInterClassRelationship r = (DependencyInterClassRelationship) architecture.getInterClassRelationships().get(0);
		
		assertNotNull(r);
		assertEquals("Class1", r.getClient().getName());
		assertEquals("Class2", r.getSupplier().getName());
		
		assertEquals("model", r.getSupplier().getNamespace());
		assertEquals("model::Package1", r.getClient().getNamespace());
		
	}
	
}