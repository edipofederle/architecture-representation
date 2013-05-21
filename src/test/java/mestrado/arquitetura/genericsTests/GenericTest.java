package mestrado.arquitetura.genericsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.exceptions.NotSuppportedOperation;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.parser.DocumentManager;
import mestrado.arquitetura.parser.Operations;
import mestrado.arquitetura.parser.method.Argument;
import mestrado.arquitetura.parser.method.Method;
import mestrado.arquitetura.parser.method.Types;
import mestrado.arquitetura.parser.method.VisibilityKind;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Variant;
import mestrado.arquitetura.representation.relationship.AssociationEnd;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.junit.Test;

/**
 * 
 * @author edipofederle
 *
 */
public class GenericTest extends TestHelper {
	
	
	@Test
	public void shouldApplyStereotypePersistense() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("interface");
		
		NamedElement klass = modelHelper.getAllClasses(model).get(0);
		Profile profileConcern = (Profile) givenAModel("perfilConcerns.profile");
		model.applyProfile(profileConcern);
		assertNotNull(klass);
		assertEquals("myInterface", klass.getName());
		
		assertFalse("Nao deve possuir concern", StereotypeHelper.hasConcern(klass));
		
		Stereotype concern = profileConcern.getOwnedStereotype("Persistence");
		assertEquals("Persistence", concern.getName());
		assertNotNull(concern);
		
		klass.applyStereotype(concern);
		
