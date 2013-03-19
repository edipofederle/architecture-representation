package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.relationship.AssociationEnd;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;


/**
 * 
 * @author edipofederle
 *
 */
public class AssociationRelationshipBuilder extends RelationshipBase {
	
	private final AssociationEndBuilder associationEndBuilder;
	private ClassBuilder classBuilder;
	
	
	public AssociationRelationshipBuilder(ClassBuilder classBuilder) {
		this.classBuilder = classBuilder;
		associationEndBuilder = new AssociationEndBuilder();
	}

	public AssociationRelationship create(Association association) {
		
		EList<Element> relatedElements = association.getRelatedElements();
		
		for (Element element : relatedElements) {
			mestrado.arquitetura.representation.Element e = classBuilder.getElementByXMIID(getModelHelper().getXmiId(element));
			e.getIdsRelationships().add((getModelHelper().getXmiId(association)));
		}
		
		AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
		
		associationRelationship.getParticipants().addAll(getParticipants(association));
		return associationRelationship;
	}

	//TODO verificar essa classe, testes com mais tipos de associaçoes
	private List<? extends AssociationEnd> getParticipants(Association association) {
		List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();
		EList<Type> ends = association.getEndTypes();
		
		EList<Property> endssInfos = association.getOwnedEnds();
		
		
		for (Property a : association.getMemberEnds()) {
			try{
				String id = getModelHelper().getXmiId(a.getType());
				mestrado.arquitetura.representation.Element c = classBuilder.getElementByXMIID(id);
				elementsOfAssociation.add(associationEndBuilder.create(a,c));
			}catch(Exception e){}
					
		}

		
		return elementsOfAssociation;
		
	}
	
}