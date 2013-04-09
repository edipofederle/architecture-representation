package mestrado.arquitetura.parser;

import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class RemoveNode extends XmiHelper {
	
	private final static Logger LOGGER = Logger.getLogger(RemoveNode.class.getName()); 
	
	private Document docUml;
	private Document docNotation;
	
	
	public RemoveNode(Document docUml, Document docNotation){
		this.docUml = docUml;
		this.docNotation = docNotation;
	}
	
	/**
	 * Remove node de um dado documento com o id informado.
	 * 
	 * @param document
	 * @param id
	 */
	public void removeClassById(String id){
		Node umlNOde = this.docUml.getElementsByTagName("uml:Model").item(0);
		try{
			umlNOde.removeChild(findByID(this.docUml, id, "packagedElement"));
			removeNodeFromNotationFile(id);
			LOGGER.info("Class with id: " + id + " removed.");
		}catch (Exception e) {
			LOGGER.info("Cannot reemove Class with id: " + id + ".");
		}
	}
	
	private void removeNodeFromNotationFile(String id) {
		Node notationNode = findByIDInNotationFile(this.docNotation, id);
		Node notationModel = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
		try{
			notationModel.removeChild(notationNode);
		}catch (Exception e) {
			LOGGER.info("Problem when trying remove node with id: "+ id + " from notation file. " + e.getMessage());
		}
		
	}

}
