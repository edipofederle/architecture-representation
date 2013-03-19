package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.ModelElementHelper;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Interface;
import mestrado.arquitetura.representation.Variability;
import mestrado.arquitetura.representation.relationship.Relationship;

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
public class ArchitectureBuilder extends RelationshipBase {
	
	private static final Element NO_PARENT = null;
	private Package model;
	private PackageBuilder packageBuilder;
	private ClassBuilder classBuilder;
	private InterfaceBuilder intefaceBuilder;
	private VariabilityBuilder variabilityBuilder;
	private AssociationRelationshipBuilder associationRelationshipBuilder;
	private AssociationClassRelationshipBuilder associationClassRelationshipBuilder;
	private GeneralizationRelationshipBuilder generalizationRelationshipBuilder;
	private DependencyRelationshipBuilder dependencyRelationshipBuilder;
	private RealizationRelationshipBuilder realizationRelationshipBuilder;
	private AbstractionRelationshipBuilder abstractionRelationshipBuilder; 
	private UsageRelationshipBuilder usageRelationshipBuilder;

	
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
		model = getModelHelper().getModel(xmiFilePath);
		Architecture architecture = new Architecture(getModelHelper().getName(xmiFilePath));
		
		initialize(architecture);
		
		architecture.getElements().addAll(loadPackages()); // Classes que possuem pacotes são carregadas juntamente com seus pacotes
		architecture.getElements().addAll(loadClasses()); // Classes que nao possuem pacotes
		architecture.getElements().addAll(loadInterfaces());
		architecture.getVariabilities().addAll(loadVariability());
		
		architecture.getInterClassRelationships().addAll(loadInterClassRelationships());
		
