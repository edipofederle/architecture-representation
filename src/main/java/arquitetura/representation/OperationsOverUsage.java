package arquitetura.representation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.UsageRelationship;

public class OperationsOverUsage {
	
	static Logger LOGGER = LogManager.getLogger(OperationsOverUsage.class.getName());
	
	private Architecture architecture;

	public OperationsOverUsage(Architecture architecture) {
		this.architecture = architecture;
	}

	public void remove(UsageRelationship usage) {
		if (!this.architecture.removeRelationship(usage))
			LOGGER.info("Cannot remove Usage " + usage + ".\n");
	}

	public void moveClient(UsageRelationship usageRelationship, Element newClient) {
		usageRelationship.getClient().getRelationships().remove(usageRelationship);
		usageRelationship.setClient(newClient);
		newClient.getRelationships().add(usageRelationship);
	}
	
	public void moveSupplier(UsageRelationship usageRelationship, Element newSupplier) {
		usageRelationship.getSupplier().getRelationships().remove(usageRelationship);
		usageRelationship.setSupplier(newSupplier);
		newSupplier.getRelationships().add(usageRelationship);
	}

	/**
	 * 
	 * @param newClient
	 * @param newSupplier
	 */
	public void create(Element newClient, Element newSupplier) {
		UsageRelationship usage = new UsageRelationship("", newSupplier, newClient, UtilResources.getRandonUUID());
		this.architecture.getAllRelationships().add(usage);
		newClient.getRelationships().add(usage);
		newSupplier.getRelationships().add(usage);
	}

	/**
	 * 
	 * 
	 * @param usageRelationship
	 * @param newSupplier
	 * @param newClient
	 */
	public void move(UsageRelationship usageRelationship, Class newSupplier, Class newClient) {
		usageRelationship.getClient().getRelationships().remove(usageRelationship);
		usageRelationship.getSupplier().getRelationships().remove(usageRelationship);
		
		usageRelationship.setClient(newClient);
		usageRelationship.setSupplier(newSupplier);
		
		newSupplier.getRelationships().add(usageRelationship);
		newClient.getRelationships().add(usageRelationship);
	}

}
