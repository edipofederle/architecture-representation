package arquitetura.builders;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationRelationshipBuilder extends ArchitectureHelper {
	
	private final AssociationEndBuilder associationEndBuilder;
	private Architecture architecture;
	
	
	public AssociationRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
		associationEndBuilder = new AssociationEndBuilder();
	}

	public AssociationRelationship create(Association association) {
		
		EList<Element> relatedElements = association.getRelatedElements();
		
		for (Element element : relatedElements) {
			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
			e.getIdsRelationships().add((getModelHelper().getXmiId(association)));
		}
		
		AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
		
		
		associationRelationship.getParticipants().addAll(getParticipants(association));
		associationRelationship.setTypeRelationship("association");
		associationRelationship.setName(association.getName());
		architecture.getAllIds().add(getModelHelper().getXmiId(association));
		return associationRelationship;
	}

	private List<? extends AssociationEnd> getParticipants(Association association) {
		List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();
		
		for (Property a : association.getMemberEnds()) {
			try{
				String id = getModelHelper().getXmiId(a.getType());
				arquitetura.representation.Element c = architecture.getElementByXMIID(id);
				
				elementsOfAssociation.add(associationEndBuilder.create(a,c));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return elementsOfAssociation;
		
	}
	
}