package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.AssociationEnd;
import mestrado.arquitetura.representation.AssociationRelationship;
import mestrado.arquitetura.representation.Class;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
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
		AssociationRelationship associationRelationship = new AssociationRelationship();
		
		associationRelationship.getParticipants().addAll(getParticipants(association));
		return associationRelationship;
	}

	private List<? extends AssociationEnd> getParticipants(Association association) {
		List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();
		EList<Type> ends = association.getEndTypes();
		
		EList<Property> endssInfos = association.getOwnedEnds();	
		
		for (int i = 0; i < ends.size(); i++) {
			Class c = classBuilder.getElementByXMIID(getModelHelper().getXmiId(ends.get(i)));
			elementsOfAssociation.add(associationEndBuilder.create(endssInfos.get(i),c));
		}
		
		return elementsOfAssociation;
		
	}
	
}