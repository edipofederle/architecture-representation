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
	public void genericTestAllElementsGenerate() throws NodeNotFound, InvalidMultiplictyForAssociationException, IOException, CustonTypeNotFound{
		DocumentManager doc = givenADocument("genericElements", "simples");
		Operations op = new Operations(doc);
		List<String> idsClass = new ArrayList<String>();
		
		for(int i=0; i< 30; i++){
			String idClass = op.forClass().createClass(generateRandomWord(5)).build().get("classId");
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
	public void genericTestAllElementsGenerate1() throws NodeNotFound, InvalidMultiplictyForAssociationException, IOException, CustonTypeNotFound{
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
		
		
		String idORder = op.forClass().createClass("Order").withMethod(listMethods.get(0)).build().get("classId");
		String class2 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(1)).build().get("classId");
		String class3 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(2)).build().get("classId");
		String class4 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(3)).build().get("classId");
		String class5 = op.forClass().createClass(generateRandomWord(5)).withMethod(listMethods.get(4)).build().get("classId");
		
		op.forAssociation().createAssociation()
		 .betweenClass(idORder).withMultiplicy("1..*")
		 .andClass(class2).withMultiplicy("0..1")
		 .build();
		
		op.forDependency().createRelation("Dependencia #1").between(class5).and(class4).build();
		op.forUsage().createRelation("USage").between(class3).and(class4).build();
		
		op.forGeneralization().createRelation("Generalizcao #1").between(class4).and(class2).build();
		
		op.forDependency().createRelation("Dependencia #1").between(class5).and(class2).build();
		
		op.forComposition().createComposition().between(class3).withMultiplicy("1..*").and(idORder).build();
		
		
	}
	
	@Test	
	public void testePacote() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		DocumentManager doc = givenADocument("comPacote", "simples");
		Operations op = new Operations(doc);
		
		String idp2 = op.forPackage().createPacakge("Pacote 1").build().get("packageId");
		String idp1 = op.forPackage().createPacakge("Pacote 2").build().get("packageId");
		
		op.forGeneralization().createRelation("Nome").between(idp2).and(idp1).build();
	}
	
	
}