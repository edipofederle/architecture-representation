package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.InterClassRelationship;
import mestrado.arquitetura.representation.Variability;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

/**
 * Builder resposável por criar a arquitetura.
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
	private GeneralizationInterClassRelationshipBuilder generalizationInterClassRelationshipBuilder;
	
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
	 * Cria a arquitetura.
	 * 
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
		architecture.getVariability().addAll(loadVariability());
		
		architecture.getInterClassRelationships().addAll(loadInterClassRelationships());
		
		
		return architecture;
	}

	private List<InterClassRelationship> loadInterClassRelationships() {
		List<InterClassRelationship> relationships = new ArrayList<InterClassRelationship>();
		relationships.addAll(loadGeneralizations());
		relationships.addAll(loadAssociations());
		return relationships;
	}

	private List<? extends InterClassRelationship> loadGeneralizations() {
		List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
		
		List<EList<Generalization>> generalizations = modelHelper.getAllGeneralizations(model);
		
		for (EList<Generalization> eList : generalizations) {
			for (Generalization generalization : eList) {
				interClassRelationships.add(generalizationInterClassRelationshipBuilder.create(generalization));
			}
		}
		
		return interClassRelationships;
	}

	private List<InterClassRelationship> loadAssociations() {
		List<InterClassRelationship> interClassRelationships = new ArrayList<InterClassRelationship>();
		
		List<Association> associations = modelHelper.getAllAssociations(model);
		
		for (Association association : associations) 
			interClassRelationships.add(associationInterClassRelationshipBuilder.create(association));
		
		return interClassRelationships;
	}

	private List<Variability> loadVariability() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		List<Variability> variabilities = new ArrayList<Variability>();
		
		List<Classifier> variabilitiesTemp = modelHelper.getAllClasses(model);
		for (Classifier classifier : variabilitiesTemp) 
			if(StereotypeHelper.isVariability(classifier))
				variabilities.add(variabilityBuilder.create(classifier));
		
		if (!variabilities.isEmpty()) return variabilities;
		
		return Collections.emptyList();
	}

	private Collection<? extends Element> loadClasses() {
		List<Class> listOfClasses = new ArrayList<Class>();
		List<Classifier> classes = modelHelper.getAllClasses(model);
		for (NamedElement element : classes)
			listOfClasses.add(classBuilder.create(element, null));
		
		return listOfClasses;
	}

	/**
	 * Retornar todos os pacotes
	 * @return {@link Collection<mestrado.arquitetura.representation.Package>}
	 */
	private Collection<mestrado.arquitetura.representation.Package> loadPackages() {
		Set<mestrado.arquitetura.representation.Package> packages = new HashSet<mestrado.arquitetura.representation.Package>();
		List<Classifier> packagess = modelHelper.getAllPackages(model);
		for (NamedElement pkg : packagess)
			packages.add(packageBuilder.create(pkg, NO_PARENT));
		
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
		generalizationInterClassRelationshipBuilder = new GeneralizationInterClassRelationshipBuilder(classBuilder);
		
	}
	
}