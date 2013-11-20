package jmetal.operators.mutation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import jmetal.core.Solution;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;

public class PLAFeatureMutation extends Mutation {

	private static final long serialVersionUID = 9039316729379302747L;
	
	private Double mutationProbability_ = null ;
	
	public PLAFeatureMutation(HashMap<String, Object> parameters) {    
	  	super(parameters) ;
	  	
	  	if (parameters.get("probability") != null)
	  		mutationProbability_ = (Double) parameters.get("probability") ;  		
	  } 
        
    public void doMutation(double probability, Solution solution) throws Exception {
        String scope = "sameComponent"; //"allComponents" usar "sameComponent" para que a troca seja realizada dentro do mesmo componente da arquitetura
    	String scopeLevels = "allLevels"; //usar "oneLevel" para não verificar a presença de interesses nos atributos e métodos

  
    	int r = 2;
    	switch(r){
    //    case 0: FeatureMutation(probability, solution, scopeLevels); break;
  //        case 1: MoveMethodMutation(probability, solution, scope); break;
        case 2: MoveAttributeMutation(probability, solution, scope); break;
//        case 3: MoveOperationMutation(probability, solution); break;
//        case 4: AddClassMutation(probability, solution, scope); break;
//        case 5: AddManagerClassMutation(probability, solution); break;
        }
    	
    	    	
    }

    //--------------------------------------------------------------------------
    //método para verificar se algum dos relacionamentos recebidos � generaliza��o
    private boolean searchForGeneralizations(Class cls){
    	Collection<Relationship> Relationships = cls.getRelationships();
    	for (Relationship relationship: Relationships){
	    	if (relationship instanceof GeneralizationRelationship){
	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
	    		if (generalization.getChild().equals(cls) || generalization.getParent().equals(cls))
	    		return true;
	    	}
	    }
    	return false;
    }
    
