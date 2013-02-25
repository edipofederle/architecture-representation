package mestrado.arquitetura.helpers;


import static mestrado.arquitetura.helpers.ElementsTypes.ASSOCIATION;
import static mestrado.arquitetura.helpers.ElementsTypes.CLASS;
import static mestrado.arquitetura.helpers.ElementsTypes.COMMENT;
import static mestrado.arquitetura.helpers.ElementsTypes.DEPENDENCY;
import static mestrado.arquitetura.helpers.ElementsTypes.INTERFACE;
import static mestrado.arquitetura.helpers.ElementsTypes.OPERATION;
import static mestrado.arquitetura.helpers.ElementsTypes.PACKAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

/**
 * Helper para atuar sobre um model ( arquitetura ).
 * 
 * @author edipofederle
 *
 */
public class ModelHelper extends ElementHelper {

	private static Uml2Helper uml2Helper;

	protected ModelHelper() throws ModelNotFoundException, ModelIncompleteException{
		uml2Helper = Uml2HelperFactory.getUml2Helper();
	}

	public List<Classifier> getAllClasses(NamedElement model) {
		return getAllElementsByType(model, CLASS);
	}

	public List<Property> getAllAttributesForAClass(NamedElement aClass) {
		List<Property> allPropertys = new ArrayList<Property>();
		allPropertys = getAllElementsByType(aClass, ElementsTypes.PROPERTY);
		return allPropertys;
	}
	
	public List<Classifier> getAllInterfaces(NamedElement model) {
		return getAllElementsByType(model, INTERFACE);
	}

	public List<Classifier> getAllAssociations(NamedElement model) {
		return getAllElementsByType(model, ASSOCIATION);
	}

	public List<Classifier> getAllDependencies(NamedElement model) {
		return getAllElementsByType(model, DEPENDENCY);
	}

	public List<Classifier> getAllPackages(NamedElement model) {
		return getAllElementsByType(model, PACKAGE);
	}

	public List<Classifier> getAllComments(NamedElement model) {
		return getAllElementsByType(model, COMMENT);
	}

	public List<EList<Classifier>> getAllGeneralizations(NamedElement model) {
		List<Classifier> allClasses = getAllClasses(model);
		List<EList<Classifier>> a = new ArrayList<EList<Classifier>>();
		for (NamedElement classImpl : allClasses) {
			if (!((Classifier) classImpl).getGeneralizations().isEmpty())
				a.add((((Classifier) classImpl).getGenerals()));
		}
		return a;
	}

	public String getName(String xmiFile) throws ModelNotFoundException {
		if (modelExists(xmiFile))
			return new File(xmiFile).getName().split("\\.")[0];
		throw new ModelNotFoundException("Model " + xmiFile + " not found");
	}

	public Package getModel(String xmiFile) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
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

	public List<Operation> getAllMethods(NamedElement model) {
		return getAllElementsByType(model, OPERATION);
	}

}