package mestrado.arquitetura.writer;

import static org.junit.Assert.*;

import mestrado.arquitetura.parser.DocumentManager;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void shouldCreateADocument(){
		String pathToFiles = "src/main/java/mestrado/arquitetura/parser/1/";
		String originalModelName  = "simples";
		String newModelName  = "simplesNew";
		DocumentManager documentManager = new DocumentManager(newModelName, pathToFiles, originalModelName);
		
		assertNotNull(documentManager);
		assertNotNull(documentManager.getDocNotation());
		assertNotNull(documentManager.getDocUml());
	}

}
