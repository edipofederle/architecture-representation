package mestrado.arquitetura.parser;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PackageOperations extends XmiHelper {
	
	
	private DocumentManager documentManager;
	private Node umlModelChild;
	private Node notatioChildren;
	private Element pkg;
	private ClassNotation notation;
	private String id;
	
	public PackageOperations(DocumentManager documentManager) {
		this.documentManager = documentManager;
		this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
		notation = new ClassNotation(this.documentManager, notatioChildren);
	}
	
	
	public PackageOperations createPacakge(final String packageName) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		setIdPackage(UtilResources.getRandonUUID());
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){

			public void useTransformation() {
				
				pkg = documentManager.getDocUml().createElement("packagedElement");
				pkg.setAttribute("xmi:type", "uml:Package");
				pkg.setAttribute("xmi:id", getId());
				pkg.setAttribute("name", packageName);
				umlModelChild.appendChild(pkg);
				
				notation.createXmiForPackageInNotationFile(id);
			}
		
		});
		
		return this;
	}

	
	private void setIdPackage(String randonUUID) {
		this.id = randonUUID;
	}
	
	private String getId(){
		return this.id;
	}


	public Map<String, String> build() throws CustonTypeNotFound, NodeNotFound {
		Map<String, String> classesInfo = new HashMap<String, String>();
		classesInfo.put("packageId", getId());
		return classesInfo;
	}
	

	public PackageOperations withClass(final String klassId) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			
			//Primeiramente é olhado para o arquivo .uml e movido a classe para o pacote.
			final Node classToMove = findByID(documentManager.getDocUml(), klassId, "packagedElement");
			final Node packageToAdd = findByID(documentManager.getDocUml(), id, "packagedElement");
			
			//Agora buscamos no arquivo .notaiton
			Node classToMoveNotation = findByIDInNotationFile(documentManager.getDocNotation(), klassId);
			Node packageToAddNotation = findByIDInNotationFile(documentManager.getDocNotation(), id);
			
			public void useTransformation() throws NodeNotFound {
				packageToAdd.appendChild(classToMove);
				packageToAddNotation.appendChild(classToMoveNotation);
			}});
		
		return this;
	}
}