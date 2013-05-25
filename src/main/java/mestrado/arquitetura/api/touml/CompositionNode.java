package mestrado.arquitetura.api.touml;

import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.utils.UtilResources;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class CompositionNode extends XmiHelper {
	
	static Logger LOGGER = LogManager.getLogger(CompositionNode.class.getName());

	private DocumentManager doc;
	private String associationId;
	private ElementXmiGenerator elementXmiGenerator;

	public CompositionNode(DocumentManager doc) {
		this.doc = doc;
		this.associationId = UtilResources.getRandonUUID();
		this.elementXmiGenerator = new ElementXmiGenerator(doc);
	}

	/**
	 * 
	 * @param client
	 * @param target
	 * @param multiplicityClassClient
	 * @param multiplicityClassTarget
	 * @param type - "composite", "shared"
	 * @param string 
	 * @throws NodeNotFound
	 * @throws InvalidMultiplictyForAssociationException
	 */
	public void createComposition(String name, String client, String target, String multiplicityClassClient, String multiplicityClassTarget, String type) {
		
		
		String multiLowerValueClient = "1";
		String multiUpperValueClient = "1";
		
		String multiLowerValueTarget = "1";
		String multiUpperValueTarget = "1";
		
		if((!"".equals(multiplicityClassClient) && (multiplicityClassClient != null))){
			multiLowerValueClient = multiplicityClassClient.substring(0, 1).trim();
			multiUpperValueClient = multiplicityClassClient.substring(multiplicityClassClient.length()-1, multiplicityClassClient.length()).trim();
			 
			if(multiLowerValueClient.equals("*"))
				LOGGER.warn("Multiplicy lower value cannot be *. FIX IT");
		}
		
		if((!"".equals(multiplicityClassTarget)) && (multiplicityClassTarget != null)){
			multiLowerValueTarget = multiplicityClassTarget.substring(0, 1).trim();
			multiUpperValueTarget = multiplicityClassTarget.substring(multiplicityClassTarget.length()-1, multiplicityClassTarget.length()).trim();
			 
			if(multiLowerValueTarget.equals("*"))
				LOGGER.warn("Multiplicy lower value cannot be *. FIX IT");
		}
		
		//Monta XMI no arquivo .uml
		
		//Members End
		String ownedEnd1 = UtilResources.getRandonUUID();
		String ownedEnd2 = UtilResources.getRandonUUID();
		
		Node modelRoot = this.doc.getDocUml().getElementsByTagName("uml:Model").item(0);
		
		Element pkgElement = doc.getDocUml().createElement("packagedElement");
		pkgElement.setAttribute("xmi:type", "uml:Association");
		pkgElement.setAttribute("xmi:id", associationId);
		pkgElement.setAttribute("name", name); 
		pkgElement.setAttribute("memberEnd", ownedEnd1 + " " + ownedEnd2);
		
	
		Element ownedEndElement1 = doc.getDocUml().createElement("ownedEnd");
		ownedEndElement1.setAttribute("xmi:id", ownedEnd1);
		ownedEndElement1.setAttribute("name", "class1"); //Pegar dinamico o nome 
		ownedEndElement1.setAttribute("type", client);
		ownedEndElement1.setAttribute("association", associationId);
		ownedEndElement1.setAttribute("aggregation", type);
		pkgElement.appendChild(ownedEndElement1);
		
		Element lowerValueOwnedEndElement1 = doc.getDocUml().createElement("lowerValue");
		lowerValueOwnedEndElement1.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValueOwnedEndElement1.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValueOwnedEndElement1.setAttribute("value", multiLowerValueClient);
		
		Element upperValueOwnedEndElement1 = doc.getDocUml().createElement("upperValue");
		upperValueOwnedEndElement1.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		upperValueOwnedEndElement1.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValueOwnedEndElement1.setAttribute("value", multiUpperValueClient);
		
		ownedEndElement1.appendChild(lowerValueOwnedEndElement1);
		ownedEndElement1.appendChild(upperValueOwnedEndElement1);
		
		
		Element ownedEndElement2 = doc.getDocUml().createElement("ownedEnd");
		ownedEndElement2.setAttribute("xmi:id", ownedEnd2);
		ownedEndElement2.setAttribute("name", "class1"); //Pegar dinamico o nome 
		ownedEndElement2.setAttribute("type", target);
		ownedEndElement2.setAttribute("association", associationId);
		pkgElement.appendChild(ownedEndElement2);
		
		Element lowerValueOwnedEndElement2 = doc.getDocUml().createElement("lowerValue");
		lowerValueOwnedEndElement2.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValueOwnedEndElement2.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValueOwnedEndElement2.setAttribute("value", multiLowerValueTarget);
		
		Element upperValueOwnedEndElement2 = doc.getDocUml().createElement("upperValue");
		upperValueOwnedEndElement2.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		upperValueOwnedEndElement2.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValueOwnedEndElement2.setAttribute("value", multiUpperValueTarget);
		
		ownedEndElement2.appendChild(lowerValueOwnedEndElement2);
		ownedEndElement2.appendChild(upperValueOwnedEndElement2);
		
		
		//Escreve XMI no Notation
		elementXmiGenerator.createEgdeAssocationOnNotationFile(this.doc.getDocNotation(), doc.getNewModelName(), client, target, this.associationId);
		
		modelRoot.appendChild(pkgElement);
		
	}
	
}