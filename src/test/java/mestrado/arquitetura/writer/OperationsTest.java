package mestrado.arquitetura.writer;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.api.touml.DocumentManager;
import arquitetura.api.touml.Operations;

public class OperationsTest extends TestHelper {
	
	@Test
	public void test() throws Exception{
		DocumentManager doc = givenADocument("666");
		Operations op = new Operations(doc);
		op.forClass().createClass("ClassName");
	}

}