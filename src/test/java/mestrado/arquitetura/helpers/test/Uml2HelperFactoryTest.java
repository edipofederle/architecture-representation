package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.*;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;

import org.junit.Test;

public class Uml2HelperFactoryTest {
	
	@Test
	public void shouldBeASingleton(){
		Uml2Helper uml2Helper1 = Uml2HelperFactory.getUml2Helper();
		Uml2Helper uml2Helper2 = Uml2HelperFactory.getUml2Helper();
		
		assertSame(uml2Helper1, uml2Helper2);
	}

}