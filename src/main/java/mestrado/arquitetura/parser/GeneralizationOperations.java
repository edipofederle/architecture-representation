package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.exceptions.NotSuppportedOperation;

public class GeneralizationOperations implements Relationship {
	
	
	private DocumentManager documentManager;
	
	private String general;
	private String client;
	private String name;
	
	public GeneralizationOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public GeneralizationOperations(DocumentManager documentManager2, String name2) {
		this.documentManager = documentManager2;
		this.name = name2;
	}

	public Relationship createRelation(String name) {
		this.name = name;
		return this;
	}

	public Relationship between(String idElement) {
		this.client = idElement;
		return this;
	}

	public Relationship and(String idElement) {
		this.general = idElement;
		return this;
	}

	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final GeneralizationNode generalizationNode = new GeneralizationNode(this.documentManager, this.general, this.client, this.name);
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound, InvalidMultiplictyForAssociationException {
				generalizationNode.createGeneralization();
			}
		});
		
		return ""; //TODO return id;
	}

	public Relationship withMultiplicy(String string)throws NotSuppportedOperation {
		return null;
	}

}