package mestrado.arquitetura.parser;

import java.util.ArrayList;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PackageOperation extends XmiHelper {
	
	
	private DocumentManager documentManager;
	private Node umlModelChild;
	private Node notatioChildren;
	private Element element;
	private ClassNotation notation;
	private String id;
	private Node klass;
	
	private ArrayList<String> classesInfo = new ArrayList<String>();
	private boolean isAbstract = false;

	public PackageOperation(DocumentManager documentManager) {
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
		notation = new ClassNotation(this.documentManager, notatioChildren);
	}
	
	
	public PackageOperation createPacakge(final String packageName) throws CustonTypeNotFound, NodeNotFound{
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){

			public void useTransformation() {
				id = UtilResources.getRandonUUID();
				element = documentManager.getDocUml().createElement("packagedElement");
				element.setAttribute("xmi:type", "uml:Package");
				element.setAttribute("xmi:id", id);
				element.setAttribute("name", packageName);
				umlModelChild.appendChild(element);
				
				notation.createXmiForPackageInNotationFile(id);
			}
		
		});
		
		return this;
	}


	public PackageOperation withClass(String klass) throws CustonTypeNotFound, NodeNotFound {
		ElementXmiGenerator elementXmi = new ElementXmiGenerator(documentManager);
		this.klass = elementXmi.generateClass(klass, this.id);
		classesInfo.add(this.klass.getAttributes().getNamedItem("xmi:id").getNodeValue()+":"+klass);
		
		return this;
	}
	

	public PackageOperation isAbstract() {
		this.isAbstract  = true;
		return this;
	}
	
	public ArrayList<String> build() throws CustonTypeNotFound, NodeNotFound {
		
		//TODO mover, comum
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
			Element e = (Element) klass;
			e.setAttribute("isAbstract", isClassAbstract(isAbstract));
			}
		});
		
		
		return classesInfo;
	}


	public void withClass(Node klass2) {
		
	}

}