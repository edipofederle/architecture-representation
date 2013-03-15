package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.DependencyPackageInterfaceRelationship;
import mestrado.arquitetura.representation.InterElementRelationship;
import mestrado.arquitetura.representation.Package;

import org.eclipse.uml2.uml.Dependency;

/**
 * 
 * @author edipofederle
 *
 */
public class DependencyPackageInterfaceRelationshipBuilder {
	
	
	private PackageBuilder packageBuilder;
	private ClassBuilder classBuilder;
	private static ModelHelper modelHelper;

	static{
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	

	public DependencyPackageInterfaceRelationshipBuilder(PackageBuilder packageBuilder, ClassBuilder classBuilder) {
		this.packageBuilder = packageBuilder;
		this.classBuilder = classBuilder;
	}

	public InterElementRelationship create(Dependency dependency) {
		 
		String clientId   = modelHelper.getXmiId(dependency.getClients().get(0));
		String supplierId = modelHelper.getXmiId(dependency.getSuppliers().get(0));
		
		
		Package client = packageBuilder.getElementByXMIID(clientId);
		Class supplier = classBuilder.getElementByXMIID(supplierId);
				
		return new DependencyPackageInterfaceRelationship(supplier, client);
	}

}