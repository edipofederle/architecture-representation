package mestrado.arquitetura.parser;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

public class DocumentOperations {
	
	private mestrado.arquitetura.parser.DocumentManager document;
	
	private final static Logger LOGGER = Logger.getLogger(DocumentOperations.class.getName()); 
	
	/**
	 * Recebe o path dos arquivos (.uml, .notaiton e .di). e o nome do modelo.
	 * 
	 * @param pathToFiles
	 * @param modelName
	 * @throws IOException
	 */
	public DocumentOperations(mestrado.arquitetura.parser.DocumentManager document){
		this.document = document;
	}

	/**
	 * Retorna o id da classe criada.
	 * 
	 * @param klassName
	 * @return
	 */
	public String createClass(String klassName) {
		ClassNotation c = new ClassNotation(this.document.getDocNotation(), this.document.getDocUml());
		c.createClass(klassName);
		return c.getId();
	}

	public void removeClassById(String id) {
		RemoveNode removeClass = new RemoveNode(this.document.getDocUml(), this.document.getDocNotation());
		removeClass.removeClassById(id);
	}
	
	/**
	 * Retorna o id da associão
	 * 
	 * @param idXPTO
	 * @param idClassBar
	 * @return
	 */
	public String createAssociation(String idXPTO, String idClassBar){
		
		AssociationNode associationNode = new AssociationNode(this.document.getDocUml(), this.document.getDocNotation());
		associationNode.createAssociation(idXPTO, idClassBar);
		return associationNode.getIdAssocation();
	}

	public void removeAssociation(String idAssociation) {
		AssociationNode associationNode = new AssociationNode(this.document.getDocUml(), this.document.getDocNotation());
			associationNode.removeAssociation(idAssociation);
	}

	
}