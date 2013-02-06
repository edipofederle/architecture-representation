package mestrado.arquitetura.helpers;

public class Uml2HelperFactory {
	
	private static Uml2Helper instance;
	
	public static Uml2Helper getUml2Helper(){
		if (instance == null)
			instance = new Uml2Helper();
		return instance;
	}

}