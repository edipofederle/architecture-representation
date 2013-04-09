package mestrado.arquitetura.writer;

import static org.junit.Assert.*;

import mestrado.arquitetura.parser.DocumentManager;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void shouldCreateADocument(){
		String pathToFiles = "src/main/java/mestrado/arquitetura/parser/";
		String originalModelName  = "simples";
		DocumentManager documentManager = new DocumentManager(pathToFiles, originalModelName);
		
		assertNotNull(documentManager);
		assertNotNull(documentManager.getDocNotation());
		assertNotNull(documentManager.getDocUml());
	}

}
