package mestrado.arquitetura.api.touml;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.exceptions.NotSuppportedOperation;

public class DependencyOperations implements Relationship {
	
	private DocumentManager documentManager;
	
	private String clientElement;
	private String supplierElement;
	private String name;
	
	public DependencyOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public DependencyOperations(DocumentManager documentManager2, String name2) {
		this.documentManager = documentManager2;
		this.name = name2;
	}

	public Relationship createRelation(String name) {
		if(("".equals(name) || name == null)) name = "dependency";
		return new DependencyOperations(this.documentManager, name);
	}

	public Relationship between(String idElement) {
		this.clientElement = idElement;
		return this;
	}

	public Relationship and(String idElement) {
		this.supplierElement = idElement;
		return this;
	}

	public String build() throws CustonTypeNotFound, NodeNotFound,	InvalidMultiplictyForAssociationException {
		final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement);
		
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				dependencyNode.createDependency("dependency");
			}
		});
		
		return ""; //TODO return id;

	}

	public Relationship withMultiplicy(String string) throws NotSuppportedOperation {
		throw new NotSuppportedOperation("Dependency dont have multiplicy");
	}

}