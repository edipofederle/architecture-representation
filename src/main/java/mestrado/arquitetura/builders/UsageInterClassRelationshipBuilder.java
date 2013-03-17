package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.UsageInterClassRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

public class UsageInterClassRelationshipBuilder {
	
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


	public UsageInterClassRelationshipBuilder(ClassBuilder classBuilder, PackageBuilder packageBuilder){
		this.classBuilder = classBuilder;
		this.packageBuilder = packageBuilder;
	}


	public UsageInterClassRelationship create(Usage element) {
		
		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
		Element client;
		Element supplier;
		
		client = classBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));
		supplier = classBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));
		
		if ((client == null) && (supplier != null)){
			client = packageBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));
		}else if ((supplier == null) && (client != null)){
			supplier = packageBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));
		}else if ((supplier == null) && (client == null)){
			client = packageBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));
			supplier = packageBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));
		}
		
		return new UsageInterClassRelationship(element.getName(), supplier, client);
	}

}