		assertTrue("Deve possuir concern", StereotypeHelper.hasConcern(klass));
	}
	
	
	@Test
	public void shouldLoadDependencyInterClassWitoutPackageAndClassWithPackage() throws Exception{
		String uriToArchitecture = getUrlToModel("classPacote");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
		assertNotNull(architecture);
		assertEquals(2, architecture.getAllClasses().size());
		assertEquals(1, architecture.getInterClassRelationships().size());
		
		DependencyRelationship r = architecture.getAllDependencies().get(0);
		
		assertNotNull(r);
		assertEquals("Class1", r.getClient().getName());
		assertEquals("Class2", r.getSupplier().getName());
		
		assertEquals("model", r.getSupplier().getNamespace());
		assertEquals("model::Package1", r.getClient().getNamespace());
	}
	
	@Test
	public void testAll() throws Exception{
		Architecture a = givenAArchitecture("all");
		assertNotNull(a);
		
		assertEquals(3, a.getAllRelationships().size());
		assertEquals(1, a.getAllGeneralizations().size());
		assertEquals(1, a.getAllAssociations().size());
		assertEquals(1, a.getAllUsage().size());
		assertEquals(2, a.getAllPackages().size());
		assertEquals(4, a.getAllClasses().size());
		
		GeneralizationRelationship g = a.getAllGeneralizations().get(0);
		assertEquals("Class2",g.getChild().getName());
		assertEquals("Class1", g.getParent().getName());
		assertNotNull(g.getId());
		
		UsageRelationship usage = a.getAllUsage().get(0);
		assertNotNull(usage.getId());
		assertEquals("Class2", usage.getClient().getName());
		assertEquals("Class3", usage.getSupplier().getName());
		
		AssociationRelationship association = a.getAllAssociations().get(0);
		assertEquals(2, association.getParticipants().size());
		
		AssociationEnd p1 = association.getParticipants().get(0);
		AssociationEnd p2 = association.getParticipants().get(1);
		assertNotNull(p1);
		assertNotNull(p2);
		assertEquals("Class3", p1.getCLSClass().getName());
		assertEquals("Class1", p2.getCLSClass().getName());
		
		mestrado.arquitetura.representation.Package server = a.findPackageByName("Server");
		assertNotNull(server);
		assertEquals(3, server.getClasses().size());
		assertContains(server.getClasses(), "Class1", "Class2", "Class3");
		
		mestrado.arquitetura.representation.Package client = a.findPackageByName("Client");
		assertNotNull(client);
		assertEquals(1, client.getClasses().size());
		assertContains(client.getClasses(), "Class1");
		
		assertEquals(9, a.getNumberOfElements());
	}
	
	@Test
	public void testeGenerico() throws Exception{
		Architecture a = givenAArchitecture("simples");
		assertNotNull(a);
		assertEquals(0 ,a.getAllClasses().size());
		assertEquals(0, a.getNumberOfElements());
	}
	
	
	@Test
	public void genericTestAllElementsGenerate() throws NodeNotFound, InvalidMultiplictyForAssociationException, IOException, CustonTypeNotFound, NotSuppportedOperation, ModelNotFoundException, ModelIncompleteException{
		DocumentManager doc = givenADocument("genericElements", "simples");
		Operations op = new Operations(doc);
		List<String> idsClass = new ArrayList<String>();
		
		for(int i=0; i< 30; i++){
			String idClass = op.forClass().createClass(generateRandomWord(5)).build().get("id");
			idsClass.add(idClass);
			
			if(i%2 == 0)
				op.forPackage().createPacakge("Pacakge_"+i).withClass(idClass);
		}
		
		for (int i = 0; i < idsClass.size()-1; i++) {
			String id =idsClass.get(i);
			String id2 = idsClass.get(i+1);
			op.forDependency().createRelation("Dependency #12").between(id).and(id2).build();
			op.forAssociation().createAssociation().betweenClass(id2).andClass(id).build();
		}
			
	}
	
	
	@Test
	public void genericTestAllElementsGenerate1() throws NodeNotFound, InvalidMultiplictyForAssociationException, IOException, CustonTypeNotFound, NotSuppportedOperation, ModelNotFoundException, ModelIncompleteException{
		DocumentManager doc = givenADocument("genericElements2", "simples");
		Operations op = new Operations(doc);
		
		List<Argument> arguments3 = new ArrayList<Argument>();
		arguments3.add(Argument.create("age", Types.INTEGER_WRAPPER));
		List<Method> listMethods = new ArrayList<Method>();
		
		for (int i = 0; i < 5; i++) {
			
			mestrado.arquitetura.parser.method.Method xpto = mestrado.arquitetura.parser.method.Method.create()
						.withName(generateRandomWord(4)).withArguments(arguments3)
						.withVisibility(VisibilityKind.PRIVATE_LITERAL)
						.withReturn(Types.LONG)
						.build();
			
			listMethods.add(xpto);
		}
		
		
		String idORder = op.forClass().createClass("Order").withMethod(listMethods.get(0)).build().get("id");
		String class2 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(1)).build().get("id");
		String class3 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(2)).build().get("id");
		String class4 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(3)).build().get("id");
		String class5 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(4)).build().get("id");
		
		String class6 = op.forClass().createClass("Foo").withMethod(listMethods.get(0)).build().get("id");
		String class7 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(1)).build().get("id");
		String class8 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(2)).build().get("id");
		String class9 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(3)).build().get("id");
		String class10 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(4)).build().get("id");
		
		op.forAssociation().createAssociation()
		 .betweenClass(idORder).withMultiplicy("1..*")
		 .andClass(class2).withMultiplicy("0..1")
		 .build();
		
		op.forAssociation().createAssociation()
		 .betweenClass(class9).withMultiplicy("1..*")
		 .andClass(class6).withMultiplicy("0..1")
		 .build();
		
		op.forDependency().createRelation("Dependencia #1").between(class5).and(class4).build();
		op.forUsage().createRelation("USage 1").between(class3).and(class4).build();
		
		op.forGeneralization().createRelation("Generalizcao #1").between(class4).and(class2).build();
		op.forGeneralization().createRelation("Generalizcao #2").between(class8).and(class2).build();
		
		op.forDependency().createRelation("Dependencia #1").between(class5).and(class2).build();
		op.forDependency().createRelation("Dependencia #2").between(idORder).and(class10).build();
		
		op.forComposition().createComposition().between(class3).withMultiplicy("1..*").and(idORder).build();
		op.forAggregation().createRelation("Alguma coisa").between(class7).and(class4).build();
		op.forAggregation().createRelation("Alguma coisa 2").between(class10).and(class2).withMultiplicy("1..*").build();
		
		op.forAggregation().createRelation("Testeseec").between(idORder).and(class6).build();
		
	}
	
	@Test
	public void createModelsWithVariants() throws Exception{
		DocumentManager doc = givenADocument("modelComVariants", "simples");
		Operations op = new Operations(doc);
		
		String idFoo = op.forClass().createClass("Foo").isVariationPoint().build().get("id");
		Variant mandatory = givenAVariant("mandatory", idFoo); // Classe Foo irá ser o rootVP.
		op.forClass().addStereotype(idFoo, mandatory);
		
		String idBar1 = op.forClass().createClass("Bar1").build().get("id");
		Variant alternative_OR = givenAVariant("alternative_OR", idFoo); // Classe Foo irá ser o rootVP.
		op.forClass().addStereotype(idBar1, alternative_OR);
		
		String idBar2 = op.forClass().createClass("Bar2").build().get("id");
		op.forClass().addStereotype(idBar2, alternative_OR);
		
		String idBar3 = op.forClass().createClass("Bar3").build().get("id");
		op.forClass().addStereotype(idBar3, alternative_OR);
		
		op.forGeneralization().createRelation("").between(idBar1).and(idFoo).build();
		op.forGeneralization().createRelation("").between(idBar2).and(idFoo).build();
		op.forGeneralization().createRelation("").between(idBar3).and(idFoo).build();
		
//		//Le o modelo criado
		
		Architecture a = givenAArchitecture2("modelComVariants");
		
		Class foo = a.findClassByName("foo");
		assertTrue(foo.isVariationPoint());
		
		Class bar1 = a.findClassByName("Bar1");
		Class bar2 = a.findClassByName("Bar2");
		Class bar3 = a.findClassByName("Bar3");
		
		assertEquals("alternative_OR", bar1.getVariantType().getVariantName());
		assertEquals("alternative_OR", bar2.getVariantType().getVariantName());
		assertEquals("alternative_OR", bar3.getVariantType().getVariantName());
	}
	
}