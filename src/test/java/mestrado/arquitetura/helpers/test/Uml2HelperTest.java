package mestrado.arquitetura.helpers.test;

import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;

import org.eclipse.emf.common.util.URI;
import org.junit.Before;
import org.junit.Test;
 
public class Uml2HelperTest  extends TestHelper{
	
	private Uml2Helper uml2Helper;
	
	@Before
	public void setUp(){
		uml2Helper = Uml2HelperFactory.getUml2Helper();
	}
	
	@Test(expected=ModelNotFoundException.class)
	public void shouldRaiseExecptioWhenModelNotFound() throws ModelNotFoundException , ModelIncompleteException{
		URI uri = URI.createFileURI("modeloNaoExiste.uml");
		uml2Helper.load(uri.toString());
	}
	
	@Test(expected=ModelIncompleteException.class)
	public void shouldRaiseModelNotFoundExceptionWhenModelIncomplete() throws ModelNotFoundException , ModelIncompleteException{
		URI uri = URI.createFileURI(getUriToResource("modelIncompleto"));
		uml2Helper.load(uri.toString());
	}
	
	

}
