package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import mestrado.arquitetura.helpers.ClassHelper;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

public class ClassHelperTest extends TestHelper {
	
	 @Test
	 public void shouldReturnAllAttributesForAClass() throws ModelNotFoundException , ModelIncompleteException{
		 PackageableElement aClass = givenAClass();
		 assertEquals("Address", aClass.getName());
		 List<Classifier> attrs = ClassHelper.getAllAttributesForAClass(aClass);
		 assertEquals(2, attrs.size());
		 assertEquals("country", ((Property)attrs.get(0)).getLabel());
		 assertEquals("String", ((Property) attrs.get(0)).getType().getName());
	 }
	
	 @Test 
	 public void shouldReturnAllMethodsForAClass() throws ModelNotFoundException , ModelIncompleteException{
		 PackageableElement aClass = givenAClass();
		 List<Classifier> methods = ClassHelper.getAllMethodsForAClass(aClass);
		 assertEquals(1, methods.size());
		 assertEquals("foo", ((Operation)methods.get(0)).getName());
	 }
	 
	 
}
