package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jmetal.core.Variable;
import main.GenerateArchitecture;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.flyweights.VariabilityFlyweight;
import arquitetura.flyweights.VariantFlyweight;
import arquitetura.flyweights.VariationPointFlyweight;
import arquitetura.helpers.Predicate;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.MemberEnd;
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

	private Set<Package> packages = new HashSet<Package>();
	private Set<Class> classes = new HashSet<Class>();
	private Set<Interface> interfaces = new HashSet<Interface>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
	private Set<Relationship> relationships = new HashSet<Relationship>();
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
		
		for(Class c : this.classes)
			elts.add(c);
		for(Interface i : this.interfaces)
			elts.add(i);
		
		return  Collections.unmodifiableList(elts);
	}

	/**
	 * Procura concern por nome.
	 * 
	 * Se o concern não estiver no arquivo de profile é lançada uma Exception.
	 * 
	 * @param name
	 * @return
	 * @throws ConcernNotFoundException 
	 */
	public Concern getOrCreateConcern(String name) throws ConcernNotFoundException {
		Concern concern = allowedConcernContains(name.toLowerCase());
		if(concerns.containsValue(concern)) return concern;
		if(concern != null){
			concerns.put(name.toLowerCase(), concern);
			return concern;
		}
		throw new ConcernNotFoundException("Concern " + name + " cannot be found");
	}


	/**
	 * Retorna um Map imutável. É feito isso para garantir que nenhum modificação seja
	 * feita diretamente na lista
	 * 
	 * @return Map<String, Concern>
	 */
	public Map<String, Concern> getAllConcerns() {
		return Collections.unmodifiableMap(concerns);
	}
	
	/**
	 * Retorna um Map imutável. É feito isso para garantir que nenhum modificação seja
	 * feita diretamente na lista
	 * 
	 * Set<Package>
	 * 
	 * @return Set<Package>
	 */
	public Set<Package> getAllPackages() {
		return Collections.unmodifiableSet(this.packages);
	}
	
	/**
	 * Retorna interfaces que não tem nenhum pacote.
	 * 
	 * Retorna um Set imutável. É feito isso para garantir que nenhum modificação seja
	 * feita diretamente na lista.
	 * 
	 * @return Set<Class>
	 */
	public Set<Interface> getInterfaces() {
		return Collections.unmodifiableSet(this.interfaces);
	}
	
	/**
	 * Retorna todas as interfaces que existem na arquiteutra.
	 * Este método faz um merge de todas as interfaces de todos os pacotes + as interfaces que não tem pacote
	 * 
	 * @return
	 */
	public Set<Interface> getAllInterfaces(){
		Set<Interface> interfaces = new HashSet<Interface>();
		for(Package p : this.packages)
			interfaces.addAll(p.getAllInterfaces());
		
		interfaces.addAll(this.interfaces);
		return Collections.unmodifiableSet(interfaces);
	}
	
	/**
	 * Retorna classes que não tem nenhum pacote.
	 * 
	 * Retorna um Set imutável. É feito isso para garantir que nenhum modificação seja
	 * feita diretamente na lista
	 * 
	 * @return Set<Class>
	 */
	public Set<Class> getClasses() {
		return Collections.unmodifiableSet(this.classes);
	}
	
	/**
	 * Retorna todas as classes que existem na arquiteutra.
	 * Este método faz um merge de todas as classes de todos os pacotes + as classes que não tem pacote
	 * 
	 * @return
	 */
	public Set<Class> getAllClasses(){
		Set<Class> klasses = new HashSet<Class>();
		for(Package p : this.packages)
			klasses.addAll(p.getClasses());
		
		klasses.addAll(this.classes);
		return Collections.unmodifiableSet(klasses);
		
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
			for(Element element : getClasses()){
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
			for(Element element : getInterfaces()){
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

	
	public Set<Relationship> getAllRelationships(){
		return Collections.unmodifiableSet(relationships);
	}

	public List<GeneralizationRelationship> getAllGeneralizations() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return GeneralizationRelationship.class.isInstance(parent);
			}
		};

		List<GeneralizationRelationship> generalizations = UtilResources.filter(relationships, isValid);
		
		return Collections.unmodifiableList(generalizations);
	}
	
	public List<AssociationRelationship> getAllAssociationsRelationships() {
		List<AssociationRelationship> associations = getAllAssociations();
		List<AssociationRelationship> association = new ArrayList<AssociationRelationship>();
		for (AssociationRelationship associationRelationship : associations) {
			if((notComposition(associationRelationship)) && (notAgregation(associationRelationship))){
				association.add(associationRelationship);
			}
		}
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

		List<AssociationRelationship> allAssociations = UtilResources.filter(relationships, isValid);
		
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
		return agragation; 
	}

	public List<UsageRelationship> getAllUsage() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return UsageRelationship.class.isInstance(parent);
			}
		};

		List<UsageRelationship> allUsages = UtilResources.filter(relationships, isValid);
		
		return allUsages;
	}

	public List<DependencyRelationship> getAllDependencies() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return DependencyRelationship.class.isInstance(parent);
			}
		};

		List<DependencyRelationship> allDependencies = UtilResources.filter(relationships, isValid);
		
		return allDependencies;
	}

	public List<RealizationRelationship> getAllRealizations() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return RealizationRelationship.class.isInstance(parent);
			}
		};

		List<RealizationRelationship> allRealizations = UtilResources.filter(relationships, realizations);
		
		return allRealizations;
	}

	public List<AbstractionRelationship> getAllAbstractions() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AbstractionRelationship.class.isInstance(parent);
			}
		};

		List<AbstractionRelationship> allAbstractions = UtilResources.filter(relationships, realizations);
		
		return allAbstractions;
	}
	

	public List<AssociationClassRelationship> getAllAssociationsClass() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationClassRelationship.class.isInstance(parent);
			}
		};

		List<AssociationClassRelationship> allAbstractions = UtilResources.filter(relationships, realizations);
		
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
		for (Class klass : getClasses()) 
			if(className.trim().equalsIgnoreCase(klass.getName().trim()))
				classesFound.add(klass);
		
		for(Package p : this.packages)
			for(Class klass : p.getClasses())
				if(className.trim().equalsIgnoreCase(klass.getName().trim()))
					classesFound.add(klass);
		
		if(classesFound.isEmpty())
			throw new ClassNotFound("Class " + className + " can not found.\n");
		return classesFound;
	}

	public Interface findInterfaceByName(String interfaceName) throws InterfaceNotFound {
		for (Interface interfacee : getInterfaces())
			if(interfaceName.equalsIgnoreCase(interfacee.getName()))
				return interfacee;
		for(Package p : this.packages)
			for(Interface interfacee : p.getAllInterfaces())
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
		this.packages.add(pkg);
		return pkg;
	}

	public void removePackage(Package p) {
		
		/**
		 * Remove qualquer relacionamento que os elementos do pacote
		 * que esta sendo deletado possa ter.
		 */
		for(Element element : p.getElements()){
			this.removeRelatedRelationships(element);
		}
		//Remove os relacionamentos que o pacote possa pertencer
		this.removeRelatedRelationships(p);
		
		this.packages.remove(p);
		LOGGER.info("Pacote:" + p.getName() + "removido");
	}

	public Interface createInterface(String interfaceName) {
		Interface interfacee = new Interface(this, interfaceName);
		return interfacee;
	}
	
	public Class createClass(String klassName, boolean isAbstract) {
		Class klass = new Class(this, klassName, isAbstract);
		return klass;
	}

	public void removeInterface(Interface interfacee) {
		this.removeRelatedRelationships(interfacee);
		if (!interfaces.remove(interfacee))
			LOGGER.info("Tentou remover Interface " + interfacee + " porém não consegiu");
		for(Package p : getAllPackages()){
			p.getElements().remove(interfacee);
		}
		LOGGER.info("Interface:" + interfacee.getName() + " removida da arquitetura");
	}
	
	/**
	 * Remove classe que estão em um primeiro nível da arquitetura, ou seja, não pertencem a nenhum pacote.
	 * 
	 * @param klass
	 */
	public void removeClass(Class klass) {
		if(!this.classes.contains(klass)){
			LOGGER.info("TENTOU remover a  Classe " + klass.getName()+"("+klass.getId()+") da arquitetura, porém não consegiu");
			return ;
		}
		this.classes.remove(klass);
		LOGGER.info("Classe " + klass.getName()+"("+klass.getId()+") removida da arquitetura");
	}

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
		for (Class klass : getClasses()) 
			if(idClass.equalsIgnoreCase(klass.getId().trim()))
				return klass;
		
		for(Package p : getAllPackages())
			for(Class klass : p.getClasses())
				if(idClass.equalsIgnoreCase(klass.getId().trim()))
					return klass;
		
		throw new ClassNotFound("Class " + idClass + " can not found.\n");
	}
	
	public Interface findIntefaceById(String idClass) throws ClassNotFound {
		for (Interface klass : getInterfaces()) 
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
		for (Interface interface_ : getInterfaces())
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
		if(interfaces.add(interface_))
			LOGGER.info("Interface: "+ interface_.getName() + " adicionada na arquiteutra");
		else
			LOGGER.info("TENTOU adicionar a interface : "+ interface_.getName() + " na arquiteutra, porém não conseguiu");
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
		if(relationships.remove(as)){
			LOGGER.info("Relacionamento : " + as.getType() + " removido da arquitetura");
			return true;
		}else{
			LOGGER.info("TENTOU remover Relacionamento : " + as.getType() + " da arquitetura porém não consegiu");
			return false;
		}
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
		return newArchitecture;
	}
	
	
	public void addImplementedInterfaceToComponent(Interface interface_, Package pkg) {
		if (pkg.getImplementedInterfaces().contains(interface_)) return;
		
		if(addRelationship(new RealizationRelationship(interface_, pkg, "", UtilResources.getRandonUUID())))
			LOGGER.info("ImplementedInterface: " + interface_.getName() + " adicionada ao pacote: " +pkg.getName());
		else
			LOGGER.info("TENTOU adicionar ImplementedInterface: "+ interface_.getName() + " ao pacote: "+pkg.getName() + " porém não consegiu");
	}
	
	public void addRequiredInterfaceToComponent(Interface interface_, Package pkg) {
		if (pkg.getRequiredInterfaces().contains(interface_)) return;
		if(getAllRelationships().add(new DependencyRelationship(interface_, pkg, "", this, UtilResources.getRandonUUID())))
			LOGGER.info("RequiredInterface: " + interface_.getName() + " adicionada ao pacote: " +pkg.getName());
		else
			LOGGER.info("TENTOU adicionar RequiredInterface: "+ interface_.getName() + " ao pacote: "+pkg.getName() + " porém não consegiu");
	}
	
	public void removeImplementedInterfaceFromPackage(Interface interface_, Package component) {
		if(component.removeImplementedInterface(interface_)){
			LOGGER.info("ImplementedInterface removida do pacote " + component.getName());
		}else{
			LOGGER.info("TENTOU remover ImplementedInterface do pacote " + component.getName() + " porém não consegiu");
			return ;
		}

		for (AbstractionRelationship relationship : getAllAbstractions()) {
			if (relationship.getSupplier().equals(interface_) && relationship.getClient().equals(component)){
				removeRelationship(relationship);
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

	public boolean addRelationship(Relationship relationship) {
		if(this.relationships.add(relationship)){
			LOGGER.info("Relacionamento: " + relationship.getType() + " adicionado na arquitetura");
			return true;
		}else{
			LOGGER.info("TENTOU adicionar Relacionamento: " + relationship.getType() + " na arquitetura porém não consegiu");
			return false;
		}
	}

	public Package findPackageOfClass(Class targetClass) throws PackageNotFound {
		String packageName = UtilResources.extractPackageName(targetClass.getNamespace());
		return findPackageByName(packageName);
	}

	public void save(Architecture architecture, String pathToSave, int i) {
		GenerateArchitecture generate = new GenerateArchitecture();
		generate.generate(architecture, pathToSave +architecture.getName() + i);
	}

	/**
	 * Procura um elemento por ID.<br>
	 * Este método busca por elementos diretamente no primeiro nível da arquitetura (Ex: classes que não possuem pacotes)
	 * , e também em pacotes.<br/><br/>
	 * 
	 * @param xmiId
	 * @return
	 */
	public Element findElementById(String xmiId) {
		for (Class element : this.classes) {
			if(element.getId().equals(xmiId))
				return element;
		}
		for (Interface element : this.interfaces) {
			if(element.getId().equals(xmiId))
				return element;
		}
		for(Package p : getAllPackages()){
			for(Element element : p.getElements()){
				if(element.getId().equalsIgnoreCase(xmiId))
					return element;
			}
		}
		
		for(Package p : getAllPackages()){
			if(p.getId().equalsIgnoreCase(xmiId))
				return p;
		}
		
		return null;
	}

	/**
	 * Dado um {@link Element} remove todos relacionamentos em que o elemento esteja envolvido
	 * 
	 * @param element
	 */
	public void removeRelatedRelationships(Element element) {
		for (Iterator<Relationship> i = this.relationships.iterator(); i.hasNext();) {
			Relationship r = i.next();
			if(r instanceof GeneralizationRelationship){
				if(((GeneralizationRelationship) r).getParent().equals(element) || ((GeneralizationRelationship) r).getChild().equals(element)){
					i.remove();
					LOGGER.info("Generalização removida");
				}
			}
			if(r instanceof RealizationRelationship){
				if(((RealizationRelationship) r).getClient().equals(element) || ((RealizationRelationship) r).getSupplier().equals(element)){
					i.remove();
					LOGGER.info("Realização removida");
				}
			}
			if(r instanceof DependencyRelationship){
				if(((DependencyRelationship) r).getClient().equals(element) || ((DependencyRelationship) r).getSupplier().equals(element)){
					i.remove();
					LOGGER.info("Dependência removida");
				}
			}
			if(r instanceof AbstractionRelationship){
				if(((AbstractionRelationship) r).getClient().equals(element) || ((AbstractionRelationship) r).getSupplier().equals(element)){
					i.remove();
					LOGGER.info("Abstraction removida");
				}
			}
			if(r instanceof AssociationRelationship){
				for(AssociationEnd a : ((AssociationRelationship)r).getParticipants()){
					if(a.getCLSClass().equals(element)){
						i.remove();
						LOGGER.info("Associação removida");
					}
				}
			}
			
			if(r instanceof AssociationClassRelationship){
				for(MemberEnd memberEnd : ((AssociationClassRelationship) r).getMemebersEnd()){
					if(memberEnd.getType().equals(element)){
						i.remove();
						LOGGER.info("AssociationClass removida");
					}
						
				}
			}
		}
	}

	/**
	 * Adiciona um pacote na lista de pacotes
	 * 
	 * @param {@link Package}
	 */
	public void addPackage(arquitetura.representation.Package p) {
		if(this.packages.add(p))
			LOGGER.info("Pacote: " + p.getName() + " adicionado na arquitetura");
		else
			LOGGER.info("TENTOU adicionar o Pacote: " + p.getName() + " na arquitetura porém não consegiu");
	}

	/**
	 * Adiciona uma classe na lista de classes. 
	 * 
	 * @param {@link Class}
	 */
	public void addExternalClass(Class klass) {
		if(this.classes.add(klass))
			LOGGER.info("Classe: " + klass.getName() + " adicionado na arquitetura");
		else
			LOGGER.info("TENTOU adicionar a Classe: " + klass.getName() + " na arquitetura porém não consegiu");
	}

}