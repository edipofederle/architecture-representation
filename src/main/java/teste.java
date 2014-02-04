import main.GenerateArchitecture;
import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;


public class teste {

	public static void main(String[] args) {
		ArchitectureBuilder builder = new ArchitectureBuilder();
		GenerateArchitecture generate =  new GenerateArchitecture();
		try {
			Architecture a = builder.create("/Users/elf/Documents/workspaceModeling/profiles/demo.uml");
			generate.generate(a, "demoOut");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}