package mestrado.arquitetura.helpers.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junitx.framework.Assert;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;
import mestrado.arquitetura.representation.Class;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

public abstract class TestHelper {

	protected static Uml2Helper uml2Helper;
	protected static ModelHelper modelHelper;

	static{
		try {
			uml2Helper = Uml2HelperFactory.getUml2Helper();
		} catch (ModelNotFoundException e) {
			// TODO AJUSTAR
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			// TODO AJUSTAR
			e.printStackTrace();
		}
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
		
	}

	public static Package givenAModel(String modelName) throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion {
		//TODO Move from Here, problemas dos estereotipos
		String uri = getUrlToModel(modelName);
		String absolutePath = new File(uri).getAbsolutePath();
		Package epo2Model = uml2Helper.load(absolutePath);
		return epo2Model;
	}

	public static NamedElement givenAClass() throws ModelNotFoundException  , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Package content = givenAModel("ExtendedPO2");
		List<Classifier> elementsClass = modelHelper.getAllClasses(content);
		return elementsClass.get(0);
	}
	/**
	 * Retorna URI para um resource dentro de "src/test/java/resources/"
	 * @param modelName. ex: "meuModel" ( sem a extensao do arquivo ).
	 * @return uri String
	 */
	protected static String getUrlToModel(String modelName) {
		
		return new File("src/test/java/resources/" + modelName + ".uml").getAbsolutePath(); 
		
	}

	protected static String getUriToResource(String resourcesNamee) {
		return URI.createFileURI(getUrlToModel(resourcesNamee)).toString();
	}
	
	public static Stereotype getStereotypeByName(String name) throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Package perfil = givenAModel("smartyProfile");
		return  perfil.getOwnedStereotype(name);
	}
	
	//Custom asserts
	protected <T> void assertContains(List<T> list, String expected) {
		for (T t : list) {
			if(list.get(0) instanceof mestrado.arquitetura.representation.Package){
				String name = ((mestrado.arquitetura.representation.Package) t).getName();
				if(expected.equalsIgnoreCase(name))
					return ;
			}
		}
		Assert.fail("list there is no element called " + expected);
	}
	
	protected void hasClassesNames(mestrado.arquitetura.representation.Package pkg, String ... names){
		List<Class> klasses = pkg.getClasses();	
		List<String> namesKlasses = new ArrayList<String>();
		for (Class name : klasses) 
			namesKlasses.add(name.getName());
		
		Assert.assertTrue(namesKlasses.containsAll(Arrays.asList(names)));
	}

}