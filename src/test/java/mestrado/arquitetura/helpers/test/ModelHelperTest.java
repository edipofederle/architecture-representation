package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.helpers.ModelNotFoundException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.junit.Test;

public class ModelHelperTest extends TestHelper {
	
	
	
	@Test
	public void shouldReturnAllClasses() throws ModelNotFoundException {
		Package content = modelHelper.getModel(getUrlToModel("ExtendedPO2"));
		List<Classifier> elementsClass = modelHelper.getAllClasses(content);
		assertEquals(10, elementsClass.size());
	}

	@Test
	public void shouldReturnAllInterfaces() throws ModelNotFoundException {
		Package content = modelHelper.getModel(getUrlToModel("ExtendedPO2"));
		List<Classifier> elementsInterfaces = modelHelper.getAllInterfaces(content);
		assertEquals(1, elementsInterfaces.size());
		assertEquals("myInterface", elementsInterfaces.get(0).getName());
	}

	@Test
	public void shouldReturnAllAssociations() throws ModelNotFoundException {
		Package content = modelHelper.getModel(getUrlToModel("ExtendedPO2"));
		List<Classifier> elementsAssociations = modelHelper.getAllAssociations(content);
		assertEquals(8, elementsAssociations.size());
	}
	
	@Test
	public void shouldReturnAllDependency() throws ModelNotFoundException{
		Package content = modelHelper.getModel(getUrlToModel("ExtendedPO2"));
		List<Classifier> dependency = modelHelper.getAllDependencies(content);
		assertEquals(1, dependency.size());
	}
	
	@Test
	public void shouldReturnAllPackages() throws ModelNotFoundException{
		Package content = modelHelper.getModel(getUrlToModel("ExtendedPO2"));
		List<Classifier> packages = modelHelper.getAllPackages(content);
		assertEquals(1, packages.size());
	}
	
	@Test
	public void shouldReturnTwoClassesForAPackage() throws ModelNotFoundException{
		Package p = modelHelper.getModel(getUrlToModel("package"));
		List<Classifier> packages = modelHelper.getAllPackages(p);
		assertEquals("Package1", ((Package)packages.get(0)).getName());
		Package p1 = (Package) packages.get(0);
		List<Classifier> classesForPackage = modelHelper.getAllClasses(p1);
		assertEquals(2, classesForPackage.size());
		ClassImpl class1 = (ClassImpl) classesForPackage.get(0);
		ClassImpl class2 = (ClassImpl) classesForPackage.get(1);
		assertEquals("Class1", class1.getName());
		assertEquals("Class2", class2.getName());
	}

	@Test
	public void shouldReturnAllComments() throws ModelNotFoundException{
		Package content = modelHelper.getModel(getUrlToModel("ExtendedPO2"));
		List<Classifier> comments = modelHelper.getAllComments(content);
		assertEquals(1, comments.size());
	}
	
	@Test
	public void shouldReturnAllGeneralizations() throws ModelNotFoundException{
		Package content = modelHelper.getModel(getUrlToModel("generalization"));
		List<EList<Classifier>> generalizations = modelHelper.getAllGeneralizations(content);
		assertEquals(3, generalizations.size());
	}
	
	@Test
	public void shouldReturnModelName() throws ModelNotFoundException{
		assertEquals("model12", modelHelper.getName(getUrlToModel("model12")));
	}
	
	@Test(expected=ModelNotFoundException.class)
	public void shouldReturnModelNotFoundWhenFileDontExists() throws ModelNotFoundException{
		modelHelper.getName("/not/found/path/model.uml");
	}
	
	@Test
	public void shouldReturnAModel() throws ModelNotFoundException{
		Package model = modelHelper.getModel(getUrlToModel("model"));
		assertNotNull(model);
	}
	
	@Test(expected=ModelNotFoundException.class)
	public void shouldReturnModelNotFoundmodelDontExists() throws ModelNotFoundException{
		modelHelper.getModel("/not/found/path/model.uml");
	}
}