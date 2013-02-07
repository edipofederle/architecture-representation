package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.assertEquals;
import mestrado.arquitetura.factories.Klass;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.helpers.StereotypeHelper;

import org.eclipse.uml2.uml.Classifier;
import org.junit.Test;

public class StereotypeHelperTest extends TestHelper {

	@Test
	public void shouldReturnTrueIfIsVariantPointClass() throws ModelNotFoundException , ModelIncompleteException {

		Classifier a = Klass.create()
				            .withName("Car")
							.withStereotypes("variationPoint").getObject();
		
		boolean result = StereotypeHelper.isVariationPoint(a);
		assertEquals("isVariationPoint should return true", true, result);
	}
	
	@Test
	public void shouldReturnFalseIfIsNOTVariantPointClass() {

		Classifier a = Klass.create().getObject();
		boolean result = StereotypeHelper.isVariationPoint(a);
		
		assertEquals("isVariationPoint should return false", false, result);
	}
	
	@Test
	public void shouldReturnTrueIfIsConcern() throws ModelNotFoundException , ModelIncompleteException{
		Classifier a = Klass.create()
	            .withName("Car")
				.withStereotypes("concern").getObject();
		
		boolean result = StereotypeHelper.isConcern(a);
		
		assertEquals("isConcern should return false", true, result);
	}
	
	@Test
	public void shouldReturnFalseIfIsNotConcern() throws ModelNotFoundException , ModelIncompleteException{
		Classifier a = Klass.create()
	            .withName("Car")
				.withStereotypes("interface").getObject();
		
		boolean result = StereotypeHelper.isConcern(a);
		
		assertEquals("isConcern should return false", false, result);
	}

}