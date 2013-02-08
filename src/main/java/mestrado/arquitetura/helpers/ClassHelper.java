package mestrado.arquitetura.helpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.PackageableElement;

public class ClassHelper extends ElementHelper {

	public static List<Classifier> getAllAttributesForAClass(PackageableElement aClass) {
		List<Classifier> allPropertys = new ArrayList<Classifier>();
		allPropertys = getAllElementsByType(aClass, ElementsTypes.PROPERTY);
		return allPropertys;
	}

	public static List<Classifier> getAllMethodsForAClass(
			PackageableElement aClass) {
		List<Classifier> allOperations = new ArrayList<Classifier>();
		allOperations = getAllElementsByType(aClass, ElementsTypes.OPERATION);
		return allOperations;
	}

}