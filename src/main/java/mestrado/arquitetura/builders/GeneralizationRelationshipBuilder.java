package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.representation.relationship.Relationship;

import org.eclipse.uml2.uml.Generalization;

/**
 * 
 * @author edipofederle
 *
 */
public class GeneralizationRelationshipBuilder extends RelationshipBase {
	
	

	private ClassBuilder classBuilder;
	private Architecture architecture;

	public GeneralizationRelationshipBuilder(ClassBuilder classBuilder, Architecture architecture) {
		this.classBuilder = classBuilder;
		this.architecture = architecture;
	}

	public Relationship create(Generalization generalization) {
		String generalKlassId = getModelHelper().getXmiId(generalization.getGeneral());
		String specificKlassId = getModelHelper().getXmiId(generalization.getSpecific());
		
		Class generalKlass = classBuilder.getElementByXMIID(generalKlassId);
		Class specificKlass = classBuilder.getElementByXMIID(specificKlassId);
		
		return new GeneralizationRelationship(generalKlass, specificKlass, architecture, getModelHelper().getXmiId(generalization));
	}

}
