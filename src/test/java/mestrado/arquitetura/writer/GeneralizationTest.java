package mestrado.arquitetura.writer;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.api.touml.DocumentManager;
import arquitetura.api.touml.Operations;
import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.GeneralizationRelationship;

public class GeneralizationTest extends TestHelper {
	
	@Test
	public void shouldCreateGeneralization() throws Exception{
		DocumentManager doc = givenADocument("generalization1");
		Operations op = new Operations(doc);
		
		Map<String, String> employee = op.forClass().createClass("Employee").build();
		Map<String, String> manager = op.forClass().createClass("Casa").build();
		
		op.forGeneralization()
		  .createRelation("Generalization #1")
		  .between(employee.get("id"))
		  .and(manager.get("id")).build();
		
		Architecture arch = givenAArchitecture2("generalization1");
		
		assertEquals(1,arch.getAllGeneralizations().size());
		GeneralizationRelationship g = arch.getAllGeneralizations().get(0);
		assertEquals("Casa",g.getParent().getName());
		assertEquals("Employee",g.getChild().getName());
	}
	
	
	@Test
	public void shouldCreateGeneralization2() throws Exception{
		DocumentManager doc = givenADocument("generalization2");
		Operations op = new Operations(doc);
		
		Map<String, String> post = op.forClass().createClass("Post").build();
		Map<String, String> comment = op.forClass().createClass("Comment").build();
		Map<String, String> user = op.forClass().createClass("User").build();
		
		op.forGeneralization()
		  .createRelation("Generalization #2")
		  .between(post.get("id"))
		  .and(comment.get("id")).build();
		
		op.forGeneralization()
		  .createRelation("Generalization #1")
		  .between(user.get("id"))
		  .and(comment.get("id")).build();
		
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
	public void shouldCreateGeneralizationClassPackageClass() throws Exception{
		DocumentManager doc = givenADocument("generalization3");
		Operations op = new Operations(doc);
		

		String post = op.forClass().createClass("Post").build().get("id");
		String category = op.forClass().createClass("Category").build().get("id");
		op.forPackage().createPacakge("Pacote1").withClass(post).build();
		
		op.forGeneralization()
		  .createRelation("Ge")
		  .between(post).and(category).build();
	}
	
	@Test(expected=NotSuppportedOperation.class)
	public void shouldNotAllowGeneralizationBetweenPackages() throws Exception{
		DocumentManager doc = givenADocument("generalizationPacotes");
		Operations op = new Operations(doc);
		
		String id1 = op.forPackage().createPacakge("Pacote1").build().get("packageId");
		String id2 = op.forPackage().createPacakge("Pacote2").build().get("packageId");
			
		op.forGeneralization().createRelation("G invalida").between(id1).and(id2);
	}
	
	@Test(expected=NotSuppportedOperation.class)
	public void shouldNotAllowGeneralizationBetweenPackages2() throws Exception{
		DocumentManager doc = givenADocument("generalizationPacotes2");
		Operations op = new Operations(doc);
		
		String id1 = op.forPackage().createPacakge("Pacote1").build().get("packageId");
		String post = op.forClass().createClass("Post").build().get("id");
			
		op.forGeneralization().createRelation("G invalida").between(post).and(id1);
	}

}