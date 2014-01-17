package jmetal.operators.crossover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArchitectureSolutionType;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.exceptions.NotFoundException;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.Variability;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;

public class PLACrossover2 extends Crossover {

	private static final long serialVersionUID = -51015356906090226L;

	private Double crossoverProbability_ = null;
	private CrossoverUtils crossoverutils;

	private static List VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class);
	//use "oneLevel" para não verificar a presença de interesses nos atributos e métodos
	private static String SCOPE_LEVEL = "allLevels"; 
	private boolean variabilitiesOk = true;
	
	public PLACrossover2(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("probability") != null)
			crossoverProbability_ = (Double) getParameter("probability");
		
		crossoverutils = new CrossoverUtils();
	}
	
	public Object execute(Object object) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
		Solution [] parents = (Solution []) object;
		if (!(VALID_TYPES.contains(parents[0].getType().getClass())  && VALID_TYPES.contains(parents[1].getType().getClass())) ) {
			Configuration.logger_.severe("PLACrossover.execute: the solutions " + "are not of the right type. The type should be 'Permutation', but " + parents[0].getType() + " and " + parents[1].getType() + " are obtained");
		} 
		crossoverProbability_ = (Double)getParameter("probability");
		if (parents.length < 2){
			Configuration.logger_.severe("PLACrossover.execute: operator needs two " +	"parents");
			java.lang.Class<String> cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;      
		}

		Solution [] offspring = doCrossover(crossoverProbability_, parents[0], parents[1]); 
		return offspring; 
	} 
	
    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
        Solution[] offspring = new Solution[2];
        
        Solution[] crossFeature = this.crossoverFeatures(crossoverProbability_, parent1, parent2, SCOPE_LEVEL);
        offspring[0] = crossFeature[0];
        offspring[1] = crossFeature[1];
     
        return offspring;
    }
    
    public Solution[] crossoverFeatures(double probability, Solution parent1, Solution parent2, String scope) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {

    	// STEP 0: Create two offsprings
        Solution[] offspring = new Solution[2];
        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);
               
        try {
            if (parent1.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
               if (PseudoRandom.randDouble() < probability) {
              
                    // STEP 1: Get feature to crossover
                    List<Concern> concernsArchitecture = new ArrayList<Concern> (((Architecture) offspring[0].getDecisionVariables()[0]).getAllConcerns());
                    Concern feature = randomObject(concernsArchitecture);
                   
                    obtainChild(feature, (Architecture) parent2.getDecisionVariables()[0], (Architecture) offspring[0].getDecisionVariables()[0], scope);
                    //Thelma - Dez2013 adicionado para descartar as solucoes com interfaces desconectadas de componentes na PLA e com variabilidades cujos pontos de variacao não fazem parte da solucao
        	        if (!(isValidSolution((Architecture) offspring[0].getDecisionVariables()[0]))){
        	        	//offspring[0] = new Solution(parent1);
        	        	offspring[0] =parent1;
        	        	OPLA.contDiscardedSolutions_++;
        	        }
        	        this.variabilitiesOk = true;
                    obtainChild(feature, (Architecture) parent1.getDecisionVariables()[0], (Architecture) offspring[1].getDecisionVariables()[0], scope);
                    //Thelma - Dez2013 adicionado para descartar as solucoes com interfaces desconectadas de componentes na PLA e com variabilidades cujos pontos de variacao não fazem parte da solucao
        	        if (!(isValidSolution((Architecture) offspring[1].getDecisionVariables()[0]))){
        	        	//offspring[0] = new Solution(parent1);
        	        	offspring[0] =parent1;
        	        	OPLA.contDiscardedSolutions_++;
        	        }
        	        concernsArchitecture = null;
               }
            }
            else {
            	Configuration.logger_.log(Level.SEVERE, "PLACrossover.doCrossover: "+ "invalid type{0}", parent1.getDecisionVariables()[0].getVariableType());
            	java.lang.Class<String> cls = java.lang.String.class;
            	String name = cls.getName();
            	throw new JMException("Exception in " + name + ".doCrossover()");
            }
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();            
        }

        return offspring;
    }
    
    public void obtainChild(Concern feature, Architecture parent, Architecture offspring, String scope) throws ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException{
    	//eliminar os elementos arquiteturais que realizam feature em offspring
    	crossoverutils.removeArchitecturalElementsRealizingFeature(feature, offspring, scope);
		//adicionar em offspring os elementos arquiteturais que realizam feature em parent
		addElementsToOffspring(feature, offspring, parent, scope);
		this.variabilitiesOk = updateVariabilitiesOffspring(offspring);
    }

	public <T> T randomObject(List<T> allObjects)  throws JMException   {
	    int numObjects= allObjects.size(); 
	    int key;
	    T object;
	    if (numObjects == 0) {
	    	object = null;
	    } else{
	    	key = PseudoRandom.randInt(0, numObjects-1); 
	    	object = allObjects.get(key);
	    }
	    return object;      	    	
	}
	
	public void addElementsToOffspring(Concern feature, Architecture offspring, Architecture parent, String scope) {
		for(Package parentPackage : parent.getAllPackages()){
			//Cria ou adiciona o pacote de parent em offspring
			addOrCreatePackageIntoOffspring(feature, offspring, parent, parentPackage);
		}
		CrossoverRelationship.cleanRelationships();
		
	}

	public void addOrCreatePackageIntoOffspring(Concern feature, Architecture offspring, Architecture parent, Package parentPackage) {
		Package packageInOffspring = null;
		
		/*
		 * Caso parentPackage cuide somente de UM interesse. Tenta localizar Pacote em offspring
		 * Caso não encontrar o cria.
		 */
		if(parentPackage.containsConcern(feature) && (parentPackage.getOwnConcerns().size() == 1)){
			packageInOffspring = offspring.findPackageByName(parentPackage.getName());
			if(packageInOffspring == null)
				packageInOffspring = offspring.createPackage(parentPackage.getName());
			addImplementedInterfacesByPackageInOffspring(parentPackage, offspring, parent);
			addRequiredInterfacesByPackageInOffspring(parentPackage, offspring, parent);
			addInterfacesToPackageInOffSpring(parentPackage, packageInOffspring, offspring, parent);
			
			addClassesToOffspring(feature, parentPackage, packageInOffspring, offspring, parent);
		}else{
			addInterfacesRealizingFeatureToOffspring(feature, parentPackage, offspring, parent);
			addClassesRealizingFeatureToOffspring(feature, parentPackage, offspring, parent, SCOPE_LEVEL);
		}
		
		saveAllRelationshiopForElement(parentPackage, parent, offspring);
		
	}

	private void addClassesRealizingFeatureToOffspring(Concern feature,	Package parentPackage, Architecture offspring, Architecture parent,	String sCOPE_LEVEL2) {
    	Package newComp = null;
    	
		newComp = offspring.findPackageByName(parentPackage.getName());
    	
    	List<Class> allClasses = new ArrayList<Class> (parentPackage.getAllClasses());
		Iterator<Class> iteratorClasses = allClasses.iterator();
		
		while (iteratorClasses.hasNext()){
        	Class classComp = iteratorClasses.next();
    		if (classComp.containsConcern(feature) && classComp.getOwnConcerns().size() == 1){
    			if(newComp == null){
    				newComp = offspring.createPackage(parentPackage.getName());
    			}
        		if (!searchForGeneralizations(classComp)){
        			addClassToOffspring(classComp, newComp, offspring, parent);
        		} else {
        			if (this.isHierarchyInASameComponent(classComp,parent)){
        				moveHierarchyToSameComponent(classComp, newComp, parentPackage, offspring, parent, feature);
        				saveAllRelationshiopForElement(classComp, parent, offspring);
        			}else{
        				newComp.addExternalClass(classComp);
        				moveHierarchyToDifferentPackage(classComp, newComp, parentPackage, offspring, parent);
        				saveAllRelationshiopForElement(classComp, parent, offspring);
        			}
        		}
        	}	
    		else{
				if ((SCOPE_LEVEL.equals("allLevels")) && (!searchForGeneralizations(classComp))){
					addAttributesRealizingFeatureToOffspring(feature, classComp, parentPackage, offspring, parent);
					addMethodsRealizingFeatureToOffspring(feature, classComp, parentPackage, offspring, parent);
				}
    		}
    		addInterfacesImplementedByClass(classComp, offspring, parent, newComp);
    		addInterfacesRequiredByClass(classComp, offspring, parent, newComp);
		}
		allClasses = null;
		iteratorClasses = null;
	}
	
	public void addAttributesRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent) {

		Class targetClass;
		try {
			targetClass = offspring.findClassByName(classComp.getName()).get(0);
		} catch (ClassNotFound e1) {
			targetClass = null;
		}
		List<Attribute> allAttributes = new ArrayList<Attribute>(classComp.getAllAttributes());
		if (!allAttributes.isEmpty()) {
			Iterator<Attribute> iteratorAttributes = allAttributes.iterator();
			try{
				while (iteratorAttributes.hasNext()) {
					Attribute attribute = iteratorAttributes.next();
					if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size() == 1) {
						if (targetClass == null) {
							Package newComp = null;
							newComp = offspring.findPackageByName(comp.getName());
							if(newComp == null)
								newComp = offspring.createPackage(comp.getName());
							targetClass = newComp.createClass(classComp.getName(), false);
							targetClass.addConcern(feature.getName());
							saveAllRelationshiopForElement(classComp, parent, offspring);
							saveAllRelationshiopForElement(newComp, parent, offspring);
						}
						classComp.moveAttributeToClass(attribute, targetClass);
						saveAllRelationshiopForElement(classComp, parent, offspring);
					}
				}
			}catch(Exception e){
				System.err.println(e);
			}
		}
		allAttributes = null;
	}
	
    private void addMethodsRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent){
    	Class targetClass;
		try {
			targetClass = offspring.findClassByName(classComp.getName()).get(0);
		} catch (ClassNotFound e2) {
			targetClass = null;
		}
    	
    	List<Method> allMethods = new ArrayList<Method> (classComp.getAllMethods());
		if (!allMethods.isEmpty()) {
			Iterator<Method> iteratorMethods = allMethods.iterator();
			try{
				while (iteratorMethods.hasNext()){
					Method method= iteratorMethods.next();
	            	if (method.containsConcern(feature) && method.getOwnConcerns().size()==1){
	        			if (targetClass == null){
	        				Package newComp;
							newComp = offspring.findPackageByName(comp.getName());
							if(newComp == null)
								newComp = offspring.createPackage(comp.getName());
							targetClass=newComp.createClass(classComp.getName(), false);
							targetClass.addConcern(feature.getName());
	            		}
	        			saveAllRelationshiopForElement(classComp, parent,offspring);
	            		classComp.moveMethodToClass(method, targetClass);
	            	}
				}
			}catch(Exception e){
				System.err.println(e);
			}
			iteratorMethods = null;
		}
		allMethods = null;
		
    }

	/**
	 * Adicionar as interfaces que o pacote possuia em parent no pacote em offspring
	 * 
	 * @param parentPackage
	 * @param packageInOffspring
	 * @param offspring
	 * @param parent
	 */
    private void addInterfacesToPackageInOffSpring(Package parentPackage,Package packageInOffspring, Architecture offspring, Architecture parent) {
    	Set<Interface> interfacesOfPackage = parentPackage.getAllInterfaces();
    	for(Interface inter : interfacesOfPackage){
    		packageInOffspring.addExternalInterface(inter);
    		saveAllRelationshiopForElement(inter, parent,offspring);
    	}
    	interfacesOfPackage = null;
	}

	private void addInterfacesRealizingFeatureToOffspring (Concern feature, Package comp, Architecture offspring, Architecture parent) {
    	Package newComp;
    	List<Interface> allInterfaces = new ArrayList<Interface> (comp.getOnlyInterfacesImplementedByPackage());
    	
		Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
		while (iteratorInterfaces.hasNext()){
			Interface interfaceComp = iteratorInterfaces.next();
        	if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1){
				newComp = offspring.findPackageByName(comp.getName());
    			if (newComp == null){
					newComp = offspring.createPackage(comp.getName());
					saveAllRelationshiopForElement(newComp, parent, offspring);
    			}
    			if(interfaceComp.getNamespace().equalsIgnoreCase("model"))
    				offspring.addExternalInterface(interfaceComp);
    			else{
					String interfaceCompPackageName = UtilResources.extractPackageName(interfaceComp.getNamespace());
					Package packageToAddInterface = findOrCreatePakage(interfaceCompPackageName, offspring);
					interfaceComp = packageToAddInterface.createInterface(interfaceComp.getName());
    			}
    			saveAllRelationshiopForElement(interfaceComp, parent, offspring);
        	}else{
        		addOperationsRealizingFeatureToOffspring(feature, interfaceComp, comp, offspring, parent);
        	}
		}
		allInterfaces = null;
		iteratorInterfaces = null;
    }
    
    private void addOperationsRealizingFeatureToOffspring(Concern feature, Interface interfaceComp, Package comp, Architecture offspring, Architecture parent) {
    	Interface targetInterface = null;
    	
		try {
			targetInterface = offspring.findInterfaceByName(interfaceComp.getName());
		} catch (InterfaceNotFound e1) { targetInterface = null;}
		
    	List<Method> allOperations = new ArrayList<Method> (interfaceComp.getOperations());
		Iterator<Method> iteratorOperations = allOperations.iterator();
		try{
			while (iteratorOperations.hasNext()){
				Method operation = iteratorOperations.next();
	        	if (operation.containsConcern(feature) && operation.getOwnConcerns().size() == 1){
	        		if (targetInterface == null){
	        			Package newComp;
						newComp = offspring.findPackageByName(comp.getName());
						
	        			if (newComp == null) {
							newComp = offspring.createPackage(comp.getName());
							saveAllRelationshiopForElement(newComp, parent, offspring);
	        			}
	    				if(interfaceComp.getNamespace().equalsIgnoreCase("model"))
	    					targetInterface = offspring.createInterface(interfaceComp.getName());
	    				else{
	    					String interfaceCompPackageName = UtilResources.extractPackageName(interfaceComp.getNamespace());
	    					Package packageToAddInterface = findOrCreatePakage(interfaceCompPackageName, offspring);
	    					targetInterface = packageToAddInterface.createInterface(interfaceComp.getName());
	    				}
						targetInterface.addConcern(feature.getName());
						saveAllRelationshiopForElement(interfaceComp, parent, offspring);
	        		}
	        		interfaceComp.moveOperationToInterface(operation, targetInterface);
	        	}
			}
		}catch(Exception e ){
			System.err.println(e);
		}
		allOperations = null;
    }    
    
    private Package findOrCreatePakage(String packageName,Architecture offspring) {
    	Package pkg = null;
    	pkg = offspring.findPackageByName(packageName);
    	if(pkg == null)
    		return  offspring.createPackage(packageName);
    	return pkg;
	}

	/**
     * Adiciona as classes do pacote em parent no pacote em offspring
     * 
     * @param feature
     * @param parentPackage
     * @param packageInOffspring
     * @param offspring
     * @param parent
     */
	private void addClassesToOffspring(Concern feature, Package parentPackage, Package packageInOffspring, Architecture offspring, Architecture parent) {
    	List<Class> allClasses = new ArrayList<Class> (parentPackage.getAllClasses());
		Iterator<Class> iteratorClasses = allClasses.iterator();
		while (iteratorClasses.hasNext()){
        	Class classComp = iteratorClasses.next();
    		if (!searchForGeneralizations(classComp)){
    			addClassToOffspring(classComp, packageInOffspring, offspring, parent);
    		} else {	
    			if (this.isHierarchyInASameComponent(classComp, parent)){
    				moveHierarchyToSameComponent(classComp, packageInOffspring, parentPackage, offspring, parent, feature);
    			}else{
    				packageInOffspring.addExternalClass(classComp);
    				moveHierarchyToDifferentPackage(classComp, packageInOffspring, parentPackage, offspring, parent);
    			}
    			saveAllRelationshiopForElement(classComp, parent, offspring);
    		}
    		addInterfacesImplementedByClass(classComp, offspring, parent, parentPackage);
    		addInterfacesRequiredByClass(classComp, offspring, parent, parentPackage);
        }
		allClasses = null;
	}

	/**
	 * Adicionar as interfaces implementadas pelo PACOTE em parent a offspring.
	 * 
	 * @param parentPackage
	 * @param offspring
	 * @param parent
	 */
	private void addImplementedInterfacesByPackageInOffspring(Package parentPackage, Architecture offspring, Architecture parent) {
		final List<Interface> allInterfaces = new ArrayList<Interface>(parentPackage.getOnlyInterfacesImplementedByPackage());
		
		if(!allInterfaces.isEmpty()){
			final Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
			while (iteratorInterfaces.hasNext()) {
				final Interface interfaceComp = iteratorInterfaces.next();
				if(interfaceComp.getNamespace().equalsIgnoreCase("model"))
					offspring.addExternalInterface(interfaceComp);
				else{
					final String interfaceCompPackageName = UtilResources.extractPackageName(interfaceComp.getNamespace());
					final Package packageToAddInterface = findOrCreatePakage(interfaceCompPackageName, offspring);
					packageToAddInterface.addExternalInterface(interfaceComp);
				}
				saveAllRelationshiopForElement(interfaceComp, parent, offspring);
			}
		}
	}
	
	private void addRequiredInterfacesByPackageInOffspring(Package parentPackage, Architecture offspring, Architecture parent) {
		final List<Interface> allInterfaces = new ArrayList<Interface>(parentPackage.getOnlyInterfacesRequiredByPackage());
		
		if(!allInterfaces.isEmpty()){
			final Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
			while (iteratorInterfaces.hasNext()) {
				final Interface interfaceComp = iteratorInterfaces.next();
				if(interfaceComp.getNamespace().equalsIgnoreCase("model"))
					offspring.addExternalInterface(interfaceComp);
				else{
					final String interfaceCompPackageName = UtilResources.extractPackageName(interfaceComp.getNamespace());
					final Package packageToAddInterface = findOrCreatePakage(interfaceCompPackageName, offspring);
					packageToAddInterface.addExternalInterface(interfaceComp);
				}
				saveAllRelationshiopForElement(interfaceComp, parent, offspring);
			}
		}
	}
	
    private boolean searchForGeneralizations(Class cls){
    	final Collection<Relationship> relationships = cls.getRelationships();
    	for (Relationship relationship: relationships){
	    	if (relationship instanceof GeneralizationRelationship){
	    		final GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
	    		if (generalization.getChild().equals(cls) || generalization.getParent().equals(cls))
	    			return true;
	    	}
	    }
    	return false;
    }
    
	private boolean isHierarchyInASameComponent (Class class_, Architecture architecture){
		boolean sameComponent = true;
		Class parent = class_;
		Package componentOfClass = null;
		componentOfClass = architecture.findPackageOfClass(class_);
		Package componentOfParent = componentOfClass;
		while (CrossoverOperations.isChild(parent)){
			parent = CrossoverOperations.getParent(parent);	
			componentOfParent = architecture.findPackageOfClass(parent);
			if (!(componentOfClass.equals(componentOfParent))){
				sameComponent = false;
				return false;
			}
		}
		return sameComponent;
	}
	
    private void moveChildrenToSameComponent(Class parent, Package sourceComp, Package targetComp, Architecture offspring, Architecture parentArch){
	  	
		final Collection<Element> children = CrossoverOperations.getChildren(parent);
		//move cada subclasse
		for (Element child: children){
			moveChildrenToSameComponent((Class) child, sourceComp, targetComp, offspring, parentArch);			
		}	
		//move a super classe
		if (sourceComp.getAllClasses().contains(parent)){
			addClassToOffspring(parent, targetComp, offspring, parentArch);
		} else{
			try{
				for (Package auxComp: parentArch.getAllPackages()){
					if (auxComp.getAllClasses().contains(parent)){
						sourceComp = auxComp;
						if (sourceComp.getName()!=targetComp.getName()){
							targetComp = offspring.findPackageByName(sourceComp.getName());
							if (targetComp == null){
								targetComp=offspring.createPackage(sourceComp.getName());
								for (Concern feature:sourceComp.getOwnConcerns())
									targetComp.addConcern(feature.getName());
								}
							}
						}
						addClassToOffspring(parent, targetComp, offspring, parentArch);
						break;
					}
			}catch (Exception e) {
				System.err.println(e);
			}
    	}
    }
    
	private void moveChildrenToDifferentComponent(Class root, Package newComp, Architecture offspring, Architecture parent) {
		final Collection<Element> children = CrossoverOperations.getChildren(root);
		
		final String rootPackageName = UtilResources.extractPackageName(root.getNamespace());
		Package rootTargetPackage = null;
			rootTargetPackage = offspring.findPackageByName(rootPackageName);
			if(rootPackageName == null)
				rootTargetPackage = offspring.createPackage(rootPackageName);
		
		addClassToOffspring(root, rootTargetPackage, offspring, parent);
		
		saveAllRelationshiopForElement(parent.findPackageByName(rootPackageName), parent, offspring);
		for (Element child: children){
			final String packageName = UtilResources.extractPackageName(child.getNamespace());
			Package targetPackage = null;
			targetPackage = parent.findPackageByName(packageName);
			if(targetPackage != null)
				moveChildrenToDifferentComponent((Class) child, targetPackage, offspring, parent);			
		}
	}
    
    /**
     * Adicionar klass a targetComp em offspring.
     * 
     * @param klass
     * @param targetComp
     * @param offspring
     * @param parent
     */
	public void addClassToOffspring(Class klass, Package targetComp, Architecture offspring, Architecture parent){
		targetComp.addExternalClass(klass);
		saveAllRelationshiopForElement(klass, parent, offspring);
	}
	
	/**
	 * Adiciona as interfaces implementadas por klass em offspring.
	 * 
	 * @param klass
	 * @param offspring
	 * @param parent
	 * @param targetComp
	 */
    private void addInterfacesImplementedByClass(Class klass, Architecture offspring, Architecture parent, Package targetComp) {
    	final Set<Interface> interfaces = klass.getImplementedInterfaces();
    	
    	for(Interface itf : interfaces){
    		if(itf.getNamespace().equalsIgnoreCase("model"))
    			offspring.addExternalInterface(itf);
    		else{
    			final String interfaceCompPackageName = UtilResources.extractPackageName(itf.getNamespace());
				Package packageToAddInterface = findOrCreatePakage(interfaceCompPackageName, offspring);
				packageToAddInterface.addExternalInterface(itf);
    		}
    		saveAllRelationshiopForElement(itf, parent, offspring);
    	}
	}
    
    /**
     * Adiciona as interfaces requeridas por klass em offspring
     * 
     * @param klass
     * @param offspring
     * @param parent
     * @param targetComp
     */
    private void addInterfacesRequiredByClass(Class klass, Architecture offspring, Architecture parent, Package targetComp) {
    	final Set<Interface> interfaces = klass.getRequiredInterfaces();
    	
    	for(Interface itf : interfaces){
    		if(itf.getNamespace().equalsIgnoreCase("model"))
    			offspring.addExternalInterface(itf);
    		else{
    			final String interfaceCompPackageName = UtilResources.extractPackageName(itf.getNamespace());
				final Package packageToAddInterface = findOrCreatePakage(interfaceCompPackageName, offspring);
				packageToAddInterface.addExternalInterface(itf);
    		}
    		saveAllRelationshiopForElement(itf, parent, offspring);
    	}
	}
    
    private  void moveHierarchyToSameComponent(Class classComp, Package targetComp, Package sourceComp, Architecture offspring, Architecture parent, Concern concern){
    	Class root = classComp;
		while (isChild(root)){
			root = getParent(root);		
		} 
		if (sourceComp.getAllClasses().contains(root)){
			moveChildrenToSameComponent(root, sourceComp, targetComp, offspring, parent);
		} 				
	}
    
	private void moveHierarchyToDifferentPackage(Class classComp, Package newComp, Package parentPackage, Architecture offspring, Architecture parent) {
		Class root = classComp;
		while(isChild(root)){
			root = getParent(root);
		}
		moveChildrenToDifferentComponent(root, newComp, offspring, parent);
	}
	
	public static  boolean isChild(Class cls){
    	boolean child=false;
    	
    	for (Relationship relationship: cls.getRelationships()){
  	    	if (relationship instanceof GeneralizationRelationship){
  	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
  	    		if (generalization.getChild().equals(cls)) {
  	    			child = true;
  	    			return child;
  	    		}
  	    	}
  	    }
    	return child;
    }
    
	public static Class getParent(Class cls){
	  Class parent = null;
	  for (Relationship relationship: cls.getRelationships()){
	    	if (relationship instanceof GeneralizationRelationship){
	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
	    		if (generalization.getChild().equals(cls)){
	    			parent = (Class) generalization.getParent();
	    			return parent;
	    		}
	    	}
	  }	    			
	  return parent;
	}
	
	private boolean updateVariabilitiesOffspring(Architecture offspring){ 
		boolean variabilitiesOk = true;
		try{
			for (Variability variability: offspring.getAllVariabilities()){	    	
				final VariationPoint variationPoint = variability.getVariationPoint();
				if(variationPoint != null){
					Element elementVP = variationPoint.getVariationPointElement();
					Element VP = offspring.findElementByName(elementVP.getName());
					if (!(VP.equals(elementVP)))
						variationPoint.replaceVariationPointElement(offspring.findElementByName(elementVP.getName(), "class"));
				}
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return variabilitiesOk;
	}
	
	// Thelma - Dez2013 método adicionado
	// verify if the architecture contains a valid PLA design, i.e., if there is not any interface without relationships in the architecture. 
	private boolean isValidSolution(Architecture solution){
		boolean isValid=true;
			
		final List<Interface> allInterfaces = new ArrayList<Interface> (solution.getAllInterfaces());
		if (!allInterfaces.isEmpty()){
			for (Interface itf: allInterfaces){
				if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getOperations().isEmpty())){
					return false;
				}
			}
		}
		if (!(this.variabilitiesOk))
			return false;
		return isValid;
	}
	
	
	
	private void saveAllRelationshiopForElement(Element element, Architecture parent, Architecture offspring) {
		
		if(element instanceof Package){
			final Package packagee = (Package)element;
			final Set<Relationship> relations = packagee.getRelationships();
			for(Relationship r : relations)
				offspring.getRelationshipHolder().addRelationship(r);
			
			return ;
		}
		if(element instanceof Class){
			final Class klass = (Class)element;
			final Set<Relationship> relations = klass.getRelationships();
			for(Relationship r : relations)
				offspring.getRelationshipHolder().addRelationship(r);
			
			return ;
		}
		if(element instanceof Interface){
			final Interface inter = (Interface)element;
			final Set<Relationship> relations = inter.getRelationships();
			for(Relationship r : relations)
				offspring.getRelationshipHolder().addRelationship(r);
			
			return ;
		}
	}
}