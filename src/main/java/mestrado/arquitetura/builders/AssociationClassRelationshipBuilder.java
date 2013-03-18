package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.relationship.AssociationClassRelationship;
import mestrado.arquitetura.representation.relationship.Relationship;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Type;

public class AssociationClassRelationshipBuilder extends RelationshipBase {
	
	private ClassBuilder classBuilder;

	public AssociationClassRelationshipBuilder(ClassBuilder classBuilder) { this.classBuilder = classBuilder; }

	public Relationship create(AssociationClass associationClass) {
		List<Class> membersEnd = new ArrayList<Class>();
		
		for (Type t : associationClass.getEndTypes()) 
			membersEnd.add(classBuilder.getElementByXMIID(getModelHelper().getXmiId(t)));
		
		Type ownedEnd = associationClass.getOwnedEnds().get(0).getType();
		mestrado.arquitetura.representation.Class onewd = classBuilder.getElementByXMIID(getModelHelper().getXmiId(ownedEnd));
		
		return new AssociationClassRelationship(associationClass.getName(), membersEnd, onewd);
	}
	
}