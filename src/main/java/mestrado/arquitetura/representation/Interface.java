package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import mestrado.arquitetura.representation.relationship.AbstractionRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;

public class Interface extends Element {
	
	private final static Logger LOGGER = Logger.getLogger(Interface.class.getName()); 
	private final List<Method> operations = new ArrayList<Method>();
	

	public Interface(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, Element parent, String namespace, String id) {
		super(architecture, name, isVariationPoint, variantType, "interface", parent, namespace, id);
	}
	
	public Interface(Architecture architecture, String name) {
		this(architecture, name, false, VariantType.NONE, null, "", ""); //TODO recber id
	}

	public  List<Method> getOperations() {
		return operations;
	}
	
	public void removeOperation(Method operation) {
		if(!operations.remove(operation))
			LOGGER.info("Try remove operation '" + operation + "', but not found");
	}
	
	public Method createOperation(String operationName) throws Exception {
		Method operation = new Method(getArchitecture(), operationName, false, VariantType.NONE, "void", false, null, this, "", ""); //Receber id 
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

}
