package mestrado.arquitetura.writer;

import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.NodeNotFound;

import org.junit.Test;

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