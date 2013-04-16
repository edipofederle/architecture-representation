package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		
		assertEquals("false",ParameterParser.isAbstract(methodName));
	}
	
	@Test
	public void shouldReturnAMapWithParams2(){
		String methodName = "foo[name:String, age:Integer]";
		assertNotNull(ParameterParser.getParams(methodName));
		Map<String, String> mapParams = ParameterParser.getParams(methodName);
		
		assertEquals("String", mapParams.get("name"));
		assertEquals("Integer", mapParams.get("age"));
		
		assertEquals("false",ParameterParser.isAbstract(methodName));
	}
	
	@Test
	public void shouldReturnAMapWithParams3(){
		String methodName = "foo[name:String, age:Integer, email:String]";
		assertNotNull(ParameterParser.getParams(methodName));
		Map<String, String> mapParams = ParameterParser.getParams(methodName);
		
		assertEquals("String", mapParams.get("name"));
		assertEquals("Integer", mapParams.get("age"));
		assertEquals("String", mapParams.get("email"));
		
		assertEquals("false",ParameterParser.isAbstract(methodName));
	}
	
	@Test
	public void shouldReturnIfAMethodIsAbstract(){
		String methodName = "foo[name:String]:abstract";
		assertNotNull(ParameterParser.getParams(methodName));
		
		assertEquals("true",ParameterParser.isAbstract(methodName));
		
	}


}
