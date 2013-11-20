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
		if(abstractionRelationship !=null)
			if (!this.architecture.removeRelationship(abstractionRelationship))
				LOGGER.info("Cannot remove Abstraction " + abstractionRelationship + ".\n");
	}

	public void moveClient(AbstractionRelationship abstractionRelationship,	Class newClient) {
		abstractionRelationship.getClient().getRelationships().remove(abstractionRelationship);
		abstractionRelationship.setClient(newClient);
		newClient.getRelationships().add(abstractionRelationship);
	}

	public void moveSupplier(AbstractionRelationship abstractionRelationship, Class newSupplier) {
		abstractionRelationship.getSupplier().getRelationships().remove(abstractionRelationship);
		abstractionRelationship.setSupplier(newSupplier);
		newSupplier.getRelationships().add(abstractionRelationship);
		
	}

	public void move(AbstractionRelationship abstractionRelationship, Class newSupplier, Class newCliente) {
		abstractionRelationship.getClient().getRelationships().remove(abstractionRelationship);
		abstractionRelationship.getSupplier().getRelationships().remove(abstractionRelationship);
		
		abstractionRelationship.setSupplier(newSupplier);
		abstractionRelationship.setClient(newCliente);
		
		newSupplier.getRelationships().add(abstractionRelationship);
		newCliente.getRelationships().add(abstractionRelationship);
	}

	public void create(Class newClient, Class newSupplier) {
		String id = UtilResources.getRandonUUID();
		AbstractionRelationship abs = new AbstractionRelationship(newClient, newSupplier, id);
		newClient.getRelationships().add(abs);
		newSupplier.getRelationships().add(abs);
		this.architecture.addRelationship(abs);
	}

}
