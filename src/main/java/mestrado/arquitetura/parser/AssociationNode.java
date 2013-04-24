package mestrado.arquitetura.parser;

import java.util.logging.Logger;

import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AssociationNode extends XmiHelper{
	
	private final static Logger LOGGER = Logger.getLogger(AssociationNode.class.getName()); 
	
	private Document docUml;
	private Document docNotation;
	
	private String idClassOwnnerAssociation;
	private String idClassDestinationAssociation;
	
	private final String idAssocation;
	private final String memberEndId;
	private final String newModelName;
	
	public AssociationNode(Document docUml, Document docNotation, String name) {
		this.newModelName = name;
		this.docUml = docUml;
		this.docNotation = docNotation;
		
		this.idAssocation = UtilResources.getRandonUUID();
		this.memberEndId  = UtilResources.getRandonUUID();
	}

	public void createAssociation(String idClassOwnnerAssociation, String idClassDestinationAssociation) throws NodeNotFound {
		
		this.idClassDestinationAssociation = idClassDestinationAssociation;
		this.idClassOwnnerAssociation = idClassOwnnerAssociation;
		
		Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);
		
		Element packageElement = this.docUml.createElement("packagedElement");
		packageElement.setAttribute("xmi:type", "uml:Association");
		packageElement.setAttribute("xmi:id", this.idAssocation);
		packageElement.setAttribute("name", "associationName"); 
		
		String memberEnd = UtilResources.getRandonUUID();
		packageElement.setAttribute("memberEnd", memberEndId + " "+ memberEnd);	
		
		Element ownedEnd = this.docUml.createElement("ownedEnd");
		ownedEnd.setAttribute("xmi:id", memberEnd);
		ownedEnd.setAttribute("name", "ClassName");
		ownedEnd.setAttribute("type", this.idClassOwnnerAssociation);
		ownedEnd.setAttribute("association", this.idAssocation);
		packageElement.appendChild(ownedEnd);
		
		Element lowerValue = this.docUml.createElement("lowerValue");
		lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValue.setAttribute("value", "1");
		ownedEnd.appendChild(lowerValue);
		
		Element upperValue = this.docUml.createElement("upperValue");
		upperValue.setAttribute("xmi:type", "uml:LiteralInteger");
		upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValue.setAttribute("value", "1");
		ownedEnd.appendChild(upperValue);
		
		modelRoot.appendChild(packageElement);
		
		ownedAttibute();
		
		createEgdeAssocationOnNotationFile();
		
	}
	
	private void ownedAttibute(){
		
		//Primeiro busca pela class que seja a "dona" da associção. Isso é feito por meio do ID.
		Node packageElementNode = findByID(docUml, this.idClassOwnnerAssociation, "packagedElement");
		
		Element ownedAttibute = this.docUml.createElement("ownedAttribute");
		ownedAttibute.setAttribute("xmi:id", memberEndId);
		ownedAttibute.setAttribute("name", "ClassDestination");
		ownedAttibute.setAttribute("type", this.idClassDestinationAssociation);
		ownedAttibute.setAttribute("association", this.idAssocation);
		
		
		Element lowerValue = this.docUml.createElement("lowerValue");
		lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValue.setAttribute("value", "1");
		ownedAttibute.appendChild(lowerValue);
		
		Element upperValue = this.docUml.createElement("upperValue");
		upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValue.setAttribute("value", "1");
		ownedAttibute.appendChild(upperValue);
		
		packageElementNode.appendChild(ownedAttibute);
	}


	private void createEgdeAssocationOnNotationFile() throws NodeNotFound{
		
		Node node = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
		
		NamedNodeMap attributesOwnner = findByIDInNotationFile(docNotation,idClassOwnnerAssociation).getAttributes();
		NamedNodeMap attributesDestination = findByIDInNotationFile(docNotation, idClassDestinationAssociation).getAttributes();
		String idSource = attributesOwnner.getNamedItem("xmi:id").getNodeValue();
		String idTarget = attributesDestination.getNamedItem("xmi:id").getNodeValue();
		
		Element edges = this.docNotation.createElement("edges");
		edges.setAttribute("xmi:type", "notation:Connector");
		edges.setAttribute("xmi:id", UtilResources.getRandonUUID());
		edges.setAttribute("type", "4001");
		edges.setAttribute("source", idSource);
		edges.setAttribute("target", idTarget);
		edges.setAttribute("lineColor", "0");
		
		Element elementAssociation = this.docNotation.createElement("element");
		elementAssociation.setAttribute("xmi:type", "uml:Association");
		elementAssociation.setAttribute("href", this.newModelName+".uml#"+this.idAssocation);
		edges.appendChild(elementAssociation);
		
		Element styles = docNotation.createElement("styles");
		styles.setAttribute("xmi:type", "notation:FontStyle");
		styles.setAttribute("xmi:id", UtilResources.getRandonUUID());
		styles.setAttribute("xmi:id", UtilResources.getRandonUUID());
		styles.setAttribute("fontName", "Lucida Grande");
		styles.setAttribute("fontHeight", "11");
		edges.appendChild(styles);
		
		Element bendpoints = docNotation.createElement("bendpoints");
		bendpoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
		bendpoints.setAttribute("xmi:id", UtilResources.getRandonUUID());
		bendpoints.setAttribute("points", "[0, 0, -200, -20]$[255, -30, -6, -50]");
		edges.appendChild(bendpoints);
		
		Element sourceAnchor = docNotation.createElement("sourceAnchor");
		sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
		sourceAnchor.setAttribute("xmi:id", UtilResources.getRandonUUID());
		sourceAnchor.setAttribute("id", "(1.0,0.36)");
		edges.appendChild(sourceAnchor);
		
		node.appendChild(edges);
	}
	
	
	public void  removeAssociation(String id){
		//Busca por node "edges" no arquivo notation.
		
		Node nodeToRemove = null;
		Node notationNode = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
		try{
			NodeList nodesEdges = docNotation.getElementsByTagName("edges");
			for (int i = 0; i < nodesEdges.getLength(); i++) {
				NodeList childNodes = nodesEdges.item(i).getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					if(childNodes.item(j).getNodeName().equalsIgnoreCase("element")){
						String idHref = childNodes.item(j).getAttributes().getNamedItem("href").getNodeValue();
						if(idHref.contains(id))
							nodeToRemove =  nodesEdges.item(i);
					}
						
				}
			}
			
			notationNode.removeChild(nodeToRemove);
		}catch(Exception e){
			LOGGER.info("Cannot remove Association with id: " + id +"." + e.getMessage());
		}
		
		removeAssociationFromUmlFile(id);
		
	}

	private void removeAssociationFromUmlFile(String id) {
		NodeList ownedAttributeElement = this.docUml.getElementsByTagName("ownedAttribute");
		NodeList ownedEndElement = this.docUml.getElementsByTagName("ownedEnd");
		NodeList packagedElementElement = this.docUml.getElementsByTagName("packagedElement");
		
		
		for (int i = 0; i < ownedAttributeElement.getLength(); i++) {
			if(ownedAttributeElement.item(i).getAttributes().getNamedItem("association") != null){
				String idNode = ownedAttributeElement.item(i).getAttributes().getNamedItem("association").getNodeValue();
				if (id.equalsIgnoreCase(idNode)){
					ownedAttributeElement.item(i).getAttributes().removeNamedItem("association");
					LOGGER.info("Association with id: " + id + " removed from UML file");
				}
			}
		}
		
		for (int i = 0; i < ownedEndElement.getLength(); i++) {
			ownedEndElement.item(i).getAttributes().removeNamedItem("association");
			LOGGER.info("Association with id: " + id + " removed from UML file");
		}
		
		for (int i = 0; i < packagedElementElement.getLength(); i++) {
			String idNode = packagedElementElement.item(i).getAttributes().getNamedItem("xmi:id").getNodeValue();
			if (id.equalsIgnoreCase(idNode)){
				packagedElementElement.item(i).getParentNode().removeChild(packagedElementElement.item(i));
				LOGGER.info("Association with id: " + id + " removed from UML file");
			}
		}
	}
	
	public String getIdAssocation(){
		return this.idAssocation;
	}
	
}