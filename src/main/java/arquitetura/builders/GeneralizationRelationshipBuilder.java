package arquitetura.builders;


import org.eclipse.uml2.uml.Generalization;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
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
		
		Element general = architecture.getElementByXMIID(generalKlassId);
		Element specific = architecture.getElementByXMIID(specificKlassId);
		architecture.getAllIds().add(getModelHelper().getXmiId(generalization));
		return new GeneralizationRelationship(general, specific, architecture, getModelHelper().getXmiId(generalization));
	}

}
