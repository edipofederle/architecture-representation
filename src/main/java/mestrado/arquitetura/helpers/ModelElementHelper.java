package mestrado.arquitetura.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

public class ModelElementHelper {

	public static <T> List<Stereotype> getAllStereotypes(NamedElement klass) {
		List<Stereotype> stereotypes = new ArrayList<Stereotype>();
		
		for(Stereotype stereotype : klass.getAppliedStereotypes())
			stereotypes.add(stereotype);
		
		if (stereotypes.isEmpty()) return Collections.emptyList();
		
		return stereotypes; 
	}
	
	public static boolean isInterface(NamedElement klass){
		return StereotypeHelper.hasStereotype(klass, StereotypesTypes.INTERFACE);
	}

	public static boolean isClass(NamedElement klass) {
		if (isInterface(klass))	return false;
		return true;
	}
	
}