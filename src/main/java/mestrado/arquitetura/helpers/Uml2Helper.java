
package mestrado.arquitetura.helpers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import mestrado.arquitetura.base.Base;
import mestrado.arquitetura.helpers.test.UtilResources;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * @author edipofederle
 */

public class Uml2Helper extends Base {
	
	private static final boolean PRINT_LOGS = false;
	private static Package profile;
	private static Uml2Helper instance;

	
	public static Uml2Helper getInstance() throws ModelNotFoundException, ModelIncompleteException{
		if (instance == null){
			instance = new Uml2Helper();
		}
		return instance;
	}

	public Profile createProfile(String name) {
		Profile profile = UMLFactory.eINSTANCE.createProfile();
		profile.setName(name);

		printLog("Profile '" + profile.getQualifiedName()
				+ "' created.");

		return profile;
	}

	public void saveResources(org.eclipse.uml2.uml.Package package_, URI uri)
			throws IOException {
		ArrayList<EObject> contents = new ArrayList<EObject>();
		contents.add(package_);
		save(contents, uri);
	}

	private void save(Collection<EObject> contents, URI uri) throws IOException {
		URI finalUri = uri.appendFileExtension(UMLResource.FILE_EXTENSION);
		Resource resource = getResources().createResource(finalUri);
		resource.getContents().addAll(contents);
		resource.save(null);
	}

	public org.eclipse.uml2.uml.Generalization createGeneralization(
			Classifier child, Classifier parent) {
		return child.createGeneralization(parent);
	}

	public org.eclipse.uml2.uml.Class createClass(
			org.eclipse.uml2.uml.Package nestingPackage, String name,
			boolean isAbstract) {
		org.eclipse.uml2.uml.Class createdClass = nestingPackage
				.createOwnedClass(name, isAbstract);
		return createdClass;
	}

	public org.eclipse.uml2.uml.Package createPackage(
			org.eclipse.uml2.uml.Package nestingPackage, String name) {
		org.eclipse.uml2.uml.Package createdPackage = nestingPackage
				.createNestedPackage(name);

		printLog("Package '" + createdPackage.getQualifiedName() + "' created.");

		return createdPackage;
	}

	public Model createModel(String name) {
		Model model = UMLFactory.eINSTANCE.createModel();
		model.setName(name);

		printLog("Model '" + model.getQualifiedName() + "' created.");

		return model;
	}

	public void err(String error) {
		System.out.println(error);
	}

	public org.eclipse.uml2.uml.Class referenceMetaclass(Profile profile,
			String name) throws ModelNotFoundException {
		Model umlMetamodel = (Model) getInternalResources(URI.createURI(UMLResource.UML_METAMODEL_URI));

		org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(name);

		profile.createMetaclassReference(metaclass);

		printLog("Metaclass '" + metaclass.getQualifiedName()
				+ "' referenced.");

		return metaclass;
	}

	public Extension createExtension(org.eclipse.uml2.uml.Class metaclass,
			Stereotype stereotype, boolean required) {
		Extension extension = stereotype.createExtension(metaclass, required);

		printLog((required ? "Required extension '" : "Extension '")
				+ extension.getQualifiedName() + "' created.");

		return extension;
	}

	public Property createAttribute(org.eclipse.uml2.uml.Class class_,
			String name, Type type, int lowerBound, int upperBound) {
		Property attribute = class_.createOwnedAttribute(name, type,
				lowerBound, upperBound);

		StringBuffer sb = new StringBuffer();

		sb.append("Attribute '");

		sb.append(attribute.getQualifiedName());

		sb.append("' : ");

		sb.append(type.getQualifiedName());

		sb.append(" [");
		sb.append(lowerBound);
		sb.append("..");
		sb.append(LiteralUnlimitedNatural.UNLIMITED == upperBound ? "*"
				: String.valueOf(upperBound));
		sb.append("]");

		sb.append(" created.");

		printLog(sb.toString());

		return attribute;
	}

	public Enumeration createEnumeration(org.eclipse.uml2.uml.Package package_,
			String name) {

		Enumeration enumeration = (Enumeration) package_.createOwnedEnumeration(name);
		printLog("Enumeration '" + enumeration.getQualifiedName()+ "' created.");

		return enumeration;
	}

	public EnumerationLiteral createEnumerationLiteral(Enumeration enumeration, String name) {
		EnumerationLiteral enumerationLiteral = enumeration
				.createOwnedLiteral(name);

		printLog("Enumeration literal '"
				+ enumerationLiteral.getQualifiedName() + "' created.");

		return enumerationLiteral;
	}

	public org.eclipse.uml2.uml.Package load(String pathAbsolute) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
				
		File file = new File(pathAbsolute);
		FilenameFilter filter = new OnlyCompleteResources();
		
