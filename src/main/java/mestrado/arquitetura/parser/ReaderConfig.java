package mestrado.arquitetura.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.ho.yaml.Yaml;

import config.DirTarget;

public class ReaderConfig {
	
	private final static Logger LOGGER = Logger.getLogger(ReaderConfig.class.getName()); 
	private final static String PATH_CONFIGURATION_FILE = "config/application.yaml";
	private static DirTarget dir;
	
	static{
		try {
			
			dir = Yaml.loadType(new File(PATH_CONFIGURATION_FILE), DirTarget.class);
		} catch (FileNotFoundException e) {
			LOGGER.info("I can't read the configuration file at " + PATH_CONFIGURATION_FILE);
		}
	}

	public static String getDirTarget() {
		return dir.getDirectoryToSaveModels();
	}
	
	public static String getDirExportTarget() {
		return dir.getDirectoryToExportModels();
	}
	
}