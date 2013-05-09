package mestrado.arquitetura.helpers;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;

/**
 * 
 * @author edipofederle
 *
 */
public class ModelHelperFactory {
	
	private static ModelHelper instance;
	
	public static ModelHelper getModelHelper(){
		if (instance == null)
			try {
				instance = new ModelHelper();
			} catch (ModelNotFoundException e) {
				e.printStackTrace();
			} catch (ModelIncompleteException e) {
				e.printStackTrace();
			}
		
		return instance;
	}
}