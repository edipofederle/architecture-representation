package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import mestrado.arquitetura.exceptions.ClassNotFound;
import mestrado.arquitetura.exceptions.InterfaceNotFound;
import mestrado.arquitetura.exceptions.PackageNotFound;
import mestrado.arquitetura.helpers.Predicate;
import mestrado.arquitetura.helpers.UtilResources;
import mestrado.arquitetura.representation.relationship.AbstractionRelationship;
import mestrado.arquitetura.representation.relationship.AssociationClassRelationship;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.representation.relationship.RealizationRelationship;
import mestrado.arquitetura.representation.relationship.Relationship;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

/**
 * 
 * @author edipofederle
 * 
 */
public class Architecture {
	
	private final static Logger LOGGER = Logger.getLogger(Architecture.class.getName()); 

	private List<Element> elements = new ArrayList<Element>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
	private List<Variability> variabilities = new ArrayList<Variability>();
	private List<Relationship> relationships = new ArrayList<Relationship>();

	private List<String> allIds = new ArrayList<String>();
	

	private String name;

	public Architecture(String name) {
		setName(name);
	}

	public void setName(String name) {
		this.name = name != null ? name : "";
	}

	public String getName() {
		return name;
	}

	public List<Element> getElements() {
		return elements;
	}

	public Concern getOrCreateConcernByName(String name) {
		Concern concern = concerns.get(name.toLowerCase());
		if (concern == null) {
			concern = new Concern(name);
			concerns.put(name.toLowerCase(), concern);
		}

		return concern;
	}

	public HashMap<String, Concern> getConcerns() {
		return concerns;
	}

	public List<Package> getAllPackages() {
		List<Package> paks = new ArrayList<Package>();

		for (Element element : elements)
			if (element.getTypeElement().equals("package"))
				paks.add(((Package) element));

		if (paks.isEmpty())
			return Collections.emptyList();
		return paks;
	}

	public List<Class> getAllClasses() {
		List<Class> klasses = new ArrayList<Class>();

		for (Element element : elements)
			if (element.getTypeElement().equals("klass"))
				klasses.add(((Class) element));

		if (klasses.isEmpty())
			return Collections.emptyList();
		return klasses;
	}

	public List<Variability> getVariabilities() {
		return variabilities;
	}

	public List<Relationship> getInterClassRelationships() {
		return relationships;
	}

	public void setInterClassRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

	// TODO refatorar para buscar todo tipo de elemento
	public Element findElementByName(String name) {
		List<Class> klasses = getAllClasses();
		List<Package> packages = getAllPackages();

		for (Class klass : klasses)
			if (foundElement(name, klass))
				return klass;

		for (Package package1 : packages)
			if (foundElement(name, package1))
				return package1;

		return null; // TODO Verifica Null
	}

	private <T> boolean foundElement(String name, Element element) {
		return name.equalsIgnoreCase(element.getName());
	}

	public List<Interface> getAllInterfaces() {
		List<Interface> klasses = new ArrayList<Interface>();

		for (Element element : elements)
			if (element.getTypeElement().equals("interface"))
			//	if (((Class) element).isInterface())
					klasses.add(((Interface) element));

		if (klasses.isEmpty())
			return Collections.emptyList();
		return klasses;

	}
	
	public List<Relationship> getAllRelationships(){
		return relationships;
	}

