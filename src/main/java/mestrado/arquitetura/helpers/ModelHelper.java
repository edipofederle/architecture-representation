package mestrado.arquitetura.helpers;


import static mestrado.arquitetura.helpers.ElementsTypes.ASSOCIATION;
import static mestrado.arquitetura.helpers.ElementsTypes.CLASS;
import static mestrado.arquitetura.helpers.ElementsTypes.COMMENT;
import static mestrado.arquitetura.helpers.ElementsTypes.DEPENDENCY;
import static mestrado.arquitetura.helpers.ElementsTypes.INTERFACE;
import static mestrado.arquitetura.helpers.ElementsTypes.OPERATION;
import static mestrado.arquitetura.helpers.ElementsTypes.PACKAGE;
import static mestrado.arquitetura.helpers.ElementsTypes.REALIZATION;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;

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
	
	/**
	 * Recupera Classes de um pacote.
	 * @param model
	 * @return
	 */
	public List<Classifier> getClasses(NamedElement model) {
		return getAllElementsByType(model, CLASS);
	}
	
	public List<Classifier> getAllClasses(NamedElement model) {
		List<Classifier> classes = new ArrayList<Classifier>();
		
		List<Classifier> pacotes  = getAllPackages(model);
		
		classes.addAll(getClasses(model));
		
		for (int i = 0; i < pacotes.size(); i++)
			classes.addAll(getAllClassesOfPackage((Package)pacotes.get(i)));
		
		return classes;
	}
	
	
	public List<Property> getAllAttributesForAClass(NamedElement aClass) {
		List<Property> allPropertys = new ArrayList<Property>();
		allPropertys = getAllElementsByType(aClass, ElementsTypes.PROPERTY);
		
		return allPropertys;
	}
	
	public List<Classifier> getAllInterfaces(NamedElement model) {
		return getAllElementsByType(model, INTERFACE);
	}

	public List<Association> getAllAssociations(NamedElement model) {
		return getAllElementsByType(model, ASSOCIATION);
	}

	public List<Dependency> getAllDependencies(NamedElement model) {
		return getAllElementsByType(model, DEPENDENCY);
	}

	public List<Classifier> getAllPackages(NamedElement model) {
		return getAllElementsByType(model, PACKAGE);
	}

	public List<Classifier> getAllComments(NamedElement model) {
		return getAllElementsByType(model, COMMENT);
	}
	
	public List<Realization> loadRealizations(Package model) {
		return getAllElementsByType(model, REALIZATION);
	}

	public List<EList<Generalization>> getAllGeneralizations(NamedElement model) {
		
		List<Classifier> allClasses = getAllClasses(model);
		List<EList<Generalization>> lista = new ArrayList<EList<Generalization>>() ;
		
		for (NamedElement classImpl : allClasses) {
			if (!((Classifier) classImpl).getGeneralizations().isEmpty()){
				EList<Generalization> g = ((Classifier) classImpl).getGeneralizations();
				lista.add(g);
			}
		}
		
		return lista;
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
		if (model.exists()) return true;
		
		return false;
	}

	public List<Operation> getAllMethods(NamedElement model) {
		return getAllElementsByType(model, OPERATION);
	}
	
	/**
	 * Retorna todas as variabilidades de um modelo.
	 * 
	 * Essas variabilidades estão em elementos do tipo {@link Comment}
	 * 
	 * @param model
	 * @return List<{@link Comment}>
	 */
	public List<Comment> getAllVariabilities(Package model) {
		List<Comment> variabilities = new ArrayList<Comment>();
		List<Classifier> classes = new ArrayList<Classifier>();
		
		classes = getAllClasses(model);
		
		for (int i = 0; i < classes.size(); i++) {
			if(StereotypeHelper.isVariability(classes.get(i)))
				variabilities.add(StereotypeHelper.getCommentVariability(classes.get(i)));
		}
		
		if(!variabilities.isEmpty()) return variabilities;
		return Collections.emptyList();
	}
	
	/**
	 * Método recursivo que recuperar todas as classes de todos os pacotes e subpacotes.
	 * 
	 * @param pacote
	 * @return
	 */
	private List<Classifier> getAllClassesOfPackage(Package pacote) {
		List<Classifier> classes = new ArrayList<Classifier>();
		
		classes.addAll(getClasses(pacote));
		List<Classifier> a = getAllPackages((Package)pacote);
		for (int i = 0; i < a.size(); i++) 
			classes.addAll(getAllClassesOfPackage((Package)a.get(i)));
		
		return classes;
	}

	public String getXmiId (EObject eObject) {
		Resource xmiResource = eObject.eResource();
		if (xmiResource == null ) return null;
		return ((XMLResource) xmiResource).getID(eObject);
	}



}