package mestrado.arquitetura.api.touml;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class CompositionOperations {

	private DocumentManager doc;
	private String client;
	private String target;
	private String multiplicityClassTarget;
	private String multiplicityClassClient;
	private String name;

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
	
	public CompositionOperations withMultiplicy(String multiplicity) {
		if(this.target != null)
			this.multiplicityClassTarget = multiplicity;
		else if(this.client != null)
			this.multiplicityClassClient = multiplicity;
		return this;
	}
	

	public void build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final CompositionNode cn = new CompositionNode(doc);
		
		mestrado.arquitetura.api.touml.Document.executeTransformation(doc, new Transformation(){
			public void useTransformation() {
				cn.createComposition(name, client, target, multiplicityClassClient, multiplicityClassTarget, "composite");
			}
		});
	}


	

}