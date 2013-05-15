package mestrado.arquitetura.base;

import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;

public abstract class ArchitectureHelper {
	
	private static ModelHelper modelHelper;
	
	protected ModelHelper getModelHelper(){
		modelHelper = ModelHelperFactory.getModelHelper();
		return modelHelper;
	}

}