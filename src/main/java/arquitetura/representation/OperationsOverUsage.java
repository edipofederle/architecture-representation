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
	 * Cria um relacionamento de Usage e o adiciona na arquitetura<br/><br/>
	 * 
	 * NOTA: usando este método você não precisa chamar explicitamente algo como<br/><br/> {@code architecture.addRelationship(relationship)}.
	 * 
	 * @param parent
	 * @param supplier
	 * @return
	 */
	public UsageRelationship create(Element client, Element supplier) {
		UsageRelationship usage = new UsageRelationship("", supplier, client, UtilResources.getRandonUUID());
		this.architecture.addRelationship(usage);
		client.addRelationship(usage);
		supplier.addRelationship(usage);
		return usage;
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
