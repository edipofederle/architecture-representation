package mestrado.arquitetura.api.touml;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;


public class Document {

	public static void executeTransformation(DocumentManager documentManager,
			Transformation transformation) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		try {
			transformation.useTransformation();
		} finally {
			documentManager.saveAndCopy(documentManager.getNewModelName());
		}
	}

}