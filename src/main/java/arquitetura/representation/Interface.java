package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private final Set<Method> operations = new HashSet<Method>();
	

	public Interface(Architecture architecture, String name, Variant variantType, String namespace, String id) {
		super(architecture, name, variantType, "interface", namespace, id);
	}
	
	/**
	 * Use este construtor quando você deseja criar uma interface.<br /><br />
	 * 
	 * OBS 1: O ID para esta interface será gerado automaticamente.<br/>
	 * OBS 2: Esse construtor automaticamente adicionar a interface na arquitetura<br/>
	 * 
	 * @param architecture Architecture em questão
	 * @param name - Nome da interface
	 */
	public Interface(Architecture a, String name) {
		this(a, name, null, UtilResources.createNamespace(a.getName(), name), UtilResources.getRandonUUID());
		a.addExternalInterface(this);
	}

	public  Set<Method> getOperations() {
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

	public Set<Package> getImplementors() {
		Set<Package> implementors = new HashSet<Package>();
		List<AbstractionRelationship> abs = getArchitecture().getAllAbstractions();
		for (AbstractionRelationship abstractionRelationship : abs) 
			if((abstractionRelationship.getClient() instanceof Interface) && (abstractionRelationship.getClient().equals(this)))
					implementors.add((Package) abstractionRelationship.getSupplier());
					
		return Collections.unmodifiableSet(implementors);
	}

	public Set<Package> getDependents() {
		Set<Package> implementors = new HashSet<Package>();
		List<DependencyRelationship> abs = getArchitecture().getAllDependencies();
		for (DependencyRelationship dependency : abs) 
				if((dependency.getSupplier() instanceof Interface) && (dependency.getSupplier().equals(this)))
					implementors.add((Package) dependency.getClient());
		
		return Collections.unmodifiableSet(implementors);
	}

	@Override
	public Set<Concern> getAllConcerns() {
		Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());
		for (Method operation : getOperations())
			concerns.addAll(operation.getAllConcerns());
		
		return Collections.unmodifiableSet(concerns);
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
