package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

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
	public void testUpdateRefsToProfiles() throws Exception{
		DocumentManager doc = new DocumentManager("saida");
		doc.updateProfilesRefs();
		//TODO Asserções
	}

}