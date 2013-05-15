package mestrado.arquitetura.writer;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;

import org.junit.Test;

public class OperationsTest extends TestHelper {
	
	@Test
	public void test() throws Exception{
		DocumentManager doc = givenADocument("666", "simples");
		Operations op = new Operations(doc);
		op.forClass().createClass("ClassName");
	}

}