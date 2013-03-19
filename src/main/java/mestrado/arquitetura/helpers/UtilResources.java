package mestrado.arquitetura.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mestrado.arquitetura.representation.Class;

/**
 * 
 * @author edipofederle
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
		for (T element: target) {
			if (predicate.apply(element)) {
				result.add((U) element);
			}
		}
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
		final Matcher matcher = Pattern.compile("::").matcher(namespace);
		if(matcher.find()){
		    return namespace.substring(matcher.end()).trim();
		}
		return "";
	}


}