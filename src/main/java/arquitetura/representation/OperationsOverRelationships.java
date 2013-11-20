package arquitetura.representation;

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
	
	private Architecture  architecture;
	
	public OperationsOverRelationships(Architecture architecture) {
		this.architecture = architecture;
	}

	public void moveAssociation(AssociationRelationship association, Class class1, Class class2) {
		class1.removeRelationship(association);
		class2.removeRelationship(association);
		
		association.getParticipants().get(0).setCLSClass(class1);
		association.getParticipants().get(1).setCLSClass(class2);
		
		class1.addRelationship(association);
		class2.addRelationship(association);
	}
	
	public void moveAssociationClass(AssociationClassRelationship association, Class member1, Class member2) {
		association.getMemebersEnd().clear();
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", member1));
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", member2));
		
		member1.addRelationship(association);
		member2.addRelationship(association);
	}
	
	public void moveDependency(DependencyRelationship dependency, Class client, Class supplier) {
		dependency.setClient(client);
		dependency.setSupplier(supplier);
		client.addRelationship(dependency);
		supplier.addRelationship(dependency);
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
		if (!removeRelationship(associationClass))
			LOGGER.info("Cannot remove AssociationClass " + associationClass + ".\n");
	}

	public void removeGeneralizationRelationship(GeneralizationRelationship generalization) {
		if (!removeRelationship(generalization))
			LOGGER.info("Cannot remove Generalization " + generalization + ".\n");
	}

	private boolean removeRelationship(Relationship as) {
		if(as == null) return false;
		return this.architecture.removeRelationship(as);
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
		realization.getSupplier().removeRelationship(realization);
		realization.setClient(newClient);
		newClient.addRelationship(realization);
	}

	/**
	 * Move o supplier de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
	 * 
	 * @param realization
	 * @param newSupplier
	 */
	public void moveRealizationSupplier(RealizationRelationship realization, Element newSupplier) {
		realization.getSupplier().removeRelationship(realization);
		realization.setSupplier(newSupplier);
		newSupplier.addRelationship(realization);
	}
	
	/**
	 * Move uma realizacão inteira.
	 * 
	 * @param realization - Realização a ser movida
	 * @param client - Novo Cliente
	 * @param supplier - Novo Supplier
	 */
	public void moveRealization(RealizationRelationship realization, Element client, Element supplier) {
		
		realization.getClient().removeRelationship(realization);
		realization.getSupplier().removeRelationship(realization);
		
		realization.setClient(client);
		realization.setSupplier(supplier);
		
		client.addRelationship(realization);
		supplier.addRelationship(realization);
		
	}

	public void createNewRealization(Element client, Element supplier) {
		String id = UtilResources.getRandonUUID();
		RealizationRelationship realization = new RealizationRelationship(client, supplier, "", id);
		client.addRelationship(realization);
		supplier.addRelationship(realization);
		this.architecture.addRelationship(realization);
	}

}
