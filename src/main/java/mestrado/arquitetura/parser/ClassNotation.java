package mestrado.arquitetura.parser;

import java.util.Random;

import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author edipofederle
 *
 */
public class ClassNotation {

	private String xmitype = "notation:Shape";
	private final String id;
	private final String type = "2008";
	private final String fontName = "Lucida Grande";
	private final String fontHeight = "11";
	private final String lineColor = "0";
	private Document docNotation;
	private Document docUml;
	private Node umlModelChild;
	private Node notatioChildren;
	private String newModelName;
	
	/**
	 * 
	 * @param xmitype
	 * @param docNotation
	 * @param docUml
	 */
	public ClassNotation(Document docNotation, Document docUml, String newModelName){
		this.newModelName = newModelName;
		this.id = UtilResources.getRandonUUID();
		this.docNotation = docNotation;
		this.docUml = docUml;
		this.umlModelChild = docUml.getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = docNotation.getElementsByTagName("notation:Diagram").item(0);
	}
	

	private void createXmiForClass() {

		Element node = docNotation.createElement("children");
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", this.type);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
		Element notationDecoratioNode = docNotation.createElement("children");
		notationDecoratioNode.setAttribute("xmi:type", "notation:DecorationNode");
		notationDecoratioNode.setAttribute("xmi:id", "020203903-03");
		notationDecoratioNode.setAttribute("type", "5029");
		node.appendChild(notationDecoratioNode);
		
	    Element klass = docNotation.createElement("element");
	    klass.setAttribute("href", this.newModelName+".uml#"+this.id);
	    klass.setAttribute("xmi:type", "uml:Class");
		
		createChildrenComportament(docNotation, node, "7017");
		createChildrenComportament(docNotation, node, "7018");

	    node.appendChild(klass);
		
	    notatioChildren.appendChild(node);
	}

	public void createClass(String className2) {
		createXmiForClass();
		Element klass = docUml.createElement("packagedElement");
		klass.setAttribute("xmi:type", "uml:Class");
		klass.setAttribute("xmi:id", this.id);
		klass.setAttribute("name", className2);
		System.out.println("Class with id: " + this.id + " created.");
		umlModelChild.appendChild(klass);
	}


	private void createChildrenComportament(Document doc, Element node, String type) {
		Element notationBasicCompartment = doc.createElement("children");
		notationBasicCompartment.setAttribute("xmi:type", "notation:BasicCompartment");
		notationBasicCompartment.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationBasicCompartment.setAttribute("type", type);
		node.appendChild(notationBasicCompartment);
		
		
		Element notationTitleStyle = doc.createElement("styles");
		notationTitleStyle.setAttribute("xmi:type", "notation:TitleStyle");
		notationTitleStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationBasicCompartment.appendChild(notationTitleStyle);
		
		
		Element notationSortingStyle = doc.createElement("styles");
		notationSortingStyle.setAttribute("xmi:type", "notation:SortingStyle");
		notationSortingStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationBasicCompartment.appendChild(notationSortingStyle);
		
		Element notationFilteringStyle = doc.createElement("styles");
		notationFilteringStyle.setAttribute("xmi:type", "notation:FilteringStyle");
		notationFilteringStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationBasicCompartment.appendChild(notationFilteringStyle);
		
		Element notationBounds = doc.createElement("layoutConstraint");
		notationBounds.setAttribute("xmi:type", "notation:Bounds");
		notationBounds.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationBasicCompartment.appendChild(notationBounds);
		
	    Element layoutConstraint = doc.createElement("layoutConstraint");
	    layoutConstraint.setAttribute("x", randomNum());
	    layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
	    layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
	    layoutConstraint.setAttribute("y", randomNum());
	    node.appendChild(layoutConstraint);
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
		
}