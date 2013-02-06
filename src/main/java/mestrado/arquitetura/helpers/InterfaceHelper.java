package mestrado.arquitetura.helpers;

import org.eclipse.uml2.uml.Classifier;



public class InterfaceHelper {

	
	public static boolean isInterface(Classifier klass){
		return StereotypeHelper.hasStereotype(klass, StereotypesTypes.INTERFACE);
	}
	

}
