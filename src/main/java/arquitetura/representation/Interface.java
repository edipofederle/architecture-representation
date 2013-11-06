package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.DependencyRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Interface extends Element {
	
	
	private static final long serialVersionUID = -1779316062511432020L;

	static Logger LOGGER = LogManager.getLogger(Interface.class.getName());
	private final List<Method> operations = new ArrayList<Method>();
	

	public Interface(Architecture architecture, String name, Variant variantType, String namespace, String id) {
		super(architecture, name, variantType, "interface", namespace, id);
	}
	
	public Interface(Architecture architecture, String name, String id) {
		this(architecture, name, null, UtilResources.createNamespace(architecture.getName(), name), id);
	}

	public  List<Method> getOperations() {
		return operations;
	}
	
	public void removeOperation(Method operation) {
		if(!operations.remove(operation))
			LOGGER.info("Try remove operation '" + operation + "', but not found");
	}
	
	public Method createOperation(String operationName) throws Exception {
		Method operation = new Method(getArchitecture(), operationName, false, null, "void", false, null,  "", ""); //Receber id 
		operations.add(operation);
		return operation;
	}

	public void moveOperationToInterface(Method operation, Interface interfaceToMove) {
		if (!getOperations().contains(operation)) return;
		
		removeOperation(operation);
		interfaceToMove.addExternalOperation(operation);
	}

	private void addExternalOperation(Method operation) {
		operations.add(operation);
	}

	public List<Package> getImplementors() {
		List<Package> implementors = new ArrayList<Package>();
		List<AbstractionRelationship> abs = getArchitecture().getAllAbstractions();
		for (AbstractionRelationship abstractionRelationship : abs) 
			if((abstractionRelationship.getClient() instanceof Interface) && (abstractionRelationship.getClient().equals(this)))
					implementors.add((Package) abstractionRelationship.getSupplier());
					
		return implementors;
	}

	public List<Package> getDependents() {
		List<Package> implementors = new ArrayList<Package>();
		List<DependencyRelationship> abs = getArchitecture().getAllDependencies();
		for (DependencyRelationship dependency : abs) 
				if((dependency.getSupplier() instanceof Interface) && (dependency.getSupplier().equals(this)))
					implementors.add((Package) dependency.getClient());
		return implementors;
	}

	@Override
	public Collection<Concern> getAllConcerns() {
		Collection<Concern> concerns = new ArrayList<Concern>(getOwnConcerns());
		for (Method operation : getOperations())
			concerns.addAll(operation.getAllConcerns());
		
		return concerns;
	}
	
	public Collection<DependencyRelationship> getDependencies() {
		Collection<DependencyRelationship> dependencies = new ArrayList<DependencyRelationship>();
		
		for (DependencyRelationship dependency : getArchitecture().getAllDependencies()) {
			if (dependency.getSupplier().equals(this))
				dependencies.add(dependency);
		}
		
		return dependencies;
	}

}
