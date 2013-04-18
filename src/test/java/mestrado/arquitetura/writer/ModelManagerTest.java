package mestrado.arquitetura.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.ClassOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.method.Argument;
import mestrado.arquitetura.parser.method.Types;
import mestrado.arquitetura.parser.method.VisibilityKind;
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
	
	@Test
	public void shouldCreateAssociationBetweenClasses() throws Exception{
		DocumentManager doc = givenADocument("teste3", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		Map<String, String> idPerson = classOperations.createClass("Person").build();
		Map<String, String> idEmployee = classOperations.createClass("Employee").build();
		Map<String, String> idManager = classOperations.createClass("Casa").build();
		
		classOperations.createAssociation(idPerson.get("classId"), idEmployee.get("classId"));
		classOperations.createAssociation(idPerson.get("classId"), idManager.get("classId"));
		
		Architecture a = givenAArchitecture2("teste3");
		assertThat("Should have 2 association", a.getAllAssociations().size() == 2);
	}
	
	@Test
	public void shouldRemoveAClass() throws Exception{
		DocumentManager doc = givenADocument("testeRemover", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		String cityId = classOperations.createClass("City").build().get("classId");
		assertTrue(modelContainId("testeRemover", cityId));
		classOperations.removeClassById(cityId);
		doc.saveAndCopy("testeRemover");
		assertFalse(modelContainId("testeRemover", cityId));
	}
	
	@Test
	public void shouldRemoveAssociation(){
		DocumentManager doc = givenADocument("teste4", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		Map<String, String> idPerson = classOperations.createClass("Person").build();
		Map<String, String> idEmployee = classOperations.createClass("Employee").build();
		
		String id = classOperations.createAssociation(idPerson.get("classId"), idEmployee.get("classId"));
		
		assertTrue(modelContainId("teste4", id));
		classOperations.removeAssociation(id);
		doc.saveAndCopy("teste4");
		assertFalse(modelContainId("teste4", id));
	}
	
	@Test
	public void shouldCreateAClass() throws Exception{
		DocumentManager doc = givenADocument("teste2", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		assertNotNull(classOperations.createClass("Helper"));
		Architecture a = givenAArchitecture2("teste2");
		assertContains(a.getAllClasses(), "Helper");
	}
	
	@Test
	public void shouldCreateAClassWithAttribute() throws Exception{
		DocumentManager document = givenADocument("testeCreateClassWithAttribute", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		Map<String, String> classInfo = classOperations.createClass("Class43").withAttribute("name").withAttribute("age").build();
		
		classOperations.createClass("ControllerHome").withAttribute("id").withAttribute("name:String").withAttribute("age:Integer").withAttribute("date:String").withAttribute("isAdmin:Boolean").build();
		assertNotNull(classInfo);
		String[] idsProperty = classInfo.get("idsProperties").split(" ");
		String idClass = classInfo.get("classId");
		assertNotNull(idClass);
		assertThat(idsProperty.length, equalTo(2));
		
		Architecture a = givenAArchitecture2("testeCreateClassWithAttribute");
		assertContains(a.getAllClasses(), "Class43");
	}
	
	@Test
	public void shouldRemoveAttributeFromClasse(){
		DocumentManager document = givenADocument("testRemoveAttribute", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		Map<String, String> classInfo = classOperations.createClass("Foo").withAttribute("name").withAttribute("age").build();
		
		String idAttr = classInfo.get("idsProperties").split(" ")[1];
		assertTrue(modelContainId("testRemoveAttribute", idAttr));
		
		classOperations.removeAttribute(idAttr);
		
		assertFalse(modelContainId("testRemoveAttribute", idAttr));
		
	}
	
	@Test
	public void shouldCreatAClassWithMethod() throws Exception{
		DocumentManager document = givenADocument("teste4", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(new Argument("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method foo = mestrado.arquitetura.parser.method.Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER).build();
		
		List<Argument> argumentsTeste = new ArrayList<Argument>();
		arguments.add(new Argument("id", Types.INTEGER));
		
		mestrado.arquitetura.parser.method.Method teste = mestrado.arquitetura.parser.method.Method.create().withName("teste").withArguments(argumentsTeste)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER).build();
		
		
		classOperations.createClass("Person").withMethod(foo).withMethod(teste).withAttribute("name:String").build();
		Architecture arch = givenAArchitecture2("teste4");
		Class barKlass = arch.findClassByName("Person");
		
		assertNotNull(barKlass);
		assertThat("Should have 4 methods", barKlass.getAllMethods().size() == 2);
		assertThat("Should have a method called 'foo'", barKlass.findMethodByName("foo").getName().equals("foo"));
		assertThat("Should have a method called 'teste'", barKlass.findMethodByName("teste").getName().equals("teste"));
		
		Method methodFoo = barKlass.findMethodByName("foo");
		
		assertThat("Should have 1 parameter", methodFoo.getParameters().size() == 1 );
		assertThat("Should parameter name be 'name'", methodFoo.getParameters().get(0).getName().equals("name") );
	}
	
	
	@Test
	public void testeMethod(){
		DocumentManager document = givenADocument("teste666", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(new Argument("name", Types.STRING));
		arguments.add(new Argument("age", Types.INTEGER));
		
		mestrado.arquitetura.parser.method.Method foo = mestrado.arquitetura.parser.method.Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER).build();
		
		classOperations.createClass("Bar").withMethod(foo).build();
	}
	
	@Test
	public void shouldCreateAMethodWithMethodAbstract() throws Exception{
		DocumentManager document = givenADocument("testeMethodoAbastrato", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(new Argument("name", Types.STRING));
		arguments.add(new Argument("age", Types.INTEGER));
		
		mestrado.arquitetura.parser.method.Method bar = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("bar").withArguments(arguments)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.abstractMethod().build();
		
		classOperations.createClass("Person").withMethod(bar).build();
		
		Architecture arch = givenAArchitecture2("testeMethodoAbastrato");
		
		assertThat("Should have 1 method", arch.findClassByName("Person").getAllMethods().size() == 1);
		assertTrue(arch.findClassByName("Person").findMethodByName("bar").isAbstract());
	}
	
	
	@Test
	public void shouldRemoveMethodFromClass(){
		DocumentManager document = givenADocument("testeRemoveMethod", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(new Argument("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method foo = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("bar").withArguments(arguments)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.build();
		
		List<Argument> arguments2 = new ArrayList<Argument>();
		arguments.add(new Argument("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method teste = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("teste").withArguments(arguments2)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.build();
		
		Map<String, String> classInfo = classOperations.createClass("Person").withMethod(foo).withMethod(teste).withAttribute("name:String").build();
		
		String idMethod = classInfo.get("idsMethods").split(" ")[1];
		assertTrue(modelContainId("testeRemoveMethod", idMethod));
		
		classOperations.removeMethod(idMethod);
		
		assertFalse(modelContainId("testeRemoveMethod", idMethod));
	}
	
	@Test
	public void shouldAddANewMethodInExistClass() throws Exception{
		DocumentManager document = givenADocument("addNewMethodToClass", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		Architecture arch = givenAArchitecture2("addNewMethodToClass");
		assertNotNull(arch);
		assertEquals(1, arch.getAllClasses().size());
		
		String idClass = arch.getAllClasses().get(0).getId();
		assertNotNull("class id should not be null", idClass);
		assertTrue("model should contain class id", modelContainId("addNewMethodToClass", idClass));
		
		List<Argument> arguments2 = new ArrayList<Argument>();
		arguments2.add(new Argument("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method teste = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("teste").withArguments(arguments2)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.build();
		
		classOperations.addMethodToClass(idClass, teste);
		
		assertTrue(modelContainId("addNewMethodToClass", teste.getId()));
		
	}
	
	@Test
	public void shouldSaveModificationAndCopyFilesToDestination(){
		
		givenADocument("teste5", "simples");
		
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/teste5.notation").exists());
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/teste5.uml").exists());
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/teste5.di").exists());
	}

	private DocumentManager givenADocument(String outputModelName, String originalModelName) {
		String pathToFiles = "src/main/java/mestrado/arquitetura/parser/1/";
		DocumentManager documentManager = new DocumentManager(outputModelName, pathToFiles, originalModelName);
		
		return documentManager;
	}
}
