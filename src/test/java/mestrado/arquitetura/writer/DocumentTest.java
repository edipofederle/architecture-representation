package mestrado.arquitetura.writer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import arquitetura.api.touml.DocumentManager;
import arquitetura.exceptions.CustonTypeNotFound;
import arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.NodeNotFound;

public class DocumentTest {
	
	@Test
	public void shouldCreateADocument() throws ModelNotFoundException, ModelIncompleteException{
		//TODO Move to conf file
		String newModelName  = "simplesNew";
		DocumentManager documentManager = new DocumentManager(newModelName);
		
		assertNotNull(documentManager);
		assertNotNull(documentManager.getDocNotation());
		assertNotNull(documentManager.getDocUml());
	}
	
	
	@Test
	public void testUpdateRefsToProfiles() throws ModelNotFoundException, ModelIncompleteException, CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		DocumentManager doc = new DocumentManager("saida");
		
		doc.updateProfilesRefs();
	}

}