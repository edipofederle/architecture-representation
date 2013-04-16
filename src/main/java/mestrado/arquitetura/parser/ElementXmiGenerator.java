package mestrado.arquitetura.parser;

import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Element;

/**
 * Cria XMI para elementos UML.
 * 
 * @author edipofederle
 *
 */
public class ElementXmiGenerator {
	
	private Element element;
	private DocumentManager documentManager;
	
	/**
	 * documentUml é o arquivo .uml
	 * 
	 * @param documentUml
	 */
	public ElementXmiGenerator(DocumentManager documentManager){
		this.documentManager = documentManager;
	}

	public Element generateClass(String klassName) {
		String id = UtilResources.getRandonUUID();
		element = documentManager.getDocUml().createElement("packagedElement");
		element.setAttribute("xmi:type", "uml:Class");
		element.setAttribute("xmi:id", id);
		element.setAttribute("name", klassName);
		
		return element;
		
	}

}