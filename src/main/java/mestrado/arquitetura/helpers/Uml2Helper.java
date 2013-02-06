package mestrado.arquitetura.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import mestrado.arquitetura.base.Base;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

public class Uml2Helper extends Base {
	
	public Uml2Helper(){}

	public Profile createProfile(String name) {
		Profile profile = UMLFactory.eINSTANCE.createProfile();
		profile.setName(name);

		System.out.println("Profile '" + profile.getQualifiedName()
				+ "' created.");

		return profile;
	}

	public void save(org.eclipse.uml2.uml.Package package_, URI uri)
			throws IOException {
		ArrayList<EObject> contents = new ArrayList<EObject>();
		contents.add(package_);
		save(contents, uri);
	}

	public void save(Collection<EObject> contents, URI uri) throws IOException {
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

		out("Package '" + createdPackage.getQualifiedName() + "' created.");

		return createdPackage;
	}

	public Model createModel(String name) {
		Model model = UMLFactory.eINSTANCE.createModel();
		model.setName(name);

		out("Model '" + model.getQualifiedName() + "' created.");

		return model;
	}

	public void out(String output) {
		System.out.println(output);
	}

	public void err(String error) {
		System.out.println(error);
	}

	public org.eclipse.uml2.uml.Class referenceMetaclass(Profile profile,
			String name) {
		Model umlMetamodel = (Model) load(
				URI.createURI(UMLResource.UML_METAMODEL_URI), "");

		org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(name);

		profile.createMetaclassReference(metaclass);

		System.out.println("Metaclass '" + metaclass.getQualifiedName()
				+ "' referenced.");

		return metaclass;
	}

	public Extension createExtension(org.eclipse.uml2.uml.Class metaclass,
			Stereotype stereotype, boolean required) {
		Extension extension = stereotype.createExtension(metaclass, required);

		System.out.println((required ? "Required extension '" : "Extension '")
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

		System.out.println(sb.toString());

		return attribute;
	}

	public Enumeration createEnumeration(org.eclipse.uml2.uml.Package package_,
			String name) {

		Enumeration enumeration = (Enumeration) package_
				.createOwnedEnumeration(name);
		System.out.println("Enumeration '" + enumeration.getQualifiedName()
				+ "' created.");

		return enumeration;
	}

	public EnumerationLiteral createEnumerationLiteral(Enumeration enumeration,
			String name) {
		EnumerationLiteral enumerationLiteral = enumeration
				.createOwnedLiteral(name);

		System.out.println("Enumeration literal '"
				+ enumerationLiteral.getQualifiedName() + "' created.");

		return enumerationLiteral;
	}

	public org.eclipse.uml2.uml.Package load(URI uri, String pathAbsolute) {
		
		org.eclipse.uml2.uml.Package package_ = null;
		if (!"".equals(pathAbsolute))
			getResources().getResource(URI.createFileURI(pathAbsolute), true);
		try {
			Resource resource = getResources().getResource(uri, true);
			package_ = (org.eclipse.uml2.uml.Package) EcoreUtil
					.getObjectByType(resource.getContents(),
							UMLPackage.Literals.PACKAGE);
		} catch (WrappedException we) {
			err(we.getMessage());
		}

		return package_;
	}

	public PackageableElement getEnumerationByName(Profile profile, String name)
			throws EnumerationNotFoundException {
		EList<PackageableElement> a = profile.getPackagedElements();
		for (PackageableElement packageableElement : a) {
			if (packageableElement instanceof Enumeration
					&& packageableElement.getName().equals(name))
				return packageableElement;
		}

		throw new EnumerationNotFoundException(name);
	}

	public Type getPrimitiveType(String typeName) {
		Package umlPrimitiveTypes = load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI), "");
		return umlPrimitiveTypes.getOwnedType(typeName);
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
			System.out.println("Profile '" + profile.getQualifiedName()
					+ "' applied to package '" + package_.getQualifiedName()
					+ "'.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
}
