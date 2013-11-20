package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import arquitetura.helpers.ElementsTypes;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Element;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationRelationship extends Relationship {

	private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();
	
	public AssociationRelationship(String id) {setId(id);}
	
	public AssociationRelationship(Element class1, Element class2) {
		setId(UtilResources.getRandonUUID());
		getParticipants().add(new AssociationEnd(class1, false, "association", null,""));
		getParticipants().add(new AssociationEnd(class2, false, "association", null,""));
		
		super.setType(ElementsTypes.ASSOCIATION);

	}

	public List<AssociationEnd> getParticipants() {
		return participants;
	}

}