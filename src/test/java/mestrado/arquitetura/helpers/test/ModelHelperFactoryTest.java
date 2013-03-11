package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.*;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;

import org.junit.Test;

/**
 * 
 * @author edipofederle
 *
 */
public class ModelHelperFactoryTest {
	
	@Test
	public void shouldBeASingleton() throws ModelNotFoundException, ModelIncompleteException{
		ModelHelper modelHelper1 = ModelHelperFactory.getModelHelper();
		ModelHelper modelHelper2 = ModelHelperFactory.getModelHelper();
		
		assertSame(modelHelper1, modelHelper2);
		
	}

}
