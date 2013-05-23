package mestrado.arquitetura.base;

import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;

public abstract class ArchitectureBase {
	
	public static DocumentManager givenADocument(String outputModelName) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		String pathToFiles = "src/main/java/mestrado/arquitetura/api/touml/1/";// model padrao vazio que o programa usa para construir o novo
		DocumentManager documentManager = new DocumentManager(outputModelName, pathToFiles);
		return documentManager;
	}
	
}
