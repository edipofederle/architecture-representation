package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.DependencyRelationship;

public class OperationsOverDependency {

	private DependencyRelationship dependency;
	private Architecture architecture;

	public OperationsOverDependency(Architecture architecture) {
		this.architecture = architecture;
	}

	public OperationsOverDependency create(String name) {
		dependency = new DependencyRelationship();
		dependency.setId(UtilResources.getRandonUUID());
		dependency.setArchitecture(architecture);
		dependency.setName(name);
		return this;
	}

	public OperationsOverDependency withClient(Element client) {
		dependency.setClient(client);
		return this;
	}

	public OperationsOverDependency withSupplier(Element supplier) {
		dependency.setSupplier(supplier);
		return this;
	}

	public void build() {
		this.architecture.getAllRelationships().add(dependency);
		this.architecture.getAllIds().add(dependency.getId());
		
	}

}