		if(fileExists(file)){
			File dir = file.getParentFile();
			String resourcesName = pathAbsolute.substring(pathAbsolute.lastIndexOf("/")+1, pathAbsolute.length()-4);
			
			if (isCompleteResources(filter, dir, resourcesName)) throw new ModelIncompleteException("Modelo Incompleto");
			
			Package model = getExternalResources(pathAbsolute);
			
			if(hasSMartyProfile(model))  return model; 

			if(model.eClass().equals(UMLPackage.Literals.PROFILE)){
				if (!((Profile) model).isDefined())
					((Profile) model).define();
				return model; 
			}
			
			throw new SMartyProfileNotAppliedToModelExcepetion("Profile SMarty Nao incluido no modelo." );
		}
		
		throw new ModelNotFoundException("Nao encontrado");
	}

	//TODO Refatorar. Considera um modelo com um único perfil
	// e o mesmo como sendo o SMArty.
	private boolean hasSMartyProfile(Package model) {
		EList<Profile> profiles = model.getAppliedProfiles();
		for (Profile profile : profiles) {
			if (profile.eClass().equals(UMLPackage.Literals.PROFILE)) return true;
		}
		return false;
	}


	public PackageableElement getEnumerationByName(Profile profile, String name)
			throws EnumerationNotFoundException {
		EList<PackageableElement> a = profile.getPackagedElements();
		for (PackageableElement packageableElement : a) {
			if (packageableElement.eClass().equals(UMLPackage.Literals.ENUMERATION)
					&& packageableElement.getName().equalsIgnoreCase(name))
				return packageableElement;
		}

		throw new EnumerationNotFoundException(name);
	}

	public Type getPrimitiveType(String typeName) throws ModelNotFoundException {
		Package umlPrimitiveTypes = getInternalResources(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
		return umlPrimitiveTypes.getOwnedType(UtilResources.capitalize(typeName));
	}
	
	public Stereotype createStereotype(Profile prof, String name,
			boolean isAbstract) {
		Stereotype stereotype = prof.createOwnedStereotype(name, isAbstract);
		return stereotype;
	}

	public PackageableElement getStereotypeByName(Profile prof, String name)
			throws StereotypeNotFoundException {
		EList<PackageableElement> a = prof.getPackagedElements();
		for (PackageableElement packageableElement : a) {
			if (packageableElement instanceof Stereotype
					&& packageableElement.getName().equals(name))
				return packageableElement;
		}
		throw new StereotypeNotFoundException(name);
	}

	public void applyProfile(org.eclipse.uml2.uml.Package package_,
			Profile profile) {

		try {
			package_.applyProfile(profile);
			printLog("Profile '" + profile.getQualifiedName()
					+ "' applied to package '" + package_.getQualifiedName()
					+ "'.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static boolean fileExists(File file) {
		return file.exists();
	}
	
	private void printLog(String message){
		if(PRINT_LOGS)
			System.out.println(message);
	}
	
	/**
	 * Carrega um recurso interno. Recurso interno pode ser entendido como por exemplo 
	 * o meta modelo uml, tipos primitivos etc.
	 * 
	 * @param createURI
	 * @return
	 */
	private Package getInternalResources(URI createURI) {
		Resource resource = getResources().getResource(createURI, true);
		return (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),
															UMLPackage.Literals.PACKAGE);
	}
	
	private static boolean isCompleteResources(FilenameFilter filter, File dir, String resourcesName) {
		return !filter.accept(dir, resourcesName);
	}

	public org.eclipse.uml2.uml.Package getExternalResources(String uri) {org.eclipse.uml2.uml.Package package_;
		
		Resource resource = getResources().getResource(URI.createFileURI(uri), true);
		package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),
															UMLPackage.Literals.PACKAGE);
		return package_;
	}

	//TODO READ FROM CONFIGURATION FILE
	private Profile loadSMartyProfile() throws ModelNotFoundException, ModelIncompleteException {
		return (Profile) getExternalResources("src/test/java/resources/smarty.profile.uml");
	}
	
	public Profile getSMartyProfile(){
		return (Profile) profile;
	}
	
	//TODO Load profile according name of model
	public void setSMartyProfile() throws ModelNotFoundException, ModelIncompleteException{
		profile =  loadSMartyProfile();
	}
	
	public EnumerationLiteral getLiteralEnumeration(String name) throws EnumerationNotFoundException {
		Enumeration a = (Enumeration) getEnumerationByName((Profile) profile, "BindingTime");
		return a.getOwnedLiteral(name);
	}
	
	public Operation createOperation(Classifier klass, String methodName, EList<String> parameterNames, EList<Type> parameterTypes, Type returnType){
		org.eclipse.uml2.uml.Class k = (org.eclipse.uml2.uml.Class) klass;
		return k.createOwnedOperation(methodName, parameterNames, parameterTypes, returnType);
	}
}