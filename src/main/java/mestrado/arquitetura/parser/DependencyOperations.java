package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

public class DependencyOperations {

	private DocumentManager documentManager;
	
	private String clientElement;
	private String supplierElement;
	

	public DependencyOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public DependencyOperations createDependency() {
		return new DependencyOperations(this.documentManager);
	}

	public DependencyOperations between(String idElement) {
		this.clientElement = idElement;
		return this;
	}

	public DependencyOperations and(String idElement) {
		this.supplierElement = idElement;
		return this;
	}

	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.clientElement, this.supplierElement);
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound, InvalidMultiplictyForAssociationException {
				dependencyNode.createDependency();
			}
		});
		
		return "";		
	}
	
}