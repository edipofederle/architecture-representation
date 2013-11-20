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
		usageRelationship.getClient().removeRelationship(usageRelationship);
		usageRelationship.setClient(newClient);
		newClient.addRelationship(usageRelationship);
	}
	
	public void moveSupplier(UsageRelationship usageRelationship, Element newSupplier) {
		usageRelationship.getSupplier().removeRelationship(usageRelationship);
		usageRelationship.setSupplier(newSupplier);
		newSupplier.addRelationship(usageRelationship);
	}

	/**
	 * 
	 * @param newClient
	 * @param newSupplier
	 */
	public void create(Element newClient, Element newSupplier) {
		UsageRelationship usage = new UsageRelationship("", newSupplier, newClient, UtilResources.getRandonUUID());
		this.architecture.addRelationship(usage);
		newClient.addRelationship(usage);
		newSupplier.addRelationship(usage);
	}

	/**
	 * 
	 * 
	 * @param usageRelationship
	 * @param newSupplier
	 * @param newClient
	 */
	public void move(UsageRelationship usageRelationship, Class newSupplier, Class newClient) {
		usageRelationship.getClient().removeRelationship(usageRelationship);
		usageRelationship.getSupplier().removeRelationship(usageRelationship);
		
		usageRelationship.setClient(newClient);
		usageRelationship.setSupplier(newSupplier);
		
		newSupplier.addRelationship(usageRelationship);
		newClient.addRelationship(usageRelationship);
	}

}
