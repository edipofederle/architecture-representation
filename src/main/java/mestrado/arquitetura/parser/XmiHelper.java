package mestrado.arquitetura.parser;

import java.util.Random;

import mestrado.arquitetura.exceptions.NodeIdNotFound;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmiHelper extends UtilResources {
	
	
	
	/**
	 * Busca por {@link Node} dado um id e um {@link Documnet}.
	 * 
	 * 
	 * 
	 * @param docNotaion - Deve ser o arquivo .notation
	 * @param id - Id a ser buscado
	 * @return {@link Node}
	 */
	public static Node findByIDInNotationFile(Document docNotaion, String id) throws NodeNotFound {
		NodeList node = docNotaion.getElementsByTagName("children");
		Node nodeFound = null;
		for (int i = 0; i < node.getLength(); i++) {
			NodeList nodes = node.item(i).getChildNodes();
			for (int j = 0; j < nodes.getLength(); j++) {
				if(nodes.item(j).getNodeName().equalsIgnoreCase("element")){
					NamedNodeMap attrs = nodes.item(j).getAttributes();
					for (int k = 0; k < attrs.getLength(); k++) {
						if(attrs.item(k).getNodeValue().contains(id)){
							nodeFound = node.item(i);
						}
					}
				}
			}
		}	
		if(nodeFound == null)
			throw new NodeNotFound("Node with id " + id + " cannot be found" );
		return nodeFound;
	}
	
	
	public static String findIdByName(String name, Document umlDocument){
		NodeList node = umlDocument.getElementsByTagName("packagedElement");
		for (int i = 0; i < node.getLength(); i++) {
			NamedNodeMap attrs = node.item(i).getAttributes();
			if(name.equalsIgnoreCase(attrs.getNamedItem("name").getNodeValue())){
				return node.item(i).getAttributes().getNamedItem("xmi:id").getNodeValue();
			}
		}
		
		return "";
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
	
	/**
	 * Retorna o Id de um dado {@link Node}. 
	 * 
	 * @param node
	 * @return String
	 * @throws NodeIdNotFound 
	 */
	public static String getIdForNode(Node node) throws NodeIdNotFound {
		if(node != null){
			String nodeId = node.getAttributes().getNamedItem("xmi:id").getNodeValue();
			if (nodeId == null) throw new NodeIdNotFound("Cannot find id for node: " + node);
			return nodeId;
		}else{
			throw new NodeIdNotFound("Cannot find id for node: " + node);
		}
	}
	
	/**
	 * Método usado para gerar as posições de X e Y para os elementos.
	 * 
	 * @return String
	 */
	public String randomNum(){
		Random rn = new Random();
		int range = 1000 - 0 + 1;
		int randomNum =  rn.nextInt(range) + 0;
		return Integer.toString(randomNum);
	}
	
	public static String isClassAbstract(boolean isAbstract) {
		return (isAbstract) ? "true" : "false";
	}

}