package mestrado.arquitetura.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DocumentManager {
	
	private org.w3c.dom.Document docUml;
	private org.w3c.dom.Document docNotation;
	private org.w3c.dom.Document docDi;
	private String originalModelName;
	private String outputModelName;
	
	static Logger LOGGER = LogManager.getLogger(DocumentManager.class.getName());
	
	public DocumentManager(String outputModelName, String pathToFiles, String originalModelName){
		this.outputModelName = outputModelName;
		this.originalModelName = originalModelName;
		
		
		makeACopy(pathToFiles, originalModelName);
		createXMIDocument(originalModelName);
		
		
		this.saveAndCopy(outputModelName);
		
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
			this.docNotation =  docBuilderNotation.parse("manipulation/"+this.originalModelName+".notation");
			this.docUml = docBuilderUml.parse("manipulation/"+this.originalModelName+".uml");
			this.docDi = docBuilderUml.parse("manipulation/"+this.originalModelName+".di");
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
		
		String notationCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.originalModelName+".notation";
		String umlCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.originalModelName+".uml";
		String diCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+this.originalModelName+".di";
		
		try {
			CopyFile.copyFile(new File(pathToFiles+modelName+".notation"), new File(notationCopy));
			CopyFile.copyFile(new File(pathToFiles+modelName+".uml"), new File(umlCopy));
			CopyFile.copyFile(new File(pathToFiles+modelName+".di"), new File(diCopy));
						
		} catch (IOException e) {
			LOGGER.error("I cannot copy all files. Here a message erros: " + e.getMessage());
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

	public void saveAndCopy(String newModelName) {
		this.outputModelName = newModelName;
		
		try {
			SaveAndCopy.saveAndCopy(docNotation, docUml, docDi, this.originalModelName, newModelName);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getModelName() {
		return this.originalModelName;
	}

	public String getNewModelName() {
		return this.outputModelName;
	}
	
}
