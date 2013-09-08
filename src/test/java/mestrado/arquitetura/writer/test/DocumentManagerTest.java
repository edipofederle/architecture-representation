package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import arquitetura.exceptions.CustonTypeNotFound;
import arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.NodeNotFound;
import arquitetura.touml.DocumentManager;

public class DocumentManagerTest {
	
	@Test
	public void shouldCreateADocument() throws ModelNotFoundException, ModelIncompleteException{
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
		//TODO Asserções
	}

}