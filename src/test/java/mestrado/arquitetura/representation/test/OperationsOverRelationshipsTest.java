package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import main.GenerateArchitecture;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.UsageRelationship;

public class OperationsOverRelationshipsTest extends TestHelper {

	@Test
	public void removeAssociation() throws Exception {
		Architecture a = givenAArchitecture("association");
		assertEquals(12,a.getAllIds().size());
		
		a.operationsOverRelationship().removeAssociationRelationship(a.getAllAssociationsRelationships().get(0));
		
		assertEquals(11,a.getAllIds().size());
		GenerateArchitecture g = new GenerateArchitecture();
		
		g.generate(a, "removendoAssociacao");
		
		Architecture genereted = givenAArchitecture2("removendoAssociacao");
		assertEquals(1, genereted.getAllAssociationsRelationships().size());
	}
	
	@Test
	public void moveEntireAssociation() throws Exception{
		Architecture a = givenAArchitecture("association");
		AssociationRelationship association = a.getAllAssociationsRelationships().get(0);
		assertEquals(12,a.getAllIds().size());
		Class idclass6 = a.findClassByName("Class6").get(0);
		Class idclass8 = a.findClassByName("Class8").get(0);
		
		a.operationsOverRelationship().moveAssociation(association,idclass6, idclass8);
		
		assertEquals(12,a.getAllIds().size());
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "removendoAssociacaoMove");
		
