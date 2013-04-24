package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.NodeNotFound;


public class Document {

	public static void executeTransformation(DocumentManager documentManager,
			Transformation transformation) throws CustonTypeNotFound, NodeNotFound {
		try {
			transformation.useTransformation();
		} finally {
			documentManager.saveAndCopy(documentManager.getNewModelName());
		}
	}

}