package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.*;

import java.util.List;

import mestrado.arquitetura.helpers.ClassHelper;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

public class ClassHelperTest extends TestHelper {
	
	 @Test
	 public void shouldReturnAllAttributesForAClass(){
		 PackageableElement aClass = givenAClass();
		 assertEquals("Address", aClass.getName());
		 List<Classifier> attrs = ClassHelper.getAllAttributesForAClass(aClass);
		 assertEquals(2, attrs.size());
		 assertEquals("country", ((Property)attrs.get(0)).getLabel());
		 assertEquals("String", ((Property) attrs.get(0)).getType().getName());
	 }
	
	 @Test 
	 public void shouldReturnAllMethodsForAClass(){
		 PackageableElement aClass = givenAClass();
		 List<Classifier> methods = ClassHelper.getAllMethodsForAClass(aClass);
		 assertEquals(1, methods.size());
		 assertEquals("foo", ((Operation)methods.get(0)).getName());
	 }
	 
	 
}