	public List<GeneralizationRelationship> getAllGeneralizations() {
		Predicate<Relationship> isAuthorized = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return GeneralizationRelationship.class.isInstance(parent);
			}
		};

		List<GeneralizationRelationship> authorizedUsers = UtilResources
				.filter(relationships, isAuthorized);
		return authorizedUsers;
	}

	public List<AssociationRelationship> getAllAssociations() {
		Predicate<Relationship> isAuthorized = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationRelationship.class.isInstance(parent);
			}
		};

		List<AssociationRelationship> authorizedUsers = UtilResources.filter(
				relationships, isAuthorized);
		return authorizedUsers;

	}

	public List<UsageRelationship> getAllUsage() {
		Predicate<Relationship> isAuthorized = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return UsageRelationship.class.isInstance(parent);
			}
		};

		List<UsageRelationship> authorizedUsers = UtilResources.filter(
				relationships, isAuthorized);
		return authorizedUsers;
	}

	public List<DependencyRelationship> getAllDependencies() {
		Predicate<Relationship> isAuthorized = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return DependencyRelationship.class.isInstance(parent);
			}
		};

		List<DependencyRelationship> authorizedUsers = UtilResources.filter(
				relationships, isAuthorized);
		return authorizedUsers;
	}

	public List<RealizationRelationship> getAllRealizations() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return RealizationRelationship.class.isInstance(parent);
			}
		};

		List<RealizationRelationship> alRealizations = UtilResources.filter(
				relationships, realizations);
		return alRealizations;
	}

	public List<AbstractionRelationship> getAllAbstractions() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AbstractionRelationship.class.isInstance(parent);
			}
		};

		List<AbstractionRelationship> alRealizations = UtilResources.filter(
				relationships, realizations);
		return alRealizations;
	}
	
	public List<AssociationClassRelationship> getAllAssociationsClass() {
		Predicate<Relationship> associationClass = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationClassRelationship.class.isInstance(parent);
			}
		};

		List<AssociationClassRelationship> allAssociationClass = UtilResources.filter(relationships, associationClass);
		return allAssociationClass;
	}


	/**
	 * Recupera uma classe por nome.
	 * 
	 * @param className
	 * @return {@link Class}
	 * @throws ClassNotFound se classe não encontrada
	 */
	public Class findClassByName(String className) throws ClassNotFound {
		for (Class klass : getAllClasses()) 
			if(className.equalsIgnoreCase(klass.getName()))
				return klass;
		
		throw new ClassNotFound("Class " + className + " can not found.");
	}

	public Interface findInterfaceByName(String interfaceName) throws InterfaceNotFound {
		for (Interface interfacee : getAllInterfaces())
			if(interfaceName.equalsIgnoreCase(interfacee.getName()))
				return interfacee;
		
		throw new InterfaceNotFound("Interface " + interfaceName + " can not found.");
	}

	public Package findPackageByName(String packageName) throws PackageNotFound {
		for(Package pkg : getAllPackages()){
			if(packageName.equalsIgnoreCase(pkg.getName()))
				return pkg;
		}
		throw new PackageNotFound("Pakcage " + packageName + " can not found.");
	}

	public void removeAssociationRelationship(AssociationRelationship as) {
		if (removeRelationship(as))
			LOGGER.info("Cannot remove Association " + as + ".");
	}

	private boolean removeRelationship(AssociationRelationship as) {
		return !relationships.remove(as);
	}

	public void removeDependencyRelationship(DependencyRelationship dp) {
		if (!relationships.remove(dp))
			LOGGER.info("Cannot remove Dependency " + dp + ".");
	}

	public void removeUsageRelationship(UsageRelationship usage) {
		if (!relationships.remove(usage))
			LOGGER.info("Cannot remove Usage " + usage + ".");
	}

	public void removeAssociationClass(AssociationClassRelationship associationClass){
		if (!relationships.remove(associationClass))
			LOGGER.info("Cannot remove AssociationClass " + associationClass + ".");
	}

	public void removeGeneralizationRelationship(GeneralizationRelationship generalization) {
		if (!relationships.remove(generalization))
			LOGGER.info("Cannot remove Generalization " + generalization + ".");
	}

	public void removeAbstractionRelationship(AbstractionRelationship ab) {
		if (!relationships.remove(ab))
			LOGGER.info("Cannot remove Abstraction " + ab + ".");
	}

	public Package createPackage(String packageName) {
		Package pkg = new Package(this, packageName, UtilResources.getRandonUUID());
		elements.add(pkg);
		return pkg;
	}

	public void removePackage(Package p) {
		List<Class> klasses = getAllClasses();
		List<String> ids  = new ArrayList<String>();
		
		for (Class klass : klasses) {
			String packageName = UtilResources.extractPackageName(klass.getNamespace());
			if(packageName.equalsIgnoreCase(p.getName())){
				ids.addAll(klass.getIdsRelationships());
				removeIdOfElementFromList(klass.getId());
				removeClass(klass);
			}
		}
		
		for (Iterator<Relationship> i = relationships.iterator(); i.hasNext();) {
			Relationship next = i.next();
			if (ids.contains(next.getId())){
				i.remove();
				removeIdOfElementFromList(next.getId());
			}
		}
		
		if (elements.remove(p))
			removeIdOfElementFromList(p.getId());
		else	
			LOGGER.info("Cannot remove Package " + p + ".");
	}


	public Interface createInterface(String interfaceName) {
		Interface interfacee = new Interface(this, interfaceName);
		elements.add(interfacee);
		return interfacee;
	}

	public void removeInterface(Interface interfacee) {
		if (!elements.remove(interfacee))
			LOGGER.info("Cannot remove Interface " + interfacee + ".");
	}

	public Class createClass(String klassName) {
		Class klass = new Class(this, klassName);
		elements.add(klass);
		return klass;
	}

	public void removeClass(Class klass) {
		if (!elements.remove(klass))
			LOGGER.info("Cannot remove Class " + klass + ".");
	}

	public Element getElementByXMIID(String xmiId) {
		for (Element element : elements) {
			if(element.getId().equalsIgnoreCase(xmiId))
				return element;
		}
		return null;
	}

	public List<String> getAllIds() {
		return allIds;
	}
	
	private void removeIdOfElementFromList(String id) {
		allIds.remove(id);
	}

}