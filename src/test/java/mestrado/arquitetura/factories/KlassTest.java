package mestrado.arquitetura.factories;

import static org.junit.Assert.*;

import org.eclipse.uml2.uml.Classifier;
import org.junit.Test;

public class KlassTest {
	
	
	@Test
	public void shouldReturnAClass(){
		Classifier klass = Klass.create().getObject();
		assertNotNull(klass);
		assertEquals("classe1", klass.getName());
	}
	
	@Test
	public void shouldReturnAClassWithNewName(){
		Classifier klass = Klass.create().withName("Person").getObject();
		assertNotNull(klass);
		assertEquals("Person", klass.getName());
	}
	
	@Test
	public void shouldReturnAClassWithStereotypeMandatoryAndNewName(){
		Classifier klass = Klass.create().withName("Person").withStereotypes("mandatory").getObject();
		assertNotNull(klass);
		assertEquals("Person", klass.getName());
		assertEquals("mandatory", klass.getAppliedStereotypes().get(0).getName());
	}
	
	@Test
	public void shouldReturnAClassWithStereotypeVariationPointAndNewName(){
		Classifier klass = Klass.create().withName("Person2").withStereotypes("variationPoint").getObject();
		assertNotNull(klass);
		assertEquals("Person2", klass.getName());
		assertEquals("variationPoint", klass.getAppliedStereotypes().get(0).getName());
	}
	
	@Test
	public void shouldReturnAClasseWithTwoStereotypesApplied(){
		Classifier klass = Klass.create().withName("Car").withStereotypes("variationPoint", "mandatory", "interface").getObject();
		assertNotNull(klass);
		assertEquals("Car", klass.getName());
		assertTrue(klass.getAppliedStereotypes().size() == 3);
		assertEquals("variationPoint", klass.getAppliedStereotypes().get(0).getName());
		assertEquals("mandatory", klass.getAppliedStereotypes().get(1).getName());
		assertEquals("interface", klass.getAppliedStereotypes().get(2).getName());
	}
	
}