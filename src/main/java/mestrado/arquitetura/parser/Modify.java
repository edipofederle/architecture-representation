//package mestrado.arquitetura.parser;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;
//
//public class Modify {
//
//	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, TransformerException {
//
//		String notation = "/Users/edipofederle/sourcesMestrado/arquitetura/src/main/java/mestrado/arquitetura/parser/simples.notation";
//
//		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//		Document doc = docBuilder.parse(notation);
//
//		// Get the root element
//		Node company = doc.getFirstChild();
//
//		Node children = doc.getElementsByTagName("notation:Diagram").item(0);
//		System.out.println(children);
//		Node staff = doc.getElementsByTagName("element").item(0);
//		System.out.println(staff.getAttributes().item(0).getNodeValue());
//		System.out.println(staff.getAttributes().item(0).getNodeName());
//		
//		
//		// append a new node to staff
//		Element age = doc.createElement("children");
//		age.setAttribute("href", "simples.uml#_Hnc9QJLxEaKiAcjfUSJfdg");
//		age.setAttribute("xmi:type", "uml:Class");
//		
//		children.appendChild(age);
//		
//		
//	//	ClassNotation.createChildrenNode(doc);
//		
//		
//		
//		
//		
//		
//		
//
////		// update staff attribute
////		NamedNodeMap attr = staff.getAttributes();
////		Node nodeAttr = attr.getNamedItem("href");
////		nodeAttr.setTextContent("meuNovoId");
//
//		System.out.println(staff.getAttributes().item(0).getNodeValue());
//
//		// write the content into xml file
//		TransformerFactory transformerFactory = TransformerFactory
//				.newInstance();
//		Transformer transformer = transformerFactory.newTransformer();
//		DOMSource source = new DOMSource(doc);
//		StreamResult result = new StreamResult(new File(notation));
//		transformer.transform(source, result);
//
//		System.out.println("Done");
//	}
//
//}
