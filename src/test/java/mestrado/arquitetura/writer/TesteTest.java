package mestrado.arquitetura.writer;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.ClassOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.PackageOperation;

import org.junit.Test;
import org.w3c.dom.Node;

public class TesteTest extends TestHelper {
	
	@Test
	public void shouldCreateAAbstractClassInsideAPackage() throws Exception{
		DocumentManager document = givenADocument("classAbstrataDentroPacote", "simples");
		PackageOperation packageOperations = new PackageOperation(document);
		ClassOperations classOperations = new ClassOperations(document);
		
		Node klass = classOperations.createClass("Person").build();
		
		packageOperations.createPacakge("fooPkg").withClass(klass);
		
		
	}

}
