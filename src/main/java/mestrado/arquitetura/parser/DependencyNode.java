package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DependencyNode extends XmiHelper {

	private Document docUml;
	private Document docNotation;
	private DocumentManager documentManager;
	private String clientElement;
	private String supplierElement;
	private String name;
	private String id;
	
	public DependencyNode(DocumentManager documentManager, String name,	String clientElement, String supplierElement) {
		this.documentManager = documentManager;
		this.docUml = documentManager.getDocUml();
		this.docNotation = documentManager.getDocNotation();
		this.clientElement = clientElement;
		this.supplierElement = supplierElement;
		this.name = name;
	}

	public void createDependency() throws DOMException, NodeNotFound {
		//Primeiramente cria o xmi necessário no document UML.
		
		createDependencyInUmlFile();
		
		Node node = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
		Element edges = this.docNotation.createElement("edges");
		edges.setAttribute("xmi:type", "notation:Connector");
		edges.setAttribute("xmi:id", UtilResources.getRandonUUID());
		edges.setAttribute("type", "4008");
		
		
		Element childrenDocorationnode1 = this.docNotation.createElement("children");
		childrenDocorationnode1.setAttribute("xmi:type", "notation:DecorationNode");
		childrenDocorationnode1.setAttribute("xmi:id", UtilResources.getRandonUUID());
		childrenDocorationnode1.setAttribute("type", "6026");
		
		Element layoutConstraint = this.docNotation.createElement("layoutConstraint");
		layoutConstraint.setAttribute("xmi:type", "notation:Location");
		layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
		layoutConstraint.setAttribute("y", "20");
		childrenDocorationnode1.appendChild(layoutConstraint);
		edges.appendChild(childrenDocorationnode1);

		
		String idSource = findByIDInNotationFile(docNotation, clientElement).getAttributes().getNamedItem("xmi:id").getNodeValue();
		String idTarget = findByIDInNotationFile(docNotation, supplierElement).getAttributes().getNamedItem("xmi:id").getNodeValue();
		
		edges.setAttribute("source", idSource);
		edges.setAttribute("target", idTarget);
		edges.setAttribute("lineColor", "0");
		
		 //<styles xmi:type="notation:FontStyle" xmi:id="_B7swwbDfEeKXAbdr0ngOng" fontName="Lucida Grande" fontHeight="11"/>
//		 Element styles = this.docNotation.createElement("styles");
//		 styles.setAttribute("xmi:type", "notation:FontStyle");
//		 styles.setAttribute("xmi:id", UtilResources.getRandonUUID());
//		 styles.setAttribute("fontName", "Lucida Grande");
//		 styles.setAttribute("fontHeight", "11");
//		 edges.appendChild(styles);
		 
		 Element element = docNotation.createElement("element");
		 element.setAttribute("xmi:type", "uml:Dependency");
		 element.setAttribute("href", documentManager.getNewModelName()+".uml#"+this.id); 
		 edges.appendChild(element);
		 
		 
		Element bendpoints = docNotation.createElement("bendpoints");
		bendpoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
		bendpoints.setAttribute("xmi:id", UtilResources.getRandonUUID());
		bendpoints.setAttribute("points", "[0, 0, 476, 181]$[-467, -170, 9, 11]");
		edges.appendChild(bendpoints);
//		
//		Element sourceAnchor = docNotation.createElement("sourceAnchor");
//		sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
//		sourceAnchor.setAttribute("xmi:id", UtilResources.getRandonUUID());
//		sourceAnchor.setAttribute("id", "(0.42,0.0)");
//		edges.appendChild(sourceAnchor);
//		
//		Element targetAnchor = docNotation.createElement("targetAnchor");
//		targetAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
//		targetAnchor.setAttribute("xmi:id", UtilResources.getRandonUUID());
//		targetAnchor.setAttribute("id", "(0.82,0.89)");
//		edges.appendChild(targetAnchor);
		
		node.appendChild(edges);
	}

	private void createDependencyInUmlFile() {
		Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);
		
		String idDependency = UtilResources.getRandonUUID();
		this.id = idDependency;
		
		Element elementDependency = this.docUml.createElement("packagedElement");
		elementDependency.setAttribute("xmi:type", "uml:Dependency");
		elementDependency.setAttribute("xmi:id", idDependency);
		elementDependency.setAttribute("name", this.name);
		elementDependency.setAttribute("client", this.clientElement);
		elementDependency.setAttribute("supplier", this.supplierElement);
		
		//Adiciona dependency no element
		Element clientElement = (Element) findByID(docUml, this.clientElement, "packagedElement");
		clientElement.setAttribute("clientDependency", idDependency);
		
		modelRoot.appendChild(elementDependency);
	}

}