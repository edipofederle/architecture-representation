package mestrado.arquitetura.helpers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;

public class XmiHelperTest extends TestHelper {
	
	private String id;
	private String idPackage;
	
	@Before
	public void setUp() throws Exception{
		Architecture a = givenAArchitecture("classes");
		Architecture b = givenAArchitecture("package");

		id = a.getAllClasses().get(0).getId();
		idPackage = b.getAllPackages().get(0).getId();
	}

	@Test
	public void getX() throws Exception {
		XmiHelper.setNotationOriginalFile("/Users/edipofederle/sourcesMestrado/arquitetura/src/test/java/resources/classes.uml");
		assertEquals("284", XmiHelper.getXValueForElement( id));
	}
	
	@Test
	public void getY() throws Exception {
		XmiHelper.setNotationOriginalFile("/Users/edipofederle/sourcesMestrado/arquitetura/src/test/java/resources/classes.uml");
		assertEquals("97", XmiHelper.getYValueForElement(id));
	}
	
	@Test
	public void getXForPackage(){
		assertEquals("135", XmiHelper.getXValueForElement(idPackage));
	}
	
	@Test
	public void getYForPackage(){
		assertEquals("45", XmiHelper.getYValueForElement(idPackage));
	}
	
	@Test
	public void getWidthForPackage(){
		assertEquals("308", XmiHelper.getWidhtForPackage(idPackage));
	}
	
	@Test
	public void getHeightForPackage(){
		assertEquals("240", XmiHelper.getHeightForPackage(idPackage));
	}
	
	@Test
	public void setNotationOriginalFile(){
		XmiHelper.setNotationOriginalFile("/Users/edipofederle/sourcesMestrado/arquitetura/src/test/java/resources/package.uml");
		assertNotNull(XmiHelper.getOriginalNotation());
	}

}