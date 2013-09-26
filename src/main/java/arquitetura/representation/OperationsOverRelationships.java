package arquitetura.representation;

import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.MemberEnd;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;

public class OperationsOverRelationships {
	
	static Logger LOGGER = LogManager.getLogger(OperationsOverRelationships.class.getName());
	
	private List<Relationship> relationships;
	private Set<String> allIds;
	private List<AssociationClassRelationship> allAssociationClass;

	
	public OperationsOverRelationships(List<Relationship> relationships, List<AssociationClassRelationship> allAssociationClass, Set<String> allIds) {
		this.relationships = relationships;
		this.allIds = allIds;
		this.allAssociationClass = allAssociationClass;
	}

	public void moveAssociation(AssociationRelationship association, Class idclass1, Class idclass2) {
		association.getParticipants().get(0).setCLSClass(idclass1);
		association.getParticipants().get(1).setCLSClass(idclass2);
	}
	
	public void moveAssociation(AssociationClassRelationship association, Class idclass1, Class idclass2) {
		association.getMemebersEnd().clear();
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", idclass1));
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", idclass2));
	}
	
	public void moveDependency(DependencyRelationship dependency, Class idclass6, Class idclass8) {
		dependency.setClient(idclass6);
		dependency.setSupplier(idclass8);
	}


	public void removeAssociationRelationship(AssociationRelationship as) {
		if (!removeRelationship(as))
			LOGGER.info("Cannot remove Association " + as + ".\n");
	}
	
	public void removeDependencyRelationship(DependencyRelationship dp) {
		if (!removeRelationship(dp))
			LOGGER.info("Cannot remove Dependency " + dp + ".\n");
	}

	public void removeUsageRelationship(UsageRelationship usage) {
		if (!removeRelationship(usage))
			LOGGER.info("Cannot remove Usage " + usage + ".\n");
	}

	public void removeAssociationClass(AssociationClassRelationship associationClass){
		if (!allAssociationClass.remove(associationClass))
			LOGGER.info("Cannot remove AssociationClass " + associationClass + ".\n");
	}

	public void removeGeneralizationRelationship(GeneralizationRelationship generalization) {
		if (!removeRelationship(generalization))
			LOGGER.info("Cannot remove Generalization " + generalization + ".\n");
	}

	public void removeAbstractionRelationship(AbstractionRelationship ab) {
		if (!removeRelationship(ab))
			LOGGER.info("Cannot remove Abstraction " + ab + ".\n");
	}
	
	private boolean removeRelationship(Relationship as) {
		if(as == null) return false;
		this.allIds.remove(as.getId());
		return relationships.remove(as);
	}

	public void moveAssociationEnd(AssociationEnd associationEnd, Class idclass8) {
		associationEnd.setCLSClass(idclass8);
	}

	public void moveDependencyClient(DependencyRelationship dependency,	Class newClient) {
		dependency.setClient(newClient);
	}

	public void moveDependencySupplier(DependencyRelationship dependency, Class newSupplier) {
		dependency.setSupplier(newSupplier);
	}

	public void moveMemberEndOf(MemberEnd memberEnd, Class klass) {
		memberEnd.setType(klass);
	}

	/**
	 * Move o client de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
	 *
	 * @param realization
	 * @param newClient
	 */
	public void moveRealizationClient(RealizationRelationship realization, Element newClient) {
		realization.setClient(newClient);
	}

	/**
	 * Move o supplier de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
	 * 
	 * @param realization
	 * @param newSupplier
	 */
	public void moveRealizationSupplier(RealizationRelationship realization, Element newSupplier) {
		realization.setSupplier(newSupplier);
		
	}

}
