package mestrado.arquitetura.writer;

import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.api.touml.Operations;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

public class OperationsTest extends TestHelper {
	
	@Test
	public void test() throws Exception{
		DocumentManager doc = givenADocument("666");
		Operations op = new Operations(doc);
		op.forClass().createClass("ClassName");
	}

}