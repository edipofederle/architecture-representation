package arquitetura.builders;


import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Generalization;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
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
		
		EList<org.eclipse.uml2.uml.Element> relatedElements = generalization.getRelatedElements();
		
		for (org.eclipse.uml2.uml.Element element : relatedElements) {
			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
			e.getIdsRelationships().add((getModelHelper().getXmiId(generalization)));
		}
		
		arquitetura.representation.Class general = (Class) architecture.getElementByXMIID(generalKlassId);
		arquitetura.representation.Class specific = (Class) architecture.getElementByXMIID(specificKlassId);
		architecture.getAllIds().add(getModelHelper().getXmiId(generalization));
		return new GeneralizationRelationship(general, specific, architecture, getModelHelper().getXmiId(generalization));
	}

}
