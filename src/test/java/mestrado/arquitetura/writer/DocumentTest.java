package mestrado.arquitetura.writer;

import static org.junit.Assert.*;

import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void shouldCreateADocument() throws ModelNotFoundException, ModelIncompleteException{
		//TODO Move to conf file
		String pathToFiles = "/Users/edipofederle/sourcesMestrado/arquitetura/src/main/java/mestrado/arquitetura/api/touml/1/";// model padrao vazio que o programa usa para construir o novo
		String newModelName  = "simplesNew";
		DocumentManager documentManager = new DocumentManager(newModelName, pathToFiles);
		
		assertNotNull(documentManager);
		assertNotNull(documentManager.getDocNotation());
		assertNotNull(documentManager.getDocUml());
	}

}