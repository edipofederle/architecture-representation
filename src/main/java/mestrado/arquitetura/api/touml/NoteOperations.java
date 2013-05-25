package mestrado.arquitetura.api.touml;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.utils.UtilResources;
import mestrado.arquitetura.writer.VariabilityStereotype;

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

	public NoteOperations createNote() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final NoteNode noteNode = new NoteNode(documentManager);
		this.id = UtilResources.getRandonUUID();
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				noteNode.createNote(id);
			}
		});
		
		return this;
	}

	public String build() {
		return this.id;
	}

	public NoteOperations addVariability(final String idNote, final VariabilityStereotype a) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.createStereotypeVariability(idNote, a);
			}
		});
		return this;
	}
	
}