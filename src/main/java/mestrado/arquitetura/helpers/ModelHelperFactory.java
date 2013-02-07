package mestrado.arquitetura.helpers;

public class ModelHelperFactory {
	
	private static ModelHelper instance;
	
	public static ModelHelper getModelHelper() throws ModelNotFoundException, ModelIncompleteException{
		if (instance == null)
			instance = new ModelHelper();
		return instance;
	}
}
