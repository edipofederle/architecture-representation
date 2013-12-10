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
		abstractionRelationship.getClient().removeRelationship(abstractionRelationship);
		abstractionRelationship.setClient(newClient);
		newClient.addRelationship(abstractionRelationship);
	}

	public void moveSupplier(AbstractionRelationship abstractionRelationship, Class newSupplier) {
		abstractionRelationship.getSupplier().removeRelationship(abstractionRelationship);
		abstractionRelationship.setSupplier(newSupplier);
		newSupplier.addRelationship(abstractionRelationship);
		
	}

	public void move(AbstractionRelationship abstractionRelationship, Class newSupplier, Class newCliente) {
		abstractionRelationship.getClient().removeRelationship(abstractionRelationship);
		abstractionRelationship.getSupplier().removeRelationship(abstractionRelationship);
		
		abstractionRelationship.setSupplier(newSupplier);
		abstractionRelationship.setClient(newCliente);
		
		newSupplier.addRelationship(abstractionRelationship);
		newCliente.addRelationship(abstractionRelationship);
	}

	public AbstractionRelationship create(Element newClient, Element newSupplier) {
		String id = UtilResources.getRandonUUID();
		AbstractionRelationship abs = new AbstractionRelationship(newClient, newSupplier, id);
		newClient.addRelationship(abs);
		newSupplier.addRelationship(abs);
		this.architecture.addRelationship(abs);
		
		return abs;
	}

}
