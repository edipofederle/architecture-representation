package mestrado.arquitetura.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ClassOperations extends XmiHelper {

	private static final String PROPERTY_ID = "3012";
	private static final String METHOD_ID = "3013";
	private static final String PROPERTY_TYPE = "uml:Property";
	private static final String METHODO_TYPE = "uml:Operation";

	private static final String PROPERTYNOTYPE = "";
	
	
	private String xmitype = "notation:Shape";
	private String id;
	private final String type = "2008";
	private final String fontName = "Lucida Grande";
	private final String fontHeight = "11";
	private final String lineColor = "0";
	private Node umlModelChild;
	private Node notatioChildren;
	private DocumentManager documentManager;
	private Element klass;
	private Element notationBasicOperation;
	private Element notationBasicProperty;
	
	private String idsProperties = new String();

	public ClassOperations(DocumentManager documentManager) {
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
	}
	

	private void createXmiForClass() {
		
		Element node = documentManager.getDocNotation().createElement("children");
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", this.type);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
		Element notationDecoratioNode = documentManager.getDocNotation().createElement("children");
		notationDecoratioNode.setAttribute("xmi:type", "notation:DecorationNode");
		notationDecoratioNode.setAttribute("xmi:id", "020203903-03");
		notationDecoratioNode.setAttribute("type", "5029");
		node.appendChild(notationDecoratioNode);
		
	    Element klass = documentManager.getDocNotation().createElement("element");
	    klass.setAttribute("href", documentManager.getModelName()+".uml#"+this.id); // TODO ver nome do model
	    klass.setAttribute("xmi:type", "uml:Class");
		
	    this.notationBasicProperty = createChildrenComportament(documentManager.getDocNotation(), node, "7017"); //onde vai as props
	    this.notationBasicOperation = createChildrenComportament(documentManager.getDocNotation(), node, "7018"); // onde vai os metodos

	    node.appendChild(klass);
		
	    notatioChildren.appendChild(node);
	}

	//<packagedElement xmi:type="uml:Class" xmi:id="404-2093-03-04" name="Teste666"/>
	public ClassOperations createClass(String className2) {
		this.id = UtilResources.getRandonUUID();
		createXmiForClass();
		klass = documentManager.getDocUml().createElement("packagedElement");
		klass.setAttribute("xmi:type", "uml:Class");
		klass.setAttribute("xmi:id", this.id);
		klass.setAttribute("name", className2);
		System.out.println("Class with id: " + this.id + " created.");
		umlModelChild.appendChild(klass);
		return this;
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
	
	private String randomNum(){
		Random rn = new Random();
		int range = 1000 - 0 + 1;
		int randomNum =  rn.nextInt(range) + 0;
		return Integer.toString(randomNum);
	}
	
	public String getId(){
		return this.id;
	}

	
	/**
	 * Adiciona um atributo na classe.<br />
	 * 
	 * Tipos válidos são: "String", "Integer", "Real", "Boolean", "UnlimetedNatural".
	 * 
	 * @param attribute - ex: "name:String"
	 * @return this
	 */
	public ClassOperations withAttribute(String attribute) {

		String attributeType = getPropertyType(attribute);
		String name = getAttributeName(attribute);
		String idProperty = writeOnUmlFile(attributeType, name);
		writeOnNotationFile(idProperty, PROPERTY_ID, PROPERTY_TYPE, notationBasicProperty);
		
		return this;
	}
	
	/**
	 * Params like: foo[name:String, age:Integer]
	 * 
	 * @param method
	 * @return
	 */
	public ClassOperations withMethod(String method){
		//<ownedOperation xmi:id="_LIsv8JujEeK5t701AkjzsQ" name="foo"/>
		
		//MOVE FROM HERE
		String idMethod = UtilResources.getRandonUUID();
		Element ownedOperation = documentManager.getDocUml().createElement("ownedOperation");
		ownedOperation.setAttribute("xmi:id", idMethod);
		ownedOperation.setAttribute("name", getMethodName(method));
		
		//Check for method params	
		Map<String, String> params = ParameterParser.getParams(method);
		

		for (Map.Entry<String, String> entry : params.entrySet()) {
		    String paramName = entry.getKey();
		    Object paramType = entry.getValue();
		    
			Element ownedParameter  = documentManager.getDocUml().createElement("ownedParameter");
			ownedParameter.setAttribute("xmi:id", UtilResources.getRandonUUID());
			ownedParameter.setAttribute("name", paramName);
			ownedParameter.setAttribute("isUnique", "false");
			
			Element typeOperation = documentManager.getDocUml().createElement("type");
			typeOperation.setAttribute("xmi:type", "uml:PrimitiveType");
			typeOperation.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#"+paramType);
			ownedParameter.appendChild(typeOperation);
			
			ownedOperation.appendChild(ownedParameter);
			
		}
	
		//Add method to current class
		klass.appendChild(ownedOperation);
		
		writeOnNotationFile(idMethod, METHOD_ID, METHODO_TYPE, notationBasicOperation);
		
		return this;
	}

	private String getMethodName(String method) {
		if(method.contains("["))
			return method.substring(0, method.indexOf("["));
		return method;
	}


	/**
	 * Finaliza a criação da classe.
	 * 
	 * @return {@link Map} com informações sobre a classe criada.
	 */
	public Map<String, String> build() {
		Map<String, String> createdClassInfos = new HashMap<String, String>();
		createdClassInfos.put("classId", this.getId());
		createdClassInfos.put("idsProperties", this.idsProperties);
		return createdClassInfos;
	}
	
	private void writeOnNotationFile(String idProperty, String idType, String type, Element appendTo) {
		createNoteForElementType(idProperty, idType, type, appendTo);
		
		this.idsProperties += idProperty + " ";
	}


	private void createNoteForElementType(String idProperty, String type, String typeElement, Element appendTo) {
		Element node = documentManager.getDocNotation().createElement("children");
		
		
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", type);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
		//Need to show params method name
//        <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_Mwp7MJ-TEeKGCvwmXOo5rw" source="CustomAppearance_Annotation">
//        <details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_-0OnIJ-TEeKGCvwmXOo5rw" key="CustomAppearance_MaskValue" value="7050"/>
//      </eAnnotations>
      	Element eAnnotations = documentManager.getDocNotation().createElement("eAnnotations");
      	eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
      	eAnnotations.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	eAnnotations.setAttribute("source", "CustomAppearance_Annotation");
      	
      	Element details = documentManager.getDocNotation().createElement("details");
      	details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
      	details.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	details.setAttribute("key", "CustomAppearance_MaskValue");
      	details.setAttribute("value", "7050");
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


	private String writeOnUmlFile(String attributeType, String name) {
		String idProperty = UtilResources.getRandonUUID();
		Element ownedAttribute = documentManager.getDocUml().createElement("ownedAttribute");
		ownedAttribute.setAttribute("xmi:id", idProperty);
		ownedAttribute.setAttribute("name", name);
		ownedAttribute.setAttribute("visibility", "public");
		klass.appendChild(ownedAttribute);
		
		if(!attributeType.equals("")){
			Element typeProperty = documentManager.getDocUml().createElement("type");
			typeProperty.setAttribute("xmi:type", "uml:PrimitiveType");
			typeProperty.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#"+attributeType);
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
		return idProperty;
	}

	private String getAttributeName(String attribute) {
		return attribute.split(":")[0];
	}

	private String getPropertyType(String attributeName) {
		if (attributeName.contains(":")){
			String type = attributeName.split(":")[1];
			if(isValidType(type))
				return type;
		}
		
		return PROPERTYNOTYPE;
	}


	private boolean isValidType(String type) {
		List<String> validTypes = new ArrayList<String>(Arrays.asList("String", "Integer", "Real", "Boolean", "UnlimetedNatural"));
		return validTypes.contains(type);
	}
	
	/**
	 * 
	 * @param idClassOwnnerAssociation
	 * @param idClassDestinationAssociation
	 * @return
	 */
	public String createAssociation(String idClassOwnnerAssociation, String idClassDestinationAssociation){
		
		//Refactoring, document.getNewName is common for many classes
		AssociationNode associationNode = new AssociationNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation(), documentManager.getModelName());
		associationNode.createAssociation(idClassOwnnerAssociation, idClassDestinationAssociation);
		return associationNode.getIdAssocation();
	}

	public void removeAssociation(String idAssociation) {
		AssociationNode associationNode = new AssociationNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation(), documentManager.getModelName());
	    associationNode.removeAssociation(idAssociation);
	}
	
	public void removeClassById(String id) {
		RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		removeClass.removeClassById(id);
	}
	
}
