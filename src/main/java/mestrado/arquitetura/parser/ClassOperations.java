package mestrado.arquitetura.parser;

import java.util.HashMap;
import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.parser.method.Attribute;
import mestrado.arquitetura.parser.method.Method;

public class ClassOperations extends XmiHelper {
	
	
	private String idClass;
	private DocumentManager documentManager;
	private ElementXmiGenerator elementXmiGenerator;
	private String idsProperties = new String();
	private String idsMethods = new String();
	

	public ClassOperations(DocumentManager documentManager) {
		this.documentManager = documentManager;
		elementXmiGenerator = new ElementXmiGenerator(documentManager);
	}
	
	public ClassOperations createClass(final String className) {
		try {
			this.idClass = elementXmiGenerator.generateClass(className);
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
	 */
	public ClassOperations withAttribute(final Attribute attribute) throws CustonTypeNotFound {

		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() throws CustonTypeNotFound {
				elementXmiGenerator.generateAttribute(attribute, null);
				idsProperties += attribute.getId() + " ";
			
			}
		});
		
		return this;
	}
	
	
	public ClassOperations withMethod(final mestrado.arquitetura.parser.method.Method method) throws CustonTypeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
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
	 */
	public Map<String, String> build() {
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
	 */
	public String createAssociation(final String idClassOwnnerAssociation, final String idClassDestinationAssociation) throws CustonTypeNotFound{
		//Refactoring, document.getNewName is common for many classes
		final AssociationNode associationNode = new AssociationNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation(), documentManager.getModelName());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				associationNode.createAssociation(idClassOwnnerAssociation, idClassDestinationAssociation);
			}
		});
		
		return associationNode.getIdAssocation();
	}

	public void removeAssociation(final String idAssociation) throws CustonTypeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				AssociationNode associationNode = new AssociationNode(documentManager.getDocUml(), documentManager.getDocNotation(), documentManager.getModelName());
				associationNode.removeAssociation(idAssociation);
			}
		});
	}
	
	public void removeClassById(final String id) throws CustonTypeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				RemoveNode removeClass = new RemoveNode(documentManager.getDocUml(), documentManager.getDocNotation());
				removeClass.removeClassById(id);
			}
		});
	}


	public void removeAttribute(final String idAttributeToRemove) throws CustonTypeNotFound {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeAttributeeById(idAttributeToRemove, idClass);
			}
		});
	}

	public void removeMethod(final String idMethodoToRmove) throws CustonTypeNotFound {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeMethodById(idMethodoToRmove, idClass);
			}
		});
	}


	public ClassOperations addMethodToClass(final String idClass, final Method method) throws CustonTypeNotFound{
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.generateMethod(method, idClass);
				idsMethods += method.getId() + " ";
			}
		});
		
		return this;
	}

	public void addAttributeToClass(final String idClass, final Attribute attribute) throws CustonTypeNotFound {
		mestrado.arquitetura.parser.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				try {
					elementXmiGenerator.generateAttribute(attribute, idClass);
					idsProperties += attribute.getId() + " ";
				} catch (CustonTypeNotFound e) {
					e.printStackTrace();
				}
			}
		});
	}
}