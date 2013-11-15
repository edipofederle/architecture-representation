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
import main.GenerateArchitecture;

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

	private Set<Element> elements = new HashSet<Element>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
	private List<Relationship> relationships = new ArrayList<Relationship>();
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
		List<Element> elts = new ArrayList<Element>();
		for(Package p : getAllPackages())
			for(Element element : p.getElements())
				elts.add(element);
		elts.addAll(this.elements);
		return  elts;
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
			if (element instanceof Package)
				paks.add(((Package) element));

		if (paks.isEmpty())
			return Collections.emptyList();
		return paks;
	}

	public List<Class> getAllClasses() {
		List<Class> klasses = new ArrayList<Class>();
		
		for(Package p : getAllPackages()){
			for(Element element : p.getElements()){
				if(element instanceof Class)
					klasses.add((Class)element);
			}
		}

		for (Element element : elements)
			if (element.getTypeElement().equals("klass"))
				klasses.add(((Class) element));
		
		excludeAssociationClassFromList(klasses);

		return klasses;
	}


	private void excludeAssociationClassFromList(List<Class> klasses) {
		for (Iterator<Class> i = klasses.iterator(); i.hasNext();) {
			Element klass = i.next();
			for(AssociationClassRelationship r : getAllAssociationsClass()){
				if(r.getAssociationClass().equals(klass)){
					i.remove();
				}
			}
		}
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
			for(Element element : getAllClasses()){
				if(element.getName().equalsIgnoreCase(name))
					return element;
			}
			for(Package p : getAllPackages()){
				for(Element element : p.getElements()){
					if(element.getName().equalsIgnoreCase(name))
						return element;
				}
			}
		}
		
		if(type.equalsIgnoreCase("interface")){
			for(Element element : getAllInterfaces()){
				if(element.getName().equalsIgnoreCase(name))
					return element;
			}
			
			for(Package p : getAllPackages()){
				for(Element element : p.getElements()){
					if(element.getName().equalsIgnoreCase(name))
						return element;
				}
			}
		}
		
		if(type.equalsIgnoreCase("package")){
			for(Element element : getAllPackages())
				if(element.getName().equalsIgnoreCase(name))
					return element;
		}
		return null;
	}

	public Set<Interface> getAllInterfaces() {
		Set<Interface> interfaces = new HashSet<Interface>();

		for (Element element : this.elements)
			if (element.getTypeElement().equals("interface"))
				interfaces.add(((Interface) element));
		
		for(Package pkg : this.getAllPackages())
			for(Element element : pkg.getElements())
				if (element.getTypeElement().equals("interface"))
					interfaces.add(((Interface) element));

		return interfaces;
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
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationClassRelationship.class.isInstance(parent);
			}
		};

		List<AssociationClassRelationship> allAbstractions = UtilResources.filter(relationships, realizations);
		
		if (allAbstractions.isEmpty())  return Collections.emptyList();
		return allAbstractions;
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
		Package pkg = new Package(this, packageName);
		this.addElement(pkg);
		return pkg;
	}

	public void removePackage(Package p) {
		List<Element> elements = getElements();
		//List<String> ids = new ArrayList<String>();
		Set<String> idsClasses = idsForAllElementsInPackage(p);
		
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
				}
			}
		}
		
		if(!idsClasses.isEmpty()){
			for (String id : idsClasses) {
				for (Iterator<Element> i = this.elements.iterator(); i.hasNext();) {
					Element element = i.next();
					if(id.equalsIgnoreCase(element.getId())){
						i.remove();
					}
				}
				
				for (Iterator<Package> i = this.getAllPackages().iterator(); i.hasNext();) {
					Package pkg = i.next();
					for (Iterator<Element> element = pkg.getElements().iterator(); element.hasNext();) {
						Element e = element.next();
						if(id.equalsIgnoreCase(e.getId()))
							element.remove();
					}
				}
				
			}
			
		}

		if(!this.elements.remove(p))
			LOGGER.info("Cannot remove Package " + p + ".");

	}

	private Set<String> idsForAllElementsInPackage(Package p) {
		Set<String> ids = new HashSet<String>();
		for(Element element : p.getElements() )
			ids.add(element.getId());
		return ids;
	}

	public Interface createInterface(String interfaceName) {
		String id = UtilResources.getRandonUUID();
		Interface interfacee = new Interface(this, interfaceName);
		elements.add(interfacee);
		return interfacee;
	}
	
	
	public Class createClass(String klassName, boolean isAbstract) {
		Class klass = new Class(this, klassName, isAbstract);
		return klass;
	}

	public void removeInterface(Interface interfacee) {
		if (!elements.remove(interfacee))
			LOGGER.info("Cannot remove Interface " + interfacee + ".");
		for(Package p : getAllPackages()){
			p.getElements().remove(interfacee);
		}
	}

//	public Element getElementByXMIID(String xmiId) {
//		for (Element element : elements)
//			if(element.getId().equalsIgnoreCase(xmiId))
//				return element;
//
//		return null;
//	}

	
	/**
	 * Remove classe que estão em um primeiro nível da arquitetura, ou seja, não pertencem a nenhum pacote.
	 * 
	 * @param klass
	 */
	public void removeClass(Class klass) {
		if(!this.elements.contains(klass)){
			LOGGER.info("A classe " + klass.getName()+"("+klass.getId()+") não encontrada");
			return ;
		}
		this.elements.remove(klass);
		LOGGER.info("A classe " + klass.getName()+"("+klass.getId()+") foi removida da arquitetura.");
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
	
	public Interface findIntefaceById(String idClass) throws ClassNotFound {
		for (Interface klass : getAllInterfaces()) 
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

	public void moveClassToPackage(Class klass, Package pkg) {
		if (!pkg.getElements().contains(klass)) return;
		removeClass(klass);
		pkg.addExternalClass(klass);
	}

	public OperationsOverGeneralization forGeneralization() {
		return new OperationsOverGeneralization(this);
	}


	public OperationsOverAbstraction forAbstraction() {
		return new OperationsOverAbstraction(this);
	}
	
	public boolean removeRelationship(Relationship as) {
		if(as == null) return false;
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

	public void save(Architecture architecture, String pathToSave, int i) {
		GenerateArchitecture generate = new GenerateArchitecture();
		generate.generate(architecture, pathToSave +architecture.getName() + i);
	}

	public Element findElementById(String xmiId) {
		for (Element element : this.elements) {
			if(element.getId().equals(xmiId))
				return element;
		}
		for(Package p : getAllPackages()){
			for(Element element : p.getElements()){
				if(element.getId().equalsIgnoreCase(xmiId))
					return element;
			}
		}
		
		return null;
	}

	public void addElements(List<? extends Element> list) {
		this.elements.addAll(list);
	}
	
	/**
	 * Método que adiciona um elemento na arquitetura.
	 * 
	 * Este método adiciona o elemento em uma lista "global" da arquitetura.<br/><br/>
	 * 
	 * <b>OBS:</b><br/>
	 * Por exemplo: Caso queira adicionar uma classe chamada "Foo" dentro do pacote chamado "Bar",
	 * Você primeiro precisa buscar o pacote "Bar" e então adicionar a classe "Foo" na lista de elementos
	 * do pacote em questão.
	 * 
	 * @param element - {@link Element}
	 */
	public void addElement(Element element) {
		this.elements.add(element);
	}

}