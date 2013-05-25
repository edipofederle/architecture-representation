package mestrado.arquitetura.base;

import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public abstract class ArchitectureHelper {
	
	private static ModelHelper modelHelper;
	
	protected ModelHelper getModelHelper(){
		modelHelper = ModelHelperFactory.getModelHelper();
		return modelHelper;
	}

}