    public void MoveAttributeMutation(double probability, Solution solution, String scope) throws JMException{
    	try { 
    		if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
            	Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            	            	 
            	if (PseudoRandom.randDouble() < probability) {
            	if (scope == "sameComponent") {
            		Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));            	    	
            		List<Class> ClassesComp = new ArrayList<Class> (sourceComp.getClasses());
            		if (ClassesComp.size() > 1) {
            			Class targetClass = randomObject(ClassesComp);
            		    Class sourceClass = randomObject(ClassesComp);
            		    if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass))&& (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){ //sourceClass n�o tem relacionamentos de generaliza��o
            		    	if ((targetClass!=null) && (!(targetClass.equals(sourceClass)))) 
            		    		moveAttribute(arch, targetClass, sourceClass);
            			}
            		}
            		    
            	} else{//considerando todos os componentes
            		if (scope == "allComponents") {
            			Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages())); 
            			List<Class> ClassesSourceComp = new ArrayList<Class> (sourceComp.getClasses());
            			if (ClassesSourceComp.size() >=1) {
            				Class sourceClass = randomObject(ClassesSourceComp);
            				if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass))&& (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){ //sourceClass n�o tem relacionamentos de generaliza��o{
            					Package targetComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
            					if (checkSameLayer(sourceComp,targetComp)){
	                				List<Class> ClassesTargetComp = new ArrayList<Class> (targetComp.getClasses());
	                				if (ClassesTargetComp.size() >=1) {
	                					Class targetClass = randomObject(ClassesTargetComp);
	                					if ((targetClass!=null) &&  (!(targetClass.equals(sourceClass)))) 
	                						moveAttribute(arch, targetClass, sourceClass);
	                				}
            					}
            				}            				
            			}
            		}
            	}
            	}//pseudoRandom
            } else {
                Configuration.logger_.log(Level.SEVERE, "MoveAttributeMutation.doMutation: invalid type. "+ "{0}", solution.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
            
    }

	private void moveAttribute(Architecture arch, Class targetClass, Class sourceClass) throws JMException, Exception {
		
		List<Attribute> attributesClass = new ArrayList<Attribute> (sourceClass.getAllAttributes());
		if (attributesClass.size() >=1 ){
			Attribute targetAttribute = randomObject(attributesClass);
			if (sourceClass.moveAttributeToClass(targetAttribute,targetClass)){
				AssociationRelationship newRelationship = new AssociationRelationship(targetClass, sourceClass); 
				arch.getAllRelationships().add(newRelationship);
			}
		}
	}
    
  //--------------------------------------------------------------------------
    
    public void MoveMethodMutation(double probability, Solution solution, String scope) throws JMException{
    	try { 
            if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
            	Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            	
            	if (PseudoRandom.randDouble() < probability) {
            	if (scope == "sameComponent") {
            		Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
            		List<Class> ClassesComp = new ArrayList<Class> (sourceComp.getClasses());
            	   	if (ClassesComp.size() > 1) {
            	   		Class targetClass = randomObject(ClassesComp);
            	   		Class sourceClass = randomObject(ClassesComp);
            	   		if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass)) && (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){
            	   			if ((targetClass!=null) && (!(targetClass.equals(sourceClass)))) 
                	   			moveMethod(arch, targetClass, sourceClass, sourceComp, sourceComp);
            	   		}
            	   	}
            	} else {
            		if (scope == "allComponents") {
            			Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages())); 
            			List<Class> ClassesSourceComp = new ArrayList<Class> (sourceComp.getClasses());
            			if (ClassesSourceComp.size()>=1) {
                			Class sourceClass = randomObject(ClassesSourceComp);
                			if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass)) && (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){
                				Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
                				if (checkSameLayer(sourceComp,targetComp)){
	                    			List<Class> ClassesTargetComp = new ArrayList<Class> (targetComp.getClasses());
	                    			if (ClassesTargetComp.size() >=1) {
	                    				Class targetClass = randomObject(ClassesTargetComp);
	                    				if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) 
	                    						moveMethod(arch, targetClass, sourceClass, targetComp, sourceComp);
	                    			}
                				}
                			}                			
                		}
            		}
            	}
            	}//PseudoRandom
            } else {
                Configuration.logger_.log(
                        Level.SEVERE, "MoveMethodMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void moveMethod(Architecture arch, Class targetClass, Class sourceClass, Package targetComp, Package sourceComp) throws JMException, Exception {
		List<Method> MethodsClass = new ArrayList<Method> (sourceClass.getAllMethods());
		if (MethodsClass.size() >=1) {
			Method targetMethod = randomObject(MethodsClass);
			if (sourceClass.moveMethodToClass(targetMethod,targetClass)){
				AssociationRelationship newRelationship = new AssociationRelationship(targetClass, sourceClass);
				arch.addRelationship(newRelationship);
			}
			
		}
	}
    
  //--------------------------------------------------------------------------
    
    public void MoveOperationMutation(double probability, Solution solution) throws JMException{
    try { 
          if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
        	if (PseudoRandom.randDouble() < probability) {
        	Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            
            Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
            Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
            if (checkSameLayer(sourceComp, targetComp)){
            	List<Interface> InterfacesSourceComp = new ArrayList<Interface> ();
            	List<Interface> InterfacesTargetComp = new ArrayList<Interface> ();
            	InterfacesSourceComp.addAll(sourceComp.getImplementedInterfaces());
            	InterfacesTargetComp.addAll(targetComp.getImplementedInterfaces());
            	if ((InterfacesSourceComp.size()>=1) && (InterfacesTargetComp.size()>=1)) {	
            		Interface targetInterface = randomObject(InterfacesTargetComp);
            		Interface sourceInterface = randomObject(InterfacesSourceComp);
            		if (targetInterface!=sourceInterface){
            			List<Method> OpsInterface = new ArrayList<Method> ();
            			OpsInterface.addAll(sourceInterface.getOperations());
            			if (OpsInterface.size() >=1) {
            				Method op = randomObject(OpsInterface);
            				sourceInterface.moveOperationToInterface(op, targetInterface);
            				if (sourceComp!=targetComp){
            					arch.addRequiredInterfaceToComponent(targetInterface, sourceComp);
            				}
            			}
            		}
            	}
            }
        	}//PseudoRandom
           } else {
                Configuration.logger_.log(
                        Level.SEVERE, "MoveOperationMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
       } catch (ClassNotFoundException e) {
            e.printStackTrace();
       } catch (Exception e) {
			e.printStackTrace();
	}
  }
    
  //--------------------------------------------------------------------------
    
    public void AddClassMutation(double probability, Solution solution, String scope) throws JMException {
    	
    	try { 
            if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
              if (PseudoRandom.randDouble() < probability) {  
               Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
               Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
               List<Class> ClassesComp = new ArrayList<Class> (sourceComp.getClasses());
               if (ClassesComp.size() >=1 ) {
            	   Class sourceClass = randomObject(ClassesComp);
            	   if ((sourceClass != null) && (!searchForGeneralizations(sourceClass)) && (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){ 	
                        	int option = PseudoRandom.randInt(0,1);
                        	if (option == 0) { //attribute          	
                        		List<Attribute> AttributesClass = new ArrayList<Attribute> (sourceClass.getAllAttributes());
                        		if (AttributesClass.size()>=1) {
                        			if (scope=="sameComponent") {
                        				Class newClass = sourceComp.createClass("Class"+ OPLA.contClass_++,false);
                        				moveAttributeSameComponent(arch, sourceClass, AttributesClass, newClass);
                        			} else {
                        				if (scope=="allComponents") {
                        					Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
                        					if (checkSameLayer(sourceComp, targetComp)){
                        						Class newClass = targetComp.createClass("Class"+ OPLA.contClass_++,false);
                        						moveAttributeAllComponents(arch, sourceComp, targetComp, sourceClass, AttributesClass, newClass);
                        					}
                        				}
                        			}
                        		}	          		
                        	} else { //method
                        		List<Method> MethodsClass = new ArrayList<Method> (sourceClass.getAllMethods());
                        		if (MethodsClass.size() >=1) {
                        			if (scope=="sameComponent") {
                        				Class newClass = sourceComp.createClass("Class"+ OPLA.contClass_++, false);
                        				moveMethodSameComponent(arch, sourceClass,	MethodsClass, newClass);
                        			} else {
                        				if (scope=="allComponents") {
                        					Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
                        					if (checkSameLayer(sourceComp, targetComp)){
                        						Class newClass = targetComp.createClass("Class"+ OPLA.contClass_++,false);
                        						moveMethodAllComponents(arch, sourceComp, targetComp, sourceClass, MethodsClass, newClass);
                        					}
                        				}
                        			}
                        		}  	          		
                        	}
            	   }
               } //ClassesComp não é vazia
              }//PseudoRandom
            }else {
                Configuration.logger_.log(
                        Level.SEVERE, "AddClassMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
    	} catch (ClassNotFoundException e) {
            	e.printStackTrace();
    	} catch (Exception e) {
			
			e.printStackTrace();
		}
    }

	private void moveMethodSameComponent(Architecture arch, Class sourceClass, List<Method> MethodsClass, Class newClass) throws JMException {
		Method targetMethod = randomObject (MethodsClass);
		sourceClass.moveMethodToClass(targetMethod, newClass);
		//if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
		for (Concern con: targetMethod.getOwnConcerns()){
			try {
				newClass.addConcern(con.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
		}
		//DependencyRelationship newDep = new DependencyRelationship(newClass, sourceClass);
		AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
		arch.getAllRelationships().add(newRelationship);
	}
	
	private void moveMethodAllComponents(Architecture arch, Package sourceComp, Package targetComp, Class sourceClass, List<Method> MethodsClass, Class newClass)
			throws JMException {
		Method targetMethod = randomObject (MethodsClass);
		sourceClass.moveMethodToClass(targetMethod, newClass);
		//if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
		for (Concern con: targetMethod.getOwnConcerns()){
			try {
				newClass.addConcern(con.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (targetComp.equals(sourceComp)){
			AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
			arch.getAllRelationships().add(newRelationship);
		}
		else{
			Collection<Interface> allItfsTargetComp = targetComp.getImplementedInterfaces();
			Interface targetInterface = null;
			for (Interface itf: allItfsTargetComp){
				for (Concern con:targetMethod.getOwnConcerns())
					if (itf.containsConcern(con)) targetInterface = itf;
			}
			if (targetInterface==null){
				if (targetComp.getImplementedInterfaces().isEmpty()) targetInterface=arch.createInterface("Interface"+ OPLA.contInt_++);
			} else{ targetInterface= randomObject(new ArrayList<Interface> (targetComp.getImplementedInterfaces()));}
			
			arch.addRequiredInterfaceToComponent(targetInterface, sourceComp);
		}		
	}

	private void moveAttributeSameComponent(Architecture arch, Class sourceClass, List<Attribute> AttributesClass, Class newClass) throws JMException {
		Attribute targetAttribute = randomObject (AttributesClass);
		sourceClass.moveAttributeToClass(targetAttribute, newClass);
		for (Concern con: targetAttribute.getOwnConcerns()){
				try {
					newClass.addConcern(con.getName());
				} catch (ConcernNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		//DependencyRelationship newDep = new DependencyRelationship(newClass, sourceClass);
		AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
		arch.getAllRelationships().add(newRelationship);
	}
    
	private void moveAttributeAllComponents(Architecture arch, Package sourceComp, Package targetComp, Class sourceClass, List<Attribute> AttributesClass, Class newClass)
			throws JMException {
		Attribute targetAttribute = randomObject (AttributesClass);
		sourceClass.moveAttributeToClass(targetAttribute, newClass);
		for (Concern con: targetAttribute.getOwnConcerns()){
				try {
					newClass.addConcern(con.getName());
				} catch (ConcernNotFoundException e) {
					e.printStackTrace();
				}
		}
		if (targetComp.equals(sourceComp)){
			AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
			arch.getAllRelationships().add(newRelationship);
		}
		else{
			Collection<Interface> allItfsTargetComp = targetComp.getImplementedInterfaces();
			Interface targetInterface = null;
			for (Interface itf: allItfsTargetComp){
				for (Concern con:targetAttribute.getOwnConcerns())
					if (itf.containsConcern(con)) targetInterface = itf;
			}
			
			if (targetInterface==null){
				if (targetComp.getImplementedInterfaces().isEmpty()) targetInterface=arch.createInterface("Interface"+ OPLA.contInt_++);
			} else{ targetInterface= randomObject(new ArrayList<Interface> (targetComp.getImplementedInterfaces()));}
			arch.addRequiredInterfaceToComponent(targetInterface, sourceComp);
		}		
	}
    
    
  //--------------------------------------------------------------------------
    
    public void AddManagerClassMutation(double probability, Solution solution) throws JMException {
    	    	
    try { 
        if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
          if (PseudoRandom.randDouble() < probability) {  	
            Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            
            Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
            	
            List<Interface> InterfacesComp = new ArrayList<Interface> ();
            InterfacesComp.addAll(sourceComp.getImplementedInterfaces());
            if (InterfacesComp.size()>=1){
            	Interface sourceInterface = randomObject(InterfacesComp);
            	List<Method> OpsInterface = new ArrayList<Method>();
            	OpsInterface.addAll(sourceInterface.getOperations()); 
            	if (OpsInterface.size()>=1){
            		Method op = randomObject(OpsInterface);
            	
            		Package newComp = arch.createPackage("Package"+ OPLA.contComp_ + getSuffix(sourceComp));
            		OPLA.contComp_++;
            		Interface newInterface = arch.createInterface("Interface"+ OPLA.contInt_++);
            		
            		arch.addImplementedInterfaceToComponent(newInterface, newComp);
            		sourceInterface.moveOperationToInterface(op, newInterface); 
            		arch.addRequiredInterfaceToComponent(newInterface, sourceComp);
            		for (Concern con: op.getOwnConcerns()){
            			newInterface.addConcern(con.getName());
            			newComp.addConcern(con.getName());
            		}
            	}
            }	
          }//PseudoRandom
         } else {
                Configuration.logger_.log(
                        Level.SEVERE, "AddManagerClassMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
      } catch (ClassNotFoundException e) {
            e.printStackTrace();
      } catch (Exception e) {
			e.printStackTrace();
		}
    }
  
    //--------------------------------------------------------------------------
    
	public void FeatureMutation(double probability, Solution solution, String scope) throws JMException {
        try { 
        	if (solution.getDecisionVariables()[0].getVariableType().toString().equals("class "+ Architecture.ARCHITECTURE_TYPE)){ 
              if (PseudoRandom.randDouble() < probability) {
            	  Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            	  List<Package> allComponents = new ArrayList<Package> (arch.getAllPackages());
            	  if (!allComponents.isEmpty()){
            		  Package selectedComp = randomObject(allComponents);
                	  List<Concern> allConcernsSelectedComp = new ArrayList<Concern> (selectedComp.getAllConcerns());
                	  List<Concern> ConcernsSelectedComp = new ArrayList<Concern> ();
                	  for (Concern concern:allConcernsSelectedComp){
                		  if (!ConcernsSelectedComp.contains(concern)) ConcernsSelectedComp.add(concern);
                	  }
                	  if (ConcernsSelectedComp.size() > 1){
                		Concern selectedConcern = randomObject(ConcernsSelectedComp);
                		List<Package> allComponentsAssignedOnlyToConcern = new ArrayList<Package> (searchComponentsAssignedToConcern(selectedConcern,allComponents));
                		if (allComponentsAssignedOnlyToConcern.size() == 0 ){
                			Package newComponent = arch.createPackage("Package"+ OPLA.contComp_ + getSuffix(selectedComp));
                			OPLA.contComp_++;
                			newComponent.addConcern(selectedConcern.getName());
                    		modularizeConcernInComponent(newComponent,selectedConcern,arch);
                		}
                		else{
                			if (allComponentsAssignedOnlyToConcern.size() == 1 ){
                				Package targetComponent = allComponentsAssignedOnlyToConcern.get(0);
                    			modularizeConcernInComponent(targetComponent,selectedConcern,arch);
                    		} 
                    		else {
                    			Package targetComponent = randomObject(allComponentsAssignedOnlyToConcern);
                    			modularizeConcernInComponent(targetComponent,selectedConcern,arch);
                    		}
                		}          		
                	  }
            	  }      	  
              }
        	}
          else {
                Configuration.logger_.log(Level.SEVERE, "FeatureMutation.doMutation: invalid type. " + "{0}", solution.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
           }            
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
	
	private List<Package> searchComponentsAssignedToConcern(Concern concern, List<Package> allComponents){
		List<Package> allComponentsAssignedToConcern = new ArrayList<Package> ();
		for (Package component: allComponents){
			if ((component.containsConcern(concern)) && (component.getOwnConcerns().size()==1)){
				allComponentsAssignedToConcern.add(component);
			}
		}
		return allComponentsAssignedToConcern;
	}
	
	private void modularizeConcernInComponent(Package targetComponent,	Concern concern, Architecture arch) {
		List<Package> allComponents = new ArrayList<Package>(arch.getAllPackages());
		for (Package comp : allComponents) {
			if (!comp.equals(targetComponent) && checkSameLayer(comp, targetComponent)) {// &&// !comp.containsConcern(concern)){
				List<Interface> allInterfaces = new ArrayList<Interface>(comp.getAllInterfaces());
				if (allInterfaces.size() >= 1) {
					for (Interface interfaceComp : allInterfaces) {
						if (interfaceComp.containsConcern(concern)	&& interfaceComp.getOwnConcerns().size() == 1) {
							moveInterfaceToComponent(interfaceComp, targetComponent, comp, arch);
						} else {
							List<Method> operationsInterfaceComp = new ArrayList<Method>(interfaceComp.getOperations());
							if (operationsInterfaceComp.size() >= 1) {
								for (Method operation : operationsInterfaceComp) {
									if (operation.containsConcern(concern) && operation.getOwnConcerns().size() == 1) {
										moveOperationToComponent(operation, interfaceComp, targetComponent, comp, arch, concern);
									}
								}
							}
						}
					}
				}
			}
		}

		for (Package comp : allComponents) {

			if (!comp.equals(targetComponent)
					&& checkSameLayer(comp, targetComponent)) {// &&
																// !comp.containsConcern(concern)){
				List<Class> allClasses = new ArrayList<Class>(comp.getClasses());
				if (allClasses.size() >= 1) {
					for (Class classComp : allClasses) {
						// ThelmaNew: nova condicao adicionada - FEITO
						if (comp.getClasses().contains(classComp)) {
							if ((classComp.containsConcern(concern))
									&& (classComp.getOwnConcerns().size() == 1)) {
								if (!searchForGeneralizations(classComp)) //realiza a mutaÁ„o em classe que n„o est· numa hierarquia de heranÁa
									moveClassToComponent(classComp,	targetComponent, comp, arch, concern);
								else
									moveHierarchyToComponent(classComp, targetComponent, comp, arch, concern); //realiza a mutação em classes estão numa hierarquia de herarquia 
							} else {
								// ThelmaNew: condicao adicionada
								if (!searchForGeneralizations(classComp)) {
									if (!isVarPointOfConcern(arch, classComp, concern) && !isVariantOfConcern(arch, classComp, concern)) {
										List<Attribute> attributesClassComp = new ArrayList<Attribute>(classComp.getAllAttributes());
										if (attributesClassComp.size() >= 1) {
											for (Attribute attribute : attributesClassComp) {
												if (attribute.containsConcern(concern) && attribute.getOwnConcerns().size() == 1) {
													moveAttributeToComponent(attribute,	classComp, targetComponent, comp, arch, concern);
												}
											}
										}
										List<Method> methodsClassComp = new ArrayList<Method>(classComp.getAllMethods());
										if (methodsClassComp.size() >= 1) {
											for (Method method : methodsClassComp) {
												if (method.containsConcern(concern) && method.getOwnConcerns().size() == 1) {
													moveMethodToComponent(method, classComp, targetComponent, comp, arch, concern);
												}
											}
										}
									}
								}

							}
						}
					}
				}
			}
		}
	}

	
	private void moveClassToComponent(Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern){
		List<Relationship> allRelationships = new ArrayList<Relationship> (classComp.getRelationships());
		if (!allRelationships.isEmpty()) {
			Iterator<Relationship> iteratorRelationships = allRelationships.iterator();
			while (iteratorRelationships.hasNext()){
				Relationship relationship= iteratorRelationships.next();
				//ThelmaNew: ifs adicionados
				if (relationship instanceof DependencyRelationship){
					DependencyRelationship dependency = (DependencyRelationship) relationship;
					if (dependency.getClient().equals(classComp) || dependency.getSupplier().equals(classComp))
						architecture.removeRelationship(dependency);
				}
				
				if (relationship instanceof AssociationRelationship){
					AssociationRelationship association = (AssociationRelationship) relationship;
					for (AssociationEnd associationEnd : association.getParticipants()) {
						if (associationEnd.getCLSClass().equals(classComp)){
							architecture.removeRelationship(association);
							break;
						}
					}
				}
										
				if (relationship instanceof GeneralizationRelationship){
					GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
					if ((generalization.getChild().equals(classComp)) || (generalization.getParent().equals(classComp))){
						architecture.removeRelationship(generalization);
					}
				}
			}
		}
		for (Relationship relationship: classComp.getRelationships()){
			architecture.removeRelationship(relationship);
		}
		
		sourceComp.moveClassToPackage(classComp, targetComp);
		List<Interface> allInterfacesTargetComp = new ArrayList<Interface> (targetComp.getImplementedInterfaces());
		Interface targetInterface = null;
		if (allInterfacesTargetComp.size()==0){
			targetInterface = architecture.createInterface("Interface"+ OPLA.contInt_++);
			try {
				targetInterface.addConcern(concern.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
			architecture.addImplementedInterfaceToComponent(targetInterface, targetComp);
			architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
		}
		else{
			if (allInterfacesTargetComp.size()== 1) {
				targetInterface = allInterfacesTargetComp.get(0);
				architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
			}
			else{
				List<Interface> allInterfacesConcernTargetComponent = new ArrayList<Interface> ();
				for (Interface itf: allInterfacesTargetComp){
					if (itf.containsConcern(concern)) allInterfacesConcernTargetComponent.add(itf);
				}
				try {
					targetInterface = randomObject(allInterfacesConcernTargetComponent);
				} catch (JMException e) {
					e.printStackTrace();
				}
				architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);				
			}
		}
		
	}
	
	private void moveAttributeToComponent(Attribute attribute, Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern){
		Class targetClass = null;
		for (Class cls:targetComp.getClasses()){
			if (cls.containsConcern(concern)) targetClass = cls;
		}
		if (targetClass==null){
			try {
				targetClass = targetComp.createClass("Class"+ OPLA.contClass_++,false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				targetClass.addConcern(concern.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
		}
		classComp.moveAttributeToClass(attribute, targetClass);
		Interface targetInterface=null;
		for (Interface itf: targetComp.getImplementedInterfaces()){
			if (itf.containsConcern(concern)) targetInterface=itf;
		}
		if (targetInterface==null) {
			targetInterface= architecture.createInterface("Interface"+ OPLA.contInt_++);
			try {
				targetInterface.addConcern(concern.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
			architecture.addImplementedInterfaceToComponent(targetInterface, targetComp);
		}
		architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
	}
	
	private void moveMethodToComponent(Method method, Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern){
		Class targetClass = null;
		for (Class cls:targetComp.getClasses()){
			if (cls.containsConcern(concern)) targetClass = cls;
		}
		if (targetClass==null)
			try {
				targetClass = targetComp.createClass("Class"+ OPLA.contClass_++,false);
				targetClass.addConcern(concern.getName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		classComp.moveMethodToClass(method, targetClass);
		//if (method.isAbstract()) method.setAbstract(false);
		
		Interface targetInterface=null;
		for (Interface itf: targetComp.getImplementedInterfaces()){

			if (itf.containsConcern(concern)){
				targetInterface=itf;
			}
		}
		if (targetInterface==null) {
			targetInterface= architecture.createInterface("Interface"+ OPLA.contInt_++);
			try {
				targetInterface.addConcern(concern.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
			architecture.addImplementedInterfaceToComponent(targetInterface, targetComp);
		}
		architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
	}
	
	private void moveInterfaceToComponent(Interface interfaceComp, Package targetComp, Package sourceComp, Architecture architecture){
		architecture.removeImplementedInterfaceFromPackage(interfaceComp, sourceComp);
		architecture.addImplementedInterfaceToComponent(interfaceComp, targetComp);
		architecture.addRequiredInterfaceToComponent(interfaceComp, sourceComp);
	}
	
	private void moveOperationToComponent(Method operation, Interface sourceInterface, Package targetComp, Package sourceComp, Architecture architecture, Concern concern){
		Interface targetInterface=null;
		for (Interface itf: targetComp.getImplementedInterfaces()){
			if (itf.containsConcern(concern)){
				targetInterface=itf;
			}
		}
		if (targetInterface==null) {
			targetInterface= architecture.createInterface("Interface"+ OPLA.contInt_++);
			try {
				targetInterface.addConcern(concern.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
			architecture.addImplementedInterfaceToComponent(targetInterface, targetComp);
		}
		sourceInterface.moveOperationToInterface(operation, targetInterface);
		architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
	}
	
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;
        Double probability = (Double) getParameter("probability");

        if (probability == null) {
            Configuration.logger_.severe("FeatureMutation.execute: probability not specified");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }
        
        try {
        	this.doMutation(mutationProbability_, solution);
		} catch (Exception e) {
			e.printStackTrace();
		}

        return solution;
    }
    
    //-------------------------------------------------------------------------------------------------
    
    public <T> T randomObject(List<T> allObjects)  throws JMException   {
        
        int numObjects= allObjects.size(); 
        int key;
        T object;
        if (numObjects == 0) 
        	object = null;
            else{
        	  key = PseudoRandom.randInt(0, numObjects-1); 
        	  object = allObjects.get(key);
           }
        return object;      	    	
    }
    
  //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se os componentes nos quais as mutacoes serao realizadas estao na mesma camada da arquitetura
      private boolean checkSameLayer(Package source, Package target){
      	boolean sameLayer=false;
      	if ((source.getName().endsWith("Mgr") && target.getName().endsWith("Mgr")) || 
      			(source.getName().endsWith("Ctrl") && target.getName().endsWith("Ctrl")) ||	
      				(source.getName().endsWith("GUI") && target.getName().endsWith("GUI"))) 
      					sameLayer = true;
      	return sameLayer;
      }
      
    //-------------------------------------------------------------------------------------------------
      //Thelma: método adicionado para retornar o sufixo do nome do componente
        private String getSuffix(Package comp){
      	  String suffix;
      	  if (comp.getName().endsWith("Mgr")) suffix = "Mgr";
      	  else if (comp.getName().endsWith("Ctrl")) suffix = "Ctrl";
      	  	else if (comp.getName().endsWith("GUI")) suffix = "GUI";
      	  		else suffix = "";
      	  return suffix;
        }
    
      //-------------------------------------------------------------------------------------------------
        //Thelma: método adicionado para verificar se a classe tem uma variabilidade relativa ao concern
        
     private boolean isVarPointOfConcern(Architecture arch, Class cls, Concern concern){
  	   boolean isVariationPointConcern = false;
  		Collection<Variability> variabilities = arch.getAllVariabilities();
  		for (Variability variability: variabilities){
  			VariationPoint varPoint = variability.getVariationPoint();
  			if(varPoint != null){
	  			Class classVP = (Class) varPoint.getVariationPointElement();
				if (classVP.equals(cls) && variability.getName().equals(concern.getName())) 
					isVariationPointConcern = true;
  			}
  		}
  		return isVariationPointConcern;
     }
     
     //-------------------------------------------------------------------------------------------------
     //Thelma: método adicionado para verificar se a classe é variant de uma variabilidade relativa ao concern
     
    private boolean isVariantOfConcern(Architecture arch, Class cls, Concern concern){
  	   boolean isVariantConcern = false;
  		Collection<Variability> variabilities = arch.getAllVariabilities();
  		for (Variability variability: variabilities){
  			
  			VariationPoint varPoint = variability.getVariationPoint();
  			
  			
  			if(varPoint != null){
				for (Variant variant : varPoint.getVariants()){
					if (variant.getVariantElement().equals(cls) && variability.getName().equals(concern.getName())) 
						isVariantConcern = true;		
				}
  			}else{
  				if(cls.getVariantType() != null){
					if(cls.getVariantType().equalsIgnoreCase("optional")){
						isVariantConcern = true;
					}
  				}
  			}
  		}
  		return isVariantConcern;
  }
    
//  //-------------------------------------------------------------------------
//    //método para identificar se a classe é superclasse na hierarquia de herança
//    private boolean isParent(Class cls){
//    	boolean parent=false;
//    	
//    	for (Relationship relationship: cls.getRelationships()){
//  	    	if (relationship instanceof GeneralizationRelationship){
//  	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
//  	    		if (generalization.getParent().equals(cls)) {
//  	    			parent = true;
//  	    			return parent;
//  	    		}	    		
//  	    	}
//  	    }
//    	return parent;
//    }

  //--------------------------------------------------------------------------
    //método para identificar se a classe é subclasse na hierarquia de herança
    private boolean isChild(Element cls){
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
    
  //--------------------------------------------------------------------------
    //método para identificar as subclasses da classe pai na hierarquia de herança
    private List<Element> getChildren(Element cls){
  	  List<Element> children = new ArrayList<Element>();
  	  for (Relationship relationship: cls.getRelationships()){
  	    	if (relationship instanceof GeneralizationRelationship){
  	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
  	    		if (generalization.getParent().equals(cls)){
  	    			children.add(generalization.getChild());
  	    		}
  	    	}
  	  }	    			
  	  return children;
    }

  //--------------------------------------------------------------------------
    //método para identificar as subclasses da classe pai na hierarquia de herança
    private Element getParent(Element cls){
  	  Element parent = null;
  	  for (Relationship relationship: cls.getRelationships()){
  	    	if (relationship instanceof GeneralizationRelationship){
  	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
  	    		if (generalization.getChild().equals(cls)){
  	    			parent = generalization.getParent();
  	    			return parent;
  	    		}
  	    	}
  	  }	    			
  	  return parent;
    }
    
//  //--------------------------------------------------------------------------
//    //método para identificar os irmaos na hierarquia de herança
//    private List<Class> getSibling(Class parent, Class child){
//  	  List<Class> sibling = new ArrayList<Class> ();
//  	  for (Relationship relationship: parent.getRelationships()){
//  	    	if (relationship instanceof GeneralizationRelationship){
//  	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
//  	    		if ((generalization.getParent().equals(parent)) && (!(generalization.getChild().equals(child)))){
//  	    			sibling.add(generalization.getChild());
//  	    		}
//  	    	}
//  	  }	    			
//  	  return sibling;
//    }
    
//    //atualizar os pontos de variacao de cada variabilidade
//    private void updateVariabilitiesOffspring(Architecture offspring){ 
//    	for (Variability variability: offspring.getAllVariabilities()){	    		
//    		VariationPoint variationPoint = variability.getVariationPoint();
//			Element elementVP = variationPoint.getVariationPointElement();
//			if (!(offspring.findElementByName(elementVP.getName(), "class").equals(elementVP))){
//				//procura o elemento em offspring e substitui o variationPoint 
//				variationPoint.replaceVariationPointElement(offspring.findElementByName(elementVP.getName(), "class"));
//			}
//    	}
//    }
    
	//metodo adicionado para mover a hierarquia de classes para um outro componente que esta modularizando o interesse concern	
private void moveHierarchyToComponent(Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern){

		Element parent = classComp;
		while (isChild(parent)){
			parent = getParent(parent);				
		}
		
		moveChildrenToComponent(parent,sourceComp,targetComp,architecture, concern);		
		
		List<Interface> allInterfacesTargetComp = new ArrayList<Interface> (targetComp.getImplementedInterfaces());
		Interface targetInterface = null;
		
		if (allInterfacesTargetComp.size()==0){
			targetInterface = architecture.createInterface("Interface"+ OPLA.contInt_++);
			try {
				targetInterface.addConcern(concern.getName());
			} catch (ConcernNotFoundException e) {
				e.printStackTrace();
			}
			architecture.addImplementedInterfaceToComponent(targetInterface, targetComp);
			architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
		}
		else{
			if (allInterfacesTargetComp.size()== 1) {
				targetInterface = allInterfacesTargetComp.get(0);
				architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);
			}
			else{
				List<Interface> allInterfacesConcernTargetComponent = new ArrayList<Interface> ();
				for (Interface itf: allInterfacesTargetComp){
					if (itf.containsConcern(concern)) allInterfacesConcernTargetComponent.add(itf);
				}
				try {
					targetInterface = randomObject(allInterfacesConcernTargetComponent);
				} catch (JMException e) {
					e.printStackTrace();
				}
				architecture.addRequiredInterfaceToComponent(targetInterface, sourceComp);				
			}
		}
		
	}

	//metodo para mover os filhos para o novo componente que está modularizando o interesse
	private void moveChildrenToComponent(Element parent, Package sourceComp, Package targetComp, Architecture architecture, Concern concern){
		Collection<Element> children = getChildren(parent);
		for (Element child: children){
			moveChildrenToComponent(child, sourceComp, targetComp, architecture, concern);
		}
		
		//ThelmaNew: if adicionado para acomodar hierarquias em diferentes componentes
		if (sourceComp.getClasses().contains(parent)){
			removeRelationshipsofClassInHierarchy(parent,architecture);
			sourceComp.moveClassToPackage(parent, targetComp);
		} else{		
			Package auxComp=null;
			List<Package> allCompsArch = new ArrayList<Package> (architecture.getAllPackages());
			if (!allCompsArch.isEmpty()) {
				Iterator<Package> iteratorCompsArch = allCompsArch.iterator();
				while (iteratorCompsArch.hasNext()){
					auxComp= iteratorCompsArch.next();
					if (auxComp.getClasses().contains(parent)){
						removeRelationshipsofClassInHierarchy(parent,architecture);
						auxComp.moveClassToPackage(parent, targetComp);
						break;
					}
				}
				List<Interface> allInterfacesTargetComp = new ArrayList<Interface> (targetComp.getImplementedInterfaces());
				Interface targetInterface = null;
				if (allInterfacesTargetComp.size()==0){
					targetInterface = architecture.createInterface("Interface"+ OPLA.contInt_++);
					try {
						targetInterface.addConcern(concern.getName());
					} catch (ConcernNotFoundException e) {
						e.printStackTrace();
					}
					architecture.addImplementedInterfaceToComponent(targetInterface, targetComp);
					architecture.addRequiredInterfaceToComponent(targetInterface, auxComp);
				}
				else{
					if (allInterfacesTargetComp.size()== 1) {
						targetInterface = allInterfacesTargetComp.get(0);
						architecture.addRequiredInterfaceToComponent(targetInterface, auxComp);
					}
					else{
						List<Interface> allInterfacesConcernTargetComponent = new ArrayList<Interface> ();
						for (Interface itf: allInterfacesTargetComp){
							if (itf.containsConcern(concern)) allInterfacesConcernTargetComponent.add(itf);
						}
						try {
							targetInterface = randomObject(allInterfacesConcernTargetComponent);
						} catch (JMException e) {
							e.printStackTrace();
						}
						architecture.addRequiredInterfaceToComponent(targetInterface, auxComp);				
					}
				}
			}		
		}
	}

	//metodo adicionado para eliminar os relacionamentos de uma classe que faz parte de uma hierarquia, com excecao das generalizacoes
	private void removeRelationshipsofClassInHierarchy(Element cls, Architecture architecture){
		List<Relationship> allRelationships = new ArrayList<Relationship> (cls.getRelationships());
		if (!allRelationships.isEmpty()) {
			Iterator<Relationship> iteratorRelationships = allRelationships.iterator();
			while (iteratorRelationships.hasNext()){
				Relationship relationship= iteratorRelationships.next();
				
				if (relationship instanceof DependencyRelationship){
					DependencyRelationship dependency = (DependencyRelationship) relationship;
					if (dependency.getClient().equals(cls) || dependency.getSupplier().equals(cls))
						architecture.removeRelationship(dependency);
				}
				
				if (relationship instanceof AssociationRelationship){
					AssociationRelationship association = (AssociationRelationship) relationship;
					for (AssociationEnd associationEnd : association.getParticipants()) {
						if (associationEnd.getCLSClass().equals(cls)){
							architecture.removeRelationship(association);
							break;
						}
					}
				}				
			}
		}
	}
        
}