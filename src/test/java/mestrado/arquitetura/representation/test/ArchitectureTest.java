package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Class;
import mestrado.arquitetura.representation.Concern;
import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Package;
import mestrado.arquitetura.representation.Variability;
import mestrado.arquitetura.representation.VariantType;

import org.junit.Before;
import org.junit.Test;

public class ArchitectureTest {
	
	private Architecture arch;
	
	@Before
	public void setUp(){
		arch = new Architecture("arquitetura");
	}
	
	@Test
	public void shouldArchitectureHaveAName(){
		assertEquals("arquitetura", arch.getName());
	}
	
	@Test
	public void shouldArchitectureHaveAEmptyNameWhenNull(){
		arch.setName(null);
		
		assertEquals("", arch.getName());
	}
	
	@Test
	public void shouldCreateConcernWhenNotExists(){
		assertEquals("core", arch.getOrCreateConcernByName("core").getName());
		assertEquals(1,arch.getConcerns().size());
	}
	
	@Test
	public void shouldNotCreateConcernWhenExists(){
		assertEquals(0, arch.getConcerns().size());
		assertEquals("core", arch.getOrCreateConcernByName("core").getName());
		assertEquals(1, arch.getConcerns().size());
		
		Concern concern = arch.getOrCreateConcernByName("core");
		
		assertSame(arch.getConcerns().get("core"), concern);
	}
	
	@Test
	public void shouldReturnAllPackages(){
		Package pkg = new Package(arch, "Pacote", false, VariantType.MANDATORY, null);
		arch.getElements().add(pkg);
		
		assertEquals(1, arch.getPackages().size());
		assertEquals("Pacote", arch.getPackages().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoPackages(){
		assertEquals(Collections.emptyList(), arch.getPackages());
	}
	
	@Test
	public void shouldReturnAllClasses(){
		Class klass = new Class(arch, "Klass", false,  VariantType.MANDATORY, false, null, false);
		arch.getElements().add(klass);
		
		assertEquals(1, arch.getClasses().size());
		assertEquals("Klass", arch.getClasses().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoClasses(){
		assertEquals(Collections.emptyList(), arch.getClasses());
	}
	
	@Test
	public void shouldReturnVariabilities(){
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("name", "var");
		Variability v = new Variability("var", "1", "1", false, attributes, null);
		arch.getVariabilities().add(v);
		
		assertNotNull(arch.getVariabilities().get(0));
		assertEquals("var", arch.getVariabilities().get(0).getName());
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoVariabilities(){
		assertEquals(Collections.emptyList(), arch.getVariabilities());
	}
	
	@Test
	public void shouldReturnNullWhenElementNotFound(){
		assertNull(arch.findElementByName("KlassNotFound"));
	}
	
	@Test
	public void shouldReturnElementClassByName(){
		arch.getElements().add(new Class(arch, "Klass", false,  VariantType.MANDATORY, false, null, false));
		Element klass = arch.findElementByName("klass");
		
		assertNotNull(klass);
		assertEquals("Klass", klass.getName());
	}
	
	@Test
	public void shouldReturnElementPackageByName(){
		arch.getElements().add( new Package(arch, "Pacote", false, VariantType.MANDATORY, null));
		Element pkg = arch.findElementByName("Pacote");
		
		assertNotNull(pkg);
		assertEquals("Pacote", pkg.getName());
	}
	
	
}
