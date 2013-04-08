package mestrado.arquitetura.parser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class DocumentManager {
	
	private org.w3c.dom.Document docUml;
	private org.w3c.dom.Document docNotation;
	private String newModelName;
	
	private final static Logger LOGGER = Logger.getLogger(DocumentManager.class.getName()); 
	
	public DocumentManager(String pathToFiles, String originalModelName, String newModelName){
		this.newModelName = newModelName;
		makeACopy(pathToFiles, originalModelName);
		createXMIDocument(originalModelName);
	}
	
	private void createXMIDocument(String modelName){
		DocumentBuilderFactory docBuilderFactoryNotation = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilderNotation = null;
		DocumentBuilder docBuilderUml = null;
		try {
			docBuilderNotation = docBuilderFactoryNotation.newDocumentBuilder();
			DocumentBuilderFactory docBuilderFactoryUml = DocumentBuilderFactory.newInstance();
			docBuilderUml = docBuilderFactoryUml.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
			
		try {
			this.docNotation =  docBuilderNotation.parse("/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.newModelName+".notation");
			this.docUml = docBuilderUml.parse("/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.newModelName+".uml");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Realiza um cópia dos três arquivos para o diretório <b>manipulation</b>.
	 * 
	 * @param pathToFiles
	 * @param modelName
	 * @throws IOException
	 */
	private void makeACopy(String pathToFiles, String modelName) {
		
		String notationCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.newModelName+".notation";
		String umlCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.newModelName+".uml";
		String diCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.newModelName+".di";
		
		try {
			CopyFile.copyFile(new File(pathToFiles+modelName+".notation"), new File(notationCopy));
			CopyFile.copyFile(new File(pathToFiles+modelName+".uml"), new File(umlCopy));
			CopyFile.copyFile(new File(pathToFiles+modelName+".di"), new File(diCopy));
		} catch (IOException e) {
			LOGGER.severe("I cannot copy all files. Here a message erros: " + e.getMessage());
		}
	}

	/**
	 * @return the docUml
	 */
	public org.w3c.dom.Document getDocUml() {
		return docUml;
	}

	/**
	 * @return the docNotation
	 */
	public org.w3c.dom.Document getDocNotation() {
		return docNotation;
	}
	
	public void saveAndCopy() {
		try {
			SaveAndCopy.saveAndCopy(docNotation, docUml, this.newModelName);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
