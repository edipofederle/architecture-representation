package mestrado.arquitetura.helpers.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import junitx.framework.Assert;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;
import mestrado.arquitetura.parser.ReaderConfig;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

/**
 * 
 * @author edipofederle
 *
 */
public abstract class TestHelper {

	protected static Uml2Helper uml2Helper;
	protected static ModelHelper modelHelper;
	protected static String targetDir;
	protected static String targetDirExport;
	
	static{
		try {
			uml2Helper = Uml2HelperFactory.getUml2Helper();
			
			targetDir = ReaderConfig.getDirTarget();
			targetDirExport = ReaderConfig.getDirExportTarget();
			
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
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
		String uri = getUrlToModel(modelName);
		String absolutePath = new File(uri).getAbsolutePath();
		Package epo2Model = uml2Helper.load(absolutePath);
		return epo2Model;
	}

	public static NamedElement givenAClass() throws ModelNotFoundException  , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Package content = givenAModel("ExtendedPO2");
		List<org.eclipse.uml2.uml.Class> elementsClass = modelHelper.getAllClasses(content);
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
	
	protected Architecture givenAArchitecture(String name) throws Exception {
		String uriToArchitecture6 = getUrlToModel(name);
		return  new ArchitectureBuilder().create(uriToArchitecture6);
	}
	
	protected Architecture givenAArchitecture2(String name) throws Exception {
		String uriToArchitecture6 = getUrlToModelManipulation(name);
		return new ArchitectureBuilder().create(uriToArchitecture6);
	}
	
	protected static String getUrlToModelManipulation(String modelName) {
		return  targetDirExport+modelName+".uml";
	}

	protected <T extends Element> void assertContains(List<T> list, String... expected) {
		requiredElements: for (String requiredElementName : expected)  {
				for (Element element : list)
					if (requiredElementName.equalsIgnoreCase(element.getName())) continue requiredElements;
				Assert.fail("list there is no element called " + requiredElementName);
			}
		}
	protected void hasClassesNames(mestrado.arquitetura.representation.Package pkg, String ... names){
		List<Element> klasses = pkg.getClasses();	
		List<String> namesKlasses = new ArrayList<String>();
		for (Element name : klasses) 
			namesKlasses.add(name.getName());
		
		Assert.assertTrue(namesKlasses.containsAll(Arrays.asList(names)));
	}
	
	protected boolean modelContainId(String modelName, String id){
		File file = new File(getUrlToModelManipulation(modelName));
		try {
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
			    String line = scanner.nextLine();
			    if(line != null)
				    if(line.contains(id))
				        return true;
			}
		}catch(FileNotFoundException e) { } //TODO
		return false;
	}

}