package mestrado.arquitetura.representation.relationship;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Class;

/**
 * 
 * @author edipofederle
 *
 */
public class AssociationRelationship extends Relationship {

	private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();
	
	public AssociationRelationship(String id) {setId(id);}
	
	public AssociationRelationship(Class class1, Class class2) {
		getParticipants().add(new AssociationEnd(class1));
		getParticipants().add(new AssociationEnd(class2));

	}

	public List<AssociationEnd> getParticipants() {
		return participants;
	}

}