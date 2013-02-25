package mestrado.arquitetura.helpers;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;

public class Uml2HelperFactory {
	
	private static Uml2Helper instance;
	
	public static Uml2Helper getUml2Helper() throws ModelNotFoundException, ModelIncompleteException{
		if (instance == null){
			instance = Uml2Helper.getInstance();
			instance.setSMartyProfile();
		}
		return instance;
	}

}