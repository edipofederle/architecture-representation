package mestrado.arquitetura.parser;

import mestrado.arquitetura.helpers.UtilResources;
import mestrado.arquitetura.parser.method.Argument;
import mestrado.arquitetura.parser.method.Method;

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
	
	
	public  Element generateMethod(Method method){
		final Element ownedOperation = documentManager.getDocUml().createElement("ownedOperation");
		ownedOperation.setAttribute("name", method.getName());
		ownedOperation.setAttribute("xmi:id", method.getId());
		ownedOperation.setAttribute("isAbstract", method.isAbstract());

		for (Argument arg : method.getArguments()) {
				Element ownedParameter  = documentManager.getDocUml().createElement("ownedParameter");
				ownedParameter.setAttribute("xmi:id", UtilResources.getRandonUUID());
				ownedParameter.setAttribute("name", arg.getName());
				ownedParameter.setAttribute("isUnique", "false"); //TODO Ver se irá ser usado ou pode ficar fixo
				
				Element typeOperation = documentManager.getDocUml().createElement("type");
				typeOperation.setAttribute("xmi:type", "uml:PrimitiveType");
				typeOperation.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#"+arg.getType());
				ownedParameter.appendChild(typeOperation);
				
				ownedOperation.appendChild(ownedParameter);
		}
		  
		return ownedOperation;
	}

}