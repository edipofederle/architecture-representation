package mestrado.arquitetura.api.touml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;
import mestrado.arquitetura.parser.method.Attribute;
import mestrado.arquitetura.parser.method.Method;
import mestrado.arquitetura.representation.Variant;
import mestrado.arquitetura.utils.UtilResources;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Profile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class ClassOperations extends XmiHelper {
	
	
	static Logger LOGGER = LogManager.getLogger(ClassOperations.class.getName());
	

	private static final String WITHOUT_PACKAGE = ""; // Classe sem pacote
	private String idClass;
	private DocumentManager documentManager;
	private ElementXmiGenerator elementXmiGenerator;
	private String idsProperties = new String();
	private String idsMethods = new String();
	private Node klass;
	private boolean isAbstract = false;

	private Uml2Helper uml2Helper;

	private org.eclipse.uml2.uml.Stereotype stereotype;

	public ClassOperations(DocumentManager documentManager) {
		uml2Helper = Uml2HelperFactory.getUml2Helper();
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
			mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
				public void useTransformation() {
					elementXmiGenerator.generateAttribute(attribute, null);
					idsProperties += attribute.getId() + " ";
				
				}
			});
		}

		
		return this;
	}
	
	
	public ClassOperations withMethod(final mestrado.arquitetura.parser.method.Method method) throws CustonTypeNotFound{
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
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
	 * @throws NodeNotFound 
	 * @throws CustonTypeNotFound 
	 * @throws InvalidMultiplictyForAssociationException 
	 */
	public Map<String, String> build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
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
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				RemoveNode removeClass = new RemoveNode(documentManager.getDocUml(), documentManager.getDocNotation());
				removeClass.removeClassById(id);
			}
		});
	}


	public void removeAttribute(final String idAttributeToRemove) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeAttributeeById(idAttributeToRemove, idClass);
			}
		});
	}

	public void removeMethod(final String idMethodoToRmove) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
		
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				removeClass.removeMethodById(idMethodoToRmove, idClass);
			}
		});
	}


	public ClassOperations addMethodToClass(final String idClass, final Method method) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException{
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.generateMethod(method, idClass);
				idsMethods += method.getId() + " ";
			}
		});
		
		return this;
	}

	public void addAttributeToClass(final String idClass, final Attribute attribute) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.generateAttribute(attribute, idClass);
				idsProperties += attribute.getId() + " ";
			}
		});
	}


	//TODO move comon
	public ClassOperations isAbstract() {
		this.isAbstract  = true;
		return this;
	}

	/**
	 * Aplica um estereótipo na classe. Se o estereótipo não existir no profile uma exceção é lançada. 
	 * 
	 * @param stereotypeName
	 * @return ClassOperations
	 * @throws SMartyProfileNotAppliedToModelExcepetion 
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 * @throws InvalidMultiplictyForAssociationException 
	 * @throws NodeNotFound 
	 * @throws CustonTypeNotFound 
	 */
	public ClassOperations withStereoype(final Variant ... stereotypeNames) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion, CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		
		Profile profile = uml2Helper.loadSMartyProfile();
		
		
		for (final Variant variant : stereotypeNames) {
			 stereotype = profile.getOwnedStereotype(variant.getVariantName());
			if(stereotype == null)
				LOGGER.warn("Stereotype + "+variant.getVariantName() + " cannot be found at profile.");
			
			mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
				public void useTransformation() {
					elementXmiGenerator.createStereotype(variant, idClass);
				}
			});
		}

		return this;
	}

	/**
	 * 
	 * Aplica um dado estereótipo a classe. Aplicavéis são:
	 * 
	 *   	<li>mandatory</li>
	 * 		<li>optional</li>
	 *  	<li>alternative_OR</li>
	 * 	 	</li>alternative_XOR</li>
	 * 
	 * @param id - Classe id
	 * @param mandatory - Estereótipo
	 * @throws ModelNotFoundException
	 * @throws ModelIncompleteException
	 * @throws SMartyProfileNotAppliedToModelExcepetion
	 * @throws CustonTypeNotFound
	 * @throws NodeNotFound
	 * @throws InvalidMultiplictyForAssociationException
	 */
	public void addStereotype(final String id, final Variant mandatory) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion, CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation()  {
				elementXmiGenerator.createStereotype(mandatory, id);
			}
		});
	}


	/**
	 * Indica que a classe sendo criada é um ponto de variação, ou seja, possui o estereótipo <b>variationPoint</b>
	 * 
	 * @return {@link ClassOperations}
	 * @throws CustonTypeNotFound
	 * @throws NodeNotFound
	 * @throws InvalidMultiplictyForAssociationException
	 */
	public ClassOperations isVariationPoint() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.createStereotypeVariationPoint(idClass);
			}
		});
		return this;
	}

	/**
	 * Anota uma classe com um dado comentário
	 * 
	 * @param id - ID do comentário.
	 * @throws InvalidMultiplictyForAssociationException 
	 * @throws NodeNotFound 
	 * @throws CustonTypeNotFound 
	 */
	public ClassOperations linkToNote(final String id) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				
				Element comment = (Element) findByID(documentManager.getDocUml(), id, "ownedComment");
				comment.setAttribute("annotatedElement", idClass);
				
				//pega o id do source e do target no arquivo .notation
				Node commentElement = findByIDInNotationFile(documentManager.getDocNotation(), id);
				Node classElement = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
				String idCommentNotation = commentElement.getAttributes().getNamedItem("xmi:id").getNodeValue();
				String idClassNotation = classElement.getAttributes().getNamedItem("xmi:id").getNodeValue();
				
				Element edges = documentManager.getDocNotation().createElement("edges");
				edges.setAttribute("xmi:type", "notation:Connector");
				edges.setAttribute("xmi:id", UtilResources.getRandonUUID());
				edges.setAttribute("type", "4013");
				edges.setAttribute("source", idCommentNotation);
				edges.setAttribute("target", idClassNotation);
				edges.setAttribute("lineColor", "0");
				
				Element styles = documentManager.getDocNotation().createElement("styles");
				styles.setAttribute("xmi:id", UtilResources.getRandonUUID());
				styles.setAttribute("fontName", "Lucida Grande");
				styles.setAttribute("xmi:type", "notation:FontStyle");
				styles.setAttribute("fontHeight", "11");
				edges.appendChild(styles);
				
				Element element = documentManager.getDocNotation().createElement("element");
				element.setAttribute("xsi:nil", "true");
				edges.appendChild(element);
				
				Element bendpoints = documentManager.getDocNotation().createElement("bendpoints");
				bendpoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
				bendpoints.setAttribute("xmi:id", UtilResources.getRandonUUID());
				bendpoints.setAttribute("points", "[-10, 17, 55, -89]$[-63, 156, 2, 50]");
				edges.appendChild(bendpoints);
				
				Element sourceAnchor = documentManager.getDocNotation().createElement("sourceAnchor");
				sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
				sourceAnchor.setAttribute("xmi:id", UtilResources.getRandonUUID());
				sourceAnchor.setAttribute("id", "(1.0,0.7166666666666667)");
				edges.appendChild(sourceAnchor);
				
				 Node root = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
				 root.appendChild(edges);
				
			}
		});
		
		return this;
	}

	public ClassOperations withId(String ownerClass) {
		this.idClass = ownerClass;
		return this;
	}
	
	/**
	 * Adiciona um <b>concern</b> na classe.
	 * 
	 * @param name - Nome do <b>concern</b> esse deve estar definido no profile <b>perfilConcerns.profile.uml</b>
	 * @return ClassOperations
	 */
	public ClassOperations withConcern(final String name) {
		mestrado.arquitetura.api.touml.Document.executeTransformation(documentManager, new Transformation(){
			public void useTransformation() {
				elementXmiGenerator.createConcern(name);
			}
		});
		
		return this;
	}

}