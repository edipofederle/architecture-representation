package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.touml.DocumentManager;
import arquitetura.touml.Operations;

public class AssociationClassTest extends TestHelper {
	
	@Test
	public void testLoadAssociationClass() throws Exception{
		Architecture a = givenAArchitecture("associationClass/associationClass");
		
		List<AssociationClassRelationship> associationClasses = a.getAllAssociationsClass();
		
		assertEquals(1, associationClasses.size());
		assertEquals(2, associationClasses.get(0).getMemebersEnd().size());
	}
	
	@Test
	public void associationClassShouldBelongsToPackage() throws Exception{
		Architecture a = givenAArchitecture("associationClass/associationClassComPacote");
		assertNotNull(a.getAllAssociationsClass().get(0).getPackageOwner());
		
	}
	
	@Test
	public void associationClass1() throws Exception {
		DocumentManager doc = givenADocument("associationClassGerado");
		
		Architecture a = givenAArchitecture("associationClass/associationClass");
		Operations op = new Operations(doc, a);
		
		generateClasses(a, op);
		
		for(AssociationClassRelationship asr : a.getAllAssociationsClass()){
			op.forAssociationClass()
			  .createAssociationClass(asr)
			  .build();
		}

		Architecture genereted = givenAArchitecture2("associationClassGerado");
		assertNotNull(genereted);
		assertEquals(3,genereted.getAllAssociationsClass().get(0).getAttributes().size());
	}
	
	@Test
	public void associationClass2() throws Exception {
		DocumentManager doc = givenADocument("associationClass2Gerado");
		
		Architecture a = givenAArchitecture("associationClass/associationClass2");
		Operations op = new Operations(doc, a);
		
		generateClasses(a, op);
		
		
		for(AssociationClassRelationship asr : a.getAllAssociationsClass()){
			op.forAssociationClass()
			  .createAssociationClass(asr)
			  .build();
		}

		Architecture genereted = givenAArchitecture2("associationClass2Gerado");
		assertEquals(1, genereted.getAllAssociationsClass().size());
		
	}
	
	@Test
	public void associationClass3() throws Exception {
		DocumentManager doc = givenADocument("associationClassComPacoteGerado");
		
		Architecture a = givenAArchitecture("associationClass/associationClassComPacote");
		Operations op = new Operations(doc, a);
		
		generateClasses(a, op);
		
		List<Package> packages = a.getAllPackages();
		
		for (Package pack : packages) {
			//Todas as classes do pacote
			List<String> ids = pack.getAllClassIdsForThisPackage();
			op.forPackage().createPacakge(pack).withClass(ids).build();
		}
		
		for(AssociationClassRelationship asr : a.getAllAssociationsClass()){
			op.forAssociationClass()
			  .createAssociationClass(asr).build();
			
			op.forPackage().withId(asr.getPackageOwner()).add(asr.getId());
		}
		

		Architecture genereted = givenAArchitecture2("associationClassComPacoteGerado");
		assertNotNull(genereted);
	}
	
	@Test
	public void comAttrEMethod() throws Exception{
		DocumentManager doc = givenADocument("associationClassWithAttrAndMethodGerado");
		
		Architecture a = givenAArchitecture("associationClass/associationClassWithAttrAndMethod");
		Operations op = new Operations(doc, a);

		
		generateClasses(a, op);
		
		
		List<Package> packages = a.getAllPackages();
		
		for (Package pack : packages) {
			//Todas as classes do pacote
			List<String> ids = pack.getAllClassIdsForThisPackage();
			op.forPackage().createPacakge(pack).withClass(ids).build();
		}
		
		for(AssociationClassRelationship asr : a.getAllAssociationsClass()){
			op.forAssociationClass()
			  .createAssociationClass(asr).build();
			
			op.forPackage().withId(asr.getPackageOwner()).add(asr.getId());
		}
		
		Architecture ar = givenAArchitecture2("associationClassWithAttrAndMethodGerado");
		assertEquals(1 ,ar.getAllAssociationsClass().size());
		assertEquals(3, ar.getAllAssociationsClass().get(0).getAttributes().size());
		assertEquals(1, ar.getAllAssociationsClass().get(0).getMethods().size());
	}

}
