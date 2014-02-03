package arquitetura.touml;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import arquitetura.exceptions.CustonTypeNotFound;
import arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.NodeNotFound;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.helpers.XmiHelper;
import arquitetura.io.CopyFile;
import arquitetura.io.ReaderConfig;
import arquitetura.io.SaveAndMove;

import com.google.common.io.Files;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class DocumentManager extends XmiHelper {
	
	private org.w3c.dom.Document docUml;
	private org.w3c.dom.Document docNotation;
	private org.w3c.dom.Document docDi;
	private final String BASE_DOCUMENT = "simples";
	private String outputModelName;
	
	static Logger LOGGER = LogManager.getLogger(DocumentManager.class.getName());
	
	public DocumentManager(String outputModelName) throws ModelNotFoundException, ModelIncompleteException {
		this.outputModelName = outputModelName;
		makeACopy(BASE_DOCUMENT);
		createXMIDocument();
		
		if(onlyUpdateProfilesAndCopyIfExists()){
			updateProfilesRefs();
			copyProfilesToDestination();
		}
		
		this.saveAndCopy(outputModelName);
	}

	private boolean onlyUpdateProfilesAndCopyIfExists() {
		return ReaderConfig.hasProfilesSeted();
	}

	private void copyProfilesToDestination() {
		String pathSmarty = ReaderConfig.getPathToProfileSMarty();
		String pathConcern = ReaderConfig.getPathToProfileConcerns();
		String pathToProfileRelationships = ReaderConfig.getPathToProfileRelationships();
		
		createResourcesDirectoryIfNotExist();
		
		final File sourceFileSmarty = new File(pathSmarty);
		final File destFileSmarty = new File(ReaderConfig.getDirExportTarget()+"/resources/smarty.profile.uml");
		
		final File sourceFileConcern = new File(pathConcern);
		final File destFileConcern = new File(ReaderConfig.getDirExportTarget()+"/resources/perfilConcerns.profile.uml");
		
		try {
			Files.copy(sourceFileSmarty, destFileSmarty);
			Files.copy(sourceFileConcern, destFileConcern);
		} catch (IOException e) {
			LOGGER.warn("I cannot copy resources to destination. " + e.getMessage() );
			e.printStackTrace();
		}
		
	}

	private void createResourcesDirectoryIfNotExist() {
		File resourcesDir = new File(ReaderConfig.getDirExportTarget()+"/resources/");
		if(!resourcesDir.exists())
			resourcesDir.mkdir();
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
			this.docNotation =  docBuilderNotation.parse(ReaderConfig.getDirTarget()+BASE_DOCUMENT+".notation");
			this.docUml = docBuilderUml.parse(ReaderConfig.getDirTarget()+BASE_DOCUMENT+".uml");
			this.docDi = docBuilderUml.parse(ReaderConfig.getDirTarget()+BASE_DOCUMENT+".di");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Realiza um cópia dos três arquivos para o diretório <b>manipulation</b>.
	 * 
	 * Esse diretório deve ser setado no arquivo de configuração <b>application.yml</b>
	 * na propriedade "directoryToSaveModels".
	 * 
	 * @param pathToFiles
	 * @param modelName
	 * @throws ModelIncompleteException 
	 * @throws ModelNotFoundException 
	 * @throws SMartyProfileNotAppliedToModelExcepetion 
	 * @throws IOException
	 */
	private void makeACopy(String modelName) throws ModelNotFoundException, ModelIncompleteException {
		
		LOGGER.info("makeACopy(String modelName) - Enter");
		
		
		String notationCopy = ReaderConfig.getDirTarget()+BASE_DOCUMENT+".notation";
		String umlCopy = ReaderConfig.getDirTarget()+BASE_DOCUMENT+".uml";
		String diCopy = ReaderConfig.getDirTarget()+BASE_DOCUMENT+".di";
		
		URL n = null;
		URL u = null;
		URL d = null;
		  try {
		     URL baseUrl = new URL("file:"+ReaderConfig.getPathToTemplateModelsDirectory());
		     if (baseUrl != null) {
		    	//Arquivos vazios usados para geração da nova arquitetura
		        n = new URL(baseUrl, modelName+".notation");
		        u = new URL(baseUrl, modelName+".uml");
		        d = new URL(baseUrl, modelName+".di");
		     }
		  } catch (MalformedURLException e) {
		     LOGGER.error("makeACopy(String modelName) - Could not find template files directory: " + ReaderConfig.getPathToTemplateModelsDirectory());
		  }
		
		CopyFile.copyFile(new File(n.getPath()), new File(notationCopy));
		CopyFile.copyFile(new File(u.getPath()),  new File(umlCopy));
		CopyFile.copyFile(new File(d.getPath()),  new File(diCopy));
						
		LOGGER.info("makeACopy(String modelName) - Exit");
		
	}

//	private void copyFileToDest(String notationCopy, InputStream n1) {
//		if (n1 == null) {
//		     //send your exception or warning
//		 }
//		 OutputStream resStreamOut;
//		 int readBytes;
//		 byte[] buffer = new byte[4096];
//		 try {
//		     resStreamOut = new FileOutputStream(new File(notationCopy));
//		     while ((readBytes = n1.read(buffer)) > 0) {
//		         resStreamOut.write(buffer, 0, readBytes);
//		     }
//		 } catch (IOException e1) {
//		     e1.printStackTrace();
//		 }
//	}

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
			SaveAndMove.saveAndMove(docNotation, docUml, docDi, BASE_DOCUMENT, newModelName);
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

	
	/**
	 * Esse método é responsável por atualizar as referencias aos profiles (definidos no arquivo application.yml)
	 * que são usados no modelo.
	 * 
	 * Basicamente é lido dois valores de cada arquivo de profile e atualizado no arquivo simples.uml do qual é
	 * usado como base para escrever o modelo novamente em disco.
	 * 
	 * @throws ModelNotFoundException
	 * @throws ModelIncompleteException
	 * @throws CustonTypeNotFound
	 * @throws NodeNotFound
	 * @throws InvalidMultiplictyForAssociationException
	 */
	public void updateProfilesRefs()  {
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
			
			updateHrefAtt(getIdOnNode(docProfile, "contents", "xmi:id"), "smarty", "appliedProfile", false);
			updateHrefAtt(getIdOnNode(docConcern, "contents", "xmi:id"), "Concerns", "appliedProfile", false);
			
			updateHrefAtt(getIdOnNode(docProfile, "uml:Profile", "xmi:id"), "smarty", "appliedProfile", true);
			updateHrefAtt(getIdOnNode(docConcern, "uml:Profile", "xmi:id"), "Concerns", "appliedProfile", true);
			
			
			//Recuperar os valores de nsURI para os profiles.
			final String nsUriPerfilSmarty = getIdOnNode(docProfile, "contents", "nsURI");
			final String nsUriPerfilConcern = getIdOnNode(docConcern, "contents", "nsURI");
			
			arquitetura.touml.Document.executeTransformation(this, new Transformation(){
				public void useTransformation() {
					Node xmlsnsSmarty = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes().getNamedItem("xmlns:smartyProfile");
					xmlsnsSmarty.setNodeValue(nsUriPerfilSmarty);
					
					Node xmlsnsConcern = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes().getNamedItem("xmlns:perfilConcerns");
					xmlsnsConcern.setNodeValue(nsUriPerfilConcern);
					
					Node nodeSchemaLocation = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes().getNamedItem("xsi:schemaLocation");
					String concernLocaltionSchema = nsUriPerfilConcern + " " + "resources/perfilConcerns.profile.uml#"+ getIdOnNode(docConcern, "contents", "xmi:id");
					String samrtyLocaltionSchema = nsUriPerfilSmarty + " " + "resources/smarty.profile.uml#"+ getIdOnNode(docProfile, "contents", "xmi:id");
					
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


	private void updateHrefAtt(final String idApplied, final String profileName, final String tagName, final boolean updateReference) {
		arquitetura.touml.Document.executeTransformation(this, new Transformation(){
			public void useTransformation() {
				Node node = null;
				if(updateReference)
					node = getAppliedHrefProfile(profileName, tagName);
				else
					node = getReference(profileName, tagName);
				Node nodeAttr = node.getAttributes().getNamedItem("href");
				String oldValueAttr = nodeAttr.getNodeValue().substring(0,nodeAttr.getNodeValue().indexOf("#"));
				nodeAttr.setNodeValue("resources/"+oldValueAttr+"#"+idApplied);
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
								for (int l = 0; l < eAnnotationsChilds.getLength(); l++) 
									if(isProfileNode(profileName, eAnnotationsChilds, l))
										return eAnnotationsChilds.item(l);
							}
						}
					}
						
				}
			}
			
			return null;
		}


	private boolean isProfileNode(String profileName, NodeList eAnnotationsChilds, int l) {
		return (eAnnotationsChilds.item(l).getNodeName().equalsIgnoreCase("references") &&
			   (eAnnotationsChilds.item(l).getAttributes().getNamedItem("href").getNodeValue().contains(profileName)));
	}
	
	/**
	 * 
	 * @param document - O documento em que se quer pesquisar.
	 * @param tagName - elemento desejado
	 * @param attrName - atributo do elemento desejado
	 * @return
	 */
	private String getIdOnNode(Document document, String tagName, String attrName) {
		return document.getElementsByTagName(tagName).item(0).getAttributes().getNamedItem(attrName).getNodeValue();
	}
	
}