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
		usageRelationship.getClient().getIdsRelationships().remove(usageRelationship.getId());
		usageRelationship.setClient(newClient);
		newClient.getIdsRelationships().add(usageRelationship.getId());
	}
	
	public void moveSupplier(UsageRelationship usageRelationship, Element newSupplier) {
		usageRelationship.getSupplier().getIdsRelationships().remove(usageRelationship.getId());
		usageRelationship.setSupplier(newSupplier);
		newSupplier.getIdsRelationships().add(usageRelationship.getId());
	}

	/**
	 * 
	 * @param newClient
	 * @param newSupplier
	 */
	public void create(Element newClient, Element newSupplier) {
		String idUsage = UtilResources.getRandonUUID();
		UsageRelationship usage = new UsageRelationship("", newSupplier, newClient, idUsage);
		this.architecture.getAllRelationships().add(usage);
		newClient.getIdsRelationships().add(idUsage);
		newSupplier.getIdsRelationships().add(idUsage);
	}

	/**
	 * 
	 * 
	 * @param usageRelationship
	 * @param newSupplier
	 * @param newClient
	 */
	public void move(UsageRelationship usageRelationship, Class newSupplier, Class newClient) {
		usageRelationship.getClient().getIdsRelationships().remove(usageRelationship.getId());
		usageRelationship.getSupplier().getIdsRelationships().remove(usageRelationship.getId());
		
		usageRelationship.setClient(newClient);
		usageRelationship.setSupplier(newSupplier);
		
		newSupplier.getIdsRelationships().add(usageRelationship.getId());
		newClient.getIdsRelationships().add(usageRelationship.getId());
	}

}