		Architecture genereted = givenAArchitecture2("removendoAssociacaoMove");
		assertEquals("Agora deve a classe deve ter 2 relacionamentos", 2, genereted.findClassByName("Class6").get(0).getRelationships().size());
	}
	
	@Test
	public void moveMemberEndOfAssociation() throws Exception{
		Architecture a = givenAArchitecture("association");
		AssociationRelationship association = a.getAllAssociationsRelationships().get(0);
		Class idclass8 = a.findClassByName("Class6").get(0);

		a.operationsOverRelationship().moveAssociationEnd(association.getParticipants().get(1), idclass8);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoPartOfAssociation");
		
		Architecture genereted = givenAArchitecture2("movendoPartOfAssociation");
		assertEquals("Agora deve a classe deve ter 2 relacionamentos", 2, genereted.findClassByName("Class6").get(0).getRelationships().size());
	}
	
	
	@Test
	public void shouldCreateANewAssociationWithBothAssociationEndNavigable() throws Exception {
		Architecture a = givenAArchitecture("association");
		
		Class class1 = a.findClassByName("Class1").get(0);
		Class class2 = a.findClassByName("Class8").get(0);
		
		
		a.forAssociation().createAssociationEnd()
					      .withKlass(class1)
					      .withMultiplicity("1..*")
					      .navigable()
					      .and()
					      .createAssociationEnd()
					      .withKlass(class2)
					      .withMultiplicity("1..1")
					      .navigable().build();
					      
		
		assertEquals(3, a.getAllAssociationsRelationships().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "associationAddNova");
		
		Architecture genereted = givenAArchitecture2("associationAddNova");
		List<AssociationRelationship> associations = genereted.getAllAssociationsRelationships();
		
		assertEquals(3,associations.size());
		
		assertTrue(associations.get(2).getParticipants().get(0).isNavigable());
		assertTrue(associations.get(2).getParticipants().get(1).isNavigable());
		
		assertEquals("1",associations.get(2).getParticipants().get(1).getMultiplicity().toString());
		assertEquals("1..*",associations.get(2).getParticipants().get(0).getMultiplicity().toString());
	}
	
	
	/* Fim testes associacao */
	
	
	
	/* Começo testes Dependencia */
	
	@Test
	public void shouldCreateNewDependency() throws Exception{
		Architecture a = givenAArchitecture("association");
		
		Class class1 = a.findClassByName("Class1").get(0);
		Class class2 = a.findClassByName("Class8").get(0);
		
		a.forDependency().create("Dependencia nome")
						 .withClient(class1)
						 .withSupplier(class2).build();
		
		assertEquals(1,a.getAllDependencies().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "dependenciaNova");
		
		Architecture genereted = givenAArchitecture2("dependenciaNova");
		DependencyRelationship dependency = genereted.getAllDependencies().get(0);
		
		assertEquals("Class1",dependency.getClient().getName());
		assertEquals("Class8",dependency.getSupplier().getName());
		assertEquals("Dependencia nome",dependency.getName());
	}
	
	
	@Test
	public void movendoEntireDependency() throws Exception{
		Architecture a = givenAArchitecture("dependenciaMovendo");
		
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		
		Class idclass6 = a.findClassByName("Class4").get(0);
		Class idclass8 = a.findClassByName("Class3").get(0);
		
		a.operationsOverRelationship().moveDependency(dependency, idclass8,idclass6);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoDependencia");
		Architecture genereted = givenAArchitecture2("movendoDependencia");
		
		DependencyRelationship dependency2 = genereted.getAllDependencies().get(0);
		assertEquals("Class4", dependency2.getSupplier().getName());
		assertEquals("Class3", dependency2.getClient().getName());
	}
	
	@Test
	public void movendoClientDependency() throws Exception{
		Architecture a = givenAArchitecture("dependenciaMovendo");
		
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		assertEquals("Class1",dependency.getClient().getName());
		
		Class newClient = a.findClassByName("Class3").get(0);
		
		a.operationsOverRelationship().moveDependencyClient(dependency, newClient);
		
		assertEquals("Class3",dependency.getClient().getName());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoClientDependencia");
	}
	
	@Test
	public void movendoSupplierDependency() throws Exception{
		Architecture a = givenAArchitecture("dependenciaMovendo");
		
		DependencyRelationship dependency = a.getAllDependencies().get(0);
		assertEquals("Class2",dependency.getSupplier().getName());
		
		Class newSupplier = a.findClassByName("Class4").get(0);
		
		a.operationsOverRelationship().moveDependencySupplier(dependency, newSupplier);
		
		assertEquals("Class4",dependency.getSupplier().getName());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoSuppluerDependencia");
	}
	
	
	/* Composicao */
	
	@Test
	public void removendoComposicao() throws Exception{
		Architecture a = givenAArchitecture("compositions/composicao1");
		a.operationsOverRelationship().removeAssociationRelationship(a.getAllCompositions().get(0));  
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "composicao1Removida");
		Architecture genereted = givenAArchitecture2("composicao1Removida");
		
		assertEquals(0, genereted.getAllCompositions().size());
	}
	
	@Test
	public void addNewComposicao() throws Exception{
		Architecture a = givenAArchitecture("association");
		
		Class class1 = a.findClassByName("Class1").get(0);
		Class class2 = a.findClassByName("Class8").get(0);
		
		a.forAssociation()
		  .createAssociationEnd()
	      .withKlass(class1)
	      .withMultiplicity("1..*")
	      .asComposition()
	      .and()
	      .createAssociationEnd()
	      .withKlass(class2)
	      .withMultiplicity("1..1")
	      .build();
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "composicaoNova");
		Architecture genereted = givenAArchitecture2("composicaoNova");
		assertEquals(2,genereted.getAllCompositions().size());
	}
	
	@Test
	public void movendoEntireComposicao() throws Exception{
		Architecture a = givenAArchitecture("association");
		
		Class idclass6 = a.findClassByName("Class4").get(0);
		Class idclass8 = a.findClassByName("Class7").get(0);
		
		a.operationsOverRelationship().moveAssociation(a.getAllCompositions().get(0), idclass6, idclass8);  
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoComposicao");
		Architecture genereted = givenAArchitecture2("movendoComposicao");
		
		assertEquals(1, genereted.getAllCompositions().size());
		assertEquals("Class7",genereted.getAllCompositions().get(0).getParticipants().get(0).getName());
		assertEquals("Class4",genereted.getAllCompositions().get(0).getParticipants().get(1).getName());
	}
	
	@Test
	public void movendoPartComposicao() throws Exception{
		Architecture a = givenAArchitecture("association");
		
		Class idclass6 = a.findClassByName("Class4").get(0);
		
		a.operationsOverRelationship().moveAssociationEnd(a.getAllCompositions().get(0).getParticipants().get(0), idclass6);  
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoPartOfComposicao");
		Architecture genereted = givenAArchitecture2("movendoPartOfComposicao");
		
		assertEquals(1, genereted.getAllCompositions().size());
		assertEquals("Class6",genereted.getAllCompositions().get(0).getParticipants().get(0).getName());
		assertEquals("Class4",genereted.getAllCompositions().get(0).getParticipants().get(1).getName());
	}
	
	
	//AssociationClass
	@Test
	public void moverEntireAssociationClass() throws Exception{
		Architecture a = givenAArchitecture("associationClass/movendoAssociationCLass");
		
		AssociationClassRelationship asc = a.getAllAssociationsClass().get(0);
		
		assertEquals("Class1", asc.getMemebersEnd().get(0).getType().getName());
		assertEquals("Class2", asc.getMemebersEnd().get(1).getType().getName());
		assertEquals("Class1", asc.getOwnedEnd().getName());
		
		Class idclass4 = a.findClassByName("Class4").get(0);
		Class idclass3 = a.findClassByName("Class3").get(0);
		
		a.operationsOverRelationship().moveAssociationClass(asc,idclass3, idclass4);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "associationClassMove");
		
		Architecture genereted = givenAArchitecture2("associationClassMove");
		
		AssociationClassRelationship ascg = genereted.getAllAssociationsClass().get(0);
		assertEquals("Class4", ascg.getMemebersEnd().get(0).getType().getName());
		assertEquals("Class3", ascg.getMemebersEnd().get(1).getType().getName());
		assertEquals("Class3", ascg.getMemebersEnd().get(1).getType().getName());
	}
	
	@Test
	public void moverPartAssociationClass() throws Exception{
		Architecture a = givenAArchitecture("associationClass/movendoAssociationCLass");
		
		AssociationClassRelationship asc = a.getAllAssociationsClass().get(0);
		
		
		Class idclass3 = a.findClassByName("Class3").get(0);
		
		a.operationsOverRelationship().moveMemberEndOf(asc.getMemebersEnd().get(0),idclass3);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "associationClassMovePart");
		
		Architecture genereted = givenAArchitecture2("associationClassMovePart");
		
		AssociationClassRelationship ascg = genereted.getAllAssociationsClass().get(0);
		assertEquals("Class2", ascg.getMemebersEnd().get(0).getType().getName());
		assertEquals("Class3", ascg.getMemebersEnd().get(1).getType().getName());
		assertEquals("Class3", ascg.getOwnedEnd().getName());
	}
	
	@Test
	public void createNewAssociationClass() throws Exception{
		Architecture a = givenAArchitecture("associationClass/semAssociationClass");
		
		List<Attribute> listAttrs = new ArrayList<Attribute>();
		List<Method> listMethods = new ArrayList<Method>();
		
		Class owner = a.findClassByName("Class1").get(0);
		Class klass = a.findClassByName("Class2").get(0);
		
		assertEquals(0,owner.getIdsRelationships().size());
		assertEquals(0,klass.getIdsRelationships().size());
		
		a.forAssociation().createAssociationClass(listAttrs, listMethods, owner, klass);
		
		assertEquals(1, a.getAllAssociationsClass().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "associationClassNovo");
		
		Architecture generated = givenAArchitecture2("associationClassNovo");
		
		assertEquals("Class2",generated.getAllAssociationsClass().get(0).getMemebersEnd().get(0).getType().getName());
		assertEquals("Class1",generated.getAllAssociationsClass().get(0).getMemebersEnd().get(1).getType().getName());
		
		assertEquals(1, owner.getIdsRelationships().size());
		assertEquals(1, klass.getIdsRelationships().size());
		
		assertEquals(1, klass.getAllAssociationClass().size());
		
	}
	
	
	
	//Generalizacao
	
	@Test
	public void shouldMoveSuperClass() throws Exception{
		Architecture a = givenAArchitecture("generalizacaoMove");
		
		GeneralizationRelationship gene = a.getAllGeneralizations().get(0);
		
		assertNotNull(gene);
		assertEquals("Class1",gene.getParent().getName());
		
		a.forGeneralization().moveGeneralizationParent(gene, a.findClassByName("Class3").get(0));
		
		assertEquals("Class3",gene.getParent().getName());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "generalizacaoMoveGerada");
		
		Architecture architecture = givenAArchitecture2("generalizacaoMoveGerada");
		GeneralizationRelationship geneg = architecture.getAllGeneralizations().get(0);
		assertEquals("Class3",geneg.getParent().getName());
	}
	
	@Test
	public void shouldMoveSubClass() throws Exception{
		Architecture a = givenAArchitecture("generalizacaoMove");
		
		GeneralizationRelationship gene = a.getAllGeneralizations().get(0);
		
		a.forGeneralization().moveGeneralizationSubClass(gene, a.findClassByName("Class5").get(0));
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "generalizacaoMoveSubGerada");
		
		Architecture architecture = givenAArchitecture2("generalizacaoMoveSubGerada");
		GeneralizationRelationship geneg = architecture.getAllGeneralizations().get(0);
		
		assertEquals("Class5",geneg.getChild().getName());
		assertEquals(1,geneg.getAllChildrenForGeneralClass().size());
	}
	
	@Test
	public void shouldMoveEntiereGeneralization() throws Exception{
		Architecture a = givenAArchitecture("generalizacaoMove");
		
		Class klass5 = a.findClassByName("Class5").get(0);
		Class klass4 = a.findClassByName("Class4").get(0);
		
		GeneralizationRelationship gene = a.getAllGeneralizations().get(0);
		
		a.forGeneralization().moveGeneralization(gene, klass5, klass4);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "generalizacaoMovidaInteira");
		
		
		Architecture architecture = givenAArchitecture2("generalizacaoMovidaInteira");
		GeneralizationRelationship geneg = architecture.getAllGeneralizations().get(0);
		assertEquals("Class5",geneg.getParent().getName());
		assertEquals("Class4",geneg.getChild().getName());
	}
	
	@Test
	public void shouldAddNewSubClassToGeneralization() throws Exception{
		Architecture a = givenAArchitecture("generalizacaoMove");
		
		GeneralizationRelationship generalizationRelationship = a.getAllGeneralizations().get(0);
		Class klass5 = a.findClassByName("Class5").get(0);
		
		a.forGeneralization().addChildToGeneralization(generalizationRelationship, klass5);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "generalizacaoNovoFilho");
		
		Architecture architecture = givenAArchitecture2("generalizacaoNovoFilho");
		
		assertEquals(2, architecture.getAllGeneralizations().size());
		
		List<Element> generalization = architecture.getAllGeneralizations().get(0).getAllChildrenForGeneralClass();
		assertEquals(2, generalization.size());
		assertEquals("Class2", generalization.get(0).getName());
		assertEquals("Class5", generalization.get(1).getName());
	}
	
	@Test
	public void shouldCreateNewGeneralization() throws Exception{
		Architecture a = givenAArchitecture("generalizacaoMove");
		
		Class klass5 = a.findClassByName("Class5").get(0);
		Class klass4= a.findClassByName("Class4").get(0);
		
		a.forGeneralization().createGeneralization(klass5,klass4);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "generalizacaoNovaGeneralizacao");
		Architecture architecture = givenAArchitecture2("generalizacaoNovaGeneralizacao");
		
		assertEquals(2, architecture.getAllGeneralizations().size());
	}
	
	/* Fim generalizacao */
	
	@Test
	public void shouldMoveRealizationClient() throws Exception{
		Architecture a = givenAArchitecture("realization");
		
		Class klassFooBar = a.createClass("FooBar");
		RealizationRelationship realization = a.getAllRealizations().get(0);
		
		a.operationsOverRelationship().moveRealizationClient(realization, klassFooBar);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "realizationMoveClientGerada");
		
		Architecture architecture = givenAArchitecture2("realizationMoveClientGerada");
		assertEquals(1,architecture.getAllRealizations().size());
		assertEquals("FooBar",architecture.getAllRealizations().get(0).getClient().getName());
	}
	
	@Test
	public void shouldMoveRealizationSupplier() throws Exception{
		Architecture a = givenAArchitecture("realization");
		
		Class klassFooBar = a.createClass("FooBar");
		RealizationRelationship realization = a.getAllRealizations().get(0);
		
		a.operationsOverRelationship().moveRealizationSupplier(realization, klassFooBar);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "realizationMoveSupplierGerada");
		
		Architecture architecture = givenAArchitecture2("realizationMoveSupplierGerada");
		assertEquals(1,architecture.getAllRealizations().size());
		assertEquals("FooBar",architecture.getAllRealizations().get(0).getSupplier().getName());
	}
	
	@Test
	public void shouldMoveEntiereRealization() throws Exception{
		Architecture a = givenAArchitecture("realization");
		
		Class klassFoo = a.createClass("Foo");
		Class klassBar = a.createClass("Bar");
		
		RealizationRelationship realization = a.getAllRealizations().get(0);
		
		Element client = realization.getClient();
		Element supplier = realization.getSupplier();
		
		assertTrue(client.getIdsRelationships().contains(realization.getId()));
		assertTrue(supplier.getIdsRelationships().contains(realization.getId()));
		
		a.operationsOverRelationship().moveRealization(realization, klassFoo, klassBar);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "realizationMoveInteiraGerada");
		
		Architecture architecture = givenAArchitecture2("realizationMoveInteiraGerada");
		
		assertEquals(1,architecture.getAllRealizations().size());
		assertEquals("Bar",architecture.getAllRealizations().get(0).getSupplier().getName());
		assertEquals("Foo",architecture.getAllRealizations().get(0).getClient().getName());
		
		assertFalse(client.getIdsRelationships().contains(realization.getId()));
		assertFalse(supplier.getIdsRelationships().contains(realization.getId()));
	}
	
	@Test
	public void shouldCreateNewRealization() throws Exception{
		Architecture a = givenAArchitecture("realization");
		
		Class client = a.createClass("Foo");
		Class supplier = a.createClass("Bar");
		
		a.operationsOverRelationship().createNewRealization(client, supplier);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "realizationNova");
		Architecture architecture = givenAArchitecture2("realizationNova");
		assertEquals(2, architecture.getAllRealizations().size());
	}
	
	/* Fim Realização testes */
	
	/* Abstração testes */
	
	@Test
	public void shouldRemoveAbstraction() throws Exception {
		Architecture a = givenAArchitecture("abstractionInterElement");
		assertEquals(1, a.getAllAbstractions().size());
		a.forAbstraction().remove(a.getAllAbstractions().get(0));
		assertEquals(0,a.getAllAbstractions().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "abstractionInterElementRemoved");
		Architecture architecture = givenAArchitecture2("abstractionInterElementRemoved");
		assertEquals(0, architecture.getAllAbstractions().size());
	}
	
	@Test
	public void shouldCreateNewAbstraction() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		
		Class newClient = a.createClass("NewClient");
		Class newSupplier = a.createClass("NewSupplier");
		
		a.forAbstraction().create(newClient, newSupplier);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "newAbstraction");
		Architecture architecture = givenAArchitecture2("newAbstraction");
		assertEquals(2, architecture.getAllAbstractions().size());
	}
	
	@Test
	public void shouldMoveClientAbstraction() throws Exception {
		Architecture a = givenAArchitecture("abstractionInterElement");
		AbstractionRelationship abstractionRelationship = a.getAllAbstractions().get(0);
		assertEquals("myInterfaceClient", abstractionRelationship.getClient().getName());
		
		Class newClient = a.createClass("NewClient");
		
		assertEquals(0, newClient.getIdsRelationships().size());
		
		a.forAbstraction().moveClient(abstractionRelationship, newClient);
		assertEquals(1,a.getAllAbstractions().size());
		assertEquals(1,newClient.getIdsRelationships().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "abstractionInterElementMoveClient");
		Architecture architecture = givenAArchitecture2("abstractionInterElementMoveClient");
		
		assertEquals("NewClient", architecture.getAllAbstractions().get(0).getClient().getName());

	}
	
	@Test
	public void shouldMoveSupplierAbstraction() throws Exception {
		Architecture a = givenAArchitecture("abstractionInterElement");
		AbstractionRelationship abstractionRelationship = a.getAllAbstractions().get(0);
		assertEquals("myInterfaceClient", abstractionRelationship.getClient().getName());
		
		Class newSupplier = a.createClass("NewSupplier");
		
		assertEquals(0, newSupplier.getIdsRelationships().size());
		
		a.forAbstraction().moveSupplier(abstractionRelationship, newSupplier);
		assertEquals(1,a.getAllAbstractions().size());
		assertEquals(1,newSupplier.getIdsRelationships().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "abstractionInterElementMoveSupplier");
		Architecture architecture = givenAArchitecture2("abstractionInterElementMoveSupplier");
		
		AbstractionRelationship ab = architecture.getAllAbstractions().get(0);
		assertEquals("NewSupplier", ab.getSupplier().getName());
		
	}
	
	@Test
	public void shouldMoveEntireAbstraction() throws Exception {
		Architecture a = givenAArchitecture("abstractionInterElement");
		AbstractionRelationship abstractionRelationship = a.getAllAbstractions().get(0);
		
		Class newSupplier = a.createClass("NewSupplier");
		Class newCliente = a.createClass("NewClient");
		
		assertEquals(0, newSupplier.getIdsRelationships().size());
		
		a.forAbstraction().move(abstractionRelationship, newSupplier, newCliente);
		assertEquals(1,a.getAllAbstractions().size());
		assertEquals(1,newSupplier.getIdsRelationships().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "abstractionInterElementMoveAll");
		Architecture architecture = givenAArchitecture2("abstractionInterElementMoveAll");
		
		AbstractionRelationship ab = architecture.getAllAbstractions().get(0);
		assertEquals("NewSupplier", ab.getSupplier().getName());
		assertEquals("NewClient", ab.getClient().getName());
	}
	
	/* Fim testes abstração */
	
	@Test
	public void shouldRemoveUsage() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		
		assertEquals(1,a.getAllUsage().size());
		a.forUsage().remove(a.getAllUsage().get(0));
		
		assertEquals(0,a.getAllUsage().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "usageRemove");
		Architecture architecture = givenAArchitecture2("usageRemove");
		
		assertEquals(0,architecture.getAllUsage().size());
	}
	
	
	@Test
	public void shouldMoveClientUsage() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		
		Class newClient = a.createClass("newClient");
		
		a.forUsage().moveClient(a.getAllUsage().get(0), newClient);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "usageMoveClient");
		Architecture architecture = givenAArchitecture2("usageMoveClient");
		
		assertEquals("newClient",architecture.getAllUsage().get(0).getClient().getName());
	}
	
	@Test
	public void shouldMoveSupplierUsage() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		
		Class newSupplier = a.createClass("newSupplier");
		
		a.forUsage().moveSupplier(a.getAllUsage().get(0), newSupplier);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "usageMoveSupplier");
		Architecture architecture = givenAArchitecture2("usageMoveSupplier");
		
		assertEquals("newSupplier",architecture.getAllUsage().get(0).getSupplier().getName());
	}
	
	@Test
	public void shouldCreateNewUsage() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		
		Class newSupplier = a.createClass("newSupplier");
		Class newClient = a.createClass("newClient");
		
		a.forUsage().create(newClient, newSupplier);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "usageNova");
		Architecture architecture = givenAArchitecture2("usageNova");
		UsageRelationship usage = architecture.getAllUsage().get(1);
		
		assertEquals("newClient", usage.getClient().getName());
		assertEquals("newSupplier", usage.getSupplier().getName());
		
	}
	
	@Test
	public void shouldMoveEntireUsage() throws Exception{
		Architecture a = givenAArchitecture("usage3");
		
		Class newSupplier = a.createClass("newSupplier");
		Class newClient = a.createClass("newClient");
		
		a.forUsage().move(a.getAllUsage().get(0), newSupplier, newClient);
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "usageMovida");
		Architecture architecture = givenAArchitecture2("usageMovida");
		
		assertEquals(1,a.getAllUsage().size());
		assertEquals("newSupplier", architecture.getAllUsage().get(0).getSupplier().getName());
		assertEquals("newClient", architecture.getAllUsage().get(0).getClient().getName());
		
	}
}


