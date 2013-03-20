package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationEnd;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;


/**
 * 
 * @author edipofederle
 *
 */
public class AssociationRelationshipBuilder extends RelationshipBase {
	
	private final AssociationEndBuilder associationEndBuilder;
	private Architecture architecture;
	
	
	public AssociationRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
		associationEndBuilder = new AssociationEndBuilder();
	}

	public AssociationRelationship create(Association association) {
		
		EList<Element> relatedElements = association.getRelatedElements();
		
		for (Element element : relatedElements) {
			mestrado.arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
			e.getIdsRelationships().add((getModelHelper().getXmiId(association)));
		}
		
		AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
		
		associationRelationship.getParticipants().addAll(getParticipants(association));
		return associationRelationship;
	}

	//TODO verificar essa classe, testes com mais tipos de associaçoes
	private List<? extends AssociationEnd> getParticipants(Association association) {
		List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();
		
		for (Property a : association.getMemberEnds()) {
			try{
				String id = getModelHelper().getXmiId(a.getType());
				mestrado.arquitetura.representation.Element c = architecture.getElementByXMIID(id);
				elementsOfAssociation.add(associationEndBuilder.create(a,c));
			}catch(Exception e){}
					
		}
		
		return elementsOfAssociation;
		
	}
	
}