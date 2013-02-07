package mestrado.arquitetura.helpers;

import static mestrado.arquitetura.helpers.ElementsTypes.ASSOCIATION;
import static mestrado.arquitetura.helpers.ElementsTypes.CLASS;
import static mestrado.arquitetura.helpers.ElementsTypes.COMMENT;
import static mestrado.arquitetura.helpers.ElementsTypes.DEPENDENCY;
import static mestrado.arquitetura.helpers.ElementsTypes.INTERFACE;
import static mestrado.arquitetura.helpers.ElementsTypes.PACKAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;

public class ModelHelper extends ElementHelper {

	private Uml2Helper uml2Helper;

	protected ModelHelper(){
		uml2Helper = Uml2HelperFactory.getUml2Helper();
	}

	public List<Classifier> getAllClasses(Package model) {
		return getAllElementsByType(model, CLASS);
	}

	public List<Classifier> getAllInterfaces(Package model) {
		return getAllElementsByType(model, INTERFACE);
	}

	public List<Classifier> getAllAssociations(Package model) {
		return getAllElementsByType(model, ASSOCIATION);
	}

	public List<Classifier> getAllDependencies(Package model) {
		return getAllElementsByType(model, DEPENDENCY);
	}

	public List<Classifier> getAllPackages(Package model) {
		return getAllElementsByType(model, PACKAGE);
	}

	public List<Classifier> getAllComments(Package model) {
		return getAllElementsByType(model, COMMENT);
	}

	public List<EList<Classifier>> getAllGeneralizations(Package model) {
		List<Classifier> allClasses = getAllClasses(model);
		List<EList<Classifier>> a = new ArrayList<EList<Classifier>>();
		for (Classifier classImpl : allClasses) {
			if (!classImpl.getGeneralizations().isEmpty())
				a.add((classImpl.getGenerals()));
		}
		return a;
	}

	public String getName(String xmiFile) throws ModelNotFoundException {
		if (modelExists(xmiFile))
			return new File(xmiFile).getName().split("\\.")[0];
		throw new ModelNotFoundException("Model " + xmiFile + " not found");
	}

	public Package getModel(String xmiFile) throws ModelNotFoundException, ModelIncompleteException {
		if (modelExists(xmiFile))
			return uml2Helper.load(URI.createURI(xmiFile).toString());
		throw new ModelNotFoundException("Model " + xmiFile + " not found");
	}

	private boolean modelExists(String xmiFile) {
		File model = new File(xmiFile);
		if (model.exists())
			return true;
		return false;
	}

}