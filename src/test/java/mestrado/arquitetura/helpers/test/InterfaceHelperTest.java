package                                                                                                                                                                                         mestrado.arquitetura.helpers.test;

import mestrado.arquitetura.helpers.InterfaceHelper;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InterfaceHelperTest extends TestHelper {
	
	private ModelHelper modelHelper; 
	
	@Before
	public void setUp(){
		modelHelper = ModelHelperFactory.getModelHelper();
	}
	
	@Test
	public void isInterfaceWhenClassWithStereotypeInterface(){
		Package model = givenAModel("interface");
		Classifier klass = modelHelper.getAllClasses(model).get(0);
		
		Assert.assertTrue("should be a interface.", InterfaceHelper.isInterface(klass));
	}
	
	@Test
	public void isNotInterfaceWhenClassWithoutStereotypeInterface(){
		Classifier klass = givenAClass();
		Assert.assertFalse("should NOT be a interface", InterfaceHelper.isInterface(klass));
	}

}