package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.AssociationEnd;
import mestrado.arquitetura.representation.AssociationInterClassRelationship;
import mestrado.arquitetura.representation.Class;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;


public class AssociationInterClassRelationshipBuilder {
	
	private final AssociationEndBuilder associationEndBuilder;
	private ClassBuilder classBuilder;
	private static ModelHelper modelHelper;
	
	static{
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	
	public AssociationInterClassRelationshipBuilder(ClassBuilder classBuilder) {
		this.classBuilder = classBuilder;
		associationEndBuilder = new AssociationEndBuilder();
	}

	public AssociationInterClassRelationship create(Association association) {
		AssociationInterClassRelationship associationRelationship = new AssociationInterClassRelationship();
		
		associationRelationship.getParticipants().addAll(getParticipants(association));
		return associationRelationship;
	}

	private List<? extends AssociationEnd> getParticipants(Association association) {
		List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();
		EList<Type> ends = association.getEndTypes();
		
		EList<Property> endssInfos = association.getOwnedEnds();	
		
		for (int i = 0; i < ends.size(); i++) {
			Class c = classBuilder.getElementByXMIID(modelHelper.getXmiId(ends.get(i)));
			elementsOfAssociation.add(associationEndBuilder.create(endssInfos.get(i),c));
		}
		
		return elementsOfAssociation;
		
	}
	
}