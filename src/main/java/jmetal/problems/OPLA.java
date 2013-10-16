package jmetal.problems;

import java.util.Collection;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.solutionType.ArchitectureSolutionType;
import jmetal.metrics.concernDrivenMetrics.concernCohesion.LCC;
import jmetal.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;
import jmetal.metrics.concernDrivenMetrics.concernDiffusion.CDAC;
import jmetal.metrics.concernDrivenMetrics.concernDiffusion.CDACResult;
import jmetal.metrics.concernDrivenMetrics.concernDiffusion.CDAI;
import jmetal.metrics.concernDrivenMetrics.concernDiffusion.CDAIResult;
import jmetal.metrics.concernDrivenMetrics.concernDiffusion.CDAO;
import jmetal.metrics.concernDrivenMetrics.concernDiffusion.CDAOResult;
import jmetal.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBC;
import jmetal.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBCResult;
import jmetal.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBC;
import jmetal.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBCResult;
import jmetal.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBC;
import jmetal.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBCResult;
import jmetal.metrics.conventionalMetrics.ClassDependencyIn;
import jmetal.metrics.conventionalMetrics.ClassDependencyOut;
import jmetal.metrics.conventionalMetrics.DependencyIn;
import jmetal.metrics.conventionalMetrics.MeanDepComponents;
import jmetal.metrics.conventionalMetrics.MeanNumOpsByInterface;
import jmetal.metrics.conventionalMetrics.RelationalCohesion;
import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;


//criado por Thelma em agosto/2012
public class OPLA extends Problem {

	private static final long serialVersionUID = 884633138619836573L;
	
	//variaveis para controlar os contadores de componentes e interfaces
    public static int contComp_=0;
    public static int contInt_=0;
	public static int contClass_=0;
	
	public Architecture architecture_;
		
	public OPLA(String xmiFilePath) throws Exception {
	        
			numberOfVariables_ = 1;
	        numberOfObjectives_ = 2;
	        numberOfConstraints_ = 0;
	        problemName_ = "OPLA";
	        solutionType_ = new ArchitectureSolutionType(this);
	        variableType_ = new java.lang.Class[numberOfVariables_];
	        length_ = new int[numberOfVariables_];
	        variableType_[0] = java.lang.Class.forName("arquitetura.representation.Architecture"); 
	      
	        architecture_ = new ArchitectureBuilder().create(xmiFilePath);
	       //length_[0] = numberOfElements_; 
	    }

	    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
	
		@Override
	 
	    
	    public void evaluate(Solution solution) {
	        double fitness0 = 0.0;
	        double fitness1 = 0.0;
			//Architecture architecture = (Architecture) solution.getDecisionVariables()[0];
            fitness0 = evaluateMSIFitness((Architecture) solution.getDecisionVariables()[0]);
	        fitness1 = evaluateMACFitness((Architecture) solution.getDecisionVariables()[0]);
	        solution.setObjective(0, fitness0);
	        solution.setObjective(1, fitness1);
	       
	    }
	    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    
		//private double evaluateElegance (Architecture architecture) {
		//	ECElegance NAC = new ECElegance(architecture);
		//	return NAC.getResults();
	//	}
		
		
		
	    private double evaluateMSIFitness (Architecture architecture) {
	    	double sumCIBC = 0.0;
	        double sumIIBC = 0.0;
	        double sumOOBC = 0.0;
	        double sumCDAC = 0.0;
	        double sumCDAI = 0.0;
	        double sumCDAO = 0.0;
	        double sumLCC = 0.0;
	        double MSIFitness = 0.0;
	        
	    	sumLCC = evaluateLCC(architecture);
	        
	        		
			CIBC cibc = new CIBC(architecture);
			for (CIBCResult c : cibc.getResults().values()) {
				sumCIBC += c.getInterlacedConcerns().size();
			}
			
			IIBC iibc = new IIBC(architecture);
			for (IIBCResult c : iibc.getResults().values()) {
				sumIIBC += c.getInterlacedConcerns().size();
			}
			
			OOBC oobc = new OOBC(architecture);
			for (OOBCResult c : oobc.getResults().values()) {
				sumOOBC += c.getInterlacedConcerns().size();
			}
			
			
			CDAC cdac = new CDAC (architecture);
			for (CDACResult c: cdac.getResults()) {
				sumCDAC += c.getElements().size();
			}
			
			CDAI cdai = new CDAI (architecture);
			for (CDAIResult c: cdai.getResults()) {
				sumCDAI += c.getElements().size();
			}
			
			CDAO cdao = new CDAO (architecture);
			for (CDAOResult c: cdao.getResults()) {
				sumCDAO += c.getElements().size();
			}
			
			MSIFitness = sumLCC + sumCDAC + sumCDAI + sumCDAO + sumCIBC + sumIIBC + sumOOBC;
			return MSIFitness;
	    }

		private double evaluateLCC(Architecture architecture) {
			double sumLCC = 0.0;
			LCC result = new LCC(architecture);

	        for (LCCComponentResult component : result.getResults()) {
				sumLCC += component.numberOfConcerns();
				
			}
			return sumLCC;
		}
	 //----------------------------------------------------------------------------------   
	    private double evaluateMACFitness (Architecture architecture) {
	    	double MACFitness= 0.0;
	    	double meanNumOps = 0.0;
	    	double meanDepComps = 0.0;
	    	double sumCohesion = 0.0;
	    	double sumClassesDepIn = 0.0;
	    	double sumClassesDepOut = 0.0;
	    	double sumDepIn = 0.0;
	    	double sumDepOut = 0.0;
	    	
	    	
	    	MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
	    	meanNumOps = numOps.getResults();
	    	
	    	MeanDepComponents depComps = new MeanDepComponents(architecture);
	    	meanDepComps = depComps.getResults();
	    	
	    	ClassDependencyOut classesDepOut = new ClassDependencyOut(architecture);
	    	sumClassesDepOut = classesDepOut.getResults();
	    	
	    	ClassDependencyIn classesDepIn = new ClassDependencyIn(architecture);
	    	sumClassesDepIn = classesDepIn.getResults();
	    		    	
	    	//TODO _ VER ISSO - EDIpo
//	    	DependencyOut DepOut = new DependencyOut(architecture);
//	    	sumDepOut = DepOut.getResults();
	    	
	    	DependencyIn DepIn = new DependencyIn(architecture);
	    	sumDepIn = DepIn.getResults();
	    		    	
	    	RelationalCohesion cohesion = new RelationalCohesion(architecture);
	    	sumCohesion = cohesion.getResults();
	    	
//	    	System.out.println("MeanNumOps: "+meanNumOps);
//	    	System.out.println("meanDepComps: "+meanDepComps);
//	    	System.out.println("sumClassesDepIn: "+sumClassesDepIn);
//	    	System.out.println("sumClassesDepOut: "+sumClassesDepOut);
//	    	System.out.println("sumDepIn: "+sumDepIn);
//	    	System.out.println("sumDepOut: "+sumDepOut);
//	    	System.out.println("sumCohesion: "+sumCohesion);
//	    	
	    	MACFitness = meanNumOps + meanDepComps  + sumClassesDepIn + sumClassesDepOut + sumDepIn + sumDepOut + (1 / sumCohesion); 
	    	return MACFitness;
	    }
	
	 //---------------------------------------------------------------------------------   
	 private double evaluateCohesionFitness (Architecture architecture) {
	    double sumCohesion = 0.0;
	    double Cohesion= 0.0;
	    
	    RelationalCohesion cohesion = new RelationalCohesion(architecture);
    	sumCohesion = cohesion.getResults();
    	Cohesion = (1 / sumCohesion); 
    	return Cohesion;
   }
    	
	    
	    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
	    public SolutionSet removeDominadas(SolutionSet result) {
	        boolean dominador, dominado;
	        double valor1 = 0;
	        double valor2 = 0;

	        for (int i = 0; i < (result.size() - 1); i++) {
	            for (int j = (i + 1); j < result.size(); j++) {
	                dominador = true;
	                dominado = true;

	                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
	                    valor1 = result.get(i).getObjective(k);
	                    valor2 = result.get(j).getObjective(k);

	                    if (valor1 > valor2 || dominador == false) {
	                        dominador = false;
	                    } else if (valor1 <= valor2) {
	                        dominador = true;
	                    }

	                    if (valor2 > valor1 || dominado == false) {
	                        dominado = false;
	                    } else if (valor2 < valor1) {
	                        dominado = true;
	                    }
	                }


	                if (dominador) {
//	                    System.out.println("--------------------------------------------");
//	                    System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
//	                    System.out.println("[" + i + "] " + result.get(i).toString());
//	                    System.out.println("[" + j + "] " + result.get(j).toString());

	                    result.remove(j);
	                    j = j - 1;
	                } else if (dominado) {
//	                    System.out.println("--------------------------------------------");
//	                    System.out.println("Solucao [" + j + "] domina a Solucao [" + i + "]");
//	                    System.out.println("[" + i + "] " + result.get(i).toString());
//	                    System.out.println("[" + j + "] " + result.get(j).toString());

	                    result.remove(i);
	                    j = i;
	                }
	            }
	        }

