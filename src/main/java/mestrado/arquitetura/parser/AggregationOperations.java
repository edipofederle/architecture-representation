package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

public class AggregationOperations implements Relationship {

	private DocumentManager doc;
	private String client;
	private String target;
	private String name;

	public AggregationOperations(DocumentManager doc, String name) {
		this.doc = doc;
		this.name = name;
	}

	public AggregationOperations(DocumentManager doc) {
		this.doc = doc;
	}

	public Relationship between(String idElement) {
		this.client = idElement;
		return this;
	}

	public Relationship and(String idElement) {
		this.target = idElement;
		return this;
	}

	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final CompositionNode compositeNode = new CompositionNode(doc);
		
		mestrado.arquitetura.parser.Document.executeTransformation(doc, new Transformation(){
			public void useTransformation() throws NodeNotFound, InvalidMultiplictyForAssociationException {
				compositeNode.createComposition(client, target, "", "", "shared");
			}
		});
		
		return ""; //TODO return id;
	}

	public Relationship createRelation(String name) {
		if(("".equals(name) || name == null)) name = "shared";
		return new AggregationOperations(this.doc, name);
	}


}