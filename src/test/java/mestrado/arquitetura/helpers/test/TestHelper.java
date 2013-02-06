package mestrado.arquitetura.helpers.test;

import java.io.File;
import java.util.List;

import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

public abstract class TestHelper {

	protected static Uml2Helper uml2Helper;
	protected static ModelHelper modelHelper;

	static{
		uml2Helper = Uml2HelperFactory.getUml2Helper();
		modelHelper = ModelHelperFactory.getModelHelper();
		
	}

	public static Package givenAModel(String modelName) {
		//TODO Move from Here, problemas dos estereotipos
		String uri = getUrlToModel(modelName);
		String absolutePath = new File(uri).getAbsolutePath();
		Package epo2Model = uml2Helper.load(URI.createURI(uri), absolutePath);
		return epo2Model;
	}

	public static Classifier givenAClass() {
		Package content = givenAModel("ExtendedPO2");
		List<Classifier> elementsClass = modelHelper.getAllClasses(content);
		return elementsClass.get(0);
	}

	protected static String getUrlToModel(String modelName) {
		return "src/test/java/resources/" + modelName + ".uml";
	}

	protected URI getUriToResource(String resourcesNamee) {
		return URI.createFileURI(getUrlToModel(resourcesNamee));
	}
	
	public static Stereotype getStereotypeByName(String name){
		Package perfil = givenAModel("smartyProfile");
		return  perfil.getOwnedStereotype(name);
		
	}

}