package arquitetura.builders;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AssociationHelper;
import arquitetura.representation.relationship.AssociationRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationRelationshipBuilder extends ArchitectureHelper {
	
	private final AssociationEndBuilder associationEndBuilder;
	private Architecture architecture;
	private AssociationHelper associationHelper;
	
	
	public AssociationRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
		associationEndBuilder = new AssociationEndBuilder();
		associationHelper = new AssociationHelper(associationEndBuilder, architecture);
	}

	public AssociationRelationship create(Association association) {
		
		EList<Element> relatedElements = association.getRelatedElements();
		
		for (Element element : relatedElements) {
			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
			e.getIdsRelationships().add((getModelHelper().getXmiId(association)));
		}
		
		AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
		associationRelationship.getParticipants().addAll(associationHelper.getParticipants(association));
		associationRelationship.setTypeRelationship("association");
		associationRelationship.setName(association.getName());
		architecture.getAllIds().add(getModelHelper().getXmiId(association));
		return associationRelationship;
	}

}