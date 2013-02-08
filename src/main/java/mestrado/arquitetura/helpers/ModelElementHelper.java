package mestrado.arquitetura.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;

public class ModelElementHelper {

	public static List<Stereotype> getAllStereotypes(Classifier klass) {
		List<Stereotype> stereotypes = new ArrayList<Stereotype>();
		
		for(Stereotype stereotype : klass.getAppliedStereotypes())
			stereotypes.add(stereotype);
		
		if (stereotypes.isEmpty()) return Collections.emptyList();
		
		return stereotypes; 
	}
	
	public static boolean isInterface(Classifier klass){
		return StereotypeHelper.hasStereotype(klass, StereotypesTypes.INTERFACE);
	}

	public static boolean isClass(Classifier klass) {
		if (isInterface(klass))	return false;
		return klass.eClass().equals(UMLPackage.Literals.CLASS) ? true : false;
	}
	
}