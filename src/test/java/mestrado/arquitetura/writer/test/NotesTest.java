package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Package;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
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
	public void shouldGenerateVariabilityIntoPackage() throws Exception{
		Architecture a = givenAArchitecture("varpacote");
		DocumentManager doc = givenADocument("varpacote_4");
		
		Operations op = new Operations(doc, a);
		
		for(Class c : a.getAllClasses()){
			op.forClass().createClass(c).build();
		}
		
		for(Package p : a.getAllPackages()){
			op.forPackage().createPacakge(p).withClass(p.getAllClassIdsForThisPackage()).build();
		}
		
		
		Variant v = null;
		
		Variant variant = a.getAllClasses().get(0).getVariant();
		if(variant != null){
			try{
				Element elementRootVp = a.findElementByName(variant.getRootVP(), "class");
				String rootVp = null;
				
				if(elementRootVp != null)
					rootVp = elementRootVp.getName();
				else
					rootVp = "";
				v = Variant.createVariant()
						   .withName(variant.getVariantName())
						   .andRootVp(rootVp)
						   .wihtVariabilities(variant.getVariabilities())
						   .withVariantType(variant.getVariantType()).build();
				
				//Se tem variant adicionar na classe
				if(v != null){
					op.forClass().addStereotype(a.getAllClasses().get(0).getId(), v);
				}
			
			}catch(Exception e){
				System.out.println("Error when try create Variant."+ e.getMessage());
				System.exit(0);
			}
		}
		//Variant Type
		
		List<Variability> variabilities = a.getAllVariabilities();
		String idOwner = "";
		for (Variability variability : variabilities) {
			VariationPoint variationPointForVariability = variability.getVariationPoint();
			if(variationPointForVariability == null){
				idOwner = a.findClassByName(variability.getOwnerClass()).get(0).getId();
			}else{
				idOwner = variationPointForVariability.getVariationPointElement().getId();
			}
			
			String idNote = op.forNote().createNote().build();
			VariabilityStereotype var = new VariabilityStereotype(variability);
			
			op.forNote().addVariability(idNote, var).build();
			op.forClass().withId(idOwner).linkToNote(idNote);
		}
		
		Architecture varpacote_3 = givenAArchitecture2("varpacote_4");
		assertNotNull(varpacote_3);
		assertEquals(1, varpacote_3.getAllVariabilities().size());
		assertEquals(1, varpacote_3.getAllClasses().size());
		
		Variability variability = varpacote_3.getAllVariabilities().get(0);
		assertEquals(1,variability.getVariants().size());
	}
	
	@Test
	public void shouldCreateANote() throws Exception{
		DocumentManager doc = givenADocument("note");
		Operations op = new Operations(doc, null);
		
		
		String idNote = op.forNote().createNote().build();
		Variability v = Mockito.mock(Variability.class);
		Mockito.when(v.getName()).thenReturn("Variabilidade1");
		Mockito.when(v.getMinSelection()).thenReturn("1");
		Mockito.when(v.getMaxSelection()).thenReturn("2");
		Mockito.when(v.allowAddingVar()).thenReturn(false);
		Mockito.when(v.getBindingTime()).thenReturn(BindingTime.DESIGN_TIME);
		
		Variant variant = Mockito.mock(Variant.class);
		Mockito.when(variant.getName()).thenReturn("class1");
		
		Mockito.when(v.getVariants()).thenReturn(Arrays.asList(variant));
		
		VariabilityStereotype a = new VariabilityStereotype(v);
		
		op.forNote().addVariability(idNote, a ).build();
		
		op.forClass().createClass(employee).linkToNote(idNote);
		
		Architecture arq = givenAArchitecture2("note");
		
		assertEquals(1, arq.getAllVariabilities().size());
	}


}