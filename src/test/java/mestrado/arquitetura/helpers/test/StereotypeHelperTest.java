package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import mestrado.arquitetura.factories.Klass;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.helpers.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.StereotypeHelper;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

public class StereotypeHelperTest extends TestHelper {

	@Test
	public void shouldReturnTrueIfIsVariantPointClass() throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion {

		Classifier a = Klass.create()
				            .withName("Car")
							.withStereotypes("variationPoint").build();
		
		boolean result = StereotypeHelper.isVariationPoint(a);
		assertEquals("isVariationPoint should return true", true, result);
	}
	
	@Test
	public void shouldReturnFalseIfIsNOTVariantPointClass() {

		Classifier a = Klass.create().build();
		boolean result = StereotypeHelper.isVariationPoint(a);
		
		assertEquals("isVariationPoint should return false", false, result);
	}
	
	@Test
	public void shouldReturnTrueIfIsConcern() throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Classifier a = Klass.create()
	            .withName("Car")
				.withStereotypes("concern").build();
		
		boolean result = StereotypeHelper.isConcern(a);
		
		assertEquals("isConcern should return false", true, result);
	}
	
	@Test
	public void shouldReturnFalseIfIsNotConcern() throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Classifier a = Klass.create()
	            .withName("Car")
				.withStereotypes("interface").build();
		
		boolean result = StereotypeHelper.isConcern(a);
		
		assertEquals("isConcern should return false", false, result);
	}
	
	@Test
	public void shouldReturnTrueIfIsVariability() throws ModelNotFoundException, ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion , SMartyProfileNotAppliedToModelExcepetion{
		String uri = getUrlToModel("variability");
		String absolutePath = new File(uri).getAbsolutePath();
		Package model = uml2Helper.load(absolutePath);
		Classifier klass = modelHelper.getAllClasses(model).get(0);
		assertNotNull(klass);
		assertEquals("Class1", klass.getName());
		assertTrue(StereotypeHelper.isVariability(klass));
	}
	
	@Test
	public void shouldReturnFalseIfIsNotVariability() throws ModelNotFoundException, ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Classifier a = Klass.create()
	            .withName("game")
				.withStereotypes("interface").build();
		
		boolean result = StereotypeHelper.isVariability(a);
		
		assertEquals("isVariability should return false", false, result);
	}

}