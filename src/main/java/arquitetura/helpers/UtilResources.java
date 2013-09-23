package arquitetura.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class UtilResources {
	
	public static String getUrlToModel(String modelName) {
		return "src/test/java/resources/" + modelName + ".uml";
	}
	
	public static String capitalize(String original){
		original = original.trim().toLowerCase();
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
	@SuppressWarnings("unchecked")
	public static <T, U extends T> List<U> filter(List<T> target, Predicate<T> predicate) {
		List<U> result = new ArrayList<U>();
		for (T element: target) 
			if (predicate.apply(element)) 
				result.add((U) element);
		return result;
	}

	/**
	 * Retorna somente o nome do elemento dado o namesapce
	 * 
	 * Ex: model::Package1, reotnra Package1
	 * @param klass
	 * @return
	 */
	public static String extractPackageName(String namespace) {
		if(namespace == null) return "";
		String name = namespace.substring(namespace.lastIndexOf("::")+2, namespace.length());
		return name != null ? name : "";
	}

	public static String getRandonUUID() {
		return UUID.randomUUID().toString();
	}
	
	public static String createNamespace(String architectureName, String name) {
		String nsp = architectureName + "::" + name;
		return nsp != null ? nsp : "";
	}

}