package mestrado.arquitetura.representation.test;

import static org.junit.Assert.*;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Test;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;

public class AbstractionRelationshipTest extends TestHelper {
	
	@Test
	public void testPackageInterfaceAbstraction() throws Exception{
		Architecture a = givenAArchitecture("abstractionTestPackageInterface");
		
		assertEquals(1,a.getAllRelationships().size());
		assertEquals(1,a.getAllAbstractions().size());
		
		System.out.println(a.getAllAbstractions().iterator().next().getClient().getName());
		System.out.println(a.getAllAbstractions().iterator().next().getSupplier().getName());
		System.out.println( ((Package) a.getAllAbstractions().iterator().next().getClient()).getImplementedInterfaces());
	}

}
