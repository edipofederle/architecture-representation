package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import arquitetura.exceptions.NotFoundException;
import arquitetura.helpers.ElementsTypes;
import arquitetura.helpers.UtilResources;
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
	
	/**
	 * 
	 * @param supplier
	 * @param client
	 * @param name
	 * @param architecture
	 * @param id
	 */
	public DependencyRelationship(Element supplier, Element client, String name, Architecture architecture, String id) {
		setSupplier(supplier);
		setClient(client);
		setName(name);
		this.architecture = architecture;
		setId(id);
		super.setType(ElementsTypes.DEPENDENCY);

		setRequiredInterfaces(supplier, client);
		
	}

	public DependencyRelationship(Element supplier, Element client, String string, Architecture a) {
		setSupplier(supplier);
		setClient(client);
		setName(name);
		this.architecture = a;
		setId(UtilResources.getRandonUUID());
		super.setType(ElementsTypes.DEPENDENCY);

		setRequiredInterfaces(supplier, client);
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
		Set<Relationship> relations = architecture.getRelationshipHolder().getAllRelationships();
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
		
		
		return Collections.unmodifiableList(dependenciesTemp);
	}
	
	private void setRequiredInterfaces(Element supplier, Element client) {
		if((client instanceof Package) && (supplier instanceof Interface)){
			((Package) client).addRequiredInterface((Interface) supplier);
		}
		
		if((client instanceof Class) && (supplier instanceof Interface)){
			((Class) client).addRequiredInterface((Interface) supplier);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result
				+ ((supplier == null) ? 0 : supplier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
       if (obj == null) {
           return false;
       }
       if (getClass() != obj.getClass()) {
           return false;
       }
       final DependencyRelationship other = (DependencyRelationship) obj;
       if (this.supplier != other.supplier && (this.supplier == null || !this.supplier.equals(other.supplier))) {
           return false;
       }
       return this.client == other.client || (this.client != null && this.client.equals(other.client));
   }
	
}