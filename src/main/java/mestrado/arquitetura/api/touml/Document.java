package mestrado.arquitetura.api.touml;


/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
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