		return architecture;
	}

	private List<? extends Relationship> loadAbstractions() {
		List<Abstraction> abstractions = getModelHelper().getAllAbstractions(model);
		List<Relationship> relations = new ArrayList<Relationship>();
		List<Package> pacakges = getModelHelper().getAllPackages(model);
		
		for (Package package1 : pacakges) {
			List<Abstraction> abs = getModelHelper().getAllAbstractions(package1);
			for (Abstraction abstraction : abs)
				relations.add(abstractionRelationshipBuilder.create(abstraction));
		}
		
		for (Abstraction abstraction : abstractions)
			relations.add(abstractionRelationshipBuilder.create(abstraction));
		
		if (relations.isEmpty()) return Collections.emptyList();
		return relations;
	}

	private List<Relationship> loadInterClassRelationships() {
		List<Relationship> relationships = new ArrayList<Relationship>();
		relationships.addAll(loadGeneralizations());
		relationships.addAll(loadAssociations());
		relationships.addAll(loadAssociationClassAssociation());
		relationships.addAll(loadInterClassDependencies()); //Todo renomear carrega todo tipo de depdenencias( pacote -> classe, class -> pacote)
		relationships.addAll(loadRealizations());
		relationships.addAll(loadUsageInterClass());
		relationships.addAll(loadAbstractions());
		
		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<? extends Relationship> loadUsageInterClass() {
		List<Relationship> usageClass = new ArrayList<Relationship>();
		List<Usage> usages = getModelHelper().getAllUsage(model);
		
		List<Package> pacotes = getModelHelper().getAllPackages(model);
		for (Package package1 : pacotes)
			for(Usage u : getModelHelper().getAllUsage(package1))
				usageClass.add(usageRelationshipBuilder.create(u));
		
		for (Usage usage : usages) 
				usageClass.add(usageRelationshipBuilder.create(usage));
		
		if (usageClass.isEmpty()) return Collections.emptyList();
		return usageClass;
	}

	private List<? extends Relationship> loadAssociationClassAssociation() {
		List<Relationship> associationClasses = new ArrayList<Relationship>();
		List<AssociationClass> associationsClass = getModelHelper().getAllAssociationsClass(model);
		
		for (AssociationClass associationClass : associationsClass) {
			associationClasses.add(associationClassRelationshipBuilder.create(associationClass));
		}
		
		if(associationClasses.isEmpty()) return Collections.emptyList();
		return associationClasses;
	}

	private List<? extends Relationship> loadRealizations() {
		List<Relationship> relationships = new ArrayList<Relationship>();
		List<Realization> realizations = getModelHelper().getAllRealizations(model);
		
		//Se tivermos uma relação de realization entre um package e uma classe 
		//a mesma é carregada como uma dependencia, por isso verificamos aqui se existe algum caso
		//destes dentro de dependencies.
		List<Dependency> depdencies = getModelHelper().getAllDependencies(model);
		
		for (Dependency dependency : depdencies) 
			if(dependency instanceof Realization)
				relationships.add(realizationRelationshipBuilder.create((Realization)dependency));
		
		for (Realization realization : realizations)
			relationships.add(realizationRelationshipBuilder.create(realization));
		
		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<? extends Relationship> loadInterClassDependencies() {
		List<Relationship> relationships = new ArrayList<Relationship>();
		List<Dependency> dependencies = getModelHelper().getAllDependencies(model);
//		List<Package> packages = getModelHelper().getAllPackages(model);
//		
//		for (Package pack : packages)
//			if(!pack.getClientDependencies().isEmpty())
//				if(!pack.getClientDependencies().get(0).getClients().isEmpty())
//					if(ModelElementHelper.isInterface(pack.getClientDependencies().get(0).getSuppliers().get(0)))
//						interClassRelationships.add(dependencyInterClassRelationshipBuilder.create(pack.getClientDependencies().get(0)));
//		
		for (Dependency dependency : dependencies) 
			if(!(dependency instanceof Usage) && (!(dependency instanceof Realization)))
				relationships.add(dependencyRelationshipBuilder.create(dependency));
		

		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<? extends Relationship> loadGeneralizations() {
		List<Relationship> relationships = new ArrayList<Relationship>();
		List<EList<Generalization>> generalizations = getModelHelper().getAllGeneralizations(model);
		
		for (EList<Generalization> eList : generalizations)
			for (Generalization generalization : eList) 
				relationships.add(generalizationRelationshipBuilder.create(generalization));
		
		if (relationships.isEmpty()) return Collections.emptyList();
		return relationships;
	}

	private List<Relationship> loadAssociations() {
		List<Relationship> relationships = new ArrayList<Relationship>();
		List<Association> associations = getModelHelper().getAllAssociations(model);
		
		for (Association association : associations) 
			relationships.add(associationRelationshipBuilder.create(association));
		
		if (!relationships.isEmpty()) return relationships;
		return relationships;
	}

	private List<Variability> loadVariability() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		List<Variability> variabilities = new ArrayList<Variability>();
		List<org.eclipse.uml2.uml.Class> variabilitiesTemp = getModelHelper().getAllClasses(model);
		
		for (Classifier classifier : variabilitiesTemp) 
			if(StereotypeHelper.isVariability(classifier))
				variabilities.add(variabilityBuilder.create(classifier));
		
		if (!variabilities.isEmpty()) return variabilities;
		return Collections.emptyList();
	}

	private List<? extends Element> loadClasses() {
		List<Class> listOfClasses = new ArrayList<Class>();
		List<org.eclipse.uml2.uml.Class> classes = getModelHelper().getAllClasses(model);
		
		for (NamedElement element : classes)
			if(!ModelElementHelper.isInterface(element))
				listOfClasses.add(classBuilder.create(element, null)); //TODO verificar parent ( owner )
		
		if (!listOfClasses.isEmpty()) return listOfClasses;
		return listOfClasses;
	}
	
	private List<? extends Element> loadInterfaces() {
		List<Interface> listOfInterfaces = new ArrayList<Interface>();
		List<org.eclipse.uml2.uml.Class> classes = getModelHelper().getAllClasses(model);
		
		for (org.eclipse.uml2.uml.Class class1 : classes) 
			if(ModelElementHelper.isInterface((NamedElement)class1))
				listOfInterfaces.add(intefaceBuilder.create(class1, null));
		
		return listOfInterfaces;
	}

	/**
	 * Retornar todos os pacotes
	 * @return {@link Collection<mestrado.arquitetura.representation.Package>}
	 */
	private List<mestrado.arquitetura.representation.Package> loadPackages() {
		List<mestrado.arquitetura.representation.Package> packages = new ArrayList<mestrado.arquitetura.representation.Package>();
		List<Package> packagess = getModelHelper().getAllPackages(model);
		
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
		intefaceBuilder = new InterfaceBuilder(architecture);
		packageBuilder = new PackageBuilder(architecture, classBuilder);
		variabilityBuilder = new VariabilityBuilder(architecture);
		
		associationRelationshipBuilder = new AssociationRelationshipBuilder(classBuilder);
		generalizationRelationshipBuilder = new GeneralizationRelationshipBuilder(classBuilder, architecture);
		dependencyRelationshipBuilder = new DependencyRelationshipBuilder(classBuilder, architecture, packageBuilder, intefaceBuilder);
		realizationRelationshipBuilder = new RealizationRelationshipBuilder(classBuilder, packageBuilder);
		abstractionRelationshipBuilder = new AbstractionRelationshipBuilder(packageBuilder, classBuilder, intefaceBuilder);
		associationClassRelationshipBuilder = new AssociationClassRelationshipBuilder(classBuilder);
		usageRelationshipBuilder = new UsageRelationshipBuilder(classBuilder, packageBuilder);
		
	}
	
}