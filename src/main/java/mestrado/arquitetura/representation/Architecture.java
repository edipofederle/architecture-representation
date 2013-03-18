package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

	public List<Class> getClasses() {
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
		List<Class> klasses = getClasses();
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

	public List<Class> getAllInterfaces() {
		List<Class> klasses = new ArrayList<Class>();

		for (Element element : elements)
			if (element.getTypeElement().equals("klass"))
				if (((Class) element).isInterface())
					klasses.add(((Class) element));

		if (klasses.isEmpty())
			return Collections.emptyList();
		return klasses;

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
		for (Class klass : getClasses()) 
			if(className.equalsIgnoreCase(klass.getName()))
				return klass;
		
		throw new ClassNotFound("Class " + className + " can not found.");
	}

	public Class findInterfaceByName(String interfaceName) throws InterfaceNotFound {
		for (Class klass : getClasses())
			if(interfaceName.equalsIgnoreCase(klass.getName()) && klass.isInterface())
				return klass;
		
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

}