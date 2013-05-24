package mestrado.arquitetura.api.touml;



public class Document {

	public static void executeTransformation(DocumentManager documentManager,
			Transformation transformation){
		try {
			transformation.useTransformation();
		} finally {
			documentManager.saveAndCopy(documentManager.getNewModelName());
		}
	}

}