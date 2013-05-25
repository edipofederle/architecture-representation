package mestrado.arquitetura.base;

import mestrado.arquitetura.api.touml.DocumentManager;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public abstract class ArchitectureBase {
	
	public static DocumentManager givenADocument(String outputModelName) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
		DocumentManager documentManager = new DocumentManager(outputModelName);
		return documentManager;
	}
	
}
