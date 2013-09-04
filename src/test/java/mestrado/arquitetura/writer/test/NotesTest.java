package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
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
	
	@Test
	public void shouldCreateANote() throws Exception{
		DocumentManager doc = givenADocument("note");
		Operations op = new Operations(doc);
		
		
		String idNote = op.forNote().createNote().build();
		VariabilityStereotype a = new VariabilityStereotype("Variabilidade1", "1", "2", false, BindingTime.DESIGN_TIME, "class1");
		
		op.forNote().addVariability(idNote, a ).build();
		
		op.forClass().createClass("Foo").linkToNote(idNote);
		
		Architecture arq = givenAArchitecture2("note");
		
		assertEquals(1, arq.getAllVariabilities().size());
	}

}