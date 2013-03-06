package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.AssociationEnd;
import mestrado.arquitetura.representation.AssociationInterClassRelationship;
import mestrado.arquitetura.representation.Class;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;


public class AssociationInterClassRelationshipBuilder {
	
	private final AssociationEndBuilder associationEndBuilder;
	private ClassBuilder classBuilder;
	
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
			Class c = classBuilder.getElementByXMIID(getXmiId(ends.get(i)));
			elementsOfAssociation.add(associationEndBuilder.create(endssInfos.get(i),c));
		}
		
		return elementsOfAssociation;
		
	}
	
	private static String getXmiId (EObject eObject) {
		Resource xmiResource = eObject.eResource();
		if (xmiResource == null ) return null; //TODO verificar isto. Não retornar NULL.
		return ((XMLResource) xmiResource).getID(eObject);
	}
	
}
