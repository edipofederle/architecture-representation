package mestrado.arquitetura.builders;

import mestrado.arquitetura.base.RelationshipBase;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.RealizationRelationship;

import org.eclipse.uml2.uml.Realization;

/**
 * 
 * @author edipofederle
 *
 */
public class RealizationRelationshipBuilder extends RelationshipBase {

	private ClassBuilder classBuilder;
	private PackageBuilder packageBuilder;
	
	public RealizationRelationshipBuilder(ClassBuilder classBuilder, PackageBuilder packageBuilder) {
		this.classBuilder = classBuilder;
		this.packageBuilder = packageBuilder;
	}

	public RealizationRelationship create(Realization realization) {
		
		String idClient = getModelHelper().getXmiId(realization.getClients().get(0));
		String idSupplier = getModelHelper().getXmiId(realization.getSuppliers().get(0));
		
		Element clientElement;
		Element supplierElement;
		
		clientElement = classBuilder.getElementByXMIID(idClient);
		supplierElement = classBuilder.getElementByXMIID(idSupplier);
		
		if( (supplierElement == null) && (clientElement != null)){
			supplierElement = packageBuilder.getElementByXMIID(idSupplier);
		}else if((clientElement == null) && (supplierElement != null)){
			clientElement = packageBuilder.getElementByXMIID(idClient);
		}else if((clientElement == null) && (supplierElement == null)){
			clientElement = packageBuilder.getElementByXMIID(idClient);
			supplierElement = packageBuilder.getElementByXMIID(idSupplier);
		}
		
		String name = realization.getName() != null ? realization.getName() : "";
		
		return new RealizationRelationship(clientElement, supplierElement, name);
	}
	
}