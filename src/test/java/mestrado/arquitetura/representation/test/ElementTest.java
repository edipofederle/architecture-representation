package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;

public class ElementTest {
	
	private Element klass;
	
	@Before
	public void setUp(){
		klass = Mockito.mock(Class.class, Mockito.CALLS_REAL_METHODS);
		
		Concern con1 = new Concern("movement");
		Concern con2 = new Concern("play");
		Concern con3 = new Concern("pause");
		List<Concern> concerns = Arrays.asList(con1, con2, con3);
		
		Mockito.when(klass.getOwnConcerns()).thenReturn(concerns);
	}

	@Test
	public void containsConcernTestShouldReturnTrue(){
		Concern concern = new Concern("play");
		assertTrue(klass.containsConcern(concern));
	}

	@Test
	public void containsConcernTestShouldReturnFalse(){
		Concern concern = new Concern("xpto");
		assertFalse(klass.containsConcern(concern));
	}
	
}
