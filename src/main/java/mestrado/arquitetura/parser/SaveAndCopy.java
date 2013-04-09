package mestrado.arquitetura.parser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SaveAndCopy {
	
	private final static Logger LOGGER = Logger.getLogger(SaveAndCopy.class.getName()); 
	
	
	// TODO mudar caminho para arquivo de configuração
	public static void saveAndCopy(Document docNotation, Document docUml, Document docDi, String originalModelName, String newModelName) throws TransformerException, IOException{
		String targetDir = ReaderConfig.getDirTarget();
		String targetDirExport = ReaderConfig.getDirExportTarget();
		
		String notationCopy = targetDir+originalModelName+".notation";
		String umlCopy = targetDir+originalModelName +".uml";
		String diCopy = targetDir+originalModelName +".di";
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");


		//Necessario atualizar referencias pois nome do modelo pode ter mudado
		NodeList elements = docNotation.getElementsByTagName("element");
		for (int i = 0; i < elements.getLength(); i++) {
			String idXmi = getOnlyIdOfXmiAttribute(elements, i);
			elements.item(i).getAttributes().getNamedItem("href").setNodeValue(newModelName+".uml#"+idXmi);
		}
		
		NodeList elementsUml = docDi.getElementsByTagName("emfPageIdentifier");
		for (int i = 0; i < elementsUml.getLength(); i++) {
			String idXmi = getOnlyIdOfXmiAttribute(elementsUml, i);
			elementsUml.item(i).getAttributes().getNamedItem("href").setNodeValue(newModelName+".notation#"+idXmi);
		}
		
		DOMSource source = new DOMSource(docNotation);
		StreamResult result = new StreamResult(new File(notationCopy));
		transformer.transform(source, result);
		
		DOMSource sourceUml = new DOMSource(docUml);
		StreamResult resultUml = new StreamResult(new File(umlCopy));
		transformer.transform(sourceUml, resultUml);
		
		
		DOMSource sourceDi = new DOMSource(docDi);
		StreamResult resultDi = new StreamResult(new File(diCopy));
		transformer.transform(sourceDi, resultDi);
		
		LOGGER.info("Copying files to Papyrus Workspace...");

		FileUtils.moveFiles(notationCopy, targetDirExport+ newModelName + ".notation");
		FileUtils.moveFiles(umlCopy, targetDirExport+ newModelName +".uml");
		FileUtils.moveFiles(diCopy, targetDirExport+newModelName +".di");
		
		System.out.println("Write: Done");
	}

	private static String getOnlyIdOfXmiAttribute(NodeList elements, int i) {
		String currentValue = elements.item(i).getAttributes().getNamedItem("href").getNodeValue();
		return currentValue.substring(currentValue.indexOf("#")+1, currentValue.length());
	}

}
