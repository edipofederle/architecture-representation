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
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationRelationship;
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

  
    	int r=PseudoRandom.randInt(0,5);
    	switch(r){
        case 0: FeatureMutation(probability, solution, scopeLevels); break;
        case 1: MoveMethodMutation(probability, solution, scope); break;
        case 2: MoveAttributeMutation(probability, solution, scope); break;
        case 3: MoveOperationMutation(probability, solution); break;
        case 4: AddClassMutation(probability, solution, scope); break;
        case 5: AddManagerClassMutation(probability, solution); break;
        }
    	
    	    	
    }

    //--------------------------------------------------------------------------
    //m�todo para verificar se algum dos relacionamentos recebidos � generaliza��o
    private boolean searchForGeneralizations(Collection<Relationship> Relationships){
    	boolean found=false;
    	for (Relationship relationship: Relationships){
	    	if (relationship instanceof GeneralizationRelationship){
	    		return true;
	    	}
	    }
    	return found;
    }
    
    public void MoveAttributeMutation(double probability, Solution solution, String scope) throws JMException{
    	try { 
    		if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName("arquitetura.representation.Architecture")) {
            	Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            	            	 
            	if (PseudoRandom.randDouble() < probability) {
            	if (scope == "sameComponent") {
            		Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));            	    	
            		List<Class> ClassesComp = new ArrayList<Class> (sourceComp.getClasses());
            		if (ClassesComp.size() > 1) {
            			Class targetClass = randomObject(ClassesComp);
            		    Class sourceClass = randomObject(ClassesComp);
            		    if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass.getRelationships()))&& (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){ //sourceClass n�o tem relacionamentos de generaliza��o
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
            				if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass.getRelationships()))&& (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){ //sourceClass n�o tem relacionamentos de generaliza��o{
            					Package targetComp = randomObject(new ArrayList<Package>(arch.getAllPackages())); 
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
            if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName("br.uem.din.architectureEvolution.representation.Architecture")) {
            	Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            	
            	if (PseudoRandom.randDouble() < probability) {
            	if (scope == "sameComponent") {
            		Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
            		List<Class> ClassesComp = new ArrayList<Class> (sourceComp.getClasses());
            	   	if (ClassesComp.size() > 1) {
            	   		Class targetClass = randomObject(ClassesComp);
            	   		Class sourceClass = randomObject(ClassesComp);
            	   		if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass.getRelationships())) && (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){
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
                			if ((sourceClass!=null) && (!searchForGeneralizations(sourceClass.getRelationships())) && (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){
                				Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages())); 
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
				arch.getAllRelationships().add(newRelationship);
			}
			
		}
	}
    
  //--------------------------------------------------------------------------
    
    public void MoveOperationMutation(double probability, Solution solution) throws JMException{
    try { 
          if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName("br.uem.din.architectureEvolution.representation.Architecture")) {
        	if (PseudoRandom.randDouble() < probability) {
        	Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            
            Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
            Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
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
            if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName("br.uem.din.architectureEvolution.representation.Architecture")) {
              if (PseudoRandom.randDouble() < probability) {  
               Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
               Package sourceComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
               List<Class> ClassesComp = new ArrayList<Class> (sourceComp.getClasses());
               if (ClassesComp.size() >=1 ) {
            	   Class sourceClass = randomObject(ClassesComp);
            	   if ((sourceClass != null) && (!searchForGeneralizations(sourceClass.getRelationships())) && (sourceClass.getAllAttributes().size()>1) && (sourceClass.getAllMethods().size()>1)){ 	
                        	int option = PseudoRandom.randInt(0,1);
                        	if (option == 0) { //attribute          	
                        		List<Attribute> AttributesClass = new ArrayList<Attribute> (sourceClass.getAllAttributes());
                        		if (AttributesClass.size()>=1) {
                        			if (scope=="sameComponent") {
                        				Class newClass = sourceComp.createClass("Class"+ OPLA.contClass_++);
                        				moveAttributeSameComponent(arch, sourceClass, AttributesClass, newClass);
                        			} else {
                        				if (scope=="allComponents") {
                        					Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
                        					Class newClass = targetComp.createClass("Class"+ OPLA.contClass_++);
                        					moveAttributeAllComponents(arch, sourceComp, targetComp, sourceClass, AttributesClass, newClass);
                        				}
                        			}
                        		}	          		
                        	} else { //method
                        		List<Method> MethodsClass = new ArrayList<Method> (sourceClass.getAllMethods());
                        		if (MethodsClass.size() >=1) {
                        			if (scope=="sameComponent") {
                        				Class newClass = sourceComp.createClass("Class"+ OPLA.contClass_++);
                        				moveMethodSameComponent(arch, sourceClass,	MethodsClass, newClass);
                        			} else {
                        				if (scope=="allComponents") {
                        					Package targetComp = randomObject(new ArrayList<Package> (arch.getAllPackages()));
                        					Class newClass = targetComp.createClass("Class"+ OPLA.contClass_++);
                        					moveMethodAllComponents(arch, sourceComp, targetComp, sourceClass, MethodsClass, newClass);
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
		for (Concern con: targetMethod.getOwnConcerns())
			newClass.addConcern(con.getName());
		//DependencyInterClassRelationship newDep = new DependencyInterClassRelationship(newClass, sourceClass);
		AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
		arch.getAllRelationships().add(newRelationship);
	}
	
	private void moveMethodAllComponents(Architecture arch, Package sourceComp, Package targetComp, Class sourceClass, List<Method> MethodsClass, Class newClass)
			throws JMException {
		Method targetMethod = randomObject (MethodsClass);
		sourceClass.moveMethodToClass(targetMethod, newClass);
		//if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
		for (Concern con: targetMethod.getOwnConcerns())
				newClass.addConcern(con.getName());
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

	private void moveAttributeSameComponent(Architecture arch, Class sourceClass, List<Attribute> AttributesClass, Class newClass)
			throws JMException {
		Attribute targetAttribute = randomObject (AttributesClass);
		sourceClass.moveAttributeToClass(targetAttribute, newClass);
		for (Concern con: targetAttribute.getOwnConcerns())
				newClass.addConcern(con.getName());
		//DependencyInterClassRelationship newDep = new DependencyInterClassRelationship(newClass, sourceClass);
		AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
		arch.getAllRelationships().add(newRelationship);
	}
    
	private void moveAttributeAllComponents(Architecture arch, Package sourceComp, Package targetComp, Class sourceClass, List<Attribute> AttributesClass, Class newClass)
			throws JMException {
		Attribute targetAttribute = randomObject (AttributesClass);
		sourceClass.moveAttributeToClass(targetAttribute, newClass);
		for (Concern con: targetAttribute.getOwnConcerns())
				newClass.addConcern(con.getName());
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
        if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName("br.uem.din.architectureEvolution.representation.Architecture")) {
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
            	
            		Package newComp = arch.createPackage("Package"+ OPLA.contComp_++);
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
        	if (solution.getDecisionVariables()[0].getVariableType().toString().equals("class br.uem.din.architectureEvolution.representation.Architecture")){ 
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
                			Package newComponent = arch.createPackage("Component"+ OPLA.contComp_++);
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
                Configuration.logger_.log(
                        Level.SEVERE, "FeatureMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
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
	
	private void modularizeConcernInComponent(Package targetComponent, Concern concern, Architecture arch){
		List<Package> allComponents = new ArrayList<Package> (arch.getAllPackages());
		for (Package comp: allComponents){
			if (!comp.equals(targetComponent)){// && !comp.containsConcern(concern)){
				List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
				if (allInterfaces.size()>=1) {
					for (Interface interfaceComp: allInterfaces){
						if (interfaceComp.containsConcern(concern) && interfaceComp.getOwnConcerns().size()==1){
							moveInterfaceToComponent(interfaceComp,targetComponent, comp, arch);
						}
						else{
							List<Method> operationsInterfaceComp = new ArrayList<Method> (interfaceComp.getOperations());
							if (operationsInterfaceComp.size()>=1){
								for (Method operation : operationsInterfaceComp){
									if (operation.containsConcern(concern) && operation.getOwnConcerns().size()==1){
										moveOperationToComponent(operation,interfaceComp,targetComponent, comp, arch, concern);
									}
								}
							}							
						}
					}
				}
			}
		}
		for (Package comp: allComponents){
				if (!comp.equals(targetComponent)){// && !comp.containsConcern(concern)){
					List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
					if (allClasses.size()>=1) {
						for (Class classComp: allClasses){
							if (!searchForGeneralizations(classComp.getRelationships())){//s� realiza a muta��o se a classe n�o estiver numa hierarquia de heran�a
								if ((classComp.containsConcern(concern)) && (classComp.getOwnConcerns().size()==1)){
									moveClassToComponent(classComp,targetComponent, comp, arch,concern);
								}
								else{
									List<Attribute> attributesClassComp = new ArrayList<Attribute>(classComp.getAllAttributes());
									if (attributesClassComp.size()>=1) {
										for (Attribute attribute: attributesClassComp){
											if (attribute.containsConcern(concern) && attribute.getOwnConcerns().size()==1){
												moveAttributeToComponent(attribute,classComp,targetComponent, comp, arch,concern);
											}
										}
									}
									List<Method> methodsClassComp = new ArrayList<Method>(classComp.getAllMethods());
									if (methodsClassComp.size()>=1){
										for (Method method: methodsClassComp){
											if (method.containsConcern(concern) && method.getOwnConcerns().size()==1){
												moveMethodToComponent(method,classComp,targetComponent, comp, arch,concern);
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
		List<Relationship> allInterClassRelationships = new ArrayList<Relationship> (classComp.getRelationships());
		if (!allInterClassRelationships.isEmpty()) {
			Iterator<Relationship> iteratorInterClassRelationships = allInterClassRelationships.iterator();
			while (iteratorInterClassRelationships.hasNext()){
				Relationship relationship= iteratorInterClassRelationships.next();
				architecture.removeRelationship(relationship);
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
			targetInterface.addConcern(concern.getName());
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
				targetClass = targetComp.createClass("Class"+ OPLA.contClass_++);
			} catch (Exception e) {
				e.printStackTrace();
			}
			targetClass.addConcern(concern.getName());
		}
		classComp.moveAttributeToClass(attribute, targetClass);
		Interface targetInterface=null;
		for (Interface itf: targetComp.getImplementedInterfaces()){
			if (itf.containsConcern(concern)) targetInterface=itf;
		}
		if (targetInterface==null) {
			targetInterface= architecture.createInterface("Interface"+ OPLA.contInt_++);
			targetInterface.addConcern(concern.getName());
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
				targetClass = targetComp.createClass("Class"+ OPLA.contClass_++);
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
			targetInterface.addConcern(concern.getName());
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
			targetInterface.addConcern(concern.getName());
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

        
}
