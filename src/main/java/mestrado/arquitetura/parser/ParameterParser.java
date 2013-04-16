package mestrado.arquitetura.parser;

import java.util.HashMap;
import java.util.Map;


/**
 * Return a HashMap with parsed params method names.
 * <br/><br />
 * <b>Input:</b> foo[name:String, age:Integer]";
 * 
 * @author edipofederle
 *
 */
public class ParameterParser {

	
	public static Map<String, String> getParams(String methodName) {
		Map<String, String> mapParams = new HashMap<String, String>();
		
		String p = methodName.substring(methodName.indexOf("[")+1, methodName.length()-1);
		String tokens[] = p.split(",");
		
		for (int i = 0; i < tokens.length; i++){
			String token[] = tokens[i].split(":");
			for (int j = 0; j < token.length-1; j++) 
				mapParams.put(token[j].trim(), token[j+1].trim());
		}
		
		return mapParams;
	}

	public static String isAbstract(String methodName) {
		int index = methodName.lastIndexOf(":");
		
		if("abstract".equalsIgnoreCase(methodName.substring(index+1, methodName.length())))
			return "true";
		
		return "false";
	}
	
}