package mestrado.arquitetura.parser;

import java.util.logging.Logger;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.NullReferenceFoundException;
import mestrado.arquitetura.helpers.UtilResources;
import mestrado.arquitetura.parser.method.Argument;
import mestrado.arquitetura.parser.method.Attribute;
import mestrado.arquitetura.parser.method.Method;
import mestrado.arquitetura.parser.method.Types;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Cria XMI para elementos UML.
 * 
 * @author edipofederle
 *
 */
public class ElementXmiGenerator extends XmiHelper {
	
	private final static Logger LOGGER = Logger.getLogger(ElementXmiGenerator.class.getName()); 
	
	private Element element;
	private DocumentManager documentManager;
	private static final String METHOD_ID = "3013";
	private static final String METHODO_TYPE = "uml:Operation";
	private static final String LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE = "7018";
	private static final String LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE = "7017";
	private String packageId;
	private Node notatioChildren;
	private Node umlModelChild;
	private Element notationBasicOperation;
	private String id;

	private Node klass;
	private static final String PROPERTY_ID = "3012";
	private static final String PROPERTY_TYPE = "uml:Property";
	
	private ClassNotation notation;
	/**
	 * documentUml é o arquivo .uml
	 * 
	 * @param documentUml
	 */
	public ElementXmiGenerator(DocumentManager documentManager){
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
		notation = new ClassNotation(this.documentManager, notatioChildren);
	}

	public String generateClass(final String klassName, final String idPackage) throws CustonTypeNotFound {
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){

			public void useTransformation() {
				id = UtilResources.getRandonUUID();
				element = documentManager.getDocUml().createElement("packagedElement");
				element.setAttribute("xmi:type", "uml:Class");
				element.setAttribute("xmi:id", id);
				element.setAttribute("name", klassName);
				klass = element;
				try {
					
					notation.createXmiForClassInNotationFile(id, idPackage);
					
					if((idPackage != null) && !("".equals(idPackage))){
						//Busca pacote para adicionar a class;
						Node packageToAppend = findByID(documentManager.getDocUml(), idPackage, "packagedElement");
						packageToAppend.appendChild(element);
					}else{
						umlModelChild.appendChild(element);
					}
				
				} catch (NullReferenceFoundException e) {
					LOGGER.severe("A null reference has been found. The process will be interrupted");
				}
			}
		});
		
		return id;
	}
	
	
	public void generateMethod(Method method, String idClass){
		final Element ownedOperation = documentManager.getDocUml().createElement("ownedOperation");
		ownedOperation.setAttribute("name", method.getName());
		ownedOperation.setAttribute("xmi:id", method.getId());
		ownedOperation.setAttribute("isAbstract", method.isAbstract());

		for (Argument arg : method.getArguments()) {
			Element ownedParameter  = documentManager.getDocUml().createElement("ownedParameter");
			ownedParameter.setAttribute("xmi:id", UtilResources.getRandonUUID());
			ownedParameter.setAttribute("name", arg.getName());
			ownedParameter.setAttribute("isUnique", "false");
			
			Element typeOperation = documentManager.getDocUml().createElement("type");
			typeOperation.setAttribute("xmi:type", "uml:PrimitiveType");
			typeOperation.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#"+arg.getType().getName());
			ownedParameter.appendChild(typeOperation);
			ownedOperation.appendChild(ownedParameter);
		}
		

		if(idClass != null){
			final Node klassToAddMethod = findByID(documentManager.getDocUml(), idClass, "packagedElement");
			klassToAddMethod.appendChild(ownedOperation);
			writeOnNotationFile(method.getId(), METHOD_ID, METHODO_TYPE, getNodeToAddMethodInNotationFile(idClass, LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE));
		}else{
			klass.appendChild(ownedOperation);
			writeOnNotationFile(method.getId(), METHOD_ID, METHODO_TYPE, notationBasicOperation);
		}
	}
	
	public void generateAttribute(Attribute attribute, String idClass) throws CustonTypeNotFound{
		if(idClass != null){
			this.klass = findByID(documentManager.getDocUml(), idClass, "packagedElement");
			writeAttributeIntoUmlFile(attribute);
			writeOnNotationFile(attribute.getId(), PROPERTY_ID, PROPERTY_TYPE, getNodeToAddMethodInNotationFile(idClass, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE));
		}else{
			writeAttributeIntoUmlFile(attribute);
			writeOnNotationFile(attribute.getId(), PROPERTY_ID, PROPERTY_TYPE, null);
		}
	}
	
	
	private void writeOnNotationFile(String idProperty, String typeId, String typeElement, Element appendTo) {
		notation.createNodeForElementType(idProperty, typeId, typeElement, appendTo);
	}
	
	private String writeAttributeIntoUmlFile(Attribute attribute) throws CustonTypeNotFound {
		Element ownedAttribute = documentManager.getDocUml().createElement("ownedAttribute");
		ownedAttribute.setAttribute("xmi:id", attribute.getId());
		ownedAttribute.setAttribute("name", attribute.getName());
		ownedAttribute.setAttribute("visibility", attribute.getVisibility());
		ownedAttribute.setAttribute("isUnique", "false");
		klass.appendChild(ownedAttribute);
		
		if(Types.isCustomType(attribute.getType())){
			String id = findIdByName(attribute.getType(), documentManager.getDocUml());
			if ("".equals(id))	throw new CustonTypeNotFound("Type " + attribute.getType() + " not found");
			ownedAttribute.setAttribute("type", id);
		}else{
			Element typeProperty = documentManager.getDocUml().createElement("type");
			typeProperty.setAttribute("xmi:type", "uml:PrimitiveType");
			typeProperty.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#"+attribute.getType());
			ownedAttribute.appendChild(typeProperty);
		}
		
		Element lowerValue = documentManager.getDocUml().createElement("lowerValue");
		lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
		lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		lowerValue.setAttribute("value", "1");
		ownedAttribute.appendChild(lowerValue);
		
		Element upperValue = documentManager.getDocUml().createElement("upperValue");
		upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		upperValue.setAttribute("value", "1");
		ownedAttribute.appendChild(upperValue);
		
		Element defaultValue = documentManager.getDocUml().createElement("defaultValue");
		defaultValue.setAttribute("xmi:type", "uml:LiteralString");
		defaultValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
		ownedAttribute.appendChild(defaultValue);
		
		Element value = documentManager.getDocUml().createElement("value");
		value.setAttribute("xmi:nil", "true"); // TODO VER ISSO
		defaultValue.appendChild(value);
		return attribute.getId();
	}
	
	
	
	private Element getNodeToAddMethodInNotationFile(final String idClass, String location) {
		Node nodeNotationToAddMethod = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
		for(int i =0; i <  nodeNotationToAddMethod.getChildNodes().getLength(); i++){
			if("children".equalsIgnoreCase(nodeNotationToAddMethod.getChildNodes().item(i).getNodeName())){
				if(isLocationToAddMethodInNotationFile(nodeNotationToAddMethod, i, location))
					return (Element) nodeNotationToAddMethod.getChildNodes().item(i);
			}
		}
		return null; //TODO remover NULL
	}
	

	private boolean isLocationToAddMethodInNotationFile(Node nodeNotationToAddMethod, int i, String location ) {
		return nodeNotationToAddMethod.getChildNodes().item(i).getAttributes().getNamedItem("type").getNodeValue().equals(location);
	}

}