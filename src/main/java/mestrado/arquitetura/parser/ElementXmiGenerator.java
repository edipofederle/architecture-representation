package mestrado.arquitetura.parser;

import java.util.Random;
import java.util.logging.Logger;

import mestrado.arquitetura.exceptions.NullReferenceFoundException;
import mestrado.arquitetura.helpers.UtilResources;
import mestrado.arquitetura.parser.method.Argument;
import mestrado.arquitetura.parser.method.Attribute;
import mestrado.arquitetura.parser.method.Method;

import org.w3c.dom.Document;
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
	private static final String SHOW_TYPE_OF_ATTRIBUTE = "7066";
	private String xmitype = "notation:Shape";
	private String id;
	private final String type = "2008";
	private final String fontName = "Lucida Grande";
	private final String fontHeight = "11";
	private final String lineColor = "0";
	private Node notatioChildren;
	private Node umlModelChild;
	private Element notationBasicOperation;
	private Element notationBasicProperty;

	private Node klass;
	private static final String PROPERTY_ID = "3012";
	private static final String PROPERTY_TYPE = "uml:Property";
	
	/**
	 * documentUml é o arquivo .uml
	 * 
	 * @param documentUml
	 */
	public ElementXmiGenerator(DocumentManager documentManager){
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
	}

	public String generateClass(final String klassName) {
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				id = UtilResources.getRandonUUID();
				element = documentManager.getDocUml().createElement("packagedElement");
				element.setAttribute("xmi:type", "uml:Class");
				element.setAttribute("xmi:id", id);
				element.setAttribute("name", klassName);
				klass = element;
				try {
					createXmiForClassInNotationFile();
					umlModelChild.appendChild(element);
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
				typeOperation.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#"+arg.getType());
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
	
	public void generateAttribute(Attribute attribute, String idClass){
		
		if(idClass != null){
			this.klass = findByID(documentManager.getDocUml(), idClass, "packagedElement");
			writeAttributeIntoUmlFile(attribute);
			writeOnNotationFile(attribute.getId(), PROPERTY_ID, PROPERTY_TYPE, getNodeToAddMethodInNotationFile(idClass, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE));
		}else{
			writeAttributeIntoUmlFile(attribute);
			writeOnNotationFile(attribute.getId(), PROPERTY_ID, PROPERTY_TYPE, notationBasicProperty);
		}
		
	}
	
	
	private void writeOnNotationFile(String idProperty, String idType, String type, Element appendTo) {
		createNodeForElementType(idProperty, idType, type, appendTo);
	}
	

	private void createNodeForElementType(String idProperty, String type, String typeElement, Element appendTo) {
		Element node = documentManager.getDocNotation().createElement("children");
		
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", type);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
      	Element eAnnotations = documentManager.getDocNotation().createElement("eAnnotations");
      	eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
      	eAnnotations.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	eAnnotations.setAttribute("source", "CustomAppearance_Annotation");
      	
      	Element details = documentManager.getDocNotation().createElement("details");
      	details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
      	details.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	details.setAttribute("key", "CustomAppearance_MaskValue");
      	details.setAttribute("value", SHOW_TYPE_OF_ATTRIBUTE);
      	eAnnotations.appendChild(details);
      	node.appendChild(eAnnotations);
      	
		Element element = documentManager.getDocNotation().createElement("element");
		element.setAttribute("xmi:type", typeElement);
		element.setAttribute("href", documentManager.getModelName()+"#"+ idProperty);
		node.appendChild(element);
		
		Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
		layoutConstraint.setAttribute("xmi:type", "notation:Location");
		layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.appendChild(layoutConstraint);
		appendTo.appendChild(node);
	}

	
	private void createXmiForClassInNotationFile() throws NullReferenceFoundException {
		
		Element node = documentManager.getDocNotation().createElement("children");
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", this.type);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
		Element notationDecoratioNode = documentManager.getDocNotation().createElement("children");
		notationDecoratioNode.setAttribute("xmi:type", "notation:DecorationNode");
		notationDecoratioNode.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationDecoratioNode.setAttribute("type", "5029");
		node.appendChild(notationDecoratioNode);
		
	    Element klass = documentManager.getDocNotation().createElement("element");
	    
	    if (this.id == null){
	    	throw new NullReferenceFoundException("A null reference found when try access attribute id. Executation will be interrupted.");
	    }
	    
	   	klass.setAttribute("href", documentManager.getModelName()+".uml#"+ this.id);
	    	
	    klass.setAttribute("xmi:type", "uml:Class");
		
	    this.notationBasicProperty = createChildrenComportament(documentManager.getDocNotation(), node, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE); //onde vai as props
	    this.notationBasicOperation = createChildrenComportament(documentManager.getDocNotation(), node, LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE); // onde vai os metodos

	    node.appendChild(klass);
		
	    notatioChildren.appendChild(node);
	    
	}

	private Element createChildrenComportament(Document doc, Element node, String type) {
		Element element = doc.createElement("children");
		element.setAttribute("xmi:type", "notation:BasicCompartment");
		element.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.setAttribute("type", type);
		node.appendChild(element);
		
		Element notationTitleStyle = doc.createElement("styles");
		notationTitleStyle.setAttribute("xmi:type", "notation:TitleStyle");
		notationTitleStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationTitleStyle);
		
		Element notationSortingStyle = doc.createElement("styles");
		notationSortingStyle.setAttribute("xmi:type", "notation:SortingStyle");
		notationSortingStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationSortingStyle);
		
		Element notationFilteringStyle = doc.createElement("styles");
		notationFilteringStyle.setAttribute("xmi:type", "notation:FilteringStyle");
		notationFilteringStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationFilteringStyle);
		
		Element notationBounds = doc.createElement("layoutConstraint");
		notationBounds.setAttribute("xmi:type", "notation:Bounds");
		notationBounds.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationBounds);
		
	    Element layoutConstraint = doc.createElement("layoutConstraint");
	    layoutConstraint.setAttribute("x", randomNum());
	    layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
	    layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
	    layoutConstraint.setAttribute("y", randomNum());
	    node.appendChild(layoutConstraint);
	    
	    return element;
	}
	
	private String writeAttributeIntoUmlFile(Attribute attribute) {
		Element ownedAttribute = documentManager.getDocUml().createElement("ownedAttribute");
		ownedAttribute.setAttribute("xmi:id", attribute.getId());
		ownedAttribute.setAttribute("name", attribute.getName());
		ownedAttribute.setAttribute("visibility", attribute.getVisibility());
		ownedAttribute.setAttribute("isUnique", "false");
		klass.appendChild(ownedAttribute);
		
		if(!"".equalsIgnoreCase(attribute.getType())){
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
	
	
	private String randomNum(){
		Random rn = new Random();
		int range = 1000 - 0 + 1;
		int randomNum =  rn.nextInt(range) + 0;
		return Integer.toString(randomNum);
	}
	
	private Element getNodeToAddMethodInNotationFile(final String idClass, String location) {
		Node nodeNotationToAddMethod = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
		for(int i =0; i <  nodeNotationToAddMethod.getChildNodes().getLength(); i++){
			if("children".equalsIgnoreCase(nodeNotationToAddMethod.getChildNodes().item(i).getNodeName())){
				if(isLocationToAddMethodInNotationFile(nodeNotationToAddMethod, i, location)){
					return (Element) nodeNotationToAddMethod.getChildNodes().item(i);
				}
			}
		}
		return null; //TODO remover NULL
	}
	

	private boolean isLocationToAddMethodInNotationFile(Node nodeNotationToAddMethod, int i, String location ) {
		return nodeNotationToAddMethod.getChildNodes().item(i).getAttributes().getNamedItem("type").getNodeValue().equals(location);
	}

}