package arquitetura.io;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ho.yaml.Yaml;


/**
 * Classe responsável por acesso ao arquivo de configuração <b>application.yaml</b>/
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class ReaderConfig {
	
	static Logger LOGGER = LogManager.getLogger(ReaderConfig.class.getName());
	private final static String PATH_CONFIGURATION_FILE = "config/application.yaml";
	private static DirTarget dir;
	
	static{
		try {
			dir = Yaml.loadType(new File(PATH_CONFIGURATION_FILE), DirTarget.class);
		} catch (FileNotFoundException e) {
			LOGGER.info("I can't read the configuration file at: " + PATH_CONFIGURATION_FILE);
		}
	}
	
	/**
	 * Diretorio onde a arquitetura sera salva para manipulacao
	 * @return
	 */
	public static String getDirTarget() {
		return dir.getDirectoryToSaveModels();
	}
	
	/**
	 * Diretório onde a arquitetura será exportada para que possa ser utilizada
	 * 
	 * @return
	 */
	public static String getDirExportTarget() {
		return dir.getDirectoryToExportModels();
	}
	
	public static String getPathToProfileSMarty(){
		return dir.getPathToProfile();
	}

	public static String getPathToProfileConcerns() {
		return dir.getPathToProfileConcern();
	}


	/**
	 * Verifica se existe ou não perfis setados no arquivo de configuração.
	 * 
	 * @return boolean
	 */
	public static boolean hasProfilesSeted() {
		if((getPathToProfileConcerns() == null) && (getPathToProfileSMarty() == null))
			return false;
		return true;
	}
	
}