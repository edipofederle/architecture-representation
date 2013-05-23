package mestrado.arquitetura.writer;

import static org.junit.Assert.*;

import java.util.Arrays;

import mestrado.arquitetura.api.touml.BindingTime;
import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.api.touml.Operations;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;

import org.junit.Test;

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
		VariabilityStereotype a = new VariabilityStereotype("1", "3", false, BindingTime.DESIGN_TIME, Arrays.asList("teste"));
		
		op.forNote().addVariability(idNote, a ).build();
		
		op.forClass().createClass("Foo").linkToNote(idNote);
		
		Architecture arq = givenAArchitecture2("note");
		
		assertEquals(1, arq.getAllVariabilities().size());
	}

}