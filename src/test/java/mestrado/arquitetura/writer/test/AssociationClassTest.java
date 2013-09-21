package mestrado.arquitetura.writer.test;

import static org.junit.Assert.assertEquals;

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
		String id = packages.get(0).getId();
		
		for (Package pack : packages) {
			//Todas as classes do pacote
			List<String> ids = pack.getAllClassIdsForThisPackage();
			op.forPackage().createPacakge(pack).withClass(ids).build();
		}
		
		for(AssociationClassRelationship asr : a.getAllAssociationsClass()){
			op.forAssociationClass()
			  .createAssociationClass(asr).build();
			
			op.forPackage().withClass(asr.getId());
		}
		

		Architecture genereted = givenAArchitecture2("associationClassComPacoteGerado");
		
	}
	


}
