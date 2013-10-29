package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.builders.ClassBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;

public class ClassTest extends TestHelper {
	
	private arquitetura.representation.Class class1, class2;
	

	@Before
	public void setUp()	throws Exception {
		
		Package model = modelHelper.getModel("src/test/java/resources/completeClass.uml");
		Class klass = modelHelper.getAllClasses(model).get(0);
		Class klass2 = modelHelper.getAllClasses(model).get(1);
		
		Architecture architecture = Mockito.mock(Architecture.class);
		Mockito.when(architecture.getName()).thenReturn("MyArch");
		ClassBuilder classBuilder = new ClassBuilder(architecture);
		class1 = classBuilder.create(klass);
		class2 = classBuilder.create(klass2);
		
	}
	
	@Test
	public void shouldHaveAName() throws Exception {
		assertNotNull(class1);
		assertNotNull("Class1",class1.getName());
	}
	
	@Test
	public void shouldHaveAAttribute() throws Exception{
		assertEquals(1, class1.getAllAttributes().size());
	}
	
	@Test
	public void shouldHaveAMethod() throws Exception{
		assertEquals(1, class1.getAllMethods().size());
	}
	
	@Test
	public void shouldRemoveAAttribute() throws Exception{
		assertEquals(1, class1.getAllAttributes().size());
		
		arquitetura.representation.Attribute att = class1.getAllAttributes().get(0);
		assertTrue(class1.removeAttribute(att));
		
		assertEquals(0, class1.getAllAttributes().size());
	}
	
	@Test
	public void shouldReturnsFalseWhenTryRemoveAttributeNotExistOnClass(){
		arquitetura.representation.Attribute att = class1.getAllAttributes().get(0);
		assertFalse(class2.removeAttribute(att));
	}
	
	@Test
	public void shouldMoveAttribute(){
		assertEquals(3, class2.getAllAttributes().size());
		
		arquitetura.representation.Attribute att = class1.getAllAttributes().get(0);
		assertTrue(class1.moveAttributeToClass(att, class2));
		
		assertEquals(4, class2.getAllAttributes().size());
		assertEquals(0, class1.getAllAttributes().size());
		
		assertEquals("model::Class2", class2.getAllAttributes().get(2).getNamespace());
	}
	
	@Test
	public void shouldReturnsFalseWhenTryMoveAttributeNotExistOnClass(){
		arquitetura.representation.Attribute att = class1.getAllAttributes().get(0);
		assertFalse(class2.moveAttributeToClass(att, class1));
	}
	
	
	@Test
	public void shouldRemoveAMethod(){
		assertEquals(1, class1.getAllMethods().size());
		Method method = class1.getAllMethods().get(0);
		
		assertTrue(class1.removeMethod(method));
		assertEquals(0, class1.getAllMethods().size());
	}
	
	@Test
	public void shouldMoveAMethod(){
		assertEquals(1, class1.getAllMethods().size());
		
		Method method = class1.getAllMethods().get(0);
		assertTrue(class1.moveMethodToClass(method, class2));
		
		assertEquals(0, class1.getAllMethods().size());
		assertEquals(1, class2.getAllMethods().size());
	}
	
	@Test
	public void shouldReturnFalseWhenTryRemoveMothodNotExists(){
		Method method = class1.getAllMethods().get(0);
		
		assertFalse(class2.removeMethod(method));
		assertEquals(1, class1.getAllMethods().size());
	}
	
	@Test
	public void shouldReturnFalseWhenTryMoveMothodNotExists(){
		Method method = class1.getAllMethods().get(0);
		assertFalse(class2.moveMethodToClass(method, class1));
	}
	
	@Test
	public void shouldNotAddConcernWhenNotExists() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		
		assertEquals(1, klass1.getOwnConcerns().size());
		assertFalse(klass1.addConcern("foo666"));
		assertEquals(1, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void shouldAddConcernToClass() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		
		assertEquals(1, klass1.getOwnConcerns().size());
		assertTrue(klass1.addConcern("play"));
		assertEquals(2, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void shouldRemoveConcernFromClass() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		
		assertEquals(1, klass1.getOwnConcerns().size());
		klass1.removeConcern("action");
		assertEquals(0, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void shouldNotRemoveConcernFromClassWhenNotExists() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		assertEquals(1, klass1.getOwnConcerns().size());
		
		klass1.removeConcern("xpto");
		assertEquals(1, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void testGetImplementedInterfaces() throws Exception {
		Architecture a = givenAArchitecture("classRealizationInterface");
		
		arquitetura.representation.Class klass1 = a.findClassByName("Class2").get(0);
		
		assertEquals("Deve retornar 2 interfaces", 2, klass1.getImplementedInterfaces().size());
	}
	
	@Test
	public void testGetRequiredInterfaces() throws Exception{
		Architecture a = givenAArchitecture("classInterface/classrealizationInterface");
		arquitetura.representation.Class klass1 = a.findClassByName("Class2").get(0);
		
		List<Interface> requiredInterface = klass1.getRequiredInterfaces();
		
		assertNotNull(requiredInterface);
		assertEquals(1,requiredInterface.size());
		assertEquals("Class1", requiredInterface.get(0).getName());
		
	}
	
}