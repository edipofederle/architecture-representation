package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.RealizationInterClassRelationship;

import org.eclipse.uml2.uml.Realization;

/**
 * 
 * @author edipofederle
 *
 */
public class RealizationInterClassRelationshipBuilder {

	private ClassBuilder classBuilder;
	private PackageBuilder packageBuilder;
	private static ModelHelper modelHelper;
	
	static {
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}

	public RealizationInterClassRelationshipBuilder(ClassBuilder classBuilder, PackageBuilder packageBuilder) {
		this.classBuilder = classBuilder;
		this.packageBuilder = packageBuilder;
	}

	public RealizationInterClassRelationship create(Realization realization) {
		
		String idClient = modelHelper.getXmiId(realization.getClients().get(0));
		String idSupplier = modelHelper.getXmiId(realization.getSuppliers().get(0));
		
		Element clientElement;
		Element supplierElement;
		
		clientElement = classBuilder.getElementByXMIID(idClient);
		supplierElement = classBuilder.getElementByXMIID(idSupplier);
		
		if( (supplierElement == null) && (clientElement != null)){
			supplierElement = packageBuilder.getElementByXMIID(idSupplier);
		}else if((clientElement == null) && (supplierElement != null)){
			clientElement = packageBuilder.getElementByXMIID(idClient);
		}
		
		String name = realization.getName() != null ? realization.getName() : "";
		
		return new RealizationInterClassRelationship(clientElement, supplierElement, name);
	}
	
}