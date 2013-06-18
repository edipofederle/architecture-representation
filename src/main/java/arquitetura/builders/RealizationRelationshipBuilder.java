package arquitetura.builders;


import org.eclipse.uml2.uml.Realization;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.RealizationRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class RealizationRelationshipBuilder extends ArchitectureHelper {

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