package arquitetura.representation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AbstractionRelationship;

public class OperationsOverAbstraction {
	
	static Logger LOGGER = LogManager.getLogger(OperationsOverAbstraction.class.getName());
	
	private Architecture architecture;

	public OperationsOverAbstraction(Architecture architecture) {
		this.architecture = architecture;
	}

	public void remove(AbstractionRelationship abstractionRelationship) {
		if (!this.architecture.removeRelationship(abstractionRelationship))
			LOGGER.info("Cannot remove Abstraction " + abstractionRelationship + ".\n");
	}

	public void moveClient(AbstractionRelationship abstractionRelationship,	Class newClient) {
		abstractionRelationship.getClient().getIdsRelationships().remove(abstractionRelationship.getId());
		abstractionRelationship.setClient(newClient);
		newClient.getIdsRelationships().add(abstractionRelationship.getId());
	}

	public void moveSupplier(AbstractionRelationship abstractionRelationship, Class newSupplier) {
		abstractionRelationship.getSupplier().getIdsRelationships().remove(abstractionRelationship.getId());
		abstractionRelationship.setSupplier(newSupplier);
		newSupplier.getIdsRelationships().add(abstractionRelationship.getId());
		
	}

	public void move(AbstractionRelationship abstractionRelationship, Class newSupplier, Class newCliente) {
		abstractionRelationship.getClient().getIdsRelationships().remove(abstractionRelationship.getId());
		abstractionRelationship.getSupplier().getIdsRelationships().remove(abstractionRelationship.getId());
		
		abstractionRelationship.setSupplier(newSupplier);
		abstractionRelationship.setClient(newCliente);
		
		newSupplier.getIdsRelationships().add(abstractionRelationship.getId());
		newCliente.getIdsRelationships().add(abstractionRelationship.getId());
	}

	public void create(Class newClient, Class newSupplier) {
		String id = UtilResources.getRandonUUID();
		AbstractionRelationship abs = new AbstractionRelationship(newClient, newSupplier, id);
		newClient.getIdsRelationships().add(id);
		newSupplier.getIdsRelationships().add(id);
		this.architecture.getAllRelationships().add(abs);
	}

}
