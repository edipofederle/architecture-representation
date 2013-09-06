package mestrado.arquitetura.genericsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.junit.Test;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.exceptions.CustonTypeNotFound;
import arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.NodeNotFound;
import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.helpers.StereotypeHelper;
import arquitetura.helpers.Uml2Helper;
import arquitetura.helpers.Uml2HelperFactory;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.UsageRelationship;
import arquitetura.touml.Argument;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Method;
import arquitetura.touml.Operations;
import arquitetura.touml.Types;
import arquitetura.touml.VisibilityKind;

/**
 * 
 * @author edipofederle
 *
 */
public class GenericTest extends TestHelper {
	
	
	@Test
	public void shouldApplyStereotypePersistense() throws Exception{
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
		
		arquitetura.representation.Package server = a.findPackageByName("Server");
		assertNotNull(server);
		assertEquals(3, server.getClasses().size());
		assertContains(server.getClasses(), "Class1", "Class2", "Class3");
		
		arquitetura.representation.Package client = a.findPackageByName("Client");
		assertNotNull(client);
		assertEquals(1, client.getClasses().size());
		assertContains(client.getClasses(), "Class1");
		
		assertEquals(10, a.getNumberOfElements());
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
		DocumentManager doc = givenADocument("genericElements");
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
		DocumentManager doc = givenADocument("genericElements2");
		Operations op = new Operations(doc);
		
		List<Argument> arguments3 = new ArrayList<Argument>();
		arguments3.add(Argument.create("age", Types.INTEGER_WRAPPER));
		List<Method> listMethods = new ArrayList<Method>();
		
		for (int i = 0; i < 5; i++) {
			
			arquitetura.touml.Method xpto = arquitetura.touml.Method.create()
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
	public void teste() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion, IOException{
		Uml2Helper helper = Uml2HelperFactory.getUml2Helper();
		Package model = helper.load("src/main/java/arquitetura/touml/1/simples.uml");
		assertNotNull(model);
		
		Profile profile = (Profile) helper.getExternalResources(ReaderConfig.getPathToProfileConcerns());
		model.applyProfile(profile);
		URI uri = URI.createFileURI("/Users/edipofederle/Desktop/output.uml");
		helper.saveResources(model, uri);
	}
	
}