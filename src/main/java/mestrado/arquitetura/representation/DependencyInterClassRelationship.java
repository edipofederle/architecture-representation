package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DependencyInterClassRelationship extends InterClassRelationship {

	private Class client;
	private Class supplier;
	private Architecture architecture;
	String  name;
	
	public DependencyInterClassRelationship(Class supplier, Class client, String name, Architecture architecture) {
		setSupplier(supplier);
		setClient(client);
		setName(name);
		this.architecture = architecture;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getClient() {
		return client;
	}

	private void setClient(Class client) {
		this.client = client;
	}

	public Class getSupplier() {
		return supplier;
	}

	private void setSupplier(Class supplier) {
		this.supplier = supplier;
	}
	
	public void replaceSupplier(Class supplier){
		setSupplier(supplier);
	}
	
	public void replaceClient(Class client){
		setClient (client);
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public String getName() {
		return name != null ? name : "" ;
	}

	//TODO Acho que devo mover daqui esses dois métodos.????
	/**
	 * Retornar todas as {@link Class}'s que são 'client' de 'supplier'; 
	 * 
	 * @return
	 */
	public List<Class> getAllClientsForSupplierClass() {
	 return getClassesForSpecificTypePartOfDependency("client");
	}

	/**
	 * Retorna todas as {@link Class}'s que são 'suppliers' da classe 'cliente'
	 * 
	 * @return
	 */
	public List<Class> getAllSuppliersForClientClass() {
		return getClassesForSpecificTypePartOfDependency("supplier");
	}

	private List<Class> getClassesForSpecificTypePartOfDependency(String type) {
		List<InterClassRelationship> relations = architecture.getInterClassRelationships();
		List<DependencyInterClassRelationship> dependencies = new ArrayList<DependencyInterClassRelationship>();
		
		List<Class> dependenciesTemp = new ArrayList<Class>();
		
		for (InterClassRelationship interClassRelationship : relations)
			if(interClassRelationship instanceof DependencyInterClassRelationship)
				dependencies.add(((DependencyInterClassRelationship)interClassRelationship));
		
		if("client".equalsIgnoreCase(type)){
			for (DependencyInterClassRelationship dependencyInterClassRelationship : dependencies)
				if(dependencyInterClassRelationship.getSupplier().getName().equalsIgnoreCase(this.supplier.getName()))
					dependenciesTemp.add(dependencyInterClassRelationship.getClient());
		}else if ("supplier".equalsIgnoreCase(type)) {
			for (DependencyInterClassRelationship dependencyInterClassRelationship : dependencies)
				if(dependencyInterClassRelationship.getClient().getName().equalsIgnoreCase(this.getClient().getName()))
					dependenciesTemp.add(dependencyInterClassRelationship.getSupplier());
		}
		
		if(dependenciesTemp.isEmpty()) return Collections.emptyList();
		return dependenciesTemp;
	}
	
}
