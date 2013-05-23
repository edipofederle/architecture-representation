package mestrado.arquitetura.api.touml;

import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.utils.UtilResources;

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

	public void createDependency(String dependency) throws DOMException, NodeNotFound {
		//Primeiramente cria o xmi necessário no document UML.
		
		createDependencyInUmlFile(dependency);
		
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
		
		 Element element = docNotation.createElement("element");
		 if("dependency".equalsIgnoreCase(dependency)){
			 element.setAttribute("xmi:type", "uml:Dependency");
		 }else if("usage".equalsIgnoreCase(dependency)){
			 element.setAttribute("xmi:type", "uml:Usage");
		 }
		 element.setAttribute("href", documentManager.getNewModelName()+".uml#"+this.id); 
		 edges.appendChild(element);
		 
		 
		Element bendpoints = docNotation.createElement("bendpoints");
		bendpoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
		bendpoints.setAttribute("xmi:id", UtilResources.getRandonUUID());
		bendpoints.setAttribute("points", "[0, 0, 476, 181]$[-467, -170, 9, 11]");
		edges.appendChild(bendpoints);

		
		node.appendChild(edges);
	}

	private void createDependencyInUmlFile(String dependency) {
		Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);
		
		String idDependency = UtilResources.getRandonUUID();
		this.id = idDependency;
		
		Element elementDependency = this.docUml.createElement("packagedElement");
		if("dependency".equalsIgnoreCase(dependency))
			elementDependency.setAttribute("xmi:type", "uml:Dependency");
		else if("usage".equalsIgnoreCase(dependency))
			elementDependency.setAttribute("xmi:type", "uml:Usage");
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
