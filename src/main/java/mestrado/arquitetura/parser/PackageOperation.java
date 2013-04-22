package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PackageOperation {
	
	
	private DocumentManager documentManager;
	private Node umlModelChild;
	private Node notatioChildren;
	private Element element;
	private ClassNotation notation;
	

	public PackageOperation(DocumentManager documentManager) {
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
		notation = new ClassNotation(this.documentManager, notatioChildren);
	}
	
	
	public void createPacakge(final String packageName) throws CustonTypeNotFound{
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				String id = UtilResources.getRandonUUID();
				element = documentManager.getDocUml().createElement("packagedElement");
				element.setAttribute("xmi:type", "uml:Package");
				element.setAttribute("xmi:id", id);
				element.setAttribute("name", packageName);
				umlModelChild.appendChild(element);
				
				notation.createXmiForPackageInNotationFile(id);
			}
		
		});
	}

}