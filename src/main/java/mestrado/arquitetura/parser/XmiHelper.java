package mestrado.arquitetura.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmiHelper {
	
	public static Node findByIDInNotationFile(Document docNotaion, String id) {
		NodeList node = docNotaion.getElementsByTagName("children");
		for (int i = 0; i < node.getLength(); i++) {
			NodeList nodes = node.item(i).getChildNodes();
			for (int j = 0; j < nodes.getLength(); j++) {
				if(nodes.item(j).getNodeName().equalsIgnoreCase("element")){
					NamedNodeMap attrs = nodes.item(j).getAttributes();
					for (int k = 0; k < attrs.getLength(); k++) {
						if(attrs.item(k).getNodeValue().contains(id)){
							return node.item(i);
						}
					}
				}
			}
		}	
		return null;
	}

	public static Node findByID(Document doc, String id, String tagName) {
		NodeList node = doc.getElementsByTagName(tagName);
		for (int i = 0; i < node.getLength(); i++) {
			NamedNodeMap attributtes = node.item(i).getAttributes();
			for (int j = 0; j < attributtes.getLength(); j++) {
				if (id.equalsIgnoreCase(attributtes.item(j).getNodeValue())) {
					return node.item(i);
				}
			}
		}
		return null;
	}


}
