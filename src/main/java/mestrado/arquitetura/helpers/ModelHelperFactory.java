package mestrado.arquitetura.helpers;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;

public class ModelHelperFactory {
	
	private static ModelHelper instance;
	
	public static ModelHelper getModelHelper() throws ModelNotFoundException, ModelIncompleteException{
		if (instance == null)
			instance = new ModelHelper();
		return instance;
	}
}
