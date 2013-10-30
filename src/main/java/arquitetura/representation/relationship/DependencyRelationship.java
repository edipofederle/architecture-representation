package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import arquitetura.exceptions.NotFoundException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;


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
		setType("dependency");

		if((client instanceof Package) && (supplier instanceof Interface)){
			((Package) client).addImplementedInterface(supplier);
		}
		
		if((supplier instanceof Package) && (client instanceof Interface)){
			((Package) supplier).addImplementedInterface(client);
		}
	}
	
	/**
	 * Retorna o {@link Package} 
	 * 
	 * @return Package se existir.
	 * @throws arquitetura.exceptions.NotFoundException caso não exista pacote envolvido na dependencia.
	 */
	public Package getPackageOfDependency() throws NotFoundException {
		if(this.client instanceof Package)
			return (Package) this.client;
		else if (this.supplier instanceof Package)
			return (Package) this.supplier;
		
		throw new NotFoundException("There is no Package in this dependency.");
	}
	
	/**
	 * Retorna a {@link Interface} 
	 * 
	 * @return Interface se existir.
	 * @throws arquitetura.exceptions.NotFoundException caso não exista interface envolvido na dependencia.
	 */
	public Interface getInterfaceOfDependency() throws NotFoundException {
		if(this.client instanceof Interface)
			return (Interface) this.client;
		else if (this.supplier instanceof Interface)
			return (Interface) this.supplier;
		
		throw new NotFoundException("There is no Interface in this dependency.");
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
