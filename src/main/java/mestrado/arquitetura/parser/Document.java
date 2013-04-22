package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;


public class Document {

	public static void executeTransformation(DocumentManager documentManager,
			Transformation transformation) throws CustonTypeNotFound {
		try {
			transformation.useTransformation();
		} finally {
			documentManager.saveAndCopy(documentManager.getNewModelName());
		}
	}

}