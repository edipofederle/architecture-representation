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
import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.api.touml.Operations;
import mestrado.arquitetura.helpers.test.TestHelper;
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
		DocumentManager doc = givenADocument("teste3");
		Operations op = new Operations(doc);
		
		Map<String, String> idPerson = op.forClass().createClass("Person").build();
		Map<String, String> idEmployee = op.forClass().createClass("Employee").build();
		Map<String, String> idManager = op.forClass().createClass("Casa").build();
		
		op.forAssociation().createAssociation()
							 .betweenClass(idPerson.get("id"))
							 .andClass(idEmployee.get("id"))
							 .build();
		
		op.forAssociation().createAssociation()
							 .betweenClass(idPerson.get("id"))
							 .andClass(idManager.get("id"))
							 .build();
		
		Architecture a = givenAArchitecture2("teste3");
		assertEquals(2, a.getAllAssociations().size());
	}
	
	@Test
	public void shouldRemoveAClass() throws Exception{
		DocumentManager doc = givenADocument("testeRemover");
		Operations op = new Operations(doc);
		String cityId = op.forClass().createClass("City").build().get("id");
		assertTrue(modelContainId("testeRemover", cityId));
		 op.forClass().removeClassById(cityId);
		assertFalse(modelContainId("testeRemover", cityId));
	}
	
	@Test
	public void shouldCreateAClass() throws Exception{
		DocumentManager doc = givenADocument("teste2");
		Operations op = new Operations(doc);
		assertNotNull(op.forClass().createClass("Helper"));
		Architecture a = givenAArchitecture2("teste2");
		assertContains(a.getAllClasses(), "Helper");
		assertFalse(a.findClassByName("Helper").isAbstract());
	}
	
	@Test
	public void shouldCreateAAbstractClass() throws Exception{
		DocumentManager doc = givenADocument("classAbstrata");
		Operations op = new Operations(doc);
		op.forClass().createClass("ClasseAbstrata").isAbstract().build();
		Architecture a = givenAArchitecture2("classAbstrata");
		assertContains(a.getAllClasses(), "ClasseAbstrata");
		Class klass = a.findClassByName("ClasseAbstrata");
		assertTrue(klass.isAbstract());
	}
	
	@Test
	public void shouldCreateAClassWithAttribute() throws Exception{
		DocumentManager doc = givenADocument("testeCreateClassWithAttribute");
		Operations op = new Operations(doc);
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		Attribute age = Attribute.create()
				 .withName("age")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.INTEGER_WRAPPER);
		
		List<Attribute> classInfoAttrs = new ArrayList<Attribute>();
		classInfoAttrs .add(xpto);
		classInfoAttrs.add(age);
		
		Attribute id = Attribute.create()
				 .withName("id")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.INTEGER_WRAPPER);
		
		Attribute name = Attribute.create()
				 .withName("name")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		List<Attribute> classInfoAttrs2 = new ArrayList<Attribute>();
		classInfoAttrs2.add(id);
		classInfoAttrs2.add(name);
		
		Map<String, String> classInfo = op.forClass().createClass("Class43").withAttribute(classInfoAttrs).build();
		op.forClass().createClass("ControllerHome").withAttribute(classInfoAttrs2).build();
		
		assertNotNull(classInfo);
		String[] idsProperty = classInfo.get("idsProperties").split(" ");
		String idClass = classInfo.get("id");
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
	public void shouldRemoveAttributeFromClasse() throws Exception{
		DocumentManager doc = givenADocument("testRemoveAttribute");
		Operations op = new Operations(doc);
		
		Attribute name = Attribute.create()
				 .withName("name")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		Attribute age = Attribute.create()
				 .withName("age")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.INTEGER_WRAPPER);
		
		List<Attribute> classInfoAttrs2 = new ArrayList<Attribute>();
		classInfoAttrs2.add(name);
		classInfoAttrs2.add(age);
		
		op.forClass().createClass("Foo").withAttribute(classInfoAttrs2).build();
		
		assertTrue(modelContainId("testRemoveAttribute", name.getId()));
		
		op.forClass().removeAttribute(name.getId());
		
		assertFalse(modelContainId("testRemoveAttribute", name.getId()));
		
	}
	
	@Test
	public void shouldCreatAClassWithMethod() throws Exception{
		DocumentManager doc = givenADocument("teste4");
		Operations op = new Operations(doc);
		
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
		
		List<Attribute> classInfoAttrs2 = new ArrayList<Attribute>();
		classInfoAttrs2.add(name);
		
		op.forClass().createClass("Person").withMethod(foo).withMethod(teste).withAttribute(classInfoAttrs2).build();
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
	public void testeMethod() throws Exception{
		DocumentManager doc = givenADocument("teste666");
		Operations op = new Operations(doc);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.STRING));
		arguments.add(Argument.create("name", Types.INTEGER_WRAPPER));
		
		mestrado.arquitetura.parser.method.Method foo = mestrado.arquitetura.parser.method.Method.create().withName("foo").withArguments(arguments)
							 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 .withReturn(Types.INTEGER).build();
		
		op.forClass().createClass("Bar").withMethod(foo).build();
	}
	
	@Test
	public void shouldCreateAMethodWithMethodAbstract() throws Exception{
		DocumentManager doc = givenADocument("testeMethodoAbastrato");
		Operations op = new Operations(doc);
		
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(Argument.create("name", Types.STRING));
		arguments.add(Argument.create("name", Types.INTEGER_WRAPPER));
		
		mestrado.arquitetura.parser.method.Method bar = mestrado.arquitetura.parser.method.Method.create()
							 						.withName("bar").withArguments(arguments)
							 						.withVisibility(VisibilityKind.PUBLIC_LITERAL)
							 						.withReturn(Types.INTEGER)
							 						.abstractMethod().build();
		
		op.forClass().createClass("Person").withMethod(bar).build();
		
		Architecture arch = givenAArchitecture2("testeMethodoAbastrato");
		
		assertThat("Should have 1 method", arch.findClassByName("Person").getAllMethods().size() == 1);
		assertTrue(arch.findClassByName("Person").findMethodByName("bar").isAbstract());
	}
	
	
	@Test
	public void shouldRemoveMethodFromClass() throws Exception{
		DocumentManager doc = givenADocument("testeRemoveMethod");
		Operations op = new Operations(doc);
		
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
		List<Attribute> classInfoAttrs2 = new ArrayList<Attribute>();
		classInfoAttrs2.add(xpto);
		op.forClass().createClass("Person").withMethod(foo).withMethod(teste).withAttribute(classInfoAttrs2).build();
		
		assertTrue(modelContainId("testeRemoveMethod", teste.getId()));
		
		op.forClass().removeMethod(teste.getId());
		
		assertFalse(modelContainId("testeRemoveMethod", teste.getId()));
	}
	
	@Test
	public void shouldAddNewAttributeToExistClass() throws Exception{
		DocumentManager doc = givenADocument("addNewAttributeToClass");
		Operations op = new Operations(doc);
		
		Architecture arch = givenAArchitecture2("addNewAttributeToClass");
		assertNotNull(arch);
		assertEquals(0, arch.getAllClasses().size());
		
		String idClass = op.forClass().createClass("Foo").build().get("id");
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		
		assertFalse(modelContainId("addNewAttributeToClass", xpto.getId()));
		op.forClass().addAttributeToClass(idClass, xpto);
		assertTrue(modelContainId("addNewAttributeToClass", xpto.getId()));
		
	}
	
	@Test
	public void shouldAddNewPrivateAttributeToExistClass() throws Exception{
		DocumentManager doc = givenADocument("addNewPrivateAttributeToClass");
		Operations op = new Operations(doc);
		
		Architecture arch = givenAArchitecture("simples");
		assertNotNull(arch);
		
		String idClass = op.forClass().createClass("foo").build().get("id");
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PRIVATE_LITERAL)
				 .withType(Types.STRING);
		
		
		assertFalse(modelContainId("addNewPrivateAttributeToClass", xpto.getId()));
		op.forClass().addAttributeToClass(idClass, xpto);
		assertTrue(modelContainId("addNewPrivateAttributeToClass", xpto.getId()));
		Architecture arch2 = givenAArchitecture2("addNewPrivateAttributeToClass");
		assertEquals("private", arch2.findClassByName("foo").getAllAttributes().get(0).getVisibility());
	}
	
	@Test
	public void shouldAddANewMethodInExistClass() throws Exception{
		DocumentManager doc = givenADocument("addNewMethodToClass");
		Operations op = new Operations(doc);
		
		Architecture arch = givenAArchitecture2("addNewMethodToClass");
		assertNotNull(arch);
		assertEquals(0, arch.getAllClasses().size());
		
		String idClass = op.forClass().createClass("Teste").build().get("id");
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
		
		op.forClass().addMethodToClass(idClass, teste);
		op.forClass().addMethodToClass(idClass, xpto);
		
		assertTrue(modelContainId("addNewMethodToClass", teste.getId()));
		assertTrue(modelContainId("addNewMethodToClass", xpto.getId()));
	}
	
	
	@Test
	public void shouldCreateAAttributeWithCustonType() throws Exception{
		
		DocumentManager doc = givenADocument("classWithAttrCuston");
		Operations op = new Operations(doc);
		
		op.forClass().createClass("MyClass");
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.custom("MyClass"));
		
		List<Attribute> classInfoAttrs2 = new ArrayList<Attribute>();
		classInfoAttrs2.add(xpto);
		op.forClass().createClass("Foo").withAttribute(classInfoAttrs2);
		
		Architecture arch = givenAArchitecture2("classWithAttrCuston");
		Class klassFoo = arch.findClassByName("Foo");
		assertNotNull(klassFoo);
		assertTrue(modelContainId("classWithAttrCuston", xpto.getId()));
	}

	@Test
	public void shouldSaveModificationAndCopyFilesToDestination() throws Exception{
		
		givenADocument("teste5");
		
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/teste5.notation").exists());
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/teste5.uml").exists());
		Assert.assertTrue("should copy exist", new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/teste5.di").exists());
	}

	/* Pacote Testes */
	@Test
	public void shouldCreateAPackage() throws Exception{
		DocumentManager doc = givenADocument("testePacote");
		Operations op = new Operations(doc);
		
		op.forPackage().createPacakge("meuPacote");
		
		Architecture arch = givenAArchitecture2("testePacote");
		
		assertThat("Should have one package", arch.getAllPackages().size() == 1);
		assertEquals("meuPacote", arch.getAllPackages().get(0).getName());
	}
	
	@Test
	public void shouldCreateAClassInsideAPackage() throws Exception{
		DocumentManager doc = givenADocument("testePacoteComClasse");
		Operations op = new Operations(doc);
		
		Map<String, String> foo = op.forClass().createClass("foo").build();
		Map<String, String> teste = op.forClass().createClass("teste").build();
		op.forPackage().createPacakge("Bar").withClass(foo.get("id")).withClass(teste.get("id")).build();
		
		Architecture arch = givenAArchitecture2("testePacote");
		
		assertThat("Should have one package", arch.getAllPackages().size() == 1);
		assertEquals("meuPacote", arch.getAllPackages().get(0).getName());
	}
	
	@Test
	public void shouldCreateAClassInsideAPackageWithAssociation() throws Exception{
		DocumentManager doc = givenADocument("testePacoteClassAsssociation");
		Operations op = new Operations(doc);
		
		Map<String, String> foo = op.forClass().createClass("foo").build();
		Map<String, String> teste = op.forClass().createClass("Teste").build();
		
		op.forPackage().createPacakge("Bar").withClass(foo.get("id")).withClass(teste.get("id")).build();

		
		
		op.forAssociation().createAssociation()
							 .betweenClass(foo.get("id")).andClass(teste.get("id")).build();
		
		Architecture arch = givenAArchitecture2("testePacoteClassAsssociation");
		
		assertEquals(2, arch.getAllPackages().get(0).getAllClassIdsForThisPackage().size());
		assertEquals(1, arch.getAllAssociations().size());
	}
	
	@Test
	public void associationClassPakackeClass() throws Exception{
		DocumentManager doc = givenADocument("testeAssociationPackageClassClass");
		Operations op = new Operations(doc);
		
		Map<String, String> foo = op.forClass().createClass("foo").build();

		op.forPackage().createPacakge("PacoteTeste").withClass(foo.get("id")).build();
	
		Map<String, String> infosClass = op.forClass().createClass("Person").build();
		String idClassPerson = infosClass.get("id");
		
		op.forAssociation().createAssociation()
							 .betweenClass(foo.get("id"))
							 .andClass(idClassPerson).build();
		
		Architecture arch = givenAArchitecture2("testeAssociationPackageClassClass");
		assertEquals(1, arch.getAllAssociations().size());
		AssociationRelationship asssociation = arch.getAllAssociations().get(0);
		assertNotNull(asssociation);
		
	}
	
	@Test
	public void shouldCreateAAbstractClassInsideAPackage() throws Exception{
		DocumentManager doc = givenADocument("classAbstrataDentroPacote");
		Operations op = new Operations(doc);
		
		Map<String, String> infosClass = op.forClass().createClass("teste").isAbstract().build();
		String idClassPerson = infosClass.get("id");
		
		op.forPackage().createPacakge("fooPkg").withClass(idClassPerson).build();
		
		Architecture a = givenAArchitecture2("classAbstrataDentroPacote");
		assertContains(a.getAllClasses(), "teste");
		Class klass = a.findClassByName("teste");
		assertTrue(klass.isAbstract());
	}

	
	@Test
	public void shouldCreateAPacakgeWithTwoClasses() throws Exception{
		
		DocumentManager doc = givenADocument("novoTest");
		Operations op = new Operations(doc);
		
		Attribute xpto = Attribute.create()
				 .withName("xpto")
				 .withVisibility(VisibilityKind.PUBLIC_LITERAL)
				 .withType(Types.STRING);
		
		
		List<Attribute> classInfoAttrs2 = new ArrayList<Attribute>();
		classInfoAttrs2.add(xpto);
		
		 Map<String, String> k = op.forClass().createClass("xpto").withAttribute(classInfoAttrs2).build();
		 Map<String, String> k1 = op.forClass().createClass("Teste").build();
		 
		op.forPackage().createPacakge("xpto").withClass(k.get("id")).withClass(k1.get("id")).build();
		
		Architecture a = givenAArchitecture2("novoTest");
		
		assertEquals(1, a.getAllPackages().size());
		assertEquals(2 ,a.findPackageByName("xpto").getAllClassIdsForThisPackage().size());
	}
	

}