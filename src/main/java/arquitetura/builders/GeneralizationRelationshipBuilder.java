package arquitetura.builders;


import org.eclipse.uml2.uml.Generalization;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class GeneralizationRelationshipBuilder extends ArchitectureHelper {
	
	private Architecture architecture;

	public GeneralizationRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
	}

	public Relationship create(Generalization generalization) {
		String generalKlassId = getModelHelper().getXmiId(generalization.getGeneral());
		String specificKlassId = getModelHelper().getXmiId(generalization.getSpecific());
		
//		EList<org.eclipse.uml2.uml.Element> relatedElements = generalization.getRelatedElements();
//		
//		for (org.eclipse.uml2.uml.Element element : relatedElements) {
//			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
//			e.getIdsRelationships().add((getModelHelper().getXmiId(generalization)));
//		}
		
		arquitetura.representation.Element general = architecture.findElementById(generalKlassId);
		arquitetura.representation.Element specific = architecture.findElementById(specificKlassId);
		GeneralizationRelationship generalizationRelation = new GeneralizationRelationship(general, specific, architecture, getModelHelper().getXmiId(generalization));
		
		generalizationRelation.getParent().addRelationship(generalizationRelation);
		generalizationRelation.getChild().addRelationship(generalizationRelation);
		return generalizationRelation;
	}

}
