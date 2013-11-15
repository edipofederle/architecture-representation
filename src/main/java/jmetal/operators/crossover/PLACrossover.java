package jmetal.operators.crossover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArchitectureSolutionType;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.exceptions.NotFoundException;
import arquitetura.exceptions.PackageNotFound;
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
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;


	public class PLACrossover extends Crossover {

		private static final long serialVersionUID = -51015356906090226L;


	/**
     * ARCHITECTURE_SOLUTION represents class jmetal.encodings.solutionType.ArchitectureSolutionType
     */
	 private Double crossoverProbability_ = null;

		
	/**
	   * Valid solution types to apply this operator 
	   */
	  private static List VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class) ;

	  	
		public PLACrossover(HashMap<String, Object> parameters) {
			super(parameters) ;
			
	  	if (parameters.get("probability") != null)
	  		crossoverProbability_ = (Double) getParameter("probability");
	} // PLACrossover

				        
    //--------------------------------------------------------------------------
    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {

    	String scopeLevel = "allLevels"; //use "oneLevel" para n„o verificar a presenÁa de interesses nos atributos e mÈtodos
        Solution[] offspring = new Solution[2];

        Solution[] crossFeature = this.crossoverFeatures(crossoverProbability_, parent1, parent2, scopeLevel);
        offspring[0] = crossFeature[0];
        offspring[1] = crossFeature[1];
     
        return offspring;
    }
	    
	     
	  //--------------------------------------------------------------------------
	    public Solution[] crossoverFeatures(double probability, Solution parent1, Solution parent2, String scope) throws JMException {

	    	// STEP 0: Create two offsprings
	        Solution[] offspring = new Solution[2];
            offspring[0] = new Solution(parent1);
	        offspring[1] = new Solution(parent2);
	      	     
	               
	        try {
	            if (parent1.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
	               if (PseudoRandom.randDouble() < probability) {
	              
	                    // STEP 1: Get feature to crossover
	                    List<Concern> allConcerns = new ArrayList<Concern> (((Architecture) offspring[0].getDecisionVariables()[0]).getConcerns());
	                    List<Concern> concernsArchitecture = new ArrayList<Concern> ();
	              	  	for (Concern concern:allConcerns){
	              	  		if (!concernsArchitecture.contains(concern)) concernsArchitecture.add(concern);
	              	  	}
	                    Concern feature = randomObject(concernsArchitecture);
	
	                    // STEP 2: Obtain the first child
	                    obtainChild(feature, (Architecture) offspring[1].getDecisionVariables()[0], (Architecture) offspring[0].getDecisionVariables()[0], scope);
	                    
	                    // STEP 3: Obtain the second child
	                    obtainChild(feature, (Architecture) offspring[0].getDecisionVariables()[0], (Architecture) offspring[1].getDecisionVariables()[0], scope);
	               }
	            }
	            else {
	            	Configuration.logger_.log(
	            			Level.SEVERE, "PLACrossover.doCrossover: "
	            					+ "invalid type{0}", parent1.getDecisionVariables()[0].getVariableType());
	            	java.lang.Class<String> cls = java.lang.String.class;
	            	String name = cls.getName();
	            	throw new JMException("Exception in " + name + ".doCrossover()");
	            }
	        } catch (ClassNotFoundException e) {
	        	e.printStackTrace();            
	        }
    
	        return offspring;
	    }
	    
	  //--------------------------------------------------------------------------
	    
	    /**
		 * Executes the operation
		 * @param object An object containing an array of two solutions 
		 * @return An object containing an array with the offSprings
		 * @throws JMException 
		 */
		public Object execute(Object object) throws JMException {
			Solution [] parents = (Solution [])object;
			//Double crossoverProbability ;
			
	    if (!(VALID_TYPES.contains(parents[0].getType().getClass())  &&
	        VALID_TYPES.contains(parents[1].getType().getClass())) ) {

				Configuration.logger_.severe("PLACrossover.execute: the solutions " +
						"are not of the right type. The type should be 'Permutation', but " +
						parents[0].getType() + " and " + 
						parents[1].getType() + " are obtained");
			} // if 

			crossoverProbability_ = (Double)getParameter("probability");
			
			if (parents.length < 2)
			{
				Configuration.logger_.severe("PLACrossover.execute: operator needs two " +
				"parents");
				java.lang.Class<String> cls = java.lang.String.class;
				String name = cls.getName(); 
				throw new JMException("Exception in " + name + ".execute()") ;      
			}

			Solution [] offspring = doCrossover(crossoverProbability_, parents[0], parents[1]); 

			//problem.evaluateConstraints(offspring[0]);
			//problem.evaluateConstraints(offspring[1]);
			
			return offspring; 
		} // execute
		
		
		
	    
	    private void obtainChild(Concern feature, Architecture parent, Architecture offspring, String scope){
	    	//eliminar os elementos arquiteturais que realizam feature em offspring
	    	removeOffspringArchitecturalElementsRealizingFeature(feature,offspring, scope);
	    	//adicionar em offspring os elementos arquiteturais que realizam feature em parent
	    	Architecture auxParent = null;
			try {
				auxParent = parent.deepClone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
	    	addParentArchitecturalElementsRealizingFeature(feature,offspring, auxParent,scope);	
	    	updateVariabilitiesOffspring(offspring);
	    }

	    
	    private void addAttributesRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent){
	    
	    	Class targetClass = null;
			try {
				targetClass = offspring.findClassByName(classComp.getName()).get(0);
			} catch (ClassNotFound e2) {
				e2.printStackTrace();
			}
	    	List<Attribute> allAttributes = new ArrayList<Attribute> (classComp.getAllAttributes());
			if (!allAttributes.isEmpty()) {
				Iterator<Attribute> iteratorAttributes = allAttributes.iterator();
				while (iteratorAttributes.hasNext()){
	            	Attribute attribute= iteratorAttributes.next();
	            	if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size()==1){
	            			if (targetClass==null){
		            			Package newComp = null;
								try {
									newComp = offspring.findPackageByName(comp.getName());
								} catch (PackageNotFound e1) {
									e1.printStackTrace();
								}
		            			if (newComp==null) {
		            				try {
										newComp=offspring.createPackage(comp.getName());
										newComp.addConcern(feature.getName());
									} catch (Exception e) {
										e.printStackTrace();
									}
		            			}
		            			try {
									targetClass=newComp.createClass(classComp.getName(), false);
									targetClass.addConcern(feature.getName());
								} catch (Exception e) {
									e.printStackTrace();
								}
		            		}
		            		classComp.moveAttributeToClass(attribute, targetClass);
	            	}
				}
			}
	    }
	    
	    private void addClassesRealizingFeatureToOffspring (Concern feature, Package comp, Architecture offspring, Architecture parent, String scope) {
	    
	    	Package newComp = null;
	    	Interface newItf;
	    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
			if (!allClasses.isEmpty()) {
				Iterator<Class> iteratorClasses = allClasses.iterator();
				while (iteratorClasses.hasNext()){
	            	Class classComp= iteratorClasses.next();
	            	if (comp.getClasses().contains(classComp)){
	            		if (classComp.containsConcern(feature) && classComp.getOwnConcerns().size()==1){
		            		try {
								newComp=offspring.findPackageByName(comp.getName());
							} catch (PackageNotFound e1) {
								e1.printStackTrace();
							}
		        			if (newComp==null)
		    					try {
		    						newComp = offspring.createPackage(comp.getName());
		    						newComp.addConcern(feature.getName());
		    					} catch (Exception e) {
		    						e.printStackTrace();
		    						newItf = offspring.createInterface("Interface"+ OPLA.contInt_++);
		    						newItf.addConcern(feature.getName());
		    						offspring.addImplementedInterfaceToComponent(newItf, newComp);
		    						addDependenciesToInterface(newItf, offspring, parent, feature);
		    						
		    					}	        			
		        			
		            		if (!searchForGeneralizations(classComp)){
		            			comp.moveClassToPackage(classComp, newComp);		            			
			    				updateClassRelationships(classComp,newComp, comp, offspring,parent);				    				
		            		}
		            		else {
		            			moveHierarchyToComponent(classComp, newComp, comp, offspring, parent, feature);
		            		}
		            	}	
			    		else{
			    				if ((scope=="allLevels") && (!searchForGeneralizations(classComp))){
			    					addAttributesRealizingFeatureToOffspring(feature, classComp, comp, offspring, parent);
			    					addMethodsRealizingFeatureToOffspring(feature, classComp, comp, offspring, parent);
			    				}
			    		}	            		
	            	}
				}
    		}
	    }
	    private void addClassesToOffspring (Concern feature, Package comp, Package newComp, Architecture offspring, Architecture parent) {
	    	
	    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
			if (!allClasses.isEmpty()) {
				Iterator<Class> iteratorClasses = allClasses.iterator();
				while (iteratorClasses.hasNext()){
	            	Element classComp= iteratorClasses.next();
	            	if (comp.getClasses().contains(classComp)){
	            		if (!searchForGeneralizations(classComp)){
		        			comp.moveClassToPackage(classComp, newComp);
							updateClassRelationships(classComp,newComp, comp, offspring,parent);
		        		}
		        		else {
		        			moveHierarchyToComponent(classComp, newComp, comp, offspring, parent, feature);
		        		}
	            	}	            	        	
	            }
    		}
	    }
	    
	 // ---------------------------------------------------
  		private void addDependenciesToInterface(Interface itf, Architecture offspring, Architecture parent, Concern feature){
  			
  			Collection<DependencyRelationship> allDependencies =  parent.getAllDependencies();
  			for (DependencyRelationship dependency: allDependencies){
  				if (dependency.getSupplier().equals(itf) && dependency.getSupplier().containsConcern(feature)){ 		            			
  					Package auxComp = null;
					try {
						auxComp = offspring.findPackageByName(dependency.getPackageOfDependency().getName());
					} catch (PackageNotFound e) {
						e.printStackTrace();
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
  					offspring.addRequiredInterfaceToComponent(itf, auxComp);
  				}
  				
  			}
  		} 

	    private void addInterfacesRealizingFeatureToOffspring (Concern feature, Package comp, Architecture offspring, Architecture parent) {
	    	
	    	Package newComp = null;
	    	List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
			if (!allInterfaces.isEmpty()) {
				Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
				while (iteratorInterfaces.hasNext()){
					Interface interfaceComp= iteratorInterfaces.next();
	            	if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size()==1){
	        			try {
							newComp=offspring.findPackageByName(comp.getName());
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}
	        			if (newComp==null)
							try {
								newComp = offspring.createPackage(comp.getName());
								newComp.addConcern(feature.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
	        			updateInterfaceDependencies(interfaceComp, offspring, parent);
	        			offspring.addExternalInterface(interfaceComp);
						offspring.addImplementedInterfaceToComponent(interfaceComp, newComp);
	        			
	            	}
	            	else{
	            		addOperationsRealizingFeatureToOffspring(feature, interfaceComp, comp, offspring, parent);
	            	}
				}
			}
	    }
	 
	    private void addInterfacesToOffspring (Concern feature, Package comp, Package newComp, Architecture offspring, Architecture parent) {
	    	
	    	List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
			if (!allInterfaces.isEmpty()) {
				Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
				while (iteratorInterfaces.hasNext()){
					Interface interfaceComp= iteratorInterfaces.next();
	            	Collection<DependencyRelationship> dependencies = interfaceComp.getDependencies();
	        		removeInterfaceRelationships(interfaceComp, offspring);
	            	
	        		offspring.addExternalInterface(interfaceComp);
	        		offspring.addImplementedInterfaceToComponent(interfaceComp, newComp);
	        		for (DependencyRelationship dependency: dependencies){
	        			Package dependent = null;
						try {
							dependent = offspring.findPackageByName(dependency.getClient().getName());
						} catch (PackageNotFound e) {
							e.printStackTrace();
						}
	        			if (dependent!=null){
	        				dependency.setClient(dependent);
	        				offspring.removeRelationship(dependency);
	        			}
	        		}	        			
	            }
	          }
	    }
	    
	    private void addParentArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, Architecture parent, String scope){
	    	
	    	Package newComp;
	    	
	    	List<Package> allParentComponents = new ArrayList<Package> (parent.getAllPackages());
	    	if(!allParentComponents.isEmpty()){
				for (Package comp: allParentComponents){
					newComp=null;
	            	if (comp.containsConcern(feature) && comp.getOwnConcerns().size()==1){
	            		try {
							newComp = offspring.findPackageByName(comp.getName());
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}
	            		if (newComp==null){
	            			try {		            			
								newComp = offspring.createPackage(comp.getName());
								newComp.addConcern(feature.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
	            		}	            		
	            		addInterfacesToOffspring (feature, comp, newComp, offspring, parent);
	            		addClassesToOffspring (feature, comp, newComp, offspring, parent);
	            	}
	            	else{
	            		addInterfacesRealizingFeatureToOffspring (feature, comp, offspring, parent);
	            		addClassesRealizingFeatureToOffspring (feature, comp, offspring, parent, scope);	            		
	            	}	            	
	            }
			}
	    }	       
	    
	    
	    private void addOperationsRealizingFeatureToOffspring(Concern feature, Interface interfaceComp, Package comp, Architecture offspring, Architecture parent){
	    	
	    	Interface targetInterface = null;
			try {
				targetInterface = offspring.findInterfaceByName(interfaceComp.getName());
			} catch (InterfaceNotFound e1) {
				e1.printStackTrace();
			}
	    	List<Method> allOperations = new ArrayList<Method> (interfaceComp.getOperations());
			if (!allOperations.isEmpty()) {
				Iterator<Method> iteratorOperations = allOperations.iterator();
				while (iteratorOperations.hasNext()){
					Method operation= iteratorOperations.next();
	            	if (operation.containsConcern(feature) && operation.getOwnConcerns().size()==1){
	            		if (targetInterface==null){
	            			Package newComp = null;
							try {
								newComp = offspring.findPackageByName(comp.getName());
							} catch (PackageNotFound e1) {
								e1.printStackTrace();
							}
	            			if (newComp==null) {
	            				try {
									newComp=offspring.createPackage(comp.getName());
									newComp.addConcern(feature.getName());
								} catch (Exception e) {
									e.printStackTrace();
								}
	            			}
	            			try {
								targetInterface=offspring.createInterface(interfaceComp.getName());
								targetInterface.addConcern(feature.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
	            		}
	            		interfaceComp.moveOperationToInterface(operation, targetInterface);
	            	}
				}
			}
	    }     
	    
	    
	    
	    private void addMethodsRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent){
	    	
	    	Class targetClass = null;
			try {
				targetClass = (Class) offspring.findClassByName(classComp.getName());
			} catch (ClassNotFound e2) {
				e2.printStackTrace();
			}
	    	List<Method> allMethods = new ArrayList<Method> (classComp.getAllMethods());
			if (!allMethods.isEmpty()) {
				Iterator<Method> iteratorMethods = allMethods.iterator();
				while (iteratorMethods.hasNext()){
					Method method= iteratorMethods.next();
	            	if (method.containsConcern(feature) && method.getOwnConcerns().size()==1){
	            			if (targetClass==null){
	            				Package newComp = null;
								try {
									newComp = offspring.findPackageByName(comp.getName());
								} catch (PackageNotFound e1) {
									e1.printStackTrace();
								}
		            			if (newComp==null) {
		            				try {
										newComp=offspring.createPackage(comp.getName());
										newComp.addConcern(feature.getName());
									} catch (Exception e) {
										e.printStackTrace();
									}
		            			}
		            			try {
									targetClass=newComp.createClass(classComp.getName(),false);
									targetClass.addConcern(feature.getName());
								} catch (Exception e) {
									e.printStackTrace();
								}
		            		}
		            		classComp.moveMethodToClass(method, targetClass);
	            	}
				}
			}
	    }
	   
	    private Package getClassComponent(Element class_, Architecture architecture){
	    	Package sourceComp=null;
	    	Collection<Package> allComponents =  architecture.getAllPackages();
	    	for (Package comp: allComponents){
	    		if (comp.getClasses().contains(class_)){
	    			return comp;
	    		}
	    	}
	    	return sourceComp;
	    }
	    
	  //--------------------------------------------------------------------------
	    //mÈtodo para identificar as subclasses da classe pai na hierarquia de heranÁa
	    private List<Element> getChildren(Element cls){
	  	  List<Element> children = new ArrayList<Element>();
	  	  
	  	  for (Relationship relationship: cls.getRelationships()){
	  	    	if (relationship instanceof GeneralizationRelationship){
	  	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
	  	    		if (generalization.getParent().equals(cls))
	  	    			children.add(generalization.getChild());
	  	    	}
	  	  }	    			
	  	  return children;
	    }

	  //--------------------------------------------------------------------------
	    //mÈtodo para identificar as subclasses da classe pai na hierarquia de heranÁa
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
							
						      
    //--------------------------------------------------------------------------
	    //mÈtodo para identificar se a classe È subclasse na hierarquia de heranÁa
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
	    
	    private void moveChildrenAndRelationshipsToComponent(Element parent, Package sourceComp, Package targetComp, Architecture offspring, Architecture parentArch){
    	  	
			Collection<Element> children = getChildren(parent);
			
			if (sourceComp.getClasses().contains(parent)){
				sourceComp.moveClassToPackage(parent, targetComp);
				this.updateClassRelationships(parent, targetComp, sourceComp, offspring, parentArch);
			} else{
				for (Package auxComp: parentArch.getAllPackages()){
					if (auxComp.getClasses().contains(parent)){
						auxComp.moveClassToPackage(parent, targetComp);
						this.updateClassRelationships(parent, targetComp, auxComp, offspring, parentArch);	
						break;
					}
				}
			}
				
			for (Element child: children){
				if (!(sourceComp.getClasses().contains(child))){
					sourceComp = this.getClassComponent(child,parentArch);
					if (sourceComp.getName()!=targetComp.getName()){
						try {
							targetComp = offspring.findPackageByName(sourceComp.getName());
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}
						if (targetComp==null){
							try {
								targetComp=offspring.createPackage(sourceComp.getName());
								for (Concern feature:sourceComp.getOwnConcerns())
									targetComp.addConcern(feature.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}								
				moveChildrenAndRelationshipsToComponent(child, sourceComp, targetComp, offspring, parentArch);			
			}					 
			
		}
					
	    private void moveHierarchyToComponent(Element classComp, Package targetComp, Package sourceComp, Architecture offspring, Architecture parent, Concern concern){
			Element root = classComp;
			
			while (isChild(root)){
				root = getParent(root);		
				if (!(sourceComp.getClasses().contains(root))){
					sourceComp = this.getClassComponent(root,parent);
					if (sourceComp.getName()!=targetComp.getName()){
						try {
							targetComp = offspring.findPackageByName(sourceComp.getName());
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}
						if (targetComp==null){
							try {
								targetComp=offspring.createPackage(sourceComp.getName());
								targetComp.addConcern(concern.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			if (sourceComp.getClasses().contains(root)){
				moveChildrenAndRelationshipsToComponent(root,sourceComp,targetComp,offspring,parent);
			} else{
				for (Package auxComp: parent.getAllPackages()){
					if (auxComp.getClasses().contains(root)){
						if (auxComp.getName()!=targetComp.getName()){
							try {
								targetComp = offspring.findPackageByName(auxComp.getName());
							} catch (PackageNotFound e1) {
								e1.printStackTrace();
							}
							if (targetComp==null){
								try {
									targetComp=offspring.createPackage(auxComp.getName());
									targetComp.addConcern(concern.getName());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						moveChildrenAndRelationshipsToComponent(root,auxComp,targetComp,offspring,parent);
						break;
					}
				}
			}
		}
	    
//--------------------------------------------------------------------------
  	
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
		
      private void removeAttributesOfClassRealizingFeature(Class cls, Concern feature){
	    	List<Attribute> attributesClassComp = new ArrayList<Attribute>(cls.getAllAttributes());
			if (!attributesClassComp.isEmpty()){
				Iterator<Attribute> iteratorAttributes = attributesClassComp.iterator();
				while (iteratorAttributes.hasNext()){
	            	Attribute attribute = iteratorAttributes.next();
					if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size()==1){
						//sÛ elimina se a classe n„o fizer parte de uma hierarquia
						if (!searchForGeneralizations(cls)){
							cls.removeAttribute(attribute);
						}
					}
				}
			}
	    }
	    	   
	    				    
	  //metodo para remover os filhos de uma classe
		private void removeChildrenOfComponent(Element parent, Package comp, Architecture architecture){
			
			Collection<Element> children = getChildren(parent);
			for (Element child: children){
				removeChildrenOfComponent(child, comp, architecture);
			}
			if (comp.getClasses().contains(parent)){
				removeClassRelationships(parent,architecture);
				comp.removeClass(parent);
			} else{				
				for (Package auxComp: architecture.getAllPackages()){
					if (auxComp.getClasses().contains(parent)){
						removeClassRelationships(parent,architecture);
						auxComp.removeClass(parent);
						break;
					}
				}
			}
		}
	    
		private void removeClassesComponent(Package comp, Architecture offspring, String scope){
	    	
	    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
			if (!allClasses.isEmpty()) {
				Iterator<Class> iteratorClasses = allClasses.iterator();
				while (iteratorClasses.hasNext()){
	            	Class classComp= iteratorClasses.next();
	            	if (comp.getClasses().contains(classComp)){
	            		//se n„o estiver numa hierarquia elimina os relacionamentos e a classe
	            		if (!searchForGeneralizations(classComp)){
	            			this.removeClassRelationships(classComp,offspring);
	            			comp.removeClass(classComp);
	            		} else{ // tem que eliminar a hierarquia toda
	            			removeHierarchyOfComponent(classComp,comp,offspring);
	            		}		
	    			}
	            }
			}
	    }
	
	    private void removeClassesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring, String scope){
	    	
	    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
			if (!allClasses.isEmpty()) {
				Iterator<Class> iteratorClasses = allClasses.iterator();
				while (iteratorClasses.hasNext()){
	            	Class classComp= iteratorClasses.next();
	            	if (comp.getClasses().contains(classComp)){
	            		if ((classComp.containsConcern(feature)) && (classComp.getOwnConcerns().size()==1)){
	    					//se n„o estiver numa hierarquia elimina os relacionamentos e a classe
	    	            		if (!searchForGeneralizations(classComp)){
	    	            			this.removeClassRelationships(classComp,offspring);
	    	            			comp.removeClass(classComp);
	    	            		} else{ // tem que eliminar a hierarquia toda
	    	            			removeHierarchyOfComponent(classComp,comp,offspring);
	    	            		}       		
	    							
	    							
	    					}
	    					else{
	    							if (scope=="allLevels"){
	    								removeAttributesOfClassRealizingFeature(classComp, feature);
	    								removeMethodsOfClassRealizingFeature(classComp, feature);
	    							}
	    					}
	            	}	            						
				}
			}
	    }
	    
	    private void removeClassRelationships(Element cls, Architecture architecture){
	    	
			List<Relationship> relationshipsCls = new ArrayList<Relationship>(cls.getRelationships());
			if (!relationshipsCls.isEmpty()){                						
				Iterator<Relationship> iteratorRelationships = relationshipsCls.iterator();
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
											
					if (relationship instanceof GeneralizationRelationship){
						GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
						if ((generalization.getChild().equals(cls)) || (generalization.getParent().equals(cls))){
							architecture.removeRelationship(generalization);
							
						}
					}
				}
			}
		 }
	    
	    private void removeComponentRelationships (Package comp, Architecture offspring){
	    	
	    	List<Relationship> relationships = new ArrayList<Relationship> (comp.getRelationships());
	    	if(!relationships.isEmpty()){
				Iterator<Relationship> iteratorRelationships = relationships.iterator();
	            while (iteratorRelationships.hasNext()){
	            	Relationship relationship= iteratorRelationships.next();
	            	if (relationship instanceof AbstractionRelationship){
	            		AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
	            		if (abstraction.getClient().equals(comp)) 
	            			offspring.removeRelationship(abstraction);
	            	}
	            	else {
	            		if (relationship instanceof DependencyRelationship){
	            			DependencyRelationship dependency = (DependencyRelationship) relationship;
		            		if (dependency.getSupplier().equals(comp))
		            			offspring.removeRelationship(dependency);
		            	}
	            	}
	            }
	    	}
	    }
	    
	    private void removeHierarchyOfComponent(Class cls, Package comp, Architecture architecture){
	    	
			Element parent = cls;
			while (isChild(parent)){
				parent = getParent(parent);				
			}
			removeChildrenOfComponent(parent,comp,architecture);			
		}
	    
	    private void removeInterfaceRelationships (Interface interface_, Architecture offspring){
	    	
//	    	List<Relationship> relationships = new ArrayList<Relationship> (interface_.getRelationships());
//	    	if(!relationships.isEmpty()){
//				Iterator<Relationship> iteratorRelationships = relationships.iterator();
//	            while (iteratorRelationships.hasNext()){
//	            	Relationship relationship= iteratorRelationships.next();
//	            	if (relationship instanceof AbstractionRelationship){
//	            		AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
//	            		if (abstraction.getParent().equals(interface_)) // Pegar client ou supplier?
//	            			offspring.removeImplementedInterfaceFromComponent(interface_, abstraction.getChild()); //  Pegar client ou supplier? (ambos sao Element)		            			
//	            	}
//	            	else {
//	            		if (relationship instanceof DependencyRelationship){
//	            			DependencyRelationship dependency = (DependencyRelationship) relationship;
//		            		if (dependency.getInterface().equals(interface_)) //  Pegar client ou supplier? (ambos sao Element)
//		            			offspring.removeRequiredInterfaceFromComponent(interface_, dependency.getComponent());) //  Pegar client ou supplier? (ambos sao Element)
//		            	}
//	            	}
//	            }
//	    	}
	    }
	    
	    private void removeInterfacesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring){
	    	
	    	List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
			if (!allInterfaces.isEmpty()) {
				Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
	            while (iteratorInterfaces.hasNext()){
	            	Interface interfaceComp= iteratorInterfaces.next();
					if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size()==1){
						this.removeInterfaceRelationships(interfaceComp, offspring);
						offspring.removeInterface(interfaceComp);
					}
					else{
						removeOperationsOfInterfaceRealizingFeature(interfaceComp, feature);						
					}
				}
			}
	    }
	    
	    private void removeMethodsOfClassRealizingFeature(Class cls, Concern feature){
	    	List<Method> methodsClassComp = new ArrayList<Method>(cls.getAllMethods());
			if (!methodsClassComp.isEmpty()){
				Iterator<Method> iteratorMethods = methodsClassComp.iterator();
				while (iteratorMethods.hasNext()){
	            	Method method = iteratorMethods.next();
					if (method.containsConcern(feature) && method.getOwnConcerns().size()==1){
						//só elimina se a classe nãoo fizer parte de uma hierarquia
						if (!searchForGeneralizations(cls)){
							cls.removeMethod(method);
						}
					}
				}	
			}
	    }
	    
	    private void removeOffspringArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, String scope){
	    	
	    	List<Package> allComponents = new ArrayList<Package> (offspring.getAllPackages());
	    	if(!allComponents.isEmpty()){
				Iterator<Package> iteratorComponents = allComponents.iterator();
	            while (iteratorComponents.hasNext()){
	            	Package comp= iteratorComponents.next();
	            	if (comp.containsConcern(feature) && comp.getOwnConcerns().size()==1){
	            		List<Interface> allInterfacesComp = new ArrayList<Interface> (comp.getImplementedInterfaces());
	            		if (!allInterfacesComp.isEmpty()) {
							Iterator<Interface> iteratorInterfaces = allInterfacesComp.iterator();
				            while (iteratorInterfaces.hasNext()){
				            	Interface interfaceComp= iteratorInterfaces.next();
				            	this.removeInterfaceRelationships(interfaceComp, offspring);
				            	offspring.removeInterface(interfaceComp);											
								}
	            		}
	            		this.removeClassesComponent(comp, offspring, scope);
	            		removeComponentRelationships(comp,offspring);
	            		offspring.removePackage(comp);
	            		//TODO ver se é preciso exlcuir as classes do componente e seus relacionamentos
					}
					else{					
						this.removeInterfacesComponentRealizingFeature(comp, feature, offspring);
						this.removeClassesComponentRealizingFeature(comp, feature, offspring, scope);		
					}
	            }	
			}
		}
	       
	   private void removeOperationsOfInterfaceRealizingFeature(Interface interfaceComp, Concern feature){
	    	List<Method> operationsInterfaceComp = new ArrayList<Method> (interfaceComp.getOperations());
			if (!operationsInterfaceComp.isEmpty()){
				Iterator<Method> iteratorOperations = operationsInterfaceComp.iterator();
				while (iteratorOperations.hasNext()){
	            	Method operation = iteratorOperations.next();
	            	if (operation.containsConcern(feature) && operation.getOwnConcerns().size()==1){
						interfaceComp.removeOperation(operation);
					}
				}
			}
	    }
	   			 
		
	   
   
    //--------------------------------------------------------------------------
	    //mÈtodo para verificar se algum dos relacionamentos recebidos È generalizaÁ„o
	    private boolean searchForGeneralizations(Element cls){
	    	
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
	    
   // - - - - - - - - - -- -
		private void updateClassRelationships(Element classComp, Package target, Package source, Architecture offspring, Architecture parent){
			//Atualiza os relacionamentos mesmo que as classes sejam de componentes distintos
			Collection<Relationship> parentRelationships = parent.getAllRelationships();
			for (Relationship relationship : parentRelationships){
				if (relationship instanceof DependencyRelationship){
					DependencyRelationship dependency = (DependencyRelationship) relationship;
					Class client = (Class) dependency.getClient();
					if (client.equals(classComp)) {
						Class targetClass = null;
						try {
							targetClass = (Class) offspring.findClassByName(dependency.getSupplier().getName());
						} catch (ClassNotFound e) {
							e.printStackTrace();
						}
						if (targetClass!=null)
							offspring.forDependency().create("").withClient(targetClass).withSupplier(classComp).build();
					}
					else{
						Class supplier = (Class) dependency.getSupplier();
    					if (supplier.equals(classComp)) {
    						Class targetClass = null;
							try {
								targetClass = (Class) offspring.findClassByName(dependency.getClient().getName());
							} catch (ClassNotFound e) {
								e.printStackTrace();
							}
    						if (targetClass!=null)
    							offspring.forDependency().create("").withClient(classComp).withSupplier(targetClass).build();
    					}
					}
				}
				if (relationship instanceof GeneralizationRelationship){
					GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
    				Class parentClass = (Class) generalization.getParent();
    				if (parentClass.equals(classComp)) {
    					Class targetClass = null;
						try {
							targetClass = (Class) offspring.findClassByName(generalization.getChild().getName());
						} catch (ClassNotFound e) {
							e.printStackTrace();
						}
    					if (targetClass!=null)
    						offspring.forGeneralization().createGeneralization(classComp, targetClass);
    				}
    				else{
    					Class childClass = (Class) generalization.getChild();
        				if (childClass.equals(classComp)) {
        					Class targetClass = null;
							try {
								targetClass = (Class) offspring.findClassByName(generalization.getParent().getName());
							} catch (ClassNotFound e) {
								e.printStackTrace();
							}
        					if (targetClass!=null) 
        						offspring.forGeneralization().createGeneralization(targetClass, classComp);
        				}
    				}	
				}
				if (relationship instanceof AssociationRelationship){
					AssociationRelationship association = (AssociationRelationship) relationship;
    				List<AssociationEnd> participants = new ArrayList<AssociationEnd>(association.getParticipants());
    				int i =0;
    				while (i<2){
						if (participants.get(i).getCLSClass().equals(classComp)){ 
							Class targetClass = null;
							int j;
    						if (i == 0) j = i+1;
    						else j = i-1;
    						try {
								targetClass = (Class) offspring.findClassByName(participants.get(j).getCLSClass().getName());
							} catch (ClassNotFound e) {
								e.printStackTrace();
							}
    						if (targetClass!=null){
    							AssociationRelationship auxAssociation = new AssociationRelationship(classComp,targetClass);
    								offspring.addRelationship(auxAssociation);
	    					}			    						
    					}	
						
						i++;
    				}
				}
			}
		}
					
	private void updateInterfaceDependencies(Interface interface_, Architecture offspring, Architecture parent){
		
		Collection<Relationship> relationships = parent.getAllRelationships();
		for (Relationship relationship:relationships){
			if (relationship instanceof DependencyRelationship){
				DependencyRelationship dependency = (DependencyRelationship) relationship;
				Interface itf = (Interface) dependency.getSupplier();
				if (itf.equals(interface_)) {
					Package targetComp = null;
					try {
						targetComp = offspring.findPackageByName(dependency.getClient().getName());
					} catch (PackageNotFound e) {
						e.printStackTrace();
					} // Pegar client ou supplier? (ambos sao Element)		            			
					if (targetComp!=null){
						offspring.addExternalInterface(interface_);
						offspring.addRequiredInterfaceToComponent(interface_, targetComp);
					}
				}
			} 
		}
	}
					
					
	private void updateVariabilitiesOffspring(Architecture offspring) {

		for (Variability variability : offspring.getAllVariabilities()) {
			VariationPoint variationPoint = variability.getVariationPoint();
			Element elementVP = variationPoint.getVariationPointElement();
			if (!(offspring.findElementByName(elementVP.getName(), "class").equals(elementVP))) {
				variationPoint.replaceVariationPointElement(offspring.findElementByName(elementVP.getName(), "class"));
			}
		}
	}
							
}