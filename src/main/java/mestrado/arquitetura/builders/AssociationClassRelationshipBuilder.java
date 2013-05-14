package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.AssociationClassRelationship;
import mestrado.arquitetura.representation.relationship.Relationship;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Type;

public class AssociationClassRelationshipBuilder extends RelationshipBase {
	
	private Architecture architecture;

	public AssociationClassRelationshipBuilder(Architecture architecture) { this.architecture = architecture; }

	public Relationship create(AssociationClass associationClass) {
		List<Element> membersEnd = new ArrayList<Element>();
		
		for (Type t : associationClass.getEndTypes()) 
			membersEnd.add(architecture.getElementByXMIID(getModelHelper().getXmiId(t)));
		
		Type ownedEnd = associationClass.getOwnedEnds().get(0).getType();
		Element onewd = architecture.getElementByXMIID(getModelHelper().getXmiId(ownedEnd));
		
		architecture.getAllIds().add(getModelHelper().getXmiId(associationClass));
		
		return new AssociationClassRelationship(associationClass.getName(), membersEnd, onewd, getModelHelper().getXmiId(associationClass));
	}
	
}