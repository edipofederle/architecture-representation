package mestrado.arquitetura.writer;

import static org.junit.Assert.*;

import java.util.Map;

import mestrado.arquitetura.parser.ParameterParser;

import org.junit.Test;

public class ParameterParserTest {
	
	@Test
	public void shouldReturnAMapWithParams(){
		String methodName = "foo[name:String]";
		assertNotNull(ParameterParser.getParams(methodName));
		Map<String, String> mapParams = ParameterParser.getParams(methodName);
		assertEquals("String", mapParams.get("name"));
	}
	
	@Test
	public void shouldReturnAMapWithParams2(){
		String methodName = "foo[name:String, age:Integer]";
		assertNotNull(ParameterParser.getParams(methodName));
		Map<String, String> mapParams = ParameterParser.getParams(methodName);
		
		assertEquals("String", mapParams.get("name"));
		assertEquals("Integer", mapParams.get("age"));
	}
	
	@Test
	public void shouldReturnAMapWithParams3(){
		String methodName = "foo[name:String, age:Integer, email:String]";
		assertNotNull(ParameterParser.getParams(methodName));
		Map<String, String> mapParams = ParameterParser.getParams(methodName);
		
		assertEquals("String", mapParams.get("name"));
		assertEquals("Integer", mapParams.get("age"));
		assertEquals("String", mapParams.get("email"));
	}


}
