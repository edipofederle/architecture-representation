package jmetal.operators.crossover;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arquitetura.exceptions.ElementNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;

public class CrossoverRelationship {
	
	private static Set<Relationship> relationships = new HashSet<Relationship>();
	
	public static void createRelationshipsInOffspring(Architecture offspring) {
		for(Relationship r : relationships){
			if(r instanceof RealizationRelationship){
				RealizationRelationship realization = (RealizationRelationship)r;
				try{
					Element client = offspring.findElementByName(realization.getClient().getName());
					Element supplier = offspring.findElementByName(realization.getSupplier().getName());
					offspring.operationsOverRelationship().createNewRealization(client, supplier);
				}catch(ElementNotFound e){}
			}
			
			if(r instanceof UsageRelationship){
				UsageRelationship usage = (UsageRelationship)r;
				try{
					Element client = offspring.findElementByName(usage.getClient().getName());
					Element supplier = offspring.findElementByName(usage.getSupplier().getName());
					offspring.forUsage().create(client, supplier);
				}catch(ElementNotFound e){}
			}
			
			if(r instanceof AbstractionRelationship){
				AbstractionRelationship abstraction = (AbstractionRelationship)r;
				try{
					Element client = offspring.findElementByName(abstraction.getClient().getName());
					Element supplier = offspring.findElementByName(abstraction.getSupplier().getName());
					offspring.forAbstraction().create(client, supplier);
				}catch(ElementNotFound e){}
			}
			
			if(r instanceof DependencyRelationship){
				DependencyRelationship dependency = (DependencyRelationship)r;
				Element client = null;
				Element supplier = null;
				try{
					client = offspring.findElementByName(dependency.getClient().getName());
					supplier = offspring.findElementByName(dependency.getSupplier().getName());
					offspring.forDependency().create(dependency.getName()).withClient(client).withSupplier(supplier).build();
				}catch(ElementNotFound e){}
			}
			
			if(r instanceof GeneralizationRelationship){
				GeneralizationRelationship generalization = (GeneralizationRelationship)r;
				Element parent = null;
				Element child = null;
				try{
					parent = offspring.findElementByName(generalization.getParent().getName());
				    child = offspring.findElementByName(generalization.getChild().getName());
				    offspring.forGeneralization().createGeneralization(parent, child);
				}catch(ElementNotFound e){}
			}
			
			if(r instanceof AssociationRelationship){
				AssociationRelationship association = (AssociationRelationship)r;
				
				List<AssociationEnd> participants = association.getParticipants();
				
				try{
					AssociationEnd associationEnd1 = participants.get(0);
					AssociationEnd associationEnd2 = participants.get(1);
					
					Element participant1 = offspring.findElementByName(associationEnd1.getCLSClass().getName());
					Element participant2 = offspring.findElementByName(associationEnd2.getCLSClass().getName());
					
					AssociationEnd associationEndOffSpring = new AssociationEnd();
					associationEndOffSpring.setAggregation(associationEnd1.getAggregation());
					associationEndOffSpring.setNavigable(associationEnd1.isNavigable());
					associationEndOffSpring.setMultiplicity(associationEnd1.getMultiplicity());
					associationEndOffSpring.setCLSClass(participant1);
					
					AssociationEnd associationEndOffSpring2 = new AssociationEnd();
					associationEndOffSpring2.setAggregation(associationEnd2.getAggregation());
					associationEndOffSpring2.setNavigable(associationEnd2.isNavigable());
					associationEndOffSpring2.setMultiplicity(associationEnd2.getMultiplicity());
					associationEndOffSpring2.setCLSClass(participant2);
					
					offspring.forAssociation().create(associationEnd1, associationEnd2);
				}catch(ElementNotFound e){}
			}
			
			if (r instanceof AssociationClassRelationship){
				AssociationClassRelationship asc = (AssociationClassRelationship)r;
				try{
					Element offspringMember1 = offspring.findElementByName(asc.getMemebersEnd().get(0).getType().getName());
					Element offspringMember2 = offspring.findElementByName(asc.getMemebersEnd().get(1).getType().getName());
					offspring.forAssociation().createAssociationClass(asc.getAllAttributes(), asc.getAllMethods(), offspringMember1, offspringMember2, asc.getAssociationClass().getName());
				}catch(ElementNotFound e){}
			}
		}
	}
	
	public static void saveAllRelationshiopForElement(Element element, Architecture parent) {
		for(DependencyRelationship dependency : parent.getAllDependencies()){
			if(dependency.getClient().equals(element) || (dependency.getSupplier().equals(element)))
				relationships.add(dependency);
		}
		
		for(RealizationRelationship realization : parent.getAllRealizations()){
			if(realization.getClient().equals(element) || (realization.getSupplier().equals(element)) ){
				relationships.add(realization);
			}
		}
		
		for(UsageRelationship usage : parent.getAllUsage()){
			if(usage.getClient().equals(element) || (usage.getSupplier().equals(element))){
				relationships.add(usage);
			}
		}
		for(AbstractionRelationship abstraction : parent.getAllAbstractions()){
			if(abstraction.getClient().equals(element) || abstraction.getSupplier().equals(element)){
				relationships.add(abstraction);
			}
		}
		
		for(GeneralizationRelationship abstraction : parent.getAllGeneralizations()){
			if(abstraction.getParent().equals(element) || abstraction.getChild().equals(element)){
				relationships.add(abstraction);
			}
		}
		
		for(AssociationRelationship association : parent.getAllAssociations()){
			for(AssociationEnd ase : association.getParticipants()){
				if(ase.getCLSClass().equals(element))
					relationships.add(association);
			}
		}
	}
}