package mestrado.arquitetura.writer;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;

import org.junit.Test;

public class AggregationTest extends TestHelper {
	
	@Test
	public void shouldCreateAggregationClass() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		DocumentManager doc = givenADocument("shared", "simples");
		Operations op = new Operations(doc);
		
		String bar =  op.forClass().createClass("foo").build().get("classId");
		String foo = op.forClass().createClass("bar").build().get("classId");
		
		op.forAggregation().createRelation("Nome").between(bar).and(foo).build();
	}

}