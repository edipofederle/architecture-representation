package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.touml.BindingTime;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;
import arquitetura.touml.VariabilityStereotype;

/**
 * Uma nota é um comentário da UML.
 * 
 * @author edipofederle
 *
 */
public class NotesTest extends TestHelper {
	
	private Element employee;
	private Element person;

	@Before
	public void setUp(){
		
		employee = Mockito.mock(Class.class);
		Mockito.when(employee.getName()).thenReturn("Employee");
		Mockito.when(employee.getId()).thenReturn("199339390");
		
		person = Mockito.mock(Class.class);
		Mockito.when(person.getName()).thenReturn("Person");
		Mockito.when(person.getId()).thenReturn("1993393123");
	}
	
	@Test
	public void shouldCreateANote() throws Exception{
		DocumentManager doc = givenADocument("note");
		Operations op = new Operations(doc, null);
		
		
		String idNote = op.forNote().createNote().build();
		VariabilityStereotype a = new VariabilityStereotype("Variabilidade1", "1", "2", false, BindingTime.DESIGN_TIME, "class1");
		
		op.forNote().addVariability(idNote, a ).build();
		
		op.forClass().createClass(employee).linkToNote(idNote);
		
		Architecture arq = givenAArchitecture2("note");
		
		assertEquals(1, arq.getAllVariabilities().size());
	}

}