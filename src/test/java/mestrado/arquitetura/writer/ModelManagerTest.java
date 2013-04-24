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
import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.ClassOperations;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.PackageOperation;
import mestrado.arquitetura.parser.method.Argument;
import mestrado.arquitetura.parser.method.Attribute;
import mestrado.arquitetura.parser.method.Types;
import mestrado.arquitetura.parser.method.VisibilityKind;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Method;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;

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
		assertFalse(modelContainId("testeRemover", cityId));
	}
	
	@Test
	public void shouldRemoveAssociation() throws CustonTypeNotFound, NodeNotFound{
		DocumentManager doc = givenADocument("teste4", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		Map<String, String> idPerson = classOperations.createClass("Person").build();
		Map<String, String> idEmployee = classOperations.createClass("Employee").build();
		
		String id = classOperations.createAssociation(idPerson.get("classId"), idEmployee.get("classId"));
		
		assertTrue(modelContainId("teste4", id));
		classOperations.removeAssociation(id);
		assertFalse(modelContainId("teste4", id));
	}
	
	@Test
	public void shouldCreateAClass() throws Exception{
		DocumentManager doc = givenADocument("teste2", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		assertNotNull(classOperations.createClass("Helper"));
		Architecture a = givenAArchitecture2("teste2");
		assertContains(a.getAllClasses(), "Helper");
		assertFalse(a.findClassByName("Helper").isAbstract());
	}
	
	@Test
	public void shouldCreateAAbstractClass() throws Exception{
		DocumentManager doc = givenADocument("classAbstrata", "simples");
		ClassOperations classOperations = new ClassOperations(doc);
		
		classOperations.createClass("ClasseAbstrata").isAbstract().build();
		Architecture a = givenAArchitecture2("classAbstrata");
		assertContains(a.getAllClasses(), "ClasseAbstrata");
		Class klass = a.findClassByName("ClasseAbstrata");
		assertTrue(klass.isAbstract());
	}
	
	@Test
	public void shouldCreateAClassWithAttribute() throws Exception{
		DocumentManager document = givenADocument("testeCreateClassWithAttribute", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		Attribute age = Attribute.create()
				 .withName("age")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.INTEGER_WRAPPER);
		
		Attribute id = Attribute.create()
				 .withName("id")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.INTEGER_WRAPPER);
		
		Attribute name = Attribute.create()
				 .withName("name")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		
		Map<String, String> classInfo = classOperations.createClass("Class43").withAttribute(xpto).withAttribute(age).build();
		classOperations.createClass("ControllerHome").withAttribute(id).withAttribute(name).build();
		
		assertNotNull(classInfo);
		String[] idsProperty = classInfo.get("idsProperties").split(" ");
		String idClass = classInfo.get("classId");
		assertNotNull(idClass);
		assertThat("should contain two attributes", idsProperty.length, equalTo(2));
		
		Architecture a = givenAArchitecture2("testeCreateClassWithAttribute");
		assertContains(a.getAllClasses(), "Class43");
		
		Class klass43 = a.findClassByName("Class43");
		mestrado.arquitetura.representation.Attribute attrXpto = klass43.findAttributeByName("xpto");
		assertNotNull(attrXpto);
		assertNotNull(attrXpto.getId());
		assertEquals("xpto", attrXpto.getName());
		assertEquals("String", attrXpto.getType());
		assertEquals("public", attrXpto.getVisibility());
		
		
	}
	
	@Test
	public void shouldRemoveAttributeFromClasse() throws CustonTypeNotFound, NodeNotFound{
		DocumentManager document = givenADocument("testRemoveAttribute", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		Attribute name = Attribute.create()
				 .withName("name")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		Attribute age = Attribute.create()
				 .withName("age")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.INTEGER_WRAPPER);
		
		classOperations.createClass("Foo").withAttribute(name).withAttribute(age).build();
		
		assertTrue(modelContainId("testRemoveAttribute", name.getId()));
		
		classOperations.removeAttribute(name.getId());
		
		assertFalse(modelContainId("testRemoveAttribute", name.getId()));
		
	}
	
	@Test
	public void shouldCreatAClassWithMethod() throws Exception{
		DocumentManager document = givenADocument("teste4", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method foo = mestrado.arquitetura.parser.method.Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER).build();
		
		List<Argument> argumentsTeste = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.INTEGER_WRAPPER));
		
		mestrado.arquitetura.parser.method.Method teste = mestrado.arquitetura.parser.method.Method.create().withName("teste").withArguments(argumentsTeste)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER).build();
		
		Attribute name = Attribute.create()
				 .withName("name")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		classOperations.createClass("Person").withMethod(foo).withMethod(teste).withAttribute(name).build();
		Architecture arch = givenAArchitecture2("teste4");
		Class barKlass = arch.findClassByName("Person");
		
		assertNotNull(barKlass);
		assertThat("Should have 2 methods", barKlass.getAllMethods().size() == 2);
		assertThat("Should have a method called 'foo'", barKlass.findMethodByName("foo").getName().equals("foo"));
		assertThat("Should have a method called 'teste'", barKlass.findMethodByName("teste").getName().equals("teste"));
		
		Method methodFoo = barKlass.findMethodByName("foo");
		
		assertThat("Should have 1 parameter", methodFoo.getParameters().size() == 1 );
		assertThat("Should parameter name be 'name'", methodFoo.getParameters().get(0).getName().equals("name") );
	}
	
	
	@Test
	public void testeMethod() throws CustonTypeNotFound, NodeNotFound{
		DocumentManager document = givenADocument("teste666", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.STRING));
		arguments.add(Argument.create("name", Types.INTEGER_WRAPPER));
		
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
		arguments.add(Argument.create("name", Types.STRING));
		arguments.add(Argument.create("name", Types.INTEGER_WRAPPER));
		
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
	public void shouldRemoveMethodFromClass() throws CustonTypeNotFound, NodeNotFound{
		DocumentManager document = givenADocument("testeRemoveMethod", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method foo = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("bar").withArguments(arguments)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.build();
		
		List<Argument> arguments2 = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method teste = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("teste").withArguments(arguments2)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.build();
		
		Attribute xpto = Attribute.create()
								 .withName("xpto")
								 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
								 .withType(Types.STRING);
		
		classOperations.createClass("Person").withMethod(foo).withMethod(teste).withAttribute(xpto).build();
		
		assertTrue(modelContainId("testeRemoveMethod", teste.getId()));
		
		classOperations.removeMethod(teste.getId());
		
		assertFalse(modelContainId("testeRemoveMethod", teste.getId()));
	}
	
	@Test
	public void shouldAddNewAttributeToExistClass() throws Exception{
		DocumentManager document = givenADocument("addNewAttributeToClass", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		Architecture arch = givenAArchitecture2("addNewAttributeToClass");
		assertNotNull(arch);
		assertEquals(1, arch.getAllClasses().size());
		
		String idClass = arch.getAllClasses().get(0).getId();
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		
		assertFalse(modelContainId("addNewAttributeToClass", xpto.getId()));
		classOperations.addAttributeToClass(idClass, xpto);
		assertTrue(modelContainId("addNewAttributeToClass", xpto.getId()));
		
	}
	
	@Test
	public void shouldAddNewPrivateAttributeToExistClass() throws Exception{
		DocumentManager document = givenADocument("addNewPrivateAttributeToClass", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		Architecture arch = givenAArchitecture("simples");
		assertNotNull(arch);
		
		String idClass = arch.getAllClasses().get(0).getId();
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PRIVATE_LITERAL)
				 .withType(Types.STRING);
		
		
		assertFalse(modelContainId("addNewPrivateAttributeToClass", xpto.getId()));
		classOperations.addAttributeToClass(idClass, xpto);
		assertTrue(modelContainId("addNewPrivateAttributeToClass", xpto.getId()));
		Architecture arch2 = givenAArchitecture2("addNewPrivateAttributeToClass");
		assertEquals("private", arch2.findClassByName("Class1").getAllAttributes().get(0).getVisibility());
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
		arguments2.add(Argument.create("name", Types.STRING));
		
		mestrado.arquitetura.parser.method.Method teste = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("teste").withArguments(arguments2)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.build();
		
		List<Argument> arguments3 = new ArrayList<Argument>();
		arguments3.add(Argument.create("age", Types.INTEGER_WRAPPER));
		
		mestrado.arquitetura.parser.method.Method xpto = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("xpto").withArguments(arguments3)
							 						.withVisibility(VisibilityKind.PRIVATE_LITERAL)
							 						.withReturn(Types.LONG)
							 						.build();
		
		classOperations.addMethodToClass(idClass, teste);
		classOperations.addMethodToClass(idClass, xpto);
		
		assertTrue(modelContainId("addNewMethodToClass", teste.getId()));
		assertTrue(modelContainId("addNewMethodToClass", xpto.getId()));
	}
	
	
	@Test
	public void shouldCreateAAttributeWithCustonType() throws Exception{
		
		DocumentManager document = givenADocument("classWithAttrCuston", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		classOperations.createClass("MyClass");
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.custom("MyClass"));
		
		
		classOperations.createClass("Foo").withAttribute(xpto);
		
		Architecture arch = givenAArchitecture2("classWithAttrCuston");
		Class klassFoo = arch.findClassByName("Foo");
		assertNotNull(klassFoo);
		assertTrue(modelContainId("classWithAttrCuston", xpto.getId()));
	}
	
	@Test(expected=CustonTypeNotFound.class)
	public void shouldNotCreateAAttributeWithCustonTypeIfTypeDontExist() throws CustonTypeNotFound, NodeNotFound{
		DocumentManager document = givenADocument("classWithAttrCuston", "simples");
		ClassOperations classOperations = new ClassOperations(document);
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.custom("MyClass"));
		
		
		classOperations.createClass("Foo").withAttribute(xpto);
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
	
	
	/* Pacote Testes */
	@Test
	public void shouldCreateAPackage() throws Exception{
		DocumentManager document = givenADocument("testePacote", "simples");
		PackageOperation packageOperations = new PackageOperation(document);
		
		packageOperations.createPacakge("meuPacote");
		
		Architecture arch = givenAArchitecture2("testePacote");
		
		assertThat("Should have one package", arch.getAllPackages().size() == 1);
		assertEquals("meuPacote", arch.getAllPackages().get(0).getName());
	}
	
	@Test
	public void shouldCreateAClassInsideAPackage() throws Exception{
		DocumentManager document = givenADocument("testePacoteComClasse", "simples");
		PackageOperation packageOperations = new PackageOperation(document);
		
		packageOperations.createPacakge("Bar").withClass("Foo").withClass("Teste").build();
		
		Architecture arch = givenAArchitecture2("testePacote");
		
		assertThat("Should have one package", arch.getAllPackages().size() == 1);
		assertEquals("meuPacote", arch.getAllPackages().get(0).getName());
	}
	
	@Test
	public void shouldCreateAClassInsideAPackageWithAssociation() throws Exception{
		DocumentManager document = givenADocument("testePacoteClassAsssociation", "simples");
		PackageOperation packageOperations = new PackageOperation(document);
		
		ArrayList<String> infos = packageOperations.createPacakge("Bar").withClass("Foo").withClass("Teste").build();
		String idClaassFoo = infos.get(0).split(":")[0];
		String idClassTeste = infos.get(1).split(":")[0];
		
		
		ClassOperations classOperations = new ClassOperations(document);
		classOperations.createAssociation(idClaassFoo, idClassTeste);
		Architecture arch = givenAArchitecture2("testePacoteClassAsssociation");
		
		assertEquals(2, arch.getAllPackages().get(0).getAllClassIdsForThisPackage().size());
		assertEquals(1, arch.getAllAssociations().size());
	}
	
	@Test
	public void associationClassPakackeClass() throws Exception{
		DocumentManager document = givenADocument("testeAssociationPackageClassClass", "simples");
		PackageOperation packageOperations = new PackageOperation(document);
		ClassOperations classOperations = new ClassOperations(document);

		ArrayList<String> infos = packageOperations.createPacakge("PacoteTeste").withClass("Foo").build();
		String idClassFoo = infos.get(0).split(":")[0];
	
		Map<String, String> infosClass = classOperations.createClass("Person").build();
		String idClassPerson = infosClass.get("classId");
		
		classOperations.createAssociation(idClassFoo, idClassPerson);
		
		Architecture arch = givenAArchitecture2("testeAssociationPackageClassClass");
		assertEquals(1, arch.getAllAssociations().size());
		AssociationRelationship asssociation = arch.getAllAssociations().get(0);
		assertNotNull(asssociation);
		
	}
	
	@Test
	public void shouldCreateAAbstractClassInsideAPackage() throws Exception{
		DocumentManager document = givenADocument("classAbstrataDentroPacote", "simples");
		PackageOperation packageOperations = new PackageOperation(document);
		
		packageOperations.createPacakge("fooPkg").withClass("teste").isAbstract().build();
		
		Architecture a = givenAArchitecture2("classAbstrataDentroPacote");
		assertContains(a.getAllClasses(), "teste");
		Class klass = a.findClassByName("teste");
		assertTrue(klass.isAbstract());
	}
	
	
		
}