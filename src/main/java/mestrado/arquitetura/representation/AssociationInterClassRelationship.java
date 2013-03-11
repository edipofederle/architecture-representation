package mestrado.arquitetura.representation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author edipofederle
 *
 */
public class AssociationInterClassRelationship extends InterClassRelationship {

	private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();
	
	public AssociationInterClassRelationship() { }
	
	public AssociationInterClassRelationship(Class class1, Class class2) {
		getParticipants().add(new AssociationEnd(class1));
		getParticipants().add(new AssociationEnd(class2));
	}

	public List<AssociationEnd> getParticipants() {
		return participants;
	}

}