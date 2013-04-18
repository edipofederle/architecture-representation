package mestrado.arquitetura.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import mestrado.arquitetura.exceptions.NullReferenceFoundException;
import mestrado.arquitetura.helpers.UtilResources;
import mestrado.arquitetura.parser.method.Method;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ClassOperations extends XmiHelper {
	
	private static final String LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE = "7018";
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
	private Element klass;
	private DocumentManager documentManager;
	private Element notationBasicOperation;
	private Element notationBasicProperty;
	private ElementXmiGenerator elementXmiGenerator;
	
	private String idsProperties = new String();
	private String idsMethods = new String();
	
	private final static Logger LOGGER = Logger.getLogger(ClassOperations.class.getName()); 

	public ClassOperations(DocumentManager documentManager) {
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
		elementXmiGenerator = new ElementXmiGenerator(documentManager);
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
		
	    this.notationBasicProperty = createChildrenComportament(documentManager.getDocNotation(), node, "7017"); //onde vai as props
	    this.notationBasicOperation = createChildrenComportament(documentManager.getDocNotation(), node, LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE); // onde vai os metodos

	    node.appendChild(klass);
		
	    notatioChildren.appendChild(node);
	}

	public ClassOperations createClass(final String className) {
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				ElementXmiGenerator generator = new ElementXmiGenerator(documentManager);
				klass = generator.generateClass(className);
				umlModelChild.appendChild(klass);
				id = klass.getAttribute("xmi:id");
				try {
					createXmiForClassInNotationFile();
				} catch (NullReferenceFoundException e) {
					LOGGER.severe("A null reference has been found. The process will be interrupted");
				}
			}
		});
		
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
	public ClassOperations withAttribute(final String attribute) {

		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				String attributeType = getPropertyType(attribute);
				String name = getAttributeName(attribute);
				String idProperty = writeAttributeIntoUmlFile(attributeType, name);
				writeOnNotationFile(idProperty, PROPERTY_ID, PROPERTY_TYPE, notationBasicProperty);
		
				//Registra elemento criado. Mover daqui
				idsProperties += idProperty + " ";
			}
		});
		
		return this;
	}
	
	
	public ClassOperations withMethod(final mestrado.arquitetura.parser.method.Method method) {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				Element ownedOperation = elementXmiGenerator.generateMethod(method);
				klass.appendChild(ownedOperation);
				writeOnNotationFile(method.getId(), METHOD_ID, METHODO_TYPE, notationBasicOperation);
				idsMethods += method.getId() + " ";
			}
		
		});
		
		return this;
	}


	/**
	 * Finaliza a criação da classe.
	 * 
	 * @return {@link Map} com informações sobre a classe criada.
	 */
	public Map<String, String> build() {
		Map<String, String> createdClassInfos = new HashMap<String, String>();
		createdClassInfos.put("classId", klass.getAttribute("xmi:id"));
		createdClassInfos.put("idsProperties", this.idsProperties);
		createdClassInfos.put("idsMethods", this.idsMethods);
		return createdClassInfos;
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


	private String writeAttributeIntoUmlFile(String attributeType, String name) {
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
			if(isValidType(type)) return type;
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
	public String createAssociation(final String idClassOwnnerAssociation, final String idClassDestinationAssociation){
		//Refactoring, document.getNewName is common for many classes
		final AssociationNode associationNode = new AssociationNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation(), documentManager.getModelName());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				associationNode.createAssociation(idClassOwnnerAssociation, idClassDestinationAssociation);
			}
		});
		
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


	public void removeAttribute(final String idAttributeToRemove) {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeAttributeeById(idAttributeToRemove, id);
			}
		});
	}

	public void removeMethod(final String idMethodoToRmove) {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeMethodById(idMethodoToRmove, id);
			}
		});
	}


	public ClassOperations addMethodToClass(final String idClass, final Method method){
		final Node klassToAddMethod = findByID(documentManager.getDocUml(), idClass, "packagedElement");
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				ElementXmiGenerator elementXmiGenerator = new ElementXmiGenerator(documentManager);
				Element ownedOperation = elementXmiGenerator.generateMethod(method);
				klassToAddMethod.appendChild(ownedOperation);
				writeOnNotationFile(method.getId(), METHOD_ID, METHODO_TYPE, getNodeToAddMethodInNotationFile(idClass));
				idsMethods += method.getId() + " ";
			}
		
		});
		
		return this;
	}

	private Element getNodeToAddMethodInNotationFile(final String idClass) {
		Node nodeNotationToAddMethod = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
		for(int i =0; i <  nodeNotationToAddMethod.getChildNodes().getLength(); i++){
			if("children".equalsIgnoreCase(nodeNotationToAddMethod.getChildNodes().item(i).getNodeName())){
				if(isLocationToAddMethodInNotationFile(nodeNotationToAddMethod, i)){
					return (Element) nodeNotationToAddMethod.getChildNodes().item(i);
				}
			}
		}
		return null; //TODO remover NULL
	}


	private boolean isLocationToAddMethodInNotationFile(Node nodeNotationToAddMethod, int i) {
		return nodeNotationToAddMethod.getChildNodes().item(i).getAttributes().getNamedItem("type").getNodeValue().equals(LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE);
	}
}