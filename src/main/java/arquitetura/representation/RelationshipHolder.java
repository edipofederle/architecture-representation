package arquitetura.representation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.MemberEnd;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;

public class RelationshipHolder {
	
	private static Set<Relationship> relationships = new HashSet<Relationship>();
	
	
	public static void clearLists(){
		relationships.clear();
	}

	public static Set<Relationship> getRelationships() {
		return relationships;
	}

	public static void setRelationships(Set<Relationship> rs) {
		relationships = rs;
	}
	
	/**
	 * Dado um {@link Element} remove todos relacionamentos em que o elemento esteja envolvido
	 * 
	 * @param element
	 */
	public static void removeRelatedRelationships(Element element) {
		for (Iterator<Relationship> i = RelationshipHolder.getRelationships().iterator(); i.hasNext();) {
			Relationship r = i.next();
			if(r instanceof GeneralizationRelationship){
				if(((GeneralizationRelationship) r).getParent().equals(element) || ((GeneralizationRelationship) r).getChild().equals(element)){
					i.remove();
				}
			}
			if(r instanceof RealizationRelationship){
				if(((RealizationRelationship) r).getClient().equals(element) || ((RealizationRelationship) r).getSupplier().equals(element)){
					i.remove();
				}
			}
			if(r instanceof DependencyRelationship){
				if(((DependencyRelationship) r).getClient().equals(element) || ((DependencyRelationship) r).getSupplier().equals(element)){
					i.remove();
				}
			}
			if(r instanceof AbstractionRelationship){
				if(((AbstractionRelationship) r).getClient().equals(element) || ((AbstractionRelationship) r).getSupplier().equals(element)){
					i.remove();
				}
			}
			if(r instanceof AssociationRelationship){
				for(AssociationEnd a : ((AssociationRelationship)r).getParticipants()){
					if(a.getCLSClass().equals(element)){
						i.remove();
					}
				}
			}
			
			if(r instanceof AssociationClassRelationship){
				for(MemberEnd memberEnd : ((AssociationClassRelationship) r).getMemebersEnd()){
					if(memberEnd.getType().equals(element)){
						i.remove();
					}
						
				}
			}
		}
	}

	
}
