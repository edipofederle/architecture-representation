package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jmetal.core.Variable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.flyweights.VariabilityFlyweight;
import arquitetura.flyweights.VariantFlyweight;
import arquitetura.flyweights.VariationPointFlyweight;
import arquitetura.helpers.Predicate;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;

import com.rits.cloning.Cloner;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Architecture extends Variable implements Cloneable {	
	private static final long serialVersionUID = -7764906574709840088L;

	static Logger LOGGER = LogManager.getLogger(Architecture.class.getName());
	public static String ARCHITECTURE_TYPE = "arquitetura.representation.Architecture"; 

	private List<Element> elements = new ArrayList<Element>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
	private List<Relationship> relationships = new ArrayList<Relationship>();
	private Set<String> allIds = new HashSet<String>();
	private String name;
	
	/**
	 * Esta lista é carregada a partir do arquivo de concerns indica no arquivo de configuração.<br/>
	 * 
	 * Ela serve para sabermos quais concern são passiveis de manipulação.<Br />
	 * 
	 * Ex: Ao adicionar um  concern em uma classe, o mesmo deve estar presente nesta lista.
	 * 
	 */
	private List<Concern> allowedConcerns = new ArrayList<Concern>();
	private List<AssociationClassRelationship> allAssociationClass = new ArrayList<AssociationClassRelationship>();

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

	/**
	 * Procura concern por nome.
	 * 
	 * Se o concern não estiver no arquivo de profile é lançada uma Exception.
	 * 
	 * @param name
	 * @return
	 */
	public Concern getOrCreateConcern(String name) {
		Concern concern = allowedConcernContains(name.toLowerCase());
		if(concerns.containsValue(concern)) return concern;
		if(concern != null){
			concerns.put(name.toLowerCase(), concern);
			return concern;
		}
		return null; // MAL
	}


	public HashMap<String, Concern> getAllConcerns() {
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

	public List<Relationship> getInterClassRelationships() {
		return relationships;
	}

	public void setInterClassRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

	
	/**
	 * Busca elemento por nome.<br/>
	 * 
	 * No momento busca por class, interface ou package <br/>
	 * 
	 * 
	 * TODO refatorar para buscar todo tipo de elemento
	 * 
	 * @param name  - Nome do elemento
	 * @parm type - tipo do elemento (class, interface ou package)
	 * @return
	 */
	public Element findElementByName(String name, String type) {
		return findElement(name, type);
	}

	private Element findElement(String name, String type) {
		if(type.equalsIgnoreCase("class")){
			for(Element element : getAllClasses())
				if(element.getName().equalsIgnoreCase(name))
					return element;
		}
		
		if(type.equalsIgnoreCase("interface")){
			for(Element element : getAllInterfaces())
				if(element.getName().equalsIgnoreCase(name))
					return element;
		}
		
		if(type.equalsIgnoreCase("package")){
			for(Element element : getAllPackages())
				if(element.getName().equalsIgnoreCase(name))
					return element;
		}
		return null;
	}

	public List<Interface> getAllInterfaces() {
		List<Interface> klasses = new ArrayList<Interface>();

		for (Element element : elements)
			if (element.getTypeElement().equals("interface"))
				klasses.add(((Interface) element));

		if (klasses.isEmpty())  return Collections.emptyList();
		return klasses;
	}
	
	public List<Relationship> getAllRelationships(){
		return relationships;
	}

	public List<GeneralizationRelationship> getAllGeneralizations() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return GeneralizationRelationship.class.isInstance(parent);
			}
		};

		List<GeneralizationRelationship> generalizations = UtilResources
				.filter(relationships, isValid);
		
		if (generalizations.isEmpty())  return Collections.emptyList();
		return generalizations;
	}
	
	public List<AssociationRelationship> getAllAssociationsRelationships() {
		List<AssociationRelationship> associations = getAllAssociations();
		List<AssociationRelationship> association = new ArrayList<AssociationRelationship>();
		for (AssociationRelationship associationRelationship : associations) {
			if((notComposition(associationRelationship)) && (notAgregation(associationRelationship))){
				association.add(associationRelationship);
			}
		}
		if (association.isEmpty())  return Collections.emptyList();
		return association; 

	}

	private boolean notAgregation(AssociationRelationship associationRelationship) {
		return ((!associationRelationship.getParticipants().get(0).isAggregation()) && (!associationRelationship.getParticipants().get(1).isAggregation()));
	}

	private boolean notComposition(AssociationRelationship associationRelationship) {
		return ((!associationRelationship.getParticipants().get(0).isComposite()) && (!associationRelationship.getParticipants().get(1).isComposite()));
	}

	private List<AssociationRelationship> getAllAssociations() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationRelationship.class.isInstance(parent);
			}
		};

		List<AssociationRelationship> allAssociations = UtilResources.filter(
				relationships, isValid);
		
		if (allAssociations.isEmpty())  return Collections.emptyList();
		return allAssociations;

	}
	
	public List<AssociationRelationship> getAllCompositions() {
		List<AssociationRelationship> associations = getAllAssociations();
		List<AssociationRelationship> compositions = new ArrayList<AssociationRelationship>();
		for (AssociationRelationship associationRelationship : associations) {
			if((associationRelationship.getParticipants().get(0).isComposite()) || (associationRelationship.getParticipants().get(1).isComposite())){
				compositions.add(associationRelationship);
			}
		}
		if (compositions.isEmpty())  return Collections.emptyList();
		return compositions; 
	}
	
	public List<AssociationRelationship> getAllAgragations() {
		List<AssociationRelationship> associations = getAllAssociations();
		List<AssociationRelationship> agragation = new ArrayList<AssociationRelationship>();
		for (AssociationRelationship associationRelationship : associations) {
			if((associationRelationship.getParticipants().get(0).isAggregation()) || (associationRelationship.getParticipants().get(0).isAggregation())){
				agragation.add(associationRelationship);
			}
		}
		if (agragation.isEmpty())  return Collections.emptyList();
		return agragation; 
	}

	public List<UsageRelationship> getAllUsage() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return UsageRelationship.class.isInstance(parent);
			}
		};

		List<UsageRelationship> allUsages = UtilResources.filter(relationships, isValid);
		
		if (allUsages.isEmpty())  return Collections.emptyList();
		return allUsages;
	}

	public List<DependencyRelationship> getAllDependencies() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return DependencyRelationship.class.isInstance(parent);
			}
		};

		List<DependencyRelationship> allDependencies = UtilResources.filter(relationships, isValid);
		
		if (allDependencies.isEmpty())  return Collections.emptyList();
		return allDependencies;
	}

	public List<RealizationRelationship> getAllRealizations() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return RealizationRelationship.class.isInstance(parent);
			}
		};

		List<RealizationRelationship> allRealizations = UtilResources.filter(relationships, realizations);
		
		if (allRealizations.isEmpty())  return Collections.emptyList();
		return allRealizations;
	}

	public List<AbstractionRelationship> getAllAbstractions() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AbstractionRelationship.class.isInstance(parent);
			}
		};

		List<AbstractionRelationship> allAbstractions = UtilResources.filter(relationships, realizations);
		
		if (allAbstractions.isEmpty())  return Collections.emptyList();
		return allAbstractions;
	}
	

	public List<AssociationClassRelationship> getAllAssociationsClass() {
		return allAssociationClass;
	}


	/**
	 * Recupera uma classe por nome.
	 * 
	 * @param className
	 * @return {@link Class}
	 * @throws ClassNotFound se classe não encontrada
	 */
	public List<Class> findClassByName(String className) throws ClassNotFound {
		List<Class> classesFound = new ArrayList<Class>();
		for (Class klass : getAllClasses()) 
			if(className.trim().equalsIgnoreCase(klass.getName().trim()))
				classesFound.add(klass);
		
		if(classesFound.isEmpty())
			throw new ClassNotFound("Class " + className + " can not found.\n");
		return classesFound;
	}

	public Interface findInterfaceByName(String interfaceName) throws InterfaceNotFound {
		for (Interface interfacee : getAllInterfaces())
			if(interfaceName.equalsIgnoreCase(interfacee.getName()))
				return interfacee;
		
		throw new InterfaceNotFound("Interface " + interfaceName + " can not found.\n");
	}

	public Package findPackageByName(String packageName) throws PackageNotFound {
		for(Package pkg : getAllPackages())
			if(packageName.equalsIgnoreCase(pkg.getName()))
				return pkg;
		
		throw new PackageNotFound("Pakcage " + packageName + " can not found.\n");
	}


	public Package createPackage(String packageName) {
		String id = UtilResources.getRandonUUID();
		Package pkg = new Package(this, packageName, id);
		getAllIds().add(id);
		elements.add(pkg);
		return pkg;
	}

	public void removePackage(Package p) {
		List<Element> elements = getElements();
		//List<String> ids = new ArrayList<String>();
		List<String> idsClasses = p.getAllClassIdsForThisPackage(); //Ids de todas as classes que pertencem ao pacote que esta sendo deletado.
		
		for (Iterator<Element> i = this.elements.iterator(); i.hasNext();) {
			Element e = i.next();
			String packageName = UtilResources.extractPackageName(e.getNamespace());
			if(e.getName().equalsIgnoreCase(packageName)){
			//	ids.addAll(e.getIdsRelationships());
			}
		}
		
		for(Element c : p.getClasses()){
			List<Relationship> res = c.getRelationships();
			for (Iterator<Relationship> i = relationships.iterator(); i.hasNext();) {
				Relationship r = i.next();
				if(res.contains(r)){
					i.remove();
					removeIdOfElementFromList(r.getId(), "Relacionamento");
				}
					
			}
		}
		
		//Relacionamentos em pacote
		List<Relationship> rp = p.getRelationships();
		for (Relationship relationship : rp) {
			for (Iterator<Relationship> r = this.relationships.iterator(); r.hasNext();) {
				Relationship relacion = r.next();
				if(relationship.equals(relacion)){
					r.remove();
					removeIdOfElementFromList(relacion.getId(), "Relacionamento");
				}
			}
		}
		
		
		if(!idsClasses.isEmpty()){
			for (String id : idsClasses) {
				for (Iterator<Element> i = this.elements.iterator(); i.hasNext();) {
					Element element = i.next();
					if(id.equalsIgnoreCase(element.getId())){
						i.remove();
						removeIdOfElementFromList(element.getId(), element.getTypeElement());
					}
				}
			}
		}

		if (elements.remove(p))
			removeIdOfElementFromList(p.getId(), "Package");
		else	
			LOGGER.info("Cannot remove Package " + p + ".");

	}

	public Interface createInterface(String interfaceName) {
		String id = UtilResources.getRandonUUID();
		Interface interfacee = new Interface(this, interfaceName, id);
		elements.add(interfacee);
		allIds.add(id);
		return interfacee;
	}
	
	public Class createClass(String klassName) {
		String id = UtilResources.getRandonUUID();
		Class klass = new Class(this, klassName, id);
		elements.add(klass);
		allIds.add(id);
		return klass;
	}

	public void removeInterface(Interface interfacee) {
		if (!elements.remove(interfacee))
			LOGGER.info("Cannot remove Interface " + interfacee + ".");
	}

	public Element getElementByXMIID(String xmiId) {
		for (Element element : elements)
			if(element.getId().equalsIgnoreCase(xmiId))
				return element;

		return null;
	}

	public Set<String> getAllIds() {
		return allIds;
	}
	
	private void removeIdOfElementFromList(String id, String message) {
		allIds.remove(id);
		LOGGER.info("Elemento ("+message+") : " + id + " removido.\n");
	}

	public void removeClass(Class klass) {
		if(!elements.remove(klass))
			LOGGER.info("Cannot remove Class " + klass + ".");
		else{
			removeIdOfElementFromList(klass.getId(), "Class removida com sucesso");
		}
	}

	public int getNumberOfElements() {
		return allIds.size();
	}

//	/**
//	 * @return the model
//	 */
//	public org.eclipse.uml2.uml.Package getModel() {
//		return model;
//	}
//
//	/**
//	 * @param model the model to set
//	 */
//	public void setModel(org.eclipse.uml2.uml.Package model) {
//		this.model = model;
//	}

	public List<VariationPoint> getAllVariationPoints() {
		return VariationPointFlyweight.getInstance().getVariationPoints();
	}

	public List<Variant> getAllVariants() {	
		return VariantFlyweight.getInstance().getVariants();
	}
	
	public List<Variability> getAllVariabilities() {
		return VariabilityFlyweight.getInstance().getVariabilities();
	}

	public Class findClassById(String idClass) throws ClassNotFound {
		for (Class klass : getAllClasses()) 
			if(idClass.equalsIgnoreCase(klass.getId().trim()))
				return klass;
		
		throw new ClassNotFound("Class " + idClass + " can not found.\n");
	}

	public List<Concern> allowedConcerns() {
		return allowedConcerns;
	}
	
	private Concern allowedConcernContains(String concernName) {
		for (Concern concern : allowedConcerns){
			if(concern.getName().equalsIgnoreCase(concernName))
				return concern;
		}
		return null;
	}

	public Concern getConcernByName(String concernName) {
		return concerns.get(concernName);
	}
	
	public Collection<Concern> getConcerns() {
		Set<Concern> concerns = new HashSet<Concern>();
//		for (Package component : getAllPackages()) 
//			concerns.addAll(component.getAllConcerns());
//		
		for (Interface interface_ : getAllInterfaces())
			concerns.addAll(interface_.getAllConcerns());
		
		return concerns;
	}

	/**
	 * @param concerns the concerns to set
	 */
	public void setConcerns(HashMap<String, Concern> concerns) {
		this.concerns = concerns;
	}
	
	public void addExternalInterface(Interface interface_){
		elements.add(interface_);
	}

	/**
	 * Retorna classe contendo método para manipular relacionamentos
	 * 
	 * @return OperationsOverRelationships
	 */
	public OperationsOverRelationships operationsOverRelationship() {
		return new OperationsOverRelationships(this);
	}

	public OperationsOverAssociation forAssociation() {
		return new OperationsOverAssociation(this);
	}

	public OperationsOverDependency forDependency() {
		return new OperationsOverDependency(this);
	}

	public void moveClassToPackage(Class klass1, Package package1) {
		package1.getAllClassIdsForThisPackage().add(klass1.getId());
	}

	public OperationsOverGeneralization forGeneralization() {
		return new OperationsOverGeneralization(this);
	}


	public OperationsOverAbstraction forAbstraction() {
		return new OperationsOverAbstraction(this);
	}
	
	public boolean removeRelationship(Relationship as) {
		if(as == null) return false;
		this.allIds.remove(as.getId());
		return relationships.remove(as);
	}

	public OperationsOverUsage forUsage() {
		return new OperationsOverUsage(this);
	}
	
	/**
	 * Create an exact copy of the <code>Architecture</code> object.
	 * 
	 * @return An exact copy of the object.
	 */
	public Variable deepCopy() {
		try {
			return this.deepClone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	// private static int count = 1;
	public Architecture deepClone() throws CloneNotSupportedException {
		Cloner cloner = new Cloner();
		Architecture newArchitecture = (Architecture) cloner.deepClone(this);
		// newArchitecture.setNumber(count++);
		// newArchitecture.addAncestor(this);
		// Dummy.addAncestor(newArchitecture, this);
		return newArchitecture;
	}
	
	
	public void addImplementedInterfaceToComponent(Interface interface_, Package pkg) {
		if (pkg.getImplementedInterfaces().contains(interface_)) return;
		
		getAllRelationships().add(new RealizationRelationship(interface_, pkg, "", UtilResources.getRandonUUID()));
	}
	
	public void addRequiredInterfaceToComponent(Interface interface_, Package pkg) {
		if (pkg.getRequiredInterfaces().contains(interface_)) return;
		getAllRelationships().add(new DependencyRelationship(interface_, pkg, "", this, UtilResources.getRandonUUID()));
	}
	
	public void removeImplementedInterfaceFromPackage(Interface interface_, Package component) {
		if (!component.removeImplementedInterface(interface_)) return;

		for (AbstractionRelationship relationship : getAllAbstractions()) {
			if (relationship.getSupplier().equals(interface_) && relationship.getClient().equals(component)){
				getAllRelationships().remove(relationship);
				return;
			}
		}
	}
	
	public void deleteClassRelationships(Class class_){
		Collection<Relationship> relationships = new ArrayList<Relationship>(class_.getRelationships());
		
		if (relationships!=null) {
		for (Relationship relationship : relationships) {
			this.removeRelationship(relationship);
		}
	  }
	}

	public void addRelationship(Relationship relationship) {
		this.relationships.add(relationship);
	}

	public Package findPackageOfClass(Class targetClass) throws PackageNotFound {
		String packageName = UtilResources.extractPackageName(targetClass.getNamespace());
		return findPackageByName(packageName);
	}

	public void save(Architecture architecture, int i) {
//		GenerateArchitecture generate = new GenerateArchitecture();
//		generate.generate(architecture, architecture.getName() + i);
	}

}