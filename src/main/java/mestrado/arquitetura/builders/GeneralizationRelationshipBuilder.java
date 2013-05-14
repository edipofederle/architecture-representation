package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.representation.relationship.Relationship;

import org.eclipse.uml2.uml.Generalization;

/**
 * 
 * @author edipofederle
 *
 */
public class GeneralizationRelationshipBuilder extends RelationshipBase {
	
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
