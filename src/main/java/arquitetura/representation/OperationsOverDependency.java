package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.DependencyRelationship;

public class OperationsOverDependency {

	private DependencyRelationship dependency;
	private Architecture architecture;

	public OperationsOverDependency(Architecture architecture) {
		this.architecture = architecture;
	}
	
	/**
	 * Criar relacionamento do tipo dependencia
	 * 
	 * @param name
	 * @return
	 */
	public OperationsOverDependency create(String name) {
		dependency = new DependencyRelationship();
		dependency.setId(UtilResources.getRandonUUID());
		dependency.setArchitecture(architecture);
		dependency.setName(name);
		return this;
	}

	/**
	 * Configura client
	 * 
	 * @param client
	 * @return
	 */
	public OperationsOverDependency withClient(Element client) {
		dependency.setClient(client);
		return this;
	}

	/**
	 * Configura Supplier
	 * 
	 * @param supplier
	 * @return
	 */
	public OperationsOverDependency withSupplier(Element supplier) {
		dependency.setSupplier(supplier);
		return this;
	}

	/**
	 * Adiciona na lista de relacionamentos
	 * 
	 */
	public DependencyRelationship build() {
		this.architecture.getAllRelationships().add(dependency);
		return dependency;
	}

}
