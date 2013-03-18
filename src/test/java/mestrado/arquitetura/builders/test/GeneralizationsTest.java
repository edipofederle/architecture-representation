package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;

import org.junit.Before;
import org.junit.Test;

public class GeneralizationsTest extends TestHelper {
	
	private Architecture arch;
	
	@Before
	public void setUp() throws Exception{
		 arch = givenAArchitecture("generalizationArch");
	}
	
	@Test
	public void shouldLoadGeneralization() throws Exception {
		assertEquals("Should contains six classes", 6, arch.getClasses().size());

		assertEquals(3, arch.getAllGeneralizations().size());

		GeneralizationRelationship generalization = arch.getAllGeneralizations().get(0);
		assertNotNull(generalization);
		assertEquals("Person", generalization.getParent().getName());
		assertEquals(1, generalization.gelAllChildrenForGeneralClass().size());
		assertContains(generalization.gelAllChildrenForGeneralClass(),"Student");
	}
	
	@Test
	public void shouldReplaceChildClass() throws Exception {
		Class professorKlass = (Class) arch.findElementByName("Professor");
		Class class1 = (Class) arch.findElementByName("Child1");
		assertNotNull(class1);

		assertNotNull(professorKlass);
		assertEquals("Professor", professorKlass.getName());

		GeneralizationRelationship generalization = arch.getAllGeneralizations().get(1);

		assertContains(generalization.gelAllChildrenForGeneralClass(), "Child2");
		generalization.replaceChild(professorKlass);
		assertEquals(2, generalization.gelAllChildrenForGeneralClass().size());
		assertContains(generalization.gelAllChildrenForGeneralClass(), "Professor", "Child2");
	}
	

	@Test
	public void shouldReplaceAParentClass() throws Exception {
		Class professorKlass = (Class) arch.findElementByName("Professor");
		assertNotNull(professorKlass);
		assertEquals("Professor", professorKlass.getName());

		GeneralizationRelationship generalization = arch.getAllGeneralizations().get(0);

		assertEquals("Person", generalization.getParent().getName());
		generalization.replaceParent((Class) professorKlass);
		assertEquals("Professor", generalization.getParent().getName());
	}
	
	@Test
	public void shouldLoadGeneralizationWithTwoChildreen() {
		assertEquals(3, arch.getAllGeneralizations().size());
		assertEquals("Parent", arch.getAllGeneralizations().get(1).getParent().getName());
		List<Class> ch = (arch.getAllGeneralizations().get(1)).gelAllChildrenForGeneralClass();
		assertEquals(2, ch.size());
		assertContains(ch, "Child1", "Child2");
	}
	
	@Test
	public void givenAParentClassShouldReturnAllChildren() {
		assertEquals(6, arch.getClasses().size());

		Element parentKlass = arch.findElementByName("Parent");
		Class student = (Class) arch.findElementByName("Student");
		assertNotNull(parentKlass);
		assertNotNull(student);
		assertEquals("Parent", parentKlass.getName());

		GeneralizationRelationship r = arch.getAllGeneralizations().get(1);
		assertEquals(2, r.gelAllChildrenForGeneralClass().size());
		assertContains(r.gelAllChildrenForGeneralClass(), "Child1", "Child2");
		assertTrue("Children of " + r.getParent() + " should NOT contain Sudent Class", !r
				.gelAllChildrenForGeneralClass().contains(student));
	}
	
	@Test
	public void resursiveGeneralization() throws Exception {
		Architecture arch = givenAArchitecture("generalizationRecur");

		GeneralizationRelationship generalization = arch.getAllGeneralizations().get(0);
		GeneralizationRelationship generalization1 = arch.getAllGeneralizations().get(3);
		assertEquals("Class1", generalization.getParent().getName());
		assertContains(generalization.gelAllChildrenForGeneralClass(),"Class3", "Class2");

		assertEquals("Class2", generalization1.getParent().getName());
		assertContains(generalization1.gelAllChildrenForGeneralClass(),"Class4", "Class5");
		assertNotNull(arch);
	}
	
	



}
