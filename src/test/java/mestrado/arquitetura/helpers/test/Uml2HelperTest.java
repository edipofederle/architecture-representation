package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.helpers.EnumerationNotFoundException;
import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Before;
import org.junit.Test;
 
public class Uml2HelperTest  extends TestHelper{
	
	private static Uml2Helper uml2Helper;
	private static ModelHelper modelHelper;
	
	@Before
	public void setUp() throws ModelNotFoundException, ModelIncompleteException{
		uml2Helper = Uml2HelperFactory.getUml2Helper();
		modelHelper = ModelHelperFactory.getModelHelper();
	}
	
	@Test(expected=ModelNotFoundException.class)
	public void shouldRaiseExecptioWhenModelNotFound() throws ModelNotFoundException , ModelIncompleteException{
		URI uri = URI.createFileURI("modeloNaoExiste.uml");
		uml2Helper.load(uri.toString());
	}
	
	@Test(expected=ModelIncompleteException.class)
	public  void shouldRaiseModelNotFoundExceptionWhenModelIncomplete() throws ModelNotFoundException , ModelIncompleteException{
		URI uri = URI.createFileURI(getUriToResource("modelIncompleto"));
		uml2Helper.load(uri.toString());
	}
	
	@Test
	public void shouldGetEnumByName() throws ModelNotFoundException, ModelIncompleteException, EnumerationNotFoundException{
		Profile profile = (Profile) modelHelper.getModel(getUriToResource("smartyProfile"));
		PackageableElement enumm = uml2Helper.getEnumerationByName(profile, "BindingTime");
		assertTrue(enumm.eClass().equals(UMLPackage.Literals.ENUMERATION));
	}
	
	@Test
	public void shouldGetEnumLiteralByName() throws ModelNotFoundException, ModelIncompleteException, EnumerationNotFoundException{
		 EnumerationLiteral a = uml2Helper.getLiteralEnumeration("DESIGN_TIME");
		 assertNotNull(a);
		 assertEquals("DESIGN_TIME", a.getName());
		
	}

}