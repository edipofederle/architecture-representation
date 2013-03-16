package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.ModelElementHelper;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.InterElementRelationship;
import mestrado.arquitetura.representation.Variability;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Usage;

/**
 * Builder responsável por criar a arquitetura.
 * 
 * @author edipofederle
 *
 */
public class ArchitectureBuilder {
	
	private static final Element NO_PARENT = null;
	private ModelHelper modelHelper;
	private Package model;
	private PackageBuilder packageBuilder;
	private ClassBuilder classBuilder;
	private VariabilityBuilder variabilityBuilder;
	
	private AssociationInterClassRelationshipBuilder associationInterClassRelationshipBuilder;
	private AssociationClassInterClassRelationshipBuilder associationClassInterClassRelationshipBuilder;
	
	private GeneralizationInterClassRelationshipBuilder generalizationInterClassRelationshipBuilder;
	
	private DependencyInterClassRelationshipBuilder dependencyInterClassRelationshipBuilder;
	private DependencyPackageInterfaceRelationshipBuilder dependencyPackageInterfaceRelationshipBuilder;
	
	private RealizationInterClassRelationshipBuilder realizationInterClassRelationshipBuilder;
	private AbstractionInterElementRelationshipBuilder abstractionInterElementRelationshipBuilder; 

	
	private UsageInterClassRelationshipBuilder usageInterClassRelationshipBuilder;
	/**
	 *  Construtor. Initializa helpers.
	 */
	public ArchitectureBuilder(){
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cria a arquitetura. Primeiramente é carregado o model (arquivo .uml), após isso é instanciado o objeto {@link Architecture}. <br/>
	 * Feito isso, é chamado método "initialize", neste método é crializada a criação dos Builders. <br/>
	 * 
	 * Em seguida, é carregado as os pacotes e suas classes. Também é carrega as classes que não pertencem a pacotes. <br/>
	 * Após isso são carregadas as variabilidade. <br/><br/>
	 * 
	 * InterClassRelationships </br>
	 *  
	 *  	<li>loadGeneralizations</li>
	 *		<li>loadAssociations</li>
	 *	    <li>loadInterClassDependencies</li>
	 *	    <li>loadRealizations</li>
	 *<br/>
	 *
	 *  InterElementRelationships </br>
	 *     <li>loadInterElementDependencies</li>
	 *	   <li>loadAbstractions</li>
	 * 
	 * <br><br/>
	 * @param xmiFilePath - arquivo da arquitetura (.uml)
	 * @return {@link Architecture}
	 * @throws Exception
	 */
	public Architecture create(String xmiFilePath) throws Exception {
		model = modelHelper.getModel(xmiFilePath);
		Architecture architecture = new Architecture(modelHelper.getName(xmiFilePath));
		
		initialize(architecture);
		
		architecture.getElements().addAll(loadPackages()); // Classes que possuem pacotes são carregadas juntamente com seus pacotes
		architecture.getElements().addAll(loadClasses()); // Classes que nao possuem pacotes
		architecture.getVariabilities().addAll(loadVariability());
		
		architecture.getInterClassRelationships().addAll(loadInterClassRelationships());
		architecture.getInterElementRelationships().addAll(loadInterElementRelationships());
		
		return architecture;
	}

	private List<? extends InterElementRelationship> loadInterElementRelationships() {
		List<InterElementRelationship> relationships = new ArrayList<InterElementRelationship>();
		relationships.addAll(loadInterElementDependencies());
		relationships.addAll(loadAbstractions());
		
		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<? extends InterElementRelationship> loadInterElementDependencies() {
		List<InterElementRelationship> relationships = new ArrayList<InterElementRelationship>();
		
		List<Package> packages = modelHelper.getAllPackages(model);
		
		for (Package pack : packages)
			if(!pack.getClientDependencies().isEmpty())
				if(!pack.getClientDependencies().get(0).getClients().isEmpty())
					if(ModelElementHelper.isInterface(pack.getClientDependencies().get(0).getSuppliers().get(0)))
					relationships.add(dependencyPackageInterfaceRelationshipBuilder.create(pack.getClientDependencies().get(0)));
			
		
		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<? extends InterElementRelationship> loadAbstractions() {
		List<Abstraction> abstractions = modelHelper.getAllAbstractions(model);
		List<InterElementRelationship> interElementRelationships = new ArrayList<InterElementRelationship>();
		
		for (Abstraction abstraction : abstractions)
			interElementRelationships.add(abstractionInterElementRelationshipBuilder.create(abstraction));
		
		if (interElementRelationships.isEmpty()) return Collections.emptyList();
		return interElementRelationships;
	}

	private List<InterClassRelationship> loadInterClassRelationships() {
		List<InterClassRelationship> relationships = new ArrayList<InterClassRelationship>();
		relationships.addAll(loadGeneralizations());
		relationships.addAll(loadAssociations());
		relationships.addAll(loadAssociationClassAssociation());
		relationships.addAll(loadInterClassDependencies());
		relationships.addAll(loadRealizations());
		relationships.addAll(loadUsageInterClass());
		
		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<? extends InterClassRelationship> loadUsageInterClass() {
		List<InterClassRelationship> usageClass = new ArrayList<InterClassRelationship>();
		List<Usage> usages = modelHelper.getAllUsage(model);
		
		List<Package> pacotes = modelHelper.getAllPackages(model);
		for (Package package1 : pacotes) {
			for(Usage u : modelHelper.getAllUsage(package1))
				usageClass.add(usageInterClassRelationshipBuilder.create(u));
		}
		
		for (Usage usage : usages) 
				usageClass.add(usageInterClassRelationshipBuilder.create(usage));
		
		if (usageClass.isEmpty()) return Collections.emptyList();
		return usageClass;
	}

	private List<? extends InterClassRelationship> loadAssociationClassAssociation() {
		List<InterClassRelationship> associationClasses = new ArrayList<InterClassRelationship>();
		List<AssociationClass> associationsClass = modelHelper.getAllAssociationsClass(model);
		
		for (AssociationClass associationClass : associationsClass) {
			associationClasses.add(associationClassInterClassRelationshipBuilder.create(associationClass));
		}
		
		if(associationClasses.isEmpty()) return Collections.emptyList();
		return associationClasses;
	}

	private List<? extends InterClassRelationship> loadRealizations() {
		List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
		List<Realization> realizations = modelHelper.getAllRealizations(model);
		
		//Se tivermos uma relação de realization entre um package e uma classe 
		//a mesma é carregada como uma dependencia, por isso verificamos aqui se existe algum caso
		//destes dentro de dependencies.
		List<Dependency> depdencies = modelHelper.getAllDependencies(model);
		
		for (Dependency dependency : depdencies) {
			if(dependency instanceof Realization){
				interClassRelationships.add(realizationInterClassRelationshipBuilder.create((Realization)dependency));
			}
		}
		
		for (Realization realization : realizations)
			interClassRelationships.add(realizationInterClassRelationshipBuilder.create(realization));
		
		if (interClassRelationships.isEmpty()) return Collections.emptyList();
		return interClassRelationships;
	}

	private List<? extends InterClassRelationship> loadInterClassDependencies() {
		List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
		List<Dependency> dependencies = modelHelper.getAllDependencies(model);
		
		for (Dependency dependency : dependencies) 
			if(!(dependency instanceof Usage) && (!(dependency instanceof Realization)))
				interClassRelationships.add(dependencyInterClassRelationshipBuilder.create(dependency));
		

		if (interClassRelationships.isEmpty()) return Collections.emptyList();
		return interClassRelationships;
	}

	private List<? extends InterClassRelationship> loadGeneralizations() {
		List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
		List<EList<Generalization>> generalizations = modelHelper.getAllGeneralizations(model);
		
		for (EList<Generalization> eList : generalizations)
			for (Generalization generalization : eList) 
				interClassRelationships.add(generalizationInterClassRelationshipBuilder.create(generalization));
		
		if (interClassRelationships.isEmpty()) return Collections.emptyList();
		return interClassRelationships;
	}

	private List<InterClassRelationship> loadAssociations() {
		List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
		List<Association> associations = modelHelper.getAllAssociations(model);
		
		for (Association association : associations) 
			interClassRelationships.add(associationInterClassRelationshipBuilder.create(association));
		
		if (!interClassRelationships.isEmpty()) return interClassRelationships;
		return interClassRelationships;
	}

	private List<Variability> loadVariability() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		List<Variability> variabilities = new ArrayList<Variability>();
		List<org.eclipse.uml2.uml.Class> variabilitiesTemp = modelHelper.getAllClasses(model);
		
		for (Classifier classifier : variabilitiesTemp) 
			if(StereotypeHelper.isVariability(classifier))
				variabilities.add(variabilityBuilder.create(classifier));
		
		if (!variabilities.isEmpty()) return variabilities;
		return Collections.emptyList();
	}

	private List<? extends Element> loadClasses() {
		List<Class> listOfClasses = new ArrayList<Class>();
		List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(model);
		
		for (NamedElement element : classes)
			listOfClasses.add(classBuilder.create(element, null));
		
		if (!listOfClasses.isEmpty()) return listOfClasses;
		return listOfClasses;
	}

	/**
	 * Retornar todos os pacotes
	 * @return {@link Collection<mestrado.arquitetura.representation.Package>}
	 */
	private List<mestrado.arquitetura.representation.Package> loadPackages() {
		List<mestrado.arquitetura.representation.Package> packages = new ArrayList<mestrado.arquitetura.representation.Package>();
		List<Package> packagess = modelHelper.getAllPackages(model);
		
		for (NamedElement pkg : packagess)
			packages.add(packageBuilder.create(pkg, NO_PARENT));
		
		if (!packages.isEmpty()) return packages;
		return packages;
	}

	/**
	 * Inicializa os elementos da arquitetura. Instanciando as classes builders
	 * juntamente com suas depedências.
	 * 
	 * @param architecture
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 */
	private void initialize(Architecture architecture) throws ModelNotFoundException, ModelIncompleteException {
		classBuilder = new ClassBuilder(architecture);
		packageBuilder = new PackageBuilder(architecture, classBuilder);
		variabilityBuilder = new VariabilityBuilder(architecture);
		
		associationInterClassRelationshipBuilder = new AssociationInterClassRelationshipBuilder(classBuilder);
		generalizationInterClassRelationshipBuilder = new GeneralizationInterClassRelationshipBuilder(classBuilder, architecture);
		dependencyInterClassRelationshipBuilder = new DependencyInterClassRelationshipBuilder(classBuilder, architecture);
		realizationInterClassRelationshipBuilder = new RealizationInterClassRelationshipBuilder(classBuilder, packageBuilder);
		abstractionInterElementRelationshipBuilder = new AbstractionInterElementRelationshipBuilder(packageBuilder, classBuilder);
		dependencyPackageInterfaceRelationshipBuilder = new DependencyPackageInterfaceRelationshipBuilder(packageBuilder, classBuilder);
		associationClassInterClassRelationshipBuilder = new AssociationClassInterClassRelationshipBuilder(classBuilder);
		usageInterClassRelationshipBuilder = new UsageInterClassRelationshipBuilder(classBuilder, packageBuilder);
	}
	
}