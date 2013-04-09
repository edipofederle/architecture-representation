package mestrado.arquitetura.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SaveAndCopy {
	
	
	// TODO mudar caminho para arquivo de configuração
	public static void saveAndCopy(Document docNotation, Document docUml, Document docDi, String originalModelName, String newModelName) throws TransformerException, IOException{
		
		String notationCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+originalModelName+".notation";
		String umlCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+originalModelName +".uml";
		
		String diCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+originalModelName +".di";
		// write the content into xml file
		
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

		System.out.println("Write: Done");
		
		System.out.print("Copying files to Papyrus Workspace...");
		

		FileUtils.moveFiles(notationCopy, "/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/"+ newModelName + ".notation");
		FileUtils.moveFiles(umlCopy, "/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/"+ newModelName +".uml");
		FileUtils.moveFiles(diCopy, "/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/"+newModelName +".di");
		
		System.out.println("Write: Done");
	}

	private static String getOnlyIdOfXmiAttribute(NodeList elements, int i) {
		String currentValue = elements.item(i).getAttributes().getNamedItem("href").getNodeValue();
		return currentValue.substring(currentValue.indexOf("#")+1, currentValue.length());
	}

}
