package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.NullReferenceFoundException;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author edipofederle
 *
 */
public class ClassNotation extends XmiHelper {
	
	private static final String SHOW_TYPE_OF_ATTRIBUTE = "7066";
	
	private String xmitype = "notation:Shape";
	private final String fontName = "Lucida Grande";
	private final String fontHeight = "11";
	private final String lineColor = "0";
	private final String type = "2008";
	private Element notationBasicOperation;
	private Node notatioChildren;
	private Element notationBasicProperty;
	private static final String LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE = "7018";
	private static final String LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE = "7017";
	
	private DocumentManager documentManager;

	public ClassNotation(DocumentManager documentManager, Node notatioChildren){
		this.documentManager = documentManager;
		this.notatioChildren = notatioChildren;
	}

	public void createNodeForElementType(String idProperty, String type, String typeElement, Element appendTo) {
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
		
		if(appendTo != null)
			appendTo.appendChild(node);
		else
			notationBasicProperty.appendChild(node);
	}
	
	public void createXmiForClassInNotationFile(String id) throws NullReferenceFoundException {
		
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
	    
	    if (id == null){
	    	throw new NullReferenceFoundException("A null reference found when try access attribute id. Executation will be interrupted.");
	    }
	    
	   	klass.setAttribute("href", documentManager.getModelName()+".uml#"+ id);
	    	
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
	
	
	

		
}