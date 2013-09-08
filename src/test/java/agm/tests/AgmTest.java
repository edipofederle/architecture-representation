package agm.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Variability;

public class AgmTest extends TestHelper {
	
	private Architecture architecture;
	
	@Before
	public void setUp() throws Exception{
		String uriToArchitecture = getUrlToModel("AGM_TESTS/agm");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
	}
	
	@Test
	public void deveCarregarTodasAsClasses(){
		assertEquals("Deve ter 30 classes", 30, architecture.getAllClasses().size());
	}
	
	@Test
	public void deveCarregarTodasAsInterfaces(){
		assertEquals("Deve ter 14 interfaces", 14,architecture.getAllInterfaces().size());
	}
	
	@Test
	public void deveCarregarTodasAsVariabilidades(){
		assertEquals("Deve ter 5 variabilidades", 5, architecture.getAllVariabilities().size());
	}
	
	@Test
	public void deveCarregarTodosOsPacotes(){
		assertEquals(9, architecture.getAllPackages().size());
	}
	
	@Test
	public void deveCarregarTodosOsInteresses(){
		assertEquals("Deve ter 11 interesses", 11, architecture.getAllConcerns().size());
	}
	
	@Test
	public void classeMovableSpritesDeveTerDoisInteresses() throws ClassNotFound{
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		assertEquals(2,movableSprites.getOwnConcerns().size());
		
		assertEquals("collision", movableSprites.getOwnConcerns().get(0).getName());
		assertEquals("movement", movableSprites.getOwnConcerns().get(1).getName());
	}
	
	@Test
	public void classeMovableSpritesDeveSerUmPontoDeVariacao() throws ClassNotFound{
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		assertTrue(movableSprites.isVariationPoint());
	}
	
	@Test
	public void classeMovableSpritesDeveSerAlternativeOr() throws ClassNotFound{
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		assertEquals("alternative_OR", movableSprites.getVariantType());
	}
	
	@Test
	public void classeMovableSpritesDeveTerTresAttributos() throws ClassNotFound{
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		assertEquals(3,movableSprites.getAllAttributes().size());		
	}
	
	@Test
	public void classeMovableSpritesIsMovingAttributeCorrectProps() throws Exception{
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		Attribute isMoving = movableSprites.findAttributeByName("isMoving");
		
		assertNotNull(isMoving);
		assertEquals("isMoving", isMoving.getName());
		assertEquals("Boolean", isMoving.getType());
		
		assertEquals("Atributo deve ter 1 interesse", 1, isMoving.getOwnConcerns().size());
		assertEquals("movement", isMoving.getOwnConcerns().get(0).getName());
	}
	
	@Test
	public void classeMovableSpritesDeveTerSeteMetodos() throws Exception{ 
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		
		assertEquals("Deve ter 7 metodos", 7, movableSprites.getAllMethods().size());
	}
	
	@Test
	public void classeMovableSpritesMovingMetodoCorrectProps() throws Exception{
		Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		Method moving = movableSprites.findMethodByName("moving");
		
		assertNotNull(moving);
		assertEquals("moving", moving.getName());
		assertEquals("Boolean", moving.getReturnType());
		
		assertEquals(1, moving.getOwnConcerns().size());
		assertEquals("movement", moving.getOwnConcerns().get(0).getName());
	}
	
	/* Interfaces */
	
	@Test
	public void interfaceIGameBoardData() throws InterfaceNotFound{
		Interface iGameBoardData = architecture.findInterfaceByName("IGameBoardData");
		
		assertNotNull(iGameBoardData);
		assertEquals(1, iGameBoardData.getOperations().size());
		
		Method operation = iGameBoardData.getOperations().get(0);
		assertEquals("getGameBoard", operation.getName());
		
		//Params
		assertEquals(1, operation.getParameters().size());
		assertEquals("gameCode",operation.getParameters().get(0).getName());
		assertEquals("String", operation.getParameters().get(0).getType());
		assertEquals("", operation.getReturnType());
		
		//Concerns
		assertEquals(1, operation.getOwnConcerns().size());
		assertEquals("play", operation.getOwnConcerns().get(0).getName());
	}
	
	/* Interfaces */
	
	
	/* Variabilidades */
	
	@Test
	public void movableSpriteVariability(){
		Variability var1 = architecture.getAllVariabilities().get(0);
		assertEquals("movable_sprite", var1.getName());
		
		assertEquals("1", var1.getMinSelection());
		assertEquals("2", var1.getMaxSelection());
		assertNotNull(var1.getVariationPoint());
		assertEquals(2, var1.getVariants().size());
		
		assertEquals("Puck", var1.getVariants().get(0).getName());
		assertEquals("Paddle", var1.getVariants().get(1).getName());
		
		assertEquals("MovableSprites", var1.getVariationPoint().getVariationPointElement().getName());
	}
	
	/* Variabilidades */
	
	
	
	@Test
	public void teste_1() throws Exception{

		
//		System.out.println("Interfaces:"+ architecture.getAllInterfaces());
//		System.out.println("Classes:"+architecture.getAllClasses());
//		System.out.println(architecture.getAllConcerns());
//		System.out.println(architecture.getAllPackages());
		
		arquitetura.representation.Class movableSprites = architecture.findClassByName("MovableSprites").get(0);
		//arquitetura.representation.Class player = architecture.findClassByName("Player").get(0);
		
		assertNotNull(movableSprites);
		assertTrue(movableSprites.isVariationPoint());
		
		//System.out.println(movableSprites.getVariationPoint().getVariabilities().get(0).getName());
		
//		for (Variability v : VariabilityFlyweight.getInstance().getVariabilities()) {
//			System.out.println("Nome:"+v.getName() + "| ClassOwner:"+v.getOwnerClass());
//			for(Variant variant : v.getVariants()){
//				System.out.println("\t"+variant.getName());
//			}
//			 
//		}
	
	}

}
