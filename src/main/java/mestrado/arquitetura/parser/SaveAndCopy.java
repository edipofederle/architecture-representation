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

public class SaveAndCopy {
	
	
	// TODO mudar caminho para arquivo de configuração
	public static void saveAndCopy(Document docNotation, Document docUml, String name) throws TransformerException, IOException{
		
		String notationCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+name+".notation";
		String umlCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+name +".uml";
		
		String diCopy = "/Users/edipofederle/sourcesMestrado/arquitetura/manipulation/"+name +".di";
		// write the content into xml file
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		DOMSource source = new DOMSource(docNotation);
		StreamResult result = new StreamResult(new File(notationCopy));
		transformer.transform(source, result);
		
		DOMSource sourceUml = new DOMSource(docUml);
		StreamResult resultUml = new StreamResult(new File(umlCopy));
		transformer.transform(sourceUml, resultUml);
		

		System.out.println("Write: Done");
		
		
		System.out.print("Copying files to Papyrus Workspace...");
		
		File notationDest = new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/"+ name + ".notation");
		File notationSource = new File(notationCopy);
		
		File umlDest = new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/"+ name +".uml");
		File umlSource = new File(umlCopy);
		
		File diDest = new File("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/"+name +".di");
		File diSource = new File(diCopy);
	
		
		CopyFile.copyFile(notationSource, notationDest);
		CopyFile.copyFile(umlSource, umlDest);
		CopyFile.copyFile(diSource, diDest);
		
		System.out.println("Write: Done");
	}

}
