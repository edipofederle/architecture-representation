package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.RealizationRelationship;

import org.eclipse.uml2.uml.Realization;

/**
 * 
 * @author edipofederle
 *
 */
public class RealizationRelationshipBuilder extends RelationshipBase {

	private Architecture architecture;
	
	public RealizationRelationshipBuilder(Architecture architecture) {
		this.architecture = architecture;
	}

	public RealizationRelationship create(Realization realization) {
		
		String idClient = getModelHelper().getXmiId(realization.getClients().get(0));
		String idSupplier = getModelHelper().getXmiId(realization.getSuppliers().get(0));
		
		
		Element clientElement = architecture.getElementByXMIID(idClient);
		Element supplierElement = architecture.getElementByXMIID(idSupplier);
		String name = realization.getName() != null ? realization.getName() : "";
		architecture.getAllIds().add(getModelHelper().getXmiId(realization));
		return new RealizationRelationship(clientElement, supplierElement, name, getModelHelper().getXmiId(realization));
	}
	
}