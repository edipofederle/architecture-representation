package mestrado.arquitetura.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
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
		this.elementXmiGenerator = new ElementXmiGenerator(documentManager);
	}
	
	public ClassOperations createClass(final String className) throws NodeNotFound, InvalidMultiplictyForAssociationException {
		try {
			klass = elementXmiGenerator.generateClass(className, WITHOUT_PACKAGE);
			this.idClass = klass.getAttributes().getNamedItem("xmi:id").getNodeValue();
		} catch (CustonTypeNotFound e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Cria {@link Attribute} para a classe
	 * 
	 * @param attributes
	 * @return
	 * @throws CustonTypeNotFound
	 * @throws NodeNotFound
	 * @throws InvalidMultiplictyForAssociationException
	 */
	public ClassOperations withAttribute(final List<Attribute> attributes) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {

		for (final Attribute attribute : attributes) {
			mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
				public void useTransformation() throws CustonTypeNotFound, NodeNotFound {
					elementXmiGenerator.generateAttribute(attribute, null);
					idsProperties += attribute.getId() + " ";
				
				}
			});
		}

		
		return this;
	}
	
	
	public ClassOperations withMethod(final mestrado.arquitetura.parser.method.Method method) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
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
	 * @throws InvalidMultiplictyForAssociationException 
	 */
	public Map<String, String> build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
			Element e = (Element) klass;
			e.setAttribute("isAbstract", isClassAbstract(isAbstract));
			}
		});
		
		Map<String, String> createdClassInfos = new HashMap<String, String>();
		createdClassInfos.put("id", this.idClass);
		createdClassInfos.put("idsProperties", this.idsProperties);
		createdClassInfos.put("idsMethods", this.idsMethods);
		
		return createdClassInfos;
	}
	

	
	public void removeClassById(final String id) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				RemoveNode removeClass = new RemoveNode(documentManager.getDocUml(), documentManager.getDocNotation());
				removeClass.removeClassById(id);
			}
		});
	}


	public void removeAttribute(final String idAttributeToRemove) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeAttributeeById(idAttributeToRemove, idClass);
			}
		});
	}

	public void removeMethod(final String idMethodoToRmove) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeMethodById(idMethodoToRmove, idClass);
			}
		});
	}


	public ClassOperations addMethodToClass(final String idClass, final Method method) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws NodeNotFound {
				elementXmiGenerator.generateMethod(method, idClass);
				idsMethods += method.getId() + " ";
			}
		});
		
		return this;
	}

	public void addAttributeToClass(final String idClass, final Attribute attribute) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
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