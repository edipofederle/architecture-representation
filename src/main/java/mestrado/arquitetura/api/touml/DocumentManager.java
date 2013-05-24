package mestrado.arquitetura.api.touml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.io.CopyFile;
import mestrado.arquitetura.io.ReaderConfig;
import mestrado.arquitetura.io.SaveAndCopy;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DocumentManager extends XmiHelper {
	
	private org.w3c.dom.Document docUml;
	private org.w3c.dom.Document docNotation;
	private org.w3c.dom.Document docDi;
	private final String BASE_DOCUMENT = "simples";
	private String outputModelName;
	
	static Logger LOGGER = LogManager.getLogger(DocumentManager.class.getName());
	
	public DocumentManager(String outputModelName) throws ModelNotFoundException, ModelIncompleteException {
		this.outputModelName = outputModelName;
		
		String pathToFiles = "src/main/java/mestrado/arquitetura/api/touml/1/";// model padrao vazio que o programa usa para construir o novo
		makeACopy(pathToFiles, BASE_DOCUMENT);
		createXMIDocument();
		
		try {
			updateProfilesRefs();
		} catch (CustonTypeNotFound e) {
			e.printStackTrace();
		} catch (NodeNotFound e) {
			e.printStackTrace();
		} catch (InvalidMultiplictyForAssociationException e) {
			e.printStackTrace();
		}
		
		this.saveAndCopy(outputModelName);
		
		
	}
	

	private void createXMIDocument(){
		DocumentBuilderFactory docBuilderFactoryNotation = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilderNotation = null;
		DocumentBuilder docBuilderUml = null;
		
		try {
			docBuilderNotation = docBuilderFactoryNotation.newDocumentBuilder();
			DocumentBuilderFactory docBuilderFactoryUml = DocumentBuilderFactory.newInstance();
			docBuilderUml = docBuilderFactoryUml.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
			
		try {
			this.docNotation =  docBuilderNotation.parse("manipulation/"+BASE_DOCUMENT+".notation");
			this.docUml = docBuilderUml.parse("manipulation/"+BASE_DOCUMENT+".uml");
			this.docDi = docBuilderUml.parse("manipulation/"+BASE_DOCUMENT+".di");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Realiza um cópia dos três arquivos para o diretório <b>manipulation</b>.
	 * 
	 * @param pathToFiles
	 * @param modelName
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 * @throws SMartyProfileNotAppliedToModelExcepetion 
	 * @throws IOException
	 */
	private void makeACopy(String pathToFiles, String modelName) throws ModelNotFoundException, ModelIncompleteException {
		
		String notationCopy = "manipulation/"+BASE_DOCUMENT+".notation";
		String umlCopy = "manipulation/"+BASE_DOCUMENT+".uml";
		String diCopy = "manipulation/"+BASE_DOCUMENT+".di";
		
		
		try {
			CopyFile.copyFile(new File(pathToFiles+modelName+".notation"), new File(notationCopy));
			CopyFile.copyFile(new File(pathToFiles+modelName+".uml"), new File(umlCopy));
			CopyFile.copyFile(new File(pathToFiles+modelName+".di"), new File(diCopy));
						
		} catch (IOException e) {
			LOGGER.error("I cannot copy all files. Here a message erros: " + e.getMessage());
		}
		
		
	}

	/**
	 * @return the docUml
	 */
	public org.w3c.dom.Document getDocUml() {
		return docUml;
	}

	/**
	 * @return the docNotation
	 */
	public org.w3c.dom.Document getDocNotation() {
		return docNotation;
	}

	public void saveAndCopy(String newModelName) {
		this.outputModelName = newModelName;
		
		try {
			SaveAndCopy.saveAndCopy(docNotation, docUml, docDi, BASE_DOCUMENT, newModelName);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getModelName() {
		return BASE_DOCUMENT;
	}

	public String getNewModelName() {
		return this.outputModelName;
	}


	public void updateProfilesRefs() throws ModelNotFoundException, ModelIncompleteException, CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		String pathToProfileSMarty = ReaderConfig.getPathToProfileSMarty();
		String pathToProfileConcern = ReaderConfig.getPathToProfileConcerns();
		
		
		DocumentBuilderFactory factorySmarty = DocumentBuilderFactory.newInstance();
		DocumentBuilder profileSmarty = null;
		
		DocumentBuilderFactory factoryConcern = DocumentBuilderFactory.newInstance();
		DocumentBuilder profileConcern = null;
		
		try {
			profileSmarty = factorySmarty.newDocumentBuilder();
			final Document docProfile = profileSmarty.parse(pathToProfileSMarty);
			
			profileConcern = factoryConcern.newDocumentBuilder();
			final Document docConcern = profileConcern.parse(pathToProfileConcern);
			
			updateHrefAtt(getRefId(docProfile, "contents", "xmi:id"), "smarty", "appliedProfile", false);
			updateHrefAtt(getRefId(docConcern, "contents", "xmi:id"), "Concerns", "appliedProfile", false);
			
			updateHrefAtt(getRefId(docProfile, "uml:Profile", "xmi:id"), "smarty", "appliedProfile", true);
			updateHrefAtt(getRefId(docConcern, "uml:Profile", "xmi:id"), "Concerns", "appliedProfile", true);
			
			
			//Recuperar os valores de nsURI para os profiles.
			final String nsUriPerfilSmarty = getRefId(docProfile, "contents", "nsURI");
			final String nsUriPerfilConcern = getRefId(docConcern, "contents", "nsURI");
			
			mestrado.arquitetura.api.touml.Document.executeTransformation(this, new Transformation(){
				public void useTransformation() throws CustonTypeNotFound, NodeNotFound {
					Node xmlsnsSmarty = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes().getNamedItem("xmlns:smartyProfile");
					xmlsnsSmarty.setNodeValue(nsUriPerfilSmarty);
					
					Node xmlsnsConcern = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes().getNamedItem("xmlns:perfilConcerns");
					xmlsnsConcern.setNodeValue(nsUriPerfilConcern);
					
					Node nodeSchemaLocation = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes().getNamedItem("xsi:schemaLocation");
					String concernLocaltionSchema = nsUriPerfilConcern + " " + "perfilConcerns.profile.uml#"+ getRefId(docConcern, "contents", "xmi:id");
					String samrtyLocaltionSchema = nsUriPerfilSmarty + " " + "smarty.profile.uml#"+ getRefId(docProfile, "contents", "xmi:id");
					
					nodeSchemaLocation.setNodeValue(concernLocaltionSchema +" " + samrtyLocaltionSchema);
					
					
				}
			});
			
			
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}	
			
	}


	private void updateHrefAtt(final String idApplied, final String profileName, final String tagName, final boolean updateReference) throws CustonTypeNotFound, NodeNotFound,	InvalidMultiplictyForAssociationException {
		mestrado.arquitetura.api.touml.Document.executeTransformation(this, new Transformation(){
			public void useTransformation() throws CustonTypeNotFound, NodeNotFound {
				Node node = null;
				if(updateReference)
					node = getAppliedHrefProfile(profileName, tagName);
				else
					node = getReference(profileName, tagName);
				Node nodeAttr = node.getAttributes().getNamedItem("href");
				String oldValueAttr = nodeAttr.getNodeValue().substring(0,nodeAttr.getNodeValue().indexOf("#"));
				nodeAttr.setNodeValue(oldValueAttr+"#"+idApplied);
			}
		});
	}


	private Node getAppliedHrefProfile(String profileName, String tagName) {
		NodeList elements = docUml.getElementsByTagName("profileApplication");
		for (int i = 0; i < elements.getLength(); i++) {
			NodeList childs = (elements.item(i).getChildNodes());
			for (int j = 0; j < childs.getLength(); j++)
				if(childs.item(j).getNodeName().equals(tagName) && (childs.item(j).getAttributes().getNamedItem("href").getNodeValue().contains(profileName)))
					return childs.item(j);
		}
		return null;
	}
	
		private Node getReference(String profileName, String tagName){
			NodeList elements = this.docUml.getElementsByTagName("profileApplication");
			for (int i = 0; i < elements.getLength(); i++) {
				NodeList childs = (elements.item(i).getChildNodes());
				for (int j = 0; j < childs.getLength(); j++) {
					if(childs.item(j).getNodeName().equalsIgnoreCase("eAnnotations")){
						for (int k = 0; k < childs.item(j).getChildNodes().getLength(); k++) {
							if(childs.item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("references")){
								NodeList eAnnotationsChilds = childs.item(j).getChildNodes();
								for (int l = 0; l < eAnnotationsChilds.getLength(); l++) {
									if(eAnnotationsChilds.item(l).getNodeName().equalsIgnoreCase("references")
											&& (eAnnotationsChilds.item(l).getAttributes().getNamedItem("href").getNodeValue().contains(profileName))){
										return eAnnotationsChilds.item(l);
									}
								}
							}
						}
						
					}
				}
			}
			return null;
		}
	

	private String getRefId(Document docProfile, String tagName, String attrName) {
		return docProfile.getElementsByTagName(tagName).item(0).getAttributes().getNamedItem(attrName).getNodeValue();
	}
	
}