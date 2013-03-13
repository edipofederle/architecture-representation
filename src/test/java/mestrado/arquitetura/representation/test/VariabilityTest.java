package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.representation.Variability;

import org.junit.Before;
import org.junit.Test;

public class VariabilityTest {
	
	private Map<String, String> attributes;
	
	@Before
	public void setUp(){
		attributes = attributes();
	}
	
	@Test
	public void shouldCreateVariability(){
		Variability v = new Variability("var", "1", "1", false, attributes, null);
		
		assertNotNull(v);
	}
	
	private Map<String, String> attributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("name", "var");
		return attributes;
	}

}
