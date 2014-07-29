import main.GenerateArchitecture;
import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;


public class teste {

	public static void main(String[] args) {
		
		ArchitectureBuilder builder = new ArchitectureBuilder();
		try {
			Architecture architecture = builder.create("/Users/elf/mestrado/sourcesMestrado/arquitetura/src/test/java/resources/concerns_teste.uml");
			
			GenerateArchitecture g = new GenerateArchitecture();
			
			g.generate(architecture, "dfdfd");
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}