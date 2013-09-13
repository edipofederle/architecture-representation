package arquitetura.builders;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Type;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.Relationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationClassRelationshipBuilder extends ArchitectureHelper {
	
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