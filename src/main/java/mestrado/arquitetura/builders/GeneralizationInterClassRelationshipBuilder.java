package mestrado.arquitetura.builders;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.GeneralizationInterClassRelationship;
import mestrado.arquitetura.representation.InterClassRelationship;

import org.eclipse.uml2.uml.Generalization;

public class GeneralizationInterClassRelationshipBuilder {
	
	
	private static ModelHelper modelHelper;
	/**
	 *  Construtor. Initializa helpers.
	 */
	static{
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
	}
	
	private ClassBuilder classBuilder;

	public GeneralizationInterClassRelationshipBuilder(ClassBuilder classBuilder) {
		this.classBuilder = classBuilder;
	}

	public InterClassRelationship create(Generalization generalization) {
		String generalKlassId = modelHelper.getXmiId(generalization.getGeneral());
		String specificKlassId = modelHelper.getXmiId(generalization.getSpecific());
		
		Class generalKlass = classBuilder.getElementByXMIID(generalKlassId);
		Class specificKlass = classBuilder.getElementByXMIID(specificKlassId);
		
		return new GeneralizationInterClassRelationship(generalKlass, specificKlass);
	}

}
