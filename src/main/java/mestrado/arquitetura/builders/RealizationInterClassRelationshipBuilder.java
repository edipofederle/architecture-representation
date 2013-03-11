package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.RealizationInterClassRelationship;

import org.eclipse.uml2.uml.Realization;

public class RealizationInterClassRelationshipBuilder {

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

	public RealizationInterClassRelationshipBuilder(ClassBuilder classBuilder) {
		this.classBuilder = classBuilder;
	}

	public RealizationInterClassRelationship create(Realization realization) {
		
		String idSource = modelHelper.getXmiId(realization.getSources().get(0));
		String idSpecific = modelHelper.getXmiId(realization.getSuppliers().get(0));
		
		Class sourceElement = classBuilder.getElementByXMIID(idSource);
		Class specificElement = classBuilder.getElementByXMIID(idSpecific);
		
		String name = realization.getName() != null ? realization.getName() : "";
		
		return new RealizationInterClassRelationship(sourceElement, specificElement, name);
	}
	
}