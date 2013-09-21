package arquitetura.touml;

import arquitetura.representation.relationship.AssociationClassRelationship;


public class AssociationKlassOperations {
	
	private DocumentManager documentManager;
	private String ownedEnd;
	private String associationEnd2;
	private String id;
	
	public AssociationKlassOperations(DocumentManager doc) {
		this.documentManager = doc;
	}

	public AssociationKlassOperations createAssociationClass(AssociationClassRelationship asr) {
		this.id = asr.getId();
		this.ownedEnd = asr.getOwnedEnd().getId();
		this.associationEnd2 = asr.getMemebersEnd().get(0).getId();
		return this;
	}

	public void build() {
		final AssociationClassNode associationClassNode = new AssociationClassNode(this.documentManager,null);
		
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				associationClassNode.createAssociationClass(id, ownedEnd, associationEnd2);
			}
		});
	}

}
