package arquitetura.builders;

import org.eclipse.uml2.uml.Association;

import arquitetura.base.ArchitectureHelper;
import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.ModelElementHelper;
import arquitetura.helpers.StereotypeHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Aspect;
import arquitetura.representation.AspectHolder;
import arquitetura.representation.relationship.AssociationHelper;
import arquitetura.representation.relationship.AssociationRelationship;
import java.util.List;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationRelationshipBuilder extends ArchitectureHelper {
	
	private final AssociationEndBuilder associationEndBuilder;
	private AssociationHelper associationHelper;
        //Inicio - Thaina 12/14 - Aspecto
        private String aspect;
        //Fim - Thaina 12/14 - Aspecto
	
	public AssociationRelationshipBuilder(Architecture architecture) {
		associationEndBuilder = new AssociationEndBuilder();
		associationHelper = new AssociationHelper(associationEndBuilder, architecture);
	}

	public AssociationRelationship create(Association association) {
                //Inicio - Thaina 12/14 - Aspecto
		inspectStereotypes(association);
		AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
		associationRelationship.getParticipants().addAll(associationHelper.getParticipants(association));
		associationRelationship.setType("association");
		associationRelationship.setName(association.getName());
                //Fim - Thaina 12/14 - Aspecto
                
                //Inicio - Thaina 12/14 - Aspecto
                try{
                    associationRelationship.addPoincut(aspect);
                } catch (ConcernNotFoundException e) {
			e.printStackTrace();
		}
		//Fim - Thaina 12/14 - Aspecto
                
//		for(AssociationEnd associationEnd : associationRelationship.getParticipants()){
//			associationEnd.getCLSClass().addRelationship(associationRelationship);
//		}
		
		return associationRelationship;
	}
 
        //Inicio - Thaina 12/14 - Aspectos
        private void inspectStereotypes(NamedElement modelElement) {
                aspect = "";
		List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
		for (Stereotype stereotype : allStereotypes) {
                        verifyAspect(stereotype);
		}
	}
        
        private void verifyAspect(Stereotype stereotype) {
		if (StereotypeHelper.hasAspect(stereotype)){
                            aspect = stereotype.getName();
                }
	}
        //Fim - Thaina 12/14 - Aspectos

}
