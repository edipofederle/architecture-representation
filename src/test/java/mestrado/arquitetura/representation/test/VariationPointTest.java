package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.VariationPoint;

import org.junit.Test;

/**
 * 
 * @author edipofederle
 *
 */
public class VariationPointTest {

	@Test
	public void voidShouldCreateVariationPoint(){
		
		
		VariationPoint vp = createVariationPoint();
		
		assertNotNull(vp);
		assertEquals("Class", vp.getVariationPointElement().getName());
		assertEquals(2, vp.getVariants().size());
	}

	private VariationPoint createVariationPoint() {
		Element element = mock(Element.class);
		when(element.getName()).thenReturn("Class");
		
		Element element1 = mock(Element.class);
		Element element2 = mock(Element.class);
		when(element1.getName()).thenReturn("Class");
		when(element2.getName()).thenReturn("Class2");
		
		List<Element> variants = new ArrayList<Element>();
		variants.add(element1); variants.add(element2);
		VariationPoint vp = new VariationPoint(element, variants);
		return vp;
	}
	
	@Test(expected=InvalidParameterException.class)
	public void shouldInvalidParameterWhenCreateInvalidVariationPoint(){
		createVariationPointInvalid();
	}

	private void createVariationPointInvalid() {
		Element element1 = mock(Element.class);
		Element element2 = mock(Element.class);
		
		List<Element> variants = new ArrayList<Element>();
		variants.add(element1); variants.add(element2);
		new VariationPoint(null,variants);
	}
	
	@Test
	public void shouldToString(){
		VariationPoint vp = createVariationPoint();
		
		assertEquals("Variants: Class, Class2", vp.toString());
	}
	
}
