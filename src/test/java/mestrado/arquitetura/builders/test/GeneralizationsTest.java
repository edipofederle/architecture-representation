package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.GeneralizationRelationship;

public class GeneralizationsTest extends TestHelper {
	
	private Architecture arch;
	
	@Before
	public void setUp() throws Exception{
		 arch = givenAArchitecture("generalizationArch");
	}
	
	@Test
	public void shouldLoadGeneralization() throws Exception {
		Architecture arch1 = givenAArchitecture("replaceGeneralization");

		assertEquals(1, arch1.getAllGeneralizations().size());

		GeneralizationRelationship generalization = arch1.getAllGeneralizations().iterator().next();
		assertNotNull(generalization);
		assertEquals("Class1", generalization.getParent().getName());
		assertEquals(1, generalization.getAllChildrenForGeneralClass().size());
		assertContains(generalization.getAllChildrenForGeneralClass(),"Class2");
	}
	
	@Test
	public void shouldReplaceChildClass() throws Exception {
		Architecture a = givenAArchitecture("ReplaceGeneralization");
		Class class3 = a.findClassByName("Class3").get(0);
		assertNotNull(class3);

		GeneralizationRelationship generalization = a.getAllGeneralizations().iterator().next();

		assertContains(generalization.getAllChildrenForGeneralClass(), "Class2");
		generalization.replaceChild(class3);
		assertEquals(1, generalization.getAllChildrenForGeneralClass().size());
		assertContains(generalization.getAllChildrenForGeneralClass(), "Class3");
	}
	

	@Test
	public void shouldReplaceAParentClass() throws Exception {
		Class professorKlass = (Class) arch.findElementByName("Professor", "class");
		assertNotNull(professorKlass);
		assertEquals("Professor", professorKlass.getName());

		GeneralizationRelationship generalization = arch.getAllGeneralizations().iterator().next();

		assertEquals("Parent", generalization.getParent().getName());
		generalization.replaceParent((Class) professorKlass);
		assertEquals("Professor", generalization.getParent().getName());
	}
	
	@Test
	public void shouldLoadGeneralizationWithTwoChildreen() {
		
												
		GeneralizationRelationship g1 = arch.getAllGeneralizations().get(0);
		
		
		assertEquals(3, arch.getAllGeneralizations().size());
		assertEquals("Parent", g1.getParent().getName());
		Set<Element> ch = g1.getAllChildrenForGeneralClass();
		assertEquals(2, ch.size());
		assertContains(ch, "Child1", "Child2");
	};
	
	@Test
	public void givenAParentClassShouldReturnAllChildren() throws Exception {
		Architecture a = givenAArchitecture("generalizationTwoChild");


		GeneralizationRelationship r = a.getAllGeneralizations().iterator().next();
		assertEquals(2, r.getAllChildrenForGeneralClass().size());
		assertContains(r.getAllChildrenForGeneralClass(), "Class1", "Class2");
	}
	
	@Test
	public void resursiveGeneralization() throws Exception {
		Architecture arch1 = givenAArchitecture("generalizationRecur");

		GeneralizationRelationship generalization = arch1.getAllGeneralizations().get(0);
		GeneralizationRelationship generalization1 = arch1.getAllGeneralizations().get(3);

		
		
		
		assertEquals("Class2", generalization.getParent().getName());
		assertContains(generalization.getAllChildrenForGeneralClass(),"Class4", "Class5");

		assertEquals("Class1", generalization1.getParent().getName());
		assertContains(generalization1.getAllChildrenForGeneralClass(),"Class2", "Class2");
	}
	
	



}
