package mestrado.arquitetura.helpers;

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

}