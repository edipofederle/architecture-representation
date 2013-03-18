package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

public class Interface extends Element {
	
	private final List<Method> operations = new ArrayList<Method>();

	public Interface(Architecture architecture, String name, boolean isVariationPoint, VariantType variantType, Element parent, String namespace) {
		super(architecture, name, isVariationPoint, variantType, "interface", parent, namespace);
	}
	
	public Interface(Architecture architecture, String name) {
		this(architecture, name, false, VariantType.NONE, null, "");
	}

	public  List<Method> getOperations() {
		return operations;
	}
	
	public void removeOperation(Method operation) {
		//perations.remove(operation);
	}
	
	public Method createOperation(String operationName) throws Exception {
//		Operation operation = new Operation(getArchitecture(), operationName);
//		operations.add(operation);
//		return operation;
		return null;
	}

	public void moveOperationToInterface(Method operation, Interface interfaceToMove) {
//		if (!getOperations().contains(operation)) return;
//		
//		removeOperation(operation);
//		interfaceToMove.addExternalOperation(operation);
	}

	private void addExternalOperation(Method operation) {
		//operations.add(operation);
	}

	public List<Package> getImplementors() {
		return null;
	}

	public List<Package> getDependents() {
		return null;
	}

}