	        return result;
	    }

	    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
	    public SolutionSet removeRepetidas(SolutionSet result) {
	        String solucao;

	        for (int i = 0; i < result.size() - 1; i++) {
	            solucao = result.get(i).getDecisionVariables()[0].toString();
	            for (int j = i + 1; j < result.size(); j++) {
	                if (solucao.equals(result.get(j).getDecisionVariables()[0].toString())) {
	                    result.remove(j);
	                }
	            }
	        }

	        return result;
	    }
	    
////---------------------------------------------------------------------------------
//	    public Solution tratarRestricoes(Solution solution){
//	    
//	    	List<Component> allComponents = new ArrayList<Component> (((Architecture) solution.getDecisionVariables()[0]).getComponents());
//		    for (Component comp: allComponents){
//		    	if ((comp.getClasses().isEmpty()) && (comp.getImplementedInterfaces().isEmpty())){
//		    		removeComponentRelationships(comp, (Architecture)solution.getDecisionVariables()[0]);
//		    		((Architecture) solution.getDecisionVariables()[0]).removeComponent(comp);
//		    		//return solution;
//		    	}
//		    	List<Class> allClasses = new ArrayList<Class>(comp.getClasses());
//		    	Iterator<Class> iteratorClasses = allClasses.iterator();
//                while (iteratorClasses.hasNext()){
//                        Class c= iteratorClasses.next();
//                        if ((c.getMethods().isEmpty()) && (c.getAttributes().isEmpty())){
//		      			    removeClassRelationships(c,(Architecture)solution.getDecisionVariables()[0]);
//                        	comp.removeClass(c);
//		      			 }
//                }
//		    	Collection<Interface> allItfs = comp.getImplementedInterfaces();
//		    	Iterator<Interface> iteratorItfs = allItfs.iterator();
//                while (iteratorItfs.hasNext()){
//                        Interface itf = iteratorItfs.next();
//                       if (itf.getOperations().isEmpty()) {
//                   			removeInterfaceRelationships(itf, (Architecture)solution.getDecisionVariables()[0]);
//                    	    iteratorItfs.remove(); 
//		      		   }
//		      	}
//                
//		    }
//		    return solution;
//	    }
//	   
//	    
//	  //-------------------------------------------------------------------------------------
//	    private void removeComponentRelationships(Component comp, Architecture architecture){
////	    	if (!comp.getRequiredInterfaces().isEmpty()){
////    			Collection<DependencyComponentInterfaceRelationship> allDeps = architecture.getDependencyComponentInterfaceRelationships();
////    			for (DependencyComponentInterfaceRelationship relationship : allDeps){
////    				if (relationship.getComponent().equals(comp)){
////    					architecture.removeInterElementComponentRelationship(relationship);
////    				}
////    				
////    			}
////    		}
////	    	if (!comp.getImplementedInterfaces().isEmpty()){
////	    		Collection<AbstractionInterElementRelationship> allItfs = architecture.getAbstractionInterElementRelationships();
////    			for (AbstractionInterElementRelationship relationship : allItfs){
////    				if (relationship.getChild().equals(comp)){
////    					architecture.removeInterElementComponentRelationship(relationship);
////    				}
////    				
////    			}
////	    	}
//	    	
//	    	InterElementRelationship[] allInterElementRelationships = architecture.getInterElementRelationships().toArray(new InterElementRelationship[0]);
//            for (InterElementRelationship relationship : allInterElementRelationships){
//    				 if (relationship instanceof AbstractionInterElementRelationship){
//    					 AbstractionInterElementRelationship abstraction = (AbstractionInterElementRelationship) relationship;
// 	        	  		 if (abstraction.getChild().equals(comp)){
//    					      architecture.removeInterElementComponentRelationship(relationship);
// 	        	  		 }
//    				 }
//    				 if (relationship instanceof DependencyComponentInterfaceRelationship){
//    					 DependencyComponentInterfaceRelationship dependency = (DependencyComponentInterfaceRelationship) relationship;
// 	        	  		 if (dependency.getComponent().equals(comp)){
//    					      architecture.removeInterElementComponentRelationship(relationship);
// 	        	  		 }
//    				 }
//    			}
//	    		
//	    }
//	    
//	  //---------------------------------------------------------------------------------------
//		 private void removeInterfaceRelationships (Interface itf, Architecture architecture){
//			 if (!(itf.getDependents().isEmpty())){
//				 
//				 Collection<DependencyComponentInterfaceRelationship> allDeps = architecture.getDependencyComponentInterfaceRelationships();
//				 for (DependencyComponentInterfaceRelationship relationship : allDeps){
//	    				if (relationship.getInterface().equals(itf)){
//	    					architecture.removeInterElementComponentRelationship(relationship);
//	    				}
//				 }
//			 }
//	         if (!(itf.getImplementors().isEmpty())){
//	        	 Collection<AbstractionInterElementRelationship> allRelations = architecture.getAbstractionInterElementRelationships();
//	        	 for (AbstractionInterElementRelationship relationship : allRelations){
//					 if (relationship.getParent().equals(itf)){
//						 architecture.removeInterElementComponentRelationship(relationship);
//					 }
//				 }
//	       	} 
//
//		 }
//	  //---------------------------------------------------------------------------------------
//	  private void removeClassRelationships(Class cls, Architecture architecture){
//		    
//		List<InterClassRelationship> relationshipsCls = new ArrayList<InterClassRelationship>(cls.getRelationships());
//		if (!relationshipsCls.isEmpty()){                						
//			Iterator<InterClassRelationship> iteratorRelationships = relationshipsCls.iterator();
//            while (iteratorRelationships.hasNext()){
//            	InterClassRelationship relationship= iteratorRelationships.next();
//            	architecture.removeInterClassRelationship(relationship);
//			}//end while relationships
//		}// end if
//	 }// end removeClassRelationships
	 

	  //método para verificar se algum dos relacionamentos recebidos � generaliza��o
	    private boolean searchForGeneralizations(Collection<Relationship> Relationships){
	    	boolean found=false;
	    	for (Relationship relationship: Relationships){
		    	if (relationship instanceof GeneralizationRelationship){
		    		return true;
		    	}
		    }
	    	return found;
	    }
	    
//	 //---------------------------------------------------------------------------------------
//	  public void evaluateConstraints(Solution solution) throws JMException {
//		  List<Component> allComponents = new ArrayList<Component> (((Architecture) solution.getDecisionVariables()[0]).getComponents());
//		  if (allComponents.isEmpty()) System.out.println("Arquitetura chegou sem componentes no evaluate constraints");
//		  
//		  for (Component comp: allComponents){
//		    	List<Class> allClasses = new ArrayList<Class> (comp.getClasses());
//		    	if (!(allClasses.isEmpty())){                						
//					Iterator<Class> iteratorClasses = allClasses.iterator();
//		            while (iteratorClasses.hasNext()){
//		            	Class cls= iteratorClasses.next();
//		            	if ((cls.getAttributes().isEmpty()) && (cls.getMethods().isEmpty()) && !(searchForGeneralizations(cls.getRelationships())) && (cls.getVariantType().toString().equals(""))){
//		            		comp.removeClass(cls);
//		            		this.removeClassRelationships(cls,(Architecture) solution.getDecisionVariables()[0]);
//		            	}
//		            }
//		    	}
//		    	List<Interface> allItfsComp = new ArrayList<Interface> (comp.getImplementedInterfaces());
//		    	if (!(allItfsComp.isEmpty())){                						
//					Iterator<Interface> iteratorInterfaces = allItfsComp.iterator();
//		            while (iteratorInterfaces.hasNext()){
//		            	Interface itf= iteratorInterfaces.next();
//		            	boolean ultimaInterface=false;
//		            	if (comp.getImplementedInterfaces().size()==1) ultimaInterface=true;
//		            	if (itf.getOperations().isEmpty() && !ultimaInterface){
//		            		try {
//								((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
////		            		this.removeInterfaceRelationships(itf, (Architecture) solution.getDecisionVariables()[0]);
//		            	}
//		            	if (itf.getOperations().isEmpty() && ultimaInterface && comp.getClasses().size()<1){
//		            		try {
//								((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//		            	}
//		            }
//		    	}
//		    	
//		    	if (comp.getClasses().isEmpty() && comp.getImplementedInterfaces().isEmpty()){
//		    		((Architecture) solution.getDecisionVariables()[0]).removeComponent(comp);
//		    		this.removeComponentRelationships(comp, (Architecture)solution.getDecisionVariables()[0]);
//		    	}
//		    }
//	  }

	  
	 //-----------------------------------------------------------------------------------------
	 
}
