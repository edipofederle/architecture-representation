package mestrado.arquitetura.parser;

public class Document {

	public static void executeTransformation(DocumentManager documentManager,
			Transformation transformation) {
		try {
			transformation.useTransformation();
		} finally {
			documentManager.saveAndCopy(documentManager.getNewModelName());
		}
	}

}
