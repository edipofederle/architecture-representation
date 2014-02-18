//import java.util.List;
//
//import main.GenerateArchitecture;
//import arquitetura.builders.ArchitectureBuilder;
//import arquitetura.helpers.UtilResources;
//import arquitetura.representation.Architecture;
//import arquitetura.representation.Class;
//import arquitetura.representation.Interface;
//
//
//public class teste {
//
//	public static void main(String[] args) {
//		
//		ArchitectureBuilder builder = new ArchitectureBuilder();
//		try {
//			Architecture architecture = builder.create("/Users/elf/mestrado/sourcesMestrado/arquitetura/src/test/java/resources/patternsSte.uml");
//			
//			GenerateArchitecture g = new GenerateArchitecture();
//			
//			g.generate(a, "dfdfd");
//			
//			
//			arquitetura.representation.Package aPackage = architecture.createPackage("PacoteDeTeste");
//			Interface anInterface = aPackage.createInterface("InterfaceDeTeste");
//			String packageName1= UtilResources.extractPackageName(anInterface.getNamespace());
//			System.out.println(packageName1);
//			
//			Class targetClass = architecture.findClassByName("Foo").get(0);
//			String packageName = UtilResources.extractPackageName(targetClass.getNamespace());
//			System.out.println(architecture.findPackageByName(packageName));
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}