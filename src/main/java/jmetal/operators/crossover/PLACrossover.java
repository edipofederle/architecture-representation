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
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
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
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;

	public class PLACrossover extends Crossover {

	    /**
	     * ARCHITECTURE_SOLUTION represents class jmetal.encodings.solutionType.ArchitectureSolutionType
	     */
	    private static Class ARCHITECTURE_SOLUTION;

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
	    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException, CloneNotSupportedException {

	    	String scopeLevel = "allLevels"; //use "oneLevel" para n�o verificar a presen�a de interesses nos atributos e m�todos
	        Solution[] offspring = new Solution[2];

	        
	        Solution[] crossFeature = this.crossoverFeatures(crossoverProbability_, parent1, parent2, scopeLevel);
	        offspring[0] = crossFeature[0];
	        offspring[1] = crossFeature[1];

         
	        return offspring;
	    }
	    
	     
	  //--------------------------------------------------------------------------
	    public Solution[] crossoverFeatures(double probability, Solution parent1, Solution parent2, String scope) throws JMException, CloneNotSupportedException {

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
	    
	    private void obtainChild(Concern feature, Architecture parent, Architecture offspring, String scope) throws CloneNotSupportedException{
	    	//eliminar os elementos arquiteturais que realizam feature em offspring
	    	removeOffspringArchitecturalElementsRealizingFeature(feature,offspring, scope);
	    	//adicionar em offspring os elementos arquiteturais que realizam feature em parent
	    	Architecture auxParent = parent.deepClone();
	    	addParentArchitecturalElementsRealizingFeature(feature,offspring, auxParent,scope);	
	    	updateVariabilitiesOffspring(offspring);
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
								offspring.removeInterface(interfaceComp);
								}
	            		}
	            		removeComponentRelationships(comp,offspring);
					}
					else{					
						this.removeInterfacesComponentRealizingFeature(comp, feature, offspring);
						this.removeClassesComponentRealizingFeature(comp, feature, offspring, scope);		
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
	            	offspring.removeRelationship(relationship);
	            }
	    	}
	    }
	    
	    private void removeClassRelationships(Class cls, Architecture architecture){
		    
			List<Relationship> relationshipsCls = new ArrayList<Relationship>(cls.getRelationships());
			if (!relationshipsCls.isEmpty()){                						
				Iterator<Relationship> iteratorRelationships = relationshipsCls.iterator();
	            while (iteratorRelationships.hasNext()){
	            	Relationship relationship= iteratorRelationships.next();
	            	architecture.removeRelationship(relationship);
				}
			}
		 }
	    
	    private void removeInterfacesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring){
	    	List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
			if (!allInterfaces.isEmpty()) {
				Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
	            while (iteratorInterfaces.hasNext()){
	            	Interface interfaceComp= iteratorInterfaces.next();
					if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size()==1){
						offspring.removeInterface(interfaceComp);
					}
					else{
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
				}
			}
	    }
	    
	    private void removeClassesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring, String scope){
	    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
			if (!allClasses.isEmpty()) {
				Iterator<Class> iteratorClasses = allClasses.iterator();
				while (iteratorClasses.hasNext()){
	            	Class classComp= iteratorClasses.next();
	            	if ((classComp.containsConcern(feature)) && (classComp.getOwnConcerns().size()==1)){
							this.removeClassRelationships(classComp,offspring);
							
							//if (!searchForGeneralizations(classComp.getRelationships())){
							comp.removeClass(classComp);
						}
						else{
							if (scope=="allLevels"){
								List<Attribute> attributesClassComp = new ArrayList<Attribute>(classComp.getAllAttributes());
								if (!attributesClassComp.isEmpty()){
									Iterator<Attribute> iteratorAttributes = attributesClassComp.iterator();
									while (iteratorAttributes.hasNext()){
						            	Attribute attribute = iteratorAttributes.next();
										if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size()==1){
											classComp.removeAttribute(attribute);
										}
									}
								}
								List<Method> methodsClassComp = new ArrayList<Method>(classComp.getAllMethods());
								if (!methodsClassComp.isEmpty()){
									Iterator<Method> iteratorMethods = methodsClassComp.iterator();
									while (iteratorMethods.hasNext()){
						            	Method method = iteratorMethods.next();
										if (method.containsConcern(feature) && method.getOwnConcerns().size()==1){
											classComp.removeMethod(method);
										}
									}	
								}
							}
						}
	            	//}					
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
							newComp = offspring.createPackage(comp.getName());
							newComp.addConcern(feature.getName());
						} catch (Exception e) {
							e.printStackTrace();
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
	    
	    private void removeInterfaceRelationships (Interface interface_, Architecture offspring){
	    	
	    	List<Relationship> relationships = new ArrayList<Relationship> (interface_.getRelationships());
	    	if(!relationships.isEmpty()){
				Iterator<Relationship> iteratorRelationships = relationships.iterator();
	            while (iteratorRelationships.hasNext()){
	            	Relationship relationship= iteratorRelationships.next();
	            	offspring.removeRelationship(relationship);
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
	        				offspring.addRelationship(dependency);
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
	            	Class classComp= iteratorClasses.next();
	            	Collection<Relationship> classRelationships = classComp.getRelationships();
	            	parent.deleteClassRelationships(classComp);
	            	comp.moveClassToPackage(classComp, newComp);
	            	for (Relationship relationship:classRelationships){
	            		if (relationship instanceof DependencyRelationship){
	            			DependencyRelationship dependency = (DependencyRelationship) relationship;
	            			Class client = (Class) dependency.getClient();
	            			if (client.equals(classComp)) {
	            				Class targetClass = null;
								try {
									targetClass = offspring.findClassByName(dependency.getSupplier().getName()).get(0);
								} catch (ClassNotFound e) {
									e.printStackTrace();
								}
	            				if (targetClass!=null){
	            					dependency.replaceSupplier(targetClass);
	            					offspring.addRelationship(dependency);
	            				}
	            			}
	            			else{
	            				Class supplier = (Class) dependency.getSupplier();
	            				if (supplier.equals(classComp)) {
	            					Class targetClass = null;
									try {
										targetClass = offspring.findClassByName(dependency.getClient().getName()).get(0);
									} catch (ClassNotFound e) {
										e.printStackTrace();
									}
	            					if (targetClass!=null){
	            						dependency.replaceClient(targetClass);
	            						offspring.addRelationship(dependency);
	            					}
	            				}
	            			}
	            		}
	            		if (relationship instanceof GeneralizationRelationship){
	            			GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
	            				Class parentClass = generalization.getParent();
	            				if (parentClass.equals(classComp)) {
	            					Class targetClass = null;
									try {
										targetClass = offspring.findClassByName(generalization.getChild().getName()).get(0);
									} catch (ClassNotFound e4) {
										e4.printStackTrace();
									}
	            					if (targetClass!=null){
	            						generalization.replaceChild(targetClass);
	               					    offspring.addRelationship(generalization);
	            					}
	            					else{//busca a classe na arquitetura parent para adicionar na offspring
	            						try {
											targetClass = parent.findClassByName(generalization.getChild().getName()).get(0);
										} catch (ClassNotFound e3) {
											e3.printStackTrace();
										}
	            						
	            						if (targetClass!=null) {
	            							parent.deleteClassRelationships(targetClass);
	            							Package sourceComp = null;
											try {
												sourceComp = parent.findPackageOfClass(targetClass);
											} catch (PackageNotFound e2) {
												e2.printStackTrace();
											}
	            							if (sourceComp.equals(newComp)){
	            								comp.moveClassToPackage(targetClass, newComp);
	            							}
	            							else{
	            								Package targetComp = null;
												try {
													targetComp = offspring.findPackageByName(sourceComp.getName());
												} catch (PackageNotFound e1) {
													e1.printStackTrace();
												}
	            								if (targetComp==null){
	            									try {
														targetComp= offspring.createPackage(sourceComp.getName());
													} catch (Exception e) {
														e.printStackTrace();
													}	            									
	            								}
	            								comp.moveClassToPackage(targetClass, targetComp);
	            							}
	            							generalization.replaceChild(targetClass);
	            	   					    offspring.addRelationship(generalization);
	            						}						
	            					}
	            				}
	            				else{
	            					Class childClass = generalization.getChild();
	            				    if (childClass.equals(classComp)) {
	            					   Class targetClass = null;
									try {
										targetClass = offspring.findClassByName(generalization.getParent().getName()).get(0);
									} catch (ClassNotFound e3) {
										e3.printStackTrace();
									}
	                				   if (targetClass!=null){ 
	                					  generalization.replaceParent(targetClass);
	                					  offspring.addRelationship(generalization);
	                				  }
	                				  else{//busca a classe no comp da parent para adicionar em newComp na offspring
	               						try {
											targetClass = parent.findClassByName(generalization.getParent().getName()).get(0);
										} catch (ClassNotFound e2) {
											e2.printStackTrace();
										}
	               						if (targetClass!=null) {
	               							parent.deleteClassRelationships(targetClass);
	               							Package sourceComp = null;
											try {
												sourceComp = parent.findPackageOfClass(targetClass);
											} catch (PackageNotFound e1) {
												e1.printStackTrace();
											}
	            							if (sourceComp.equals(newComp)){
	            								comp.moveClassToPackage(targetClass, newComp);
	            							}
	            							else{
	            								Package targetComp = null;
												try {
													targetComp = offspring.findPackageByName(sourceComp.getName());
												} catch (PackageNotFound e1) {
													e1.printStackTrace();
												}
	            								if (targetComp==null){
	            									try {
														targetComp= offspring.createPackage(sourceComp.getName());
													} catch (Exception e) {
														e.printStackTrace();
													}	            									
	            								}
	            								comp.moveClassToPackage(targetClass, targetComp);
	            							}
	               							generalization.replaceParent(targetClass);
	               	   					    offspring.addRelationship(generalization);
	               						}						
	               					}
	                			   }
	            			   }	
	            		}
	            		if (relationship instanceof AssociationRelationship){
	            			AssociationRelationship association = (AssociationRelationship) relationship;
	            				for (AssociationEnd associationEnd : association.getParticipants()) {
	            					if (!(associationEnd.getCLSClass().equals(classComp))){ 
	            						Class targetClass = null;
										try {
											targetClass = offspring.findClassByName(associationEnd.getCLSClass().getName()).get(0);
										} catch (ClassNotFound e) {
											e.printStackTrace();
										}
	            						if (targetClass!=null){
	            							associationEnd.replaceCLSClass(targetClass);
	            							offspring.addRelationship(association);
	            					}
	            				}
	            			}
	            		}
	            	}
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
	            	if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1){
	        			try {
							newComp=offspring.findPackageByName(comp.getName());
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}
	        			if (newComp == null)
							try {
								newComp = offspring.createPackage(comp.getName());
								newComp.addConcern(feature.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
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
	        					offspring.addRelationship(dependency);
	        				}
	        			}	        			
	            	}
	            	else{
	            		addOperationsRealizingFeatureToOffspring(feature, interfaceComp, comp, offspring, parent);
	            	}
				}
			}
	    }
	    
	    private void addOperationsRealizingFeatureToOffspring(Concern feature, Interface interfaceComp, Package comp, Architecture offspring, Architecture parent){
	    	Interface targetInterface = null;
			try {
				targetInterface = offspring.findInterfaceByName(interfaceComp.getName());
			} catch (InterfaceNotFound e2) {
				e2.printStackTrace();
			}
	    	List<Method> allOperations = new ArrayList<Method> (interfaceComp.getOperations());
			if (!allOperations.isEmpty()) {
				Iterator<Method> iteratorOperations = allOperations.iterator();
				while (iteratorOperations.hasNext()){
					Method operation= iteratorOperations.next();
	            	if (operation.containsConcern(feature) && operation.getOwnConcerns().size()==1){
            			Package newComp;
						try {
							newComp = offspring.findPackageByName(comp.getName());
	            			if (newComp==null) {
	            				try {
									newComp=offspring.createPackage(comp.getName());
									newComp.addConcern(feature.getName());
								} catch (Exception e) {
									e.printStackTrace();
								}
	            			}
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}

            			try {
							targetInterface=offspring.createInterface(interfaceComp.getName());
							targetInterface.addConcern(feature.getName());
						} catch (Exception e) {
							e.printStackTrace();
						}
	            		interfaceComp.moveOperationToInterface(operation, targetInterface);
	            	}
				}
			}
	    }
	    
	    private void addClassesRealizingFeatureToOffspring (Concern feature, Package comp, Architecture offspring, Architecture parent, String scope) {
	    	Package newComp = null;
	    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
			if (!allClasses.isEmpty()) {
				Iterator<Class> iteratorClasses = allClasses.iterator();
				while (iteratorClasses.hasNext()){
	            	Class classComp= iteratorClasses.next();
	            	if (classComp.containsConcern(feature) && classComp.getOwnConcerns().size()==1){
		        			try {
								newComp = offspring.findPackageByName(comp.getName());
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
		    				Collection<Relationship> classRelationships = classComp.getRelationships();
		        			parent.deleteClassRelationships(classComp);
		        			comp.moveClassToPackage(classComp, newComp);
		    				
		        			for (Relationship relationship:classRelationships){
		        				if (relationship instanceof DependencyRelationship){
		        					DependencyRelationship dependency = (DependencyRelationship) relationship;
		        					Class client = (Class) dependency.getClient();
		        					if (client.equals(classComp)) {
		        						Class targetClass = null;
										try {
											targetClass = offspring.findClassByName(dependency.getSupplier().getName()).get(0);
										} catch (ClassNotFound e) {
											e.printStackTrace();
										}
		        						if (targetClass!=null){
		        							dependency.replaceSupplier(targetClass);
		        							offspring.addRelationship(dependency);
		        						}
		        					}
		        					else{
		        						Class supplier = (Class) dependency.getSupplier();
		            					if (supplier.equals(classComp)) {
		            						Class targetClass = null;
											try {
												targetClass = offspring.findClassByName(dependency.getClient().getName()).get(0);
											} catch (ClassNotFound e) {
												e.printStackTrace();
											}
		            						if (targetClass!=null){
		            							dependency.replaceClient(targetClass);
		            							offspring.addRelationship(dependency);
		            						}
		            					}
		        					}
		        				}
		        				if (relationship instanceof GeneralizationRelationship){
		        					GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
		            				Class parentClass = generalization.getParent();
		            				if (parentClass.equals(classComp)) {
		            					Class targetClass = null;
										try {
											targetClass = offspring.findClassByName(generalization.getChild().getName()).get(0);
										} catch (ClassNotFound e1) {
											e1.printStackTrace();
										}
		            					if (targetClass!=null){
		            						generalization.replaceChild(targetClass);
			            					offspring.addRelationship(generalization);
		            					}
		            					else{//busca a classe no comp da parent para adicionar em newComp na offspring
		            						try {
												targetClass = parent.findClassByName(generalization.getChild().getName()).get(0);
											} catch (ClassNotFound e2) {
												e2.printStackTrace();
											}
		            						if (targetClass!=null) {
		            							parent.deleteClassRelationships(targetClass);
		            							Package sourceComp = null;
												try {
													sourceComp = parent.findPackageOfClass(targetClass);
												} catch (PackageNotFound e2) {
													e2.printStackTrace();
												}
		            							if (sourceComp.equals(newComp)){
		            								comp.moveClassToPackage(targetClass, newComp);
		            							}
		            							else{
		            								Package targetComp = null;
													try {
														targetComp = offspring.findPackageByName(sourceComp.getName());
													} catch (PackageNotFound e1) {
														e1.printStackTrace();
													}
		            								if (targetComp==null){
		            									try {
															targetComp= offspring.createPackage(sourceComp.getName());
														} catch (Exception e) {
															e.printStackTrace();
														}	            									
		            								}
		            								comp.moveClassToPackage(targetClass, targetComp);
		            							}
		            							generalization.replaceChild(targetClass);
		            	   					    offspring.addRelationship(generalization);
		            						}						
		            					}
		            				}
		            				else{
		            					Class childClass = generalization.getChild();
			            				if (childClass.equals(classComp)) {
			            					Class targetClass = null;
											try {
												targetClass = offspring.findClassByName(generalization.getParent().getName()).get(0);
											} catch (ClassNotFound e2) {
												e2.printStackTrace();
											}
			            					if (targetClass != null){ 
			            						generalization.replaceParent(targetClass);
			            						offspring.addRelationship(generalization);
			            					}
			            					else{//busca a classe no comp da parent para adicionar em newComp na offspring
			            						try {
													targetClass = parent.findClassByName(generalization.getParent().getName()).get(0);
												} catch (ClassNotFound e1) {
													e1.printStackTrace();
												}
			            						if (targetClass!=null) {
			            							parent.deleteClassRelationships(targetClass);
			            							Package sourceComp = null;
													try {
														sourceComp = parent.findPackageOfClass(targetClass);
													} catch (PackageNotFound e1) {
														e1.printStackTrace();
													}
			            							if (sourceComp.equals(newComp)){
			            								comp.moveClassToPackage(targetClass, newComp);
			            							}
			            							else{
			            								Package targetComp = null;
														try {
															targetComp = offspring.findPackageByName(sourceComp.getName());
														} catch (PackageNotFound e1) {
															e1.printStackTrace();
														}
			            								if (targetComp==null){
			            									try {
																targetComp= offspring.createPackage(sourceComp.getName());
															} catch (Exception e) {
																e.printStackTrace();
															}	            									
			            								}
			            								comp.moveClassToPackage(targetClass, targetComp);
			            							}
			            							generalization.replaceParent(targetClass);
			            	   					    offspring.addRelationship(generalization);
			            						}						
			            					}
			            				}
		            				}	
		        				}
		        				if (relationship instanceof AssociationRelationship){
		        					AssociationRelationship association = (AssociationRelationship) relationship;
		            				for (AssociationEnd associationEnd : association.getParticipants()) {
		            					if (!(associationEnd.getCLSClass().equals(classComp))){ 
		            						Class targetClass = null;
											try {
												targetClass = offspring.findClassByName(associationEnd.getCLSClass().getName()).get(0);
											} catch (ClassNotFound e) {
												e.printStackTrace();
											}
		            						if (targetClass!=null){
		            							associationEnd.replaceCLSClass(targetClass);
		            							offspring.addRelationship(association);
		            						}
		            					}
		            				}
		        				}
		        			}
		        		}
		    			else{
		    				if (scope=="allLevels"){
		    					addAttributesRealizingFeatureToOffspring(feature, classComp, comp, offspring, parent);
		    					addMethodsRealizingFeatureToOffspring(feature, classComp, comp, offspring, parent);
		    				}
		    			}
				}
    		}
	    }
	    
	    private void addAttributesRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent){
	    	Class targetClass = null;
			try {
				targetClass = offspring.findClassByName(classComp.getName()).get(0);
			} catch (ClassNotFound e1) {
				e1.printStackTrace();
			}
	    	List<Attribute> allAttributes = new ArrayList<Attribute> (classComp.getAllAttributes());
			if (!allAttributes.isEmpty()) {
				Iterator<Attribute> iteratorAttributes = allAttributes.iterator();
				while (iteratorAttributes.hasNext()){
	            	Attribute attribute= iteratorAttributes.next();
	            	if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size()==1){
            			Package newComp = null;
						try {
							newComp = offspring.findPackageByName(comp.getName());
		         			if (newComp==null) {
	            				try {
									newComp=offspring.createPackage(comp.getName());
									newComp.addConcern(feature.getName());
								} catch (Exception e) {
									e.printStackTrace();
								}
	            			}
						} catch (PackageNotFound e1) {
							e1.printStackTrace();
						}
            			try {
							targetClass=newComp.createClass(classComp.getName());
							targetClass.addConcern(feature.getName());
						} catch (Exception e) {
							e.printStackTrace();
						}
	            		classComp.moveAttributeToClass(attribute, targetClass);
	            	}
				}
			}
	    }
	    
	private void addMethodsRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring,	Architecture parent) {
		Class targetClass = null;
		try {
			targetClass = offspring.findClassByName(classComp.getName()).get(0);
		} catch (ClassNotFound e1) {
			e1.printStackTrace();
		}
		List<Method> allMethods = new ArrayList<Method>(classComp.getAllMethods());
		if (!allMethods.isEmpty()) {
			Iterator<Method> iteratorMethods = allMethods.iterator();
			while (iteratorMethods.hasNext()) {
				Method method = iteratorMethods.next();
				if (method.containsConcern(feature)	&& method.getOwnConcerns().size() == 1) {
					Package newComp = null;
					try {
						newComp = offspring.findPackageByName(comp.getName());
						if (newComp == null) {
							try {
								newComp = offspring.createPackage(comp.getName());
								newComp.addConcern(feature.getName());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (PackageNotFound e1) {
						e1.printStackTrace();
					}
					try {
						targetClass = newComp.createClass(classComp.getName());
						targetClass.addConcern(feature.getName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					classComp.moveMethodToClass(method, targetClass);
				}
			}
		}
	}

	private void updateVariabilitiesOffspring(Architecture offspring) {
		for (Variability variability : offspring.getAllVariabilities()) {
			VariationPoint variationPoint = variability.getVariationPoint();
			Element elementVP = variationPoint.getVariationPointElement();
			if (!(offspring.findElementByName(elementVP.getName(), "class")	.equals(elementVP))) {
				// procura o elemento em offspring e substitui o variationPoint
				variationPoint.replaceVariationPointElement(offspring.findElementByName(elementVP.getName(), "class"));
			}
		}
	}
	    
	     //--------------------------------------------------------------------------
	    	    
	    /**
		 * Executes the operation
		 * @param object An object containing an array of two solutions 
		 * @return An object containing an array with the offSprings
		 * @throws JMException 
	     * @throws  
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

			Solution[] offspring = null;
			try {
				offspring = doCrossover(crossoverProbability_, parents[0], parents[1]);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 

			//problem.evaluateConstraints(offspring[0]);
			//problem.evaluateConstraints(offspring[1]);
			
			return offspring; 
		} // execute

	    //--------------------------------------------------------------------------
	
		public <T> T randomObject(List<T> allObjects)  throws JMException   {
	        
	        int numObjects= allObjects.size(); 
	        int key;
	        T object;
	        if (numObjects == 0) {
	        	object = null;
	        } else{
	        	key = PseudoRandom.randInt(0, numObjects-1); //sorteio um n�mero para representar a posi��o da classe a ser alterada
	        	object = allObjects.get(key);
	        }
	        
	        return object;      	    	
	        }
		
	
}
