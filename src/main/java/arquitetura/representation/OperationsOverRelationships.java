package arquitetura.representation;

import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.MemberEnd;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;

public class OperationsOverRelationships {
	
	static Logger LOGGER = LogManager.getLogger(OperationsOverRelationships.class.getName());
	
	private List<Relationship> relationships;
	private Set<String> allIds;
	private List<AssociationClassRelationship> allAssociationClass;
	private Architecture  architecture;
	
	public OperationsOverRelationships(Architecture architecture) {
		this.architecture = architecture;
		this.relationships = architecture.getAllRelationships();
		this.allIds = architecture.getAllIds();
		this.allAssociationClass = architecture.getAllAssociationsClass();
	}

	public void moveAssociation(AssociationRelationship association, Class class1, Class class2) {
		class1.getIdsRelationships().remove(association.getId());
		class2.getIdsRelationships().remove(association.getId());
		
		association.getParticipants().get(0).setCLSClass(class1);
		association.getParticipants().get(1).setCLSClass(class2);
		
		class1.getIdsRelationships().add(association.getId());
		class2.getIdsRelationships().add(association.getId());
	}
	
	public void moveAssociationClass(AssociationClassRelationship association, Class member1, Class member2) {
		association.getMemebersEnd().clear();
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", member1));
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", member2));
		
		member1.getIdsRelationships().add(association.getId());
		member2.getIdsRelationships().add(association.getId());
	}
	
	public void moveDependency(DependencyRelationship dependency, Class client, Class supplier) {
		dependency.setClient(client);
		dependency.setSupplier(supplier);
		client.getIdsRelationships().add(dependency.getId());
		supplier.getIdsRelationships().add(dependency.getId());
	}


	public void removeAssociationRelationship(AssociationRelationship as) {
		if (!removeRelationship(as))
			LOGGER.info("Cannot remove Association " + as + ".\n");
	}
	
	public void removeDependencyRelationship(DependencyRelationship dp) {
		if (!removeRelationship(dp))
			LOGGER.info("Cannot remove Dependency " + dp + ".\n");
	}

	public void removeAssociationClass(AssociationClassRelationship associationClass){
		if (!allAssociationClass.remove(associationClass))
			LOGGER.info("Cannot remove AssociationClass " + associationClass + ".\n");
	}

	public void removeGeneralizationRelationship(GeneralizationRelationship generalization) {
		if (!removeRelationship(generalization))
			LOGGER.info("Cannot remove Generalization " + generalization + ".\n");
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
		realization.getSupplier().getIdsRelationships().remove(realization.getId());
		realization.setClient(newClient);
		newClient.getIdsRelationships().add(realization.getId());
	}

	/**
	 * Move o supplier de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
	 * 
	 * @param realization
	 * @param newSupplier
	 */
	public void moveRealizationSupplier(RealizationRelationship realization, Element newSupplier) {
		realization.getSupplier().getIdsRelationships().remove(realization.getId());
		realization.setSupplier(newSupplier);
		newSupplier.getIdsRelationships().add(realization.getId());
	}
	
	/**
	 * Move uma realizacão inteira.
	 * 
	 * @param realization - Realização a ser movida
	 * @param client - Novo Cliente
	 * @param supplier - Novo Supplier
	 */
	public void moveRealization(RealizationRelationship realization, Element client, Element supplier) {
		
		realization.getClient().getIdsRelationships().remove(realization.getId());
		realization.getSupplier().getIdsRelationships().remove(realization.getId());
		
		realization.setClient(client);
		realization.setSupplier(supplier);
		
		client.getIdsRelationships().add(realization.getId());
		supplier.getIdsRelationships().add(realization.getId());
		
	}

	public void createNewRealization(Element client, Element supplier) {
		String id = UtilResources.getRandonUUID();
		RealizationRelationship realization = new RealizationRelationship(client, supplier, "", id);
		client.getIdsRelationships().add(id);
		supplier.getIdsRelationships().add(id);
		this.architecture.getAllRelationships().add(realization);
	}

}
