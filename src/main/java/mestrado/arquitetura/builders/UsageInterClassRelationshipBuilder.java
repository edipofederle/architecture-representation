package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.UsageInterClassRelationship;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

public class UsageInterClassRelationshipBuilder {
	
	private ClassBuilder classBuilder;
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


	public UsageInterClassRelationshipBuilder(ClassBuilder classBuilder){
		this.classBuilder = classBuilder;
	}


	public UsageInterClassRelationship create(Usage element) {
		
		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();

		Class client = classBuilder.getElementByXMIID(modelHelper.getXmiId(clieents.get(0)));
		Class supplier = classBuilder.getElementByXMIID(modelHelper.getXmiId(suppliers.get(0)));
		
		return new UsageInterClassRelationship(element.getName(), supplier, client);
	}

}
