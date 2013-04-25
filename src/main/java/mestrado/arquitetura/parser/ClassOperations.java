package mestrado.arquitetura.parser;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.parser.method.Attribute;
import mestrado.arquitetura.parser.method.Method;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ClassOperations extends XmiHelper {
	
	
	private static final String WITHOUT_PACKAGE = ""; // Classe sem pacote
	private String idClass;
	private DocumentManager documentManager;
	private ElementXmiGenerator elementXmiGenerator;
	private String idsProperties = new String();
	private String idsMethods = new String();
	private Node klass;
	private boolean isAbstract = false;
	

	public ClassOperations(DocumentManager documentManager) {
		this.documentManager = documentManager;
		elementXmiGenerator = new ElementXmiGenerator(documentManager);
	}
	
	public ClassOperations createClass(final String className) throws NodeNotFound {
		try {
			klass = elementXmiGenerator.generateClass(className, WITHOUT_PACKAGE);
			this.idClass = klass.getAttributes().getNamedItem("xmi:id").getNodeValue();
		} catch (CustonTypeNotFound e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Adiciona um atributo na classe.<br />
	 * 
	 * Tipos válidos são: "String", "Integer", "Real", "Boolean", "UnlimetedNatural".
	 * 
	 * @param attribute - ex: "name:String"
	 * @return this
	 * @throws CustonTypeNotFound 
	 * @throws NodeNotFound 
	 */
	public ClassOperations withAttribute(final Attribute attribute) throws CustonTypeNotFound, NodeNotFound {

		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws CustonTypeNotFound, NodeNotFound {
				elementXmiGenerator.generateAttribute(attribute, null);
				idsProperties += attribute.getId() + " ";
			
			}
		});
		
		return this;
	}
	
	
	public ClassOperations withMethod(final mestrado.arquitetura.parser.method.Method method) throws CustonTypeNotFound, NodeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
				elementXmiGenerator.generateMethod(method, null);
				idsMethods += method.getId() + " ";
			}
		});
		
		return this;
	}


	/**
	 * Finaliza a criação da classe.
	 * 
	 * @return {@link Map} com informações sobre a classe criada.
	 * @throws NodeNotFound 
	 * @throws CustonTypeNotFound 
	 */
	public Map<String, String> build() throws CustonTypeNotFound, NodeNotFound {
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
			Element e = (Element) klass;
			e.setAttribute("isAbstract", isClassAbstract(isAbstract));
			}
		});
		
		Map<String, String> createdClassInfos = new HashMap<String, String>();
		createdClassInfos.put("classId", this.idClass);
		createdClassInfos.put("idsProperties", this.idsProperties);
		createdClassInfos.put("idsMethods", this.idsMethods);
		
		return createdClassInfos;
	}
	

	/**
	 * 
	 * @param idClassOwnnerAssociation
	 * @param idClassDestinationAssociation
	 * @return
	 * @throws CustonTypeNotFound 
	 * @throws NodeNotFound 
	 */
	public String createAssociation(final String idClassOwnnerAssociation, final String idClassDestinationAssociation) throws CustonTypeNotFound, NodeNotFound{
		//Refactoring, document.getNewName is common for many classes
		final AssociationNode associationNode = new AssociationNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation(), documentManager.getModelName());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
				associationNode.createAssociation(idClassOwnnerAssociation, idClassDestinationAssociation);
			}
		});
		
		return associationNode.getIdAssocation();
	}

	public void removeAssociation(final String idAssociation) throws CustonTypeNotFound, NodeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				AssociationNode associationNode = new AssociationNode(documentManager.getDocUml(), documentManager.getDocNotation(), documentManager.getModelName());
				associationNode.removeAssociation(idAssociation);
			}
		});
	}
	
	public void removeClassById(final String id) throws CustonTypeNotFound, NodeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				RemoveNode removeClass = new RemoveNode(documentManager.getDocUml(), documentManager.getDocNotation());
				removeClass.removeClassById(id);
			}
		});
	}


	public void removeAttribute(final String idAttributeToRemove) throws CustonTypeNotFound, NodeNotFound {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeAttributeeById(idAttributeToRemove, idClass);
			}
		});
	}

	public void removeMethod(final String idMethodoToRmove) throws CustonTypeNotFound, NodeNotFound {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeMethodById(idMethodoToRmove, idClass);
			}
		});
	}


	public ClassOperations addMethodToClass(final String idClass, final Method method) throws CustonTypeNotFound, NodeNotFound{
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
				elementXmiGenerator.generateMethod(method, idClass);
				idsMethods += method.getId() + " ";
			}
		});
		
		return this;
	}

	public void addAttributeToClass(final String idClass, final Attribute attribute) throws CustonTypeNotFound, NodeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
				try {
					elementXmiGenerator.generateAttribute(attribute, idClass);
					idsProperties += attribute.getId() + " ";
				} catch (CustonTypeNotFound e) {
					e.printStackTrace();
				}
			}
		});
	}


	//TODO move comon
	public ClassOperations isAbstract() {
		this.isAbstract  = true;
		return this;
	}
	
}