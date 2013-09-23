package arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationRelationship extends Relationship {

	private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();
	
	public AssociationRelationship(String id) {setId(id);}
	
//	public AssociationRelationship(Class class1, Class class2) {
//		setId(UtilResources.getRandonUUID());
//		getParticipants().add(new AssociationEnd(class1));
//		getParticipants().add(new AssociationEnd(class2));
//		
//		setTypeRelationship("associacao");
//
//	}

	public List<AssociationEnd> getParticipants() {
		return participants;
	}

}