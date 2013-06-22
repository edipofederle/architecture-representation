package arquitetura.touml;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class AssociationNode extends XmiHelper{
	
	static Logger LOGGER = LogManager.getLogger(AssociationNode.class.getName());
	
	private Document docUml;
	private Document docNotation;
	
	private String idClassOwnnerAssociation;
	private String idClassDestinationAssociation;
	
	private final String idAssocation;
	private final String memberEndId;
	private final String newModelName;

	private ElementXmiGenerator elementXmiGenerator;
	
	public AssociationNode(DocumentManager doc) {
		this.newModelName = doc.getModelName();
		this.docUml = doc.getDocUml();
		this.docNotation = doc.getDocNotation();
		
		this.idAssocation = UtilResources.getRandonUUID();
		this.memberEndId  = UtilResources.getRandonUUID();
		this.elementXmiGenerator = new ElementXmiGenerator(doc);
	}

	public void createAssociation(String idClassOwnnerAssociation, String idClassDestinationAssociation, String multiplicityClassDestination, String multiplicityClassOwnner)  {
		
		this.idClassDestinationAssociation = idClassDestinationAssociation;
		this.idClassOwnnerAssociation = idClassOwnnerAssociation;
		
		String multiLowerValue = "1";
		String multiUpperValue = "1";
		
		if(multiplicityClassOwnner != null){
			 multiLowerValue = multiplicityClassOwnner.substring(0, 1).trim();
			 multiUpperValue = multiplicityClassOwnner.substring(multiplicityClassOwnner.length()-1, multiplicityClassOwnner.length()).trim();
			 
			if(multiLowerValue.equals("*"))
				LOGGER.warn("Multiplicy lower value cannot be *. FIX it");
		}
		
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
		
		lowerValue.setAttribute("value", multiLowerValue);
		ownedEnd.appendChild(lowerValue);

		Element upperValue = this.docUml.createElement("upperValue");
		upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValue.setAttribute("value", multiUpperValue);
		ownedEnd.appendChild(upperValue);
		
		modelRoot.appendChild(packageElement);
		
		ownedAttibute(multiplicityClassDestination);
		
		elementXmiGenerator.createEgdeAssocationOnNotationFile(docNotation, newModelName, idClassOwnnerAssociation, idClassDestinationAssociation, this.idAssocation);
		
	}
	
	private void ownedAttibute(String multiplicityClassDestination){
		
		//Primeiro busca pela class que seja a "dona" da associção. Isso é feito por meio do ID.
		Node packageElementNode = findByID(docUml, this.idClassOwnnerAssociation, "packagedElement");
		
		Element ownedAttibute = this.docUml.createElement("ownedAttribute");
		ownedAttibute.setAttribute("xmi:id", memberEndId);
		ownedAttibute.setAttribute("name", "ClassDestination");
		ownedAttibute.setAttribute("type", this.idClassDestinationAssociation);
		ownedAttibute.setAttribute("association", this.idAssocation);
		
		String multiLowerValue = "1";
		String multiUpperValue = "1";
		
		if(multiplicityClassDestination != null){
			 multiLowerValue = multiplicityClassDestination.substring(0, 1).trim();
			 multiUpperValue = multiplicityClassDestination.substring(multiplicityClassDestination.length()-1, multiplicityClassDestination.length()).trim();
		}
		Element lowerValue = this.docUml.createElement("lowerValue");
		if(multiLowerValue.equals("*"))
			LOGGER.warn("Multiplicy lower value cannot be *. FIX IT");
		else
			lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValue.setAttribute("value",multiLowerValue);
		ownedAttibute.appendChild(lowerValue);
		
		Element upperValue = this.docUml.createElement("upperValue");
		if(multiUpperValue.equals("*"))
			upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		else
			upperValue.setAttribute("xmi:type", "uml:LiteralInteger");	
		upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValue.setAttribute("value", multiUpperValue);
		ownedAttibute.appendChild(upperValue);
		
		packageElementNode.appendChild(ownedAttibute);
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
			LOGGER.error("Cannot remove Association with id: " + id +"." + e.getMessage());
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