package mestrado.arquitetura.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Map;

import junit.framework.Assert;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.ClassOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.DocumentOperations;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class ModelManagerTest extends TestHelper {
	
	
	@Before @After
	public void setUp(){
		cleanManipulationFolder();
	}
	
	private void cleanManipulationFolder() {
		File file = new File("/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/");     
		File file2 = new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/");

        String[] myFiles2;    
        if(file.isDirectory()){
            myFiles2 = file2.list();
            for (int i=0; i<myFiles2.length; i++) {
                File myFile = new File(file, myFiles2[i]); 
                myFile.delete();
            }
         }
		
        String[] myFiles;    
            if(file.isDirectory()){
                myFiles = file.list();
                for (int i=0; i<myFiles.length; i++) {
                    File myFile = new File(file, myFiles[i]); 
                    myFile.delete();
                }
             }
	}
//
//	public void functionalTest() throws IOException, SAXException, ParserConfigurationException, TransformerException{
//	
//		String pathToFiles = "src/main/java/mestrado/arquitetura/parser/";
//		String modelName = "simples";
//
//		DocumentManager documentManager = new DocumentManager(pathToFiles, modelName);
//		DocumentOperations documentOperations = new DocumentOperations(documentManager);
//		Assert.assertNotNull(documentOperations);
//		
//		ClassOperations classOperations = new ClassOperations(documentManager);
//		classOperations.createClass("Home");
//
////		String idXPTO = documentOperations.createClass("XPTO");
////		String idClassBar = documentOperations.createClass("Bar");
////		
////		String person = documentOperations.createClass("Person");
////		String professor = documentOperations.createClass("Professor");
////		
////		String teste = documentOperations.createClass("Cidade");
////	 
////		documentOperations.removeClassById(idXPTO);
////		//documentManager.createAssociation(idXPTO, idClassBar);
////		documentOperations.createAssociation(professor, idClassBar);
////		documentOperations.createAssociation(teste, person);
////		//documentManager.createAssociation(idXPTO, professor);
//		documentManager.saveAndCopy();
//		
//	}
	
	@Test
	public void shouldCreateAClass() throws Exception{
		DocumentManager doc = givenADocument("simples", "simples2");
		ClassOperations classOperations = new ClassOperations(doc);
		assertNotNull(classOperations.createClass("Helper"));
		doc.saveAndCopy();
		Architecture a = givenAArchitecture2("simples2");
		assertContains(a.getAllClasses(), "Helper");
	}
	
	@Test
	public void shouldCreateAClassWithAttribute() throws Exception{
		DocumentManager document = givenADocument("simples", "meuModeloTest");
		ClassOperations classOperations = new ClassOperations(document);
		Map<String, String> classInfo = classOperations.createClass("Class43").withAttribute("name").withAttribute("age").build();
		
		classOperations.createClass("ControllerHome").withAttribute("id").withAttribute("name:String").withAttribute("age:Integer").withAttribute("date:String").withAttribute("isAdmin:Boolean").build();
		assertNotNull(classInfo);
		String[] idsProperty = classInfo.get("idsProperties").split(" ");
		String idClass = classInfo.get("classId");
		assertNotNull(idClass);
		assertThat(idsProperty.length, equalTo(2));
		document.saveAndCopy();
		
		Architecture a = givenAArchitecture2("meuModeloTest");
		assertContains(a.getAllClasses(), "Class43");
	}
	
	@Test
	public void shouldCreatAClassWithMethod() throws Exception{
		DocumentManager document = givenADocument("simples", "newModelSaved");
		ClassOperations classOperations = new ClassOperations(document);
		
		classOperations.createClass("Person").withMethod("foo[name:String]").withMethod("teste[id:Integer]").withMethod("index").withMethod("update").withAttribute("name:String").build();
		document.saveAndCopy();
		
		Architecture arch = givenAArchitecture2("newModelSaved");
		Class barKlass = arch.findClassByName("Person");
		
		assertNotNull(barKlass);
		assertThat("Should have 4 methods", barKlass.getAllMethods().size() == 4);
		assertThat("Should have a method called 'foo'", barKlass.findMethodByName("foo").getName().equals("foo"));
		assertThat("Should have a method called 'teste'", barKlass.findMethodByName("teste").getName().equals("teste"));
		assertThat("Should have a method called 'index'", barKlass.findMethodByName("index").getName().equals("index"));
		assertThat("Should have a method called 'update'", barKlass.findMethodByName("update").getName().equals("update"));
		
		Method methodFoo = barKlass.findMethodByName("foo");
		assertThat("Should have 1 parameter", methodFoo.getParameters().size() == 1 );
		assertThat("Should parameter name be 'name'", methodFoo.getParameters().get(0).getName().equals("name") );
	}

	@Test
	public void whenCreateADocumentManagerShouldMakeACopyOfFiles(){

		DocumentOperations documentOperations = new DocumentOperations(givenADocument("simples", "newModel"));
		Assert.assertNotNull(documentOperations);
		
		Assert.assertTrue("should copy exist", new File("manipulation/newModel.uml").exists());
		Assert.assertTrue("should copy exist", new File("manipulation/newModel.notation").exists());
		Assert.assertTrue("should copy exist", new File("manipulation/newModel.di").exists());
	}
	
	@Test
	public void shouldSaveModificationAndCopyFilesToDestination(){
		
		DocumentManager documentManager = givenADocument("simples", "teste4");
		documentManager.saveAndCopy();
		
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/simples.notation").exists());
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/simples.uml").exists());
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/simples.di").exists());
	}

	private DocumentManager givenADocument(String originalModelName, String newModelName) {
		String pathToFiles = "src/main/java/mestrado/arquitetura/parser/";
		DocumentManager documentManager = new DocumentManager(pathToFiles, originalModelName, newModelName);
		return documentManager;
		
	}
}
