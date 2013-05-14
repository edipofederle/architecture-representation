package mestrado.arquitetura.writer;

import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;

import org.junit.Test;

public class OperationsTest extends TestHelper {
	
	@Test
	public void test() throws NodeNotFound, InvalidMultiplictyForAssociationException{
		DocumentManager doc = givenADocument("666", "simples");
		Operations op = new Operations(doc);
		op.forClass().createClass("ClassName");
	}

}
