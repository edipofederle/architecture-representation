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
import java.util.logging.Level;

import jmetal.core.Variable;
import main.GenerateArchitecture;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.ElementNotFound;
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
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;

import com.rits.cloning.Cloner;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Architecture extends Variable {	
	private static final long serialVersionUID = -7764906574709840088L;
	
	private Cloner cloner;

	static Logger LOGGER = LogManager.getLogger(Architecture.class.getName());
	public static String ARCHITECTURE_TYPE = "arquitetura.representation.Architecture"; 

	private Set<Package> packages = new HashSet<Package>();
	private Set<Class> classes = new HashSet<Class>();
	private Set<Interface> interfaces = new HashSet<Interface>();
	private HashMap<String, Concern> concerns = new HashMap<String, Concern>();
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

//	/**
//	 * Procura concern por nome.
//	 * 
//	 * Se o concern não estiver no arquivo de profile é lançada uma Exception.
//	 * 
//	 * @param name
//	 * @return
//	 * @throws ConcernNotFoundException 
//	 */
//	public Concern getOrCreateConcern(String name) throws ConcernNotFoundException {
//		Concern concern = allowedConcernContains(name.toLowerCase());
//		if(concerns.containsValue(concern)) return concern;
//		if(concern != null){
//			concerns.put(name.toLowerCase(), concern);
//			return concern;
//		}
//		throw new ConcernNotFoundException("Concern " + name + " cannot be found");
//	}


	/**
	 * Retorna um Map imutável. É feito isso para garantir que nenhum modificação seja
	 * feita diretamente na lista
	 * 
	 * @return Map<String, Concern>
	 */
	public List<Concern> getAllConcerns() {
		List<Concern> concerns = new ArrayList<Concern>();
		for (Map.Entry<String, Concern> entry : ConcernHolder.INSTANCE.getConcerns().entrySet()) {
			concerns.add(entry.getValue());
		}
		return Collections.unmodifiableList(concerns);
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
			klasses.addAll(p.getAllClasses());
		
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
		return Collections.unmodifiableSet(RelationshipHolder.getRelationships());
	}

	public List<GeneralizationRelationship> getAllGeneralizations() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return GeneralizationRelationship.class.isInstance(parent);
			}
		};

		List<GeneralizationRelationship> generalizations = UtilResources.filter(RelationshipHolder.getRelationships(), isValid);
		
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
		associations = null;
		return association; 

	}

	private boolean notAgregation(AssociationRelationship associationRelationship) {
		return ((!associationRelationship.getParticipants().get(0).isAggregation()) && (!associationRelationship.getParticipants().get(1).isAggregation()));
	}

	private boolean notComposition(AssociationRelationship associationRelationship) {
		return ((!associationRelationship.getParticipants().get(0).isComposite()) && (!associationRelationship.getParticipants().get(1).isComposite()));
	}

	public List<AssociationRelationship> getAllAssociations() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationRelationship.class.isInstance(parent);
			}
		};

		List<AssociationRelationship> allAssociations = UtilResources.filter(RelationshipHolder.getRelationships(), isValid);
		
		return Collections.unmodifiableList(allAssociations);

	}
	
	public List<AssociationRelationship> getAllCompositions() {
		List<AssociationRelationship> associations = getAllAssociations();
		List<AssociationRelationship> compositions = new ArrayList<AssociationRelationship>();
		for (AssociationRelationship associationRelationship : associations) {
			if((associationRelationship.getParticipants().get(0).isComposite()) || (associationRelationship.getParticipants().get(1).isComposite())){
				compositions.add(associationRelationship);
			}
		}
		associations = null;
		return Collections.unmodifiableList(compositions); 
	}
	
	public List<AssociationRelationship> getAllAgragations() {
		List<AssociationRelationship> associations = getAllAssociations();
		List<AssociationRelationship> agragation = new ArrayList<AssociationRelationship>();
		for (AssociationRelationship associationRelationship : associations) {
			if((associationRelationship.getParticipants().get(0).isAggregation()) || (associationRelationship.getParticipants().get(1).isAggregation())){
				agragation.add(associationRelationship);
			}
		}
		return Collections.unmodifiableList(agragation); 
	}

	public List<UsageRelationship> getAllUsage() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return UsageRelationship.class.isInstance(parent);
			}
		};

		List<UsageRelationship> allUsages = UtilResources.filter(RelationshipHolder.getRelationships(), isValid);
		
		return Collections.unmodifiableList(allUsages);
	}

	public List<DependencyRelationship> getAllDependencies() {
		Predicate<Relationship> isValid = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return DependencyRelationship.class.isInstance(parent);
			}
		};

		List<DependencyRelationship> allDependencies = UtilResources.filter(RelationshipHolder.getRelationships(), isValid);
		
		return Collections.unmodifiableList(allDependencies);
	}

	public List<RealizationRelationship> getAllRealizations() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return RealizationRelationship.class.isInstance(parent);
			}
		};

		List<RealizationRelationship> allRealizations = UtilResources.filter(RelationshipHolder.getRelationships(), realizations);
		
		return Collections.unmodifiableList(allRealizations);
	}

	public List<AbstractionRelationship> getAllAbstractions() {
		Predicate<Relationship> realizations = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AbstractionRelationship.class.isInstance(parent);
			}
		};

		List<AbstractionRelationship> allAbstractions = UtilResources.filter(RelationshipHolder.getRelationships(), realizations);
		
		return Collections.unmodifiableList(allAbstractions);
	}
	

	public List<AssociationClassRelationship> getAllAssociationsClass() {
		Predicate<Relationship> associationClasses = new Predicate<Relationship>() {
			public boolean apply(Relationship parent) {
				return AssociationClassRelationship.class.isInstance(parent);
			}
		};

		List<AssociationClassRelationship> allAssociationClasses = UtilResources.filter(RelationshipHolder.getRelationships(), associationClasses);
		
		return Collections.unmodifiableList(allAssociationClasses);
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
			for(Class klass : p.getAllClasses())
				if(className.trim().equalsIgnoreCase(klass.getName().trim()))
					classesFound.add(klass);
		
		if(classesFound.isEmpty())
			throw new ClassNotFound("Class " + className + " can not be found.\n");
		return classesFound;
	}
	
	public Element findElementByName(String elementName) throws ElementNotFound{
		Element element = searchRecursivellyInPackage(this.packages, elementName);
		if(element == null){
			for(Class klass : this.classes)
				if(klass.getName().equals(elementName))
					return klass;
			for(Interface inter : this.interfaces)
				if(inter.getName().equals(elementName))
					return inter;
		}
		if(element == null)
			throw new ElementNotFound("No element called: " + elementName +" found");
		return element;
	}

	private Element searchRecursivellyInPackage(Set<Package> packages, String elementName) {
		for(Package p : packages){
			for(Element element : p.getElements()){
				if(element.getName().equals(elementName))
					return element;
				searchRecursivellyInPackage(p.getNestedPackages(), elementName);
			}
			if(p.getName().equals(elementName))
				return p;
			searchRecursivellyInPackage(p.getNestedPackages(), elementName);
		}
		
		return null;
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
		Package pkg = new Package(packageName);
		this.packages.add(pkg);
		return pkg;
	}
	
	public Package createPackage(String packageName, String id) {
		Package pkg = new Package(packageName, id);
		this.packages.add(pkg);
		return pkg;
	}

	public void removePackage(Package p) {
		/**
		 * Remove qualquer relacionamento que os elementos do pacote
		 * que esta sendo deletado possa ter.
		 */
		for(Element element : p.getElements()){
			RelationshipHolder.removeRelatedRelationships(element);
		}
		//Remove os relacionamentos que o pacote possa pertencer
		RelationshipHolder.removeRelatedRelationships(p);
		
		this.packages.remove(p);
		LOGGER.info("Pacote:" + p.getName() + "removido");
	}

	public Interface createInterface(String interfaceName) {
		Interface interfacee = new Interface(interfaceName);
		this.addExternalInterface(interfacee);
		return interfacee;
	}
	
	public Interface createInterface(String interfaceName, String id) {
		Interface interfacee = new Interface(interfaceName, id);
		this.addExternalInterface(interfacee);
		return interfacee;
	}
	
	public Class createClass(String klassName, boolean isAbstract) {
		Class klass = new Class(klassName, isAbstract);
		this.addExternalClass(klass);
		return klass;
	}

	public void removeInterface(Interface interfacee) {
		interfacee.removeInterfaceFromRequiredOrImplemented();
		RelationshipHolder.removeRelatedRelationships(interfacee);
		if (removeInterfaceFromArch(interfacee)){
			LOGGER.info("Interface:" + interfacee.getName() + " removida da arquitetura");
		}
	}
	

	private boolean removeInterfaceFromArch(Interface interfacee) {
		if(this.interfaces.remove(interfacee))
			return true;
		for(Package p : this.packages){
			if(p.removeInterface(interfacee))
				return true;
		}
		return false;
	}

	public void removeClass(Element klass) {
		RelationshipHolder.removeRelatedRelationships(klass);
		if(this.classes.remove(klass))
			LOGGER.info("Classe " + klass.getName()+"("+klass.getId()+") removida da arquitetura");
		
		for(Package pkg : this.getAllPackages()){
			if(pkg.getAllClasses().contains(klass)){
				if(pkg.removeClass(klass))
					LOGGER.info("Classe " + klass.getName()+"("+klass.getId()+") removida da arquitetura. Pacote(" + pkg.getName()+ ")");
			}
		}
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
			for(Class klass : p.getAllClasses())
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

	public void moveElementToPackage(Element klass, Package pkg) {
       try {
           if (pkg.getElements().contains(klass)) {
               return;
           }
           String oldPackageName = UtilResources.extractPackageName(klass.getNamespace());
           if(oldPackageName.equals("model")){
	           addClassOrInterface(klass, pkg);
	           this.removeOnlyElement(klass);
           }else{
        	   Package oldPackage = this.findPackageByName(oldPackageName);
	           addClassOrInterface(klass, pkg);
	           oldPackage.removeOnlyElement(klass);
           }
       } catch (PackageNotFound ex) {
           java.util.logging.Logger.getLogger(Architecture.class.getName()).log(Level.SEVERE, null, ex);
       }
	}

	private void addClassOrInterface(Element klass, Package pkg) {
		if (klass instanceof Class) {
		       pkg.addExternalClass(klass);
		   } else if (klass instanceof Interface) {
		       pkg.addExternalInterface((Interface) klass);
		   }
	}

	
	public OperationsOverGeneralization forGeneralization() {
		return new OperationsOverGeneralization(this);
	}


	public OperationsOverAbstraction forAbstraction() {
		return new OperationsOverAbstraction(this);
	}
	
	public boolean removeRelationship(Relationship as) {
		if(as == null) return false;
		if(RelationshipHolder.getRelationships().remove(as)){
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
		return this.deepClone();
	}

	//private static int count = 1;
	public Architecture deepClone() {
		if(cloner == null)
			cloner = new Cloner();
		Architecture newArchitecture = cloner.deepClone(this);
		return newArchitecture;
	}
	
	
	public boolean addImplementedInterface(Interface supplier, Class client) {
		if(!haveRelationship(supplier, client)){
			if(addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandonUUID()))){
				LOGGER.info("ImplementedInterface: " + supplier.getName() + " adicionada na classe: " +client.getName());
				return true;
			}else{
				LOGGER.info("Tentou adicionar a interface " + supplier.getName() + " como interface implementada pela classe: " + client.getName());
				return false;
			}
		}
		return false;
	}
	
	private boolean haveRelationship(Interface supplier, Element client) {
		for(Relationship r : this.getAllRelationships()){
			if(r instanceof RealizationRelationship)
				if(((RealizationRelationship) r).getClient().equals(client) && ((RealizationRelationship) r).getSupplier().equals(supplier))
					return true;
			
			if(r instanceof DependencyRelationship)
				if(((DependencyRelationship) r).getClient().equals(client) && ((DependencyRelationship) r).getSupplier().equals(supplier))
					return true;
		}
		return false;
	}

	public boolean addImplementedInterface(Interface supplier, Package client) {
		if(!haveRelationship(supplier, client)){
			if(addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandonUUID()))){
				LOGGER.info("ImplementedInterface: " + supplier.getName() + " adicionada ao pacote: " +client.getName());
				return true;
			}else{
				LOGGER.info("Tentou adicionar a interface " + supplier.getName() + " como interface implementada no pacote: " + client.getName());
				return false;
			}
		}
		return false;
	}
	
	public void removeImplementedInterface(Interface inter, Package pacote) {
		pacote.removeImplementedInterface(inter);
		RelationshipHolder.removeRelatedRelationships(inter);
	}
	
	public void removeImplementedInterface(Class foo, Interface inter) {
		foo.removeImplementedInterface(inter);
		RelationshipHolder.removeRelatedRelationships(inter);
	}
	
	public void addRequiredInterface(Interface supplier, Class client) {
		if(!haveRelationship(supplier, client)){
			if(addRelationship(new DependencyRelationship(supplier, client, "", this, UtilResources.getRandonUUID())))
				LOGGER.info("RequiredInterface: " + supplier.getName() + " adicionada a: " +client.getName());
			else
				LOGGER.info("TENTOU adicionar RequiredInterface: "+ supplier.getName() + " a : "+client.getName() + " porém não consegiu");
		}
	}
	
	public void addRequiredInterface(Interface supplier, Package client) {
		if(!haveRelationship(supplier, client)){
			if(addRelationship(new DependencyRelationship(supplier, client, "", this, UtilResources.getRandonUUID())))
				LOGGER.info("RequiredInterface: " + supplier.getName() + " adicionada a: " +client.getName());
			else
				LOGGER.info("TENTOU adicionar RequiredInterface: "+ supplier.getName() + " a : "+client.getName() + " porém não consegiu");
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
		if(!haveRelationship(relationship)){
			if(RelationshipHolder.getRelationships().add(relationship)){
				LOGGER.info("Relacionamento: " + relationship.getType() + " adicionado na arquitetura.("+UtilResources.detailLogRelationship(relationship)+")");
				return true;
			}else{
				LOGGER.info("TENTOU adicionar Relacionamento: " + relationship.getType() + " na arquitetura porém não consegiu");
				return false;
			}
		}
		return false;
	}

	public boolean haveRelationship(Relationship relationship) {
		//Association
		for(Relationship r : this.getAllRelationships()){
			if((r instanceof AssociationRelationship) && (relationship instanceof AssociationRelationship)){
				List<AssociationEnd> participantsNew = ((AssociationRelationship)relationship).getParticipants();
				List<AssociationEnd> participantsExists = ((AssociationRelationship)r).getParticipants();

				if(participantsNew.equals(participantsExists))
					return true;
			}
		}
		
		if(relationship instanceof GeneralizationRelationship)
			if(this.getAllGeneralizations().contains(relationship)) return true;
		if(relationship instanceof DependencyRelationship )
			if(this.getAllDependencies().contains(relationship)) return true;
		if(relationship instanceof UsageRelationship)
			if(this.getAllUsage().contains(relationship)) return true;
		if(relationship instanceof RealizationRelationship)
			if(this.getAllRealizations().contains(relationship)) return true;
		if(relationship instanceof AbstractionRelationship)
			if(this.getAllAbstractions().contains(relationship)) return true;
		if(relationship instanceof AssociationClassRelationship)
			if(this.getAllAssociationsClass().contains(relationship)) return true;
		
		return false;
		
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

	public void removeRequiredInterface(Interface supplier, Package client) {
		if(!client.removeRequiredInterface(supplier));
		RelationshipHolder.removeRelatedRelationships(supplier);
	}
	
	public void removeRequiredInterface(Interface supplier, Class client) {
		if(!client.removeRequiredInterface(supplier));
		RelationshipHolder.removeRelatedRelationships(supplier);
	}
	
	public boolean removeOnlyElement(Element element) {
		if(element instanceof Class){
			if(this.classes.remove(element)){
				LOGGER.info("Classe: " +element.getName() + " removida do pacote: "+this.getName());
				return true;
			}
		}else if(element instanceof Interface){
			if(this.interfaces.remove(element)){
				LOGGER.info("Interface: " +element.getName() + " removida do pacote: "+this.getName());
				return true;
			}
		}
		
		return false;
	}

	public void setCloner(Cloner cloner) {
		this.cloner = cloner;
	}
}