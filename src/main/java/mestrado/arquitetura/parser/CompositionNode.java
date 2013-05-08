package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CompositionNode extends XmiHelper {

	private DocumentManager doc;
	private String associationId;
	private ElementXmiGenerator elementXmiGenerator;

	public CompositionNode(DocumentManager doc) {
		this.doc = doc;
		this.associationId = UtilResources.getRandonUUID();
		this.elementXmiGenerator = new ElementXmiGenerator(doc);
	}

	public void createComposition(String client, String target) throws NodeNotFound {
		
		//Monta XMI no arquivo .uml
		
		//Members End
		String ownedEnd1 = UtilResources.getRandonUUID();
		String ownedEnd2 = UtilResources.getRandonUUID();
		
		Node modelRoot = this.doc.getDocUml().getElementsByTagName("uml:Model").item(0);
		
		Element pkgElement = doc.getDocUml().createElement("packagedElement");
		pkgElement.setAttribute("xmi:type", "uml:Association");
		pkgElement.setAttribute("xmi:id", associationId);
		pkgElement.setAttribute("name", ""); //TODO nome
		pkgElement.setAttribute("memberEnd", ownedEnd1 + " " + ownedEnd2);
		
	
		Element ownedEndElement1 = doc.getDocUml().createElement("ownedEnd");
		ownedEndElement1.setAttribute("xmi:id", ownedEnd1);
		ownedEndElement1.setAttribute("name", "class1"); //Pegar dinamico o nome 
		ownedEndElement1.setAttribute("type", client);
		ownedEndElement1.setAttribute("association", associationId);
		ownedEndElement1.setAttribute("aggregation", "composite");
		pkgElement.appendChild(ownedEndElement1);
		
		Element lowerValueOwnedEndElement1 = doc.getDocUml().createElement("lowerValue");
		lowerValueOwnedEndElement1.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValueOwnedEndElement1.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValueOwnedEndElement1.setAttribute("value", "1");
		
		Element upperValueOwnedEndElement1 = doc.getDocUml().createElement("upperValue");
		upperValueOwnedEndElement1.setAttribute("xmi:type", "LiteralUnlimitedNatural");
		upperValueOwnedEndElement1.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValueOwnedEndElement1.setAttribute("value", "1");
		
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
		lowerValueOwnedEndElement2.setAttribute("value", "1");
		
		Element upperValueOwnedEndElement2 = doc.getDocUml().createElement("upperValue");
		upperValueOwnedEndElement2.setAttribute("xmi:type", "LiteralUnlimitedNatural");
		upperValueOwnedEndElement2.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValueOwnedEndElement2.setAttribute("value", "1");
		
		ownedEndElement2.appendChild(lowerValueOwnedEndElement2);
		ownedEndElement2.appendChild(upperValueOwnedEndElement2);
		
		
		//Escreve XMI no Notation
		
		
		elementXmiGenerator.createEgdeAssocationOnNotationFile(this.doc.getDocNotation(), doc.getNewModelName(), client, target, this.associationId);
		
		modelRoot.appendChild(pkgElement);
		
		
	}
	

}
