package arquitetura.touml;

import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class NoteOperations extends XmiHelper {
	
	private DocumentManager documentManager;
	private ElementXmiGenerator elementXmiGenerator;
	private String id;
	
	public NoteOperations(DocumentManager documentManager){
		this.documentManager = documentManager;
		this.elementXmiGenerator = new ElementXmiGenerator(documentManager);

	}

	public NoteOperations createNote()  {
		final NoteNode noteNode = new NoteNode(documentManager);
		this.id = UtilResources.getRandonUUID();
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				noteNode.createNote(id);
			}
		});
		
		return this;
	}

	public String build() {
		return this.id;
	}

	public NoteOperations addVariability(final String idNote, final VariabilityStereotype variability) {
		arquitetura.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.createStereotypeVariability(idNote, variability);
			}
		});
		return this;
	}
	
}