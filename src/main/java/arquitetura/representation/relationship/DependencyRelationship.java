package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class DependencyRelationship extends Relationship {

	private Element client;
	private Element supplier;
	private Architecture architecture;
	String  name;
	
	public DependencyRelationship(){}
	
	public DependencyRelationship(Element supplier, Element client, String name, Architecture architecture, String id) {
		setSupplier(supplier);
		setClient(client);
		setName(name);
		this.architecture = architecture;
		setId(id);
		setTypeRelationship("dependency");
	}

	public void setName(String name) {
		this.name = name;
	}

	public Element getClient() {
		return client;
	}

	public void setClient(Element client) {
		this.client = client;
	}

	public Element getSupplier() {
		return supplier;
	}

	public void setSupplier(Element supplier) {
		this.supplier = supplier;
	}
	
	public void replaceSupplier(Element supplier){
		setSupplier(supplier);
	}
	
	public void replaceClient(Element client){
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

	/**
	 * Retornar todas as {@link Class}'s que são 'client' de 'supplier'; 
	 * 
	 * @return
	 */
	public List<Element> getAllClientsForSupplierClass() {
	 return getClassesForSpecificTypePartOfDependency("client");
	}

	/**
	 * Retorna todas as {@link Class}'s que são 'suppliers' da classe 'cliente'
	 * 
	 * @return
	 */
	public List<Element> getAllSuppliersForClientClass() {
		return getClassesForSpecificTypePartOfDependency("supplier");
	}

	private List<Element> getClassesForSpecificTypePartOfDependency(String type) {
		List<Relationship> relations = architecture.getInterClassRelationships();
		List<DependencyRelationship> dependencies = new ArrayList<DependencyRelationship>();
		
		List<Element> dependenciesTemp = new ArrayList<Element>();
		
		for (Relationship relationship : relations)
			if(relationship instanceof DependencyRelationship)
				dependencies.add(((DependencyRelationship)relationship));
		
		if("client".equalsIgnoreCase(type)){
			for (DependencyRelationship dependencyInterClassRelationship : dependencies)
				if(dependencyInterClassRelationship.getSupplier().getName().equalsIgnoreCase(this.supplier.getName()))
					dependenciesTemp.add(dependencyInterClassRelationship.getClient());
		}else if ("supplier".equalsIgnoreCase(type)) {
			for (DependencyRelationship dependencyInterClassRelationship : dependencies)
				if(dependencyInterClassRelationship.getClient().getName().equalsIgnoreCase(this.getClient().getName()))
					dependenciesTemp.add(dependencyInterClassRelationship.getSupplier());
		}
		
		if(dependenciesTemp.isEmpty()) return Collections.emptyList();
		return dependenciesTemp;
	}
	
}
