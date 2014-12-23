package arquitetura.representation.relationship;

import arquitetura.exceptions.ConcernNotFoundException;
import java.util.ArrayList;
import java.util.List;

import arquitetura.helpers.ElementsTypes;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Aspect;
import arquitetura.representation.AspectHolder;
import arquitetura.representation.Element;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationRelationship extends Relationship {

	private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();
        private boolean pointcut = false;
	
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
        
        //Inicio - Thaina 12/14 - Aspecto
                
        public boolean isPoincut(){
            return pointcut;
	}
        
        public void removePoincut(){
            pointcut = false;
        }
                
        public void addPoincut(String aspectName) throws ConcernNotFoundException {
		if(aspectName.equalsIgnoreCase("pointcut")){
                    pointcut = true;
                }
	}
        //Fim - Thaina 12/14 - Aspecto

//THAINA 12/14 - REMOVIDO ESSE METODO PQ NAO ESTAVA FUNCIONANDO
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result
//				+ ((participants == null) ? 0 : participants.hashCode());
//		return result;
//	}
        

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociationRelationship other = (AssociationRelationship) obj;
		if (participants == null) {
			if (other.participants != null)
				return false;
		} else if (!participants.containsAll(other.participants))
			return false;
		return true;
	}

}