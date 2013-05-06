package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;

import org.junit.Test;

public class GeneralizationTest extends TestHelper {
	
	@Test
	public void shouldCreateGeneralization() throws Exception{
		DocumentManager doc = givenADocument("generalization1", "simples");
		Operations op = new Operations(doc);
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forGeneralization()
		  .createRelation("Generalization #1")
		  .between(employee.get("classId"))
		  .and(manager.get("classId")).build();
		
		Architecture arch = givenAArchitecture2("generalization1");
		
		assertEquals(1,arch.getAllGeneralizations().size());
		GeneralizationRelationship g = arch.getAllGeneralizations().get(0);
		assertEquals("Casa",g.getParent().getName());
		assertEquals("Employee",g.getChild().getName());
	}
	
	
	@Test
	public void shouldCreateGeneralization2() throws Exception{
		DocumentManager doc = givenADocument("generalization2", "simples");
		Operations op = new Operations(doc);
		
		Map<String, String> post = op.forClass().createClass("Post").build();
		Map<String, String> comment = op.forClass().createClass("Comment").build();
		Map<String, String> user = op.forClass().createClass("User").build();
		
		op.forGeneralization()
		  .createRelation("Generalization #2")
		  .between(post.get("classId"))
		  .and(comment.get("classId")).build();
		
		op.forGeneralization()
		  .createRelation("Generalization #1")
		  .between(user.get("classId"))
		  .and(comment.get("classId")).build();
		
		Architecture arch = givenAArchitecture2("generalization2");
		
		assertEquals(2,arch.getAllGeneralizations().size());
		GeneralizationRelationship g = arch.getAllGeneralizations().get(0);
		assertEquals("Comment",g.getParent().getName());
		assertEquals("Post",g.getChild().getName());
		
		assertEquals(2, g.getAllChildrenForGeneralClass().size());
		assertEquals("Post", g.getAllChildrenForGeneralClass().get(0).getName());
		assertEquals("User", g.getAllChildrenForGeneralClass().get(1).getName());
	}
	
	@Test
	public void shouldCreateGeneralizationClassPackageClass() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		DocumentManager doc = givenADocument("generalization3", "simples");
		Operations op = new Operations(doc);
		

		String post = op.forClass().createClass("Post").build().get("classId");
		String category = op.forClass().createClass("Category").build().get("classId");
		op.forPackage().createPacakge("Pacote1").withClass(post).build();
		
		op.forGeneralization()
		  .createRelation("Ge")
		  .between(post).and(category).build();
		
	}

}