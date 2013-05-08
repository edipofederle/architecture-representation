package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

public class CompositionOperations {

	private DocumentManager doc;
	private String client;
	private String target;

	public CompositionOperations(DocumentManager doc) {
		this.doc = doc;
	}

	public CompositionOperations createComposition() {
		return new CompositionOperations(doc);
	}

	public CompositionOperations between(String idElement) {
		this.client = idElement;
		return this;
	}

	public CompositionOperations and(String idElement) {
		this.target = idElement;
		return this;
	}

	public void build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final CompositionNode cn = new CompositionNode(doc);
		
		mestrado.arquitetura.parser.Document.executeTransformation(doc, new Transformation(){
			public void useTransformation() throws NodeNotFound, InvalidMultiplictyForAssociationException {
				cn.createComposition(client, target);
			}
		});
	}
	
	

}