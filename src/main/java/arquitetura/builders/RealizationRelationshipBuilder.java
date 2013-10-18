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
		
//		EList<org.eclipse.uml2.uml.Element> relatedElements = realization.getRelatedElements();
//		
//		for (org.eclipse.uml2.uml.Element element : relatedElements) {
//			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(element));
//			e.getIdsRelationships().add((getModelHelper().getXmiId(realization)));
//		}
		
		
		Element clientElement = architecture.getElementByXMIID(idClient);
		Element supplierElement = architecture.getElementByXMIID(idSupplier);
		String name = realization.getName() != null ? realization.getName() : "";
		architecture.getAllIds().add(getModelHelper().getXmiId(realization));
		RealizationRelationship realizationRelationship = new RealizationRelationship(clientElement, supplierElement, name, getModelHelper().getXmiId(realization));
		
		realizationRelationship.getClient().getRelationships().add(realizationRelationship);
		realizationRelationship.getSupplier().getRelationships().add(realizationRelationship);
		
		return realizationRelationship;
	}
	
}