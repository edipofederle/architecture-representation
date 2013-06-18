package arquitetura.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class CopyFile {
	
	static Logger LOGGER = LogManager.getLogger(CopyFile.class.getName());
	
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile) {
		
		LOGGER.info("copyFile(File sourceFile, File destFile) - Enter");
		LOGGER.info("SourceFile: "+sourceFile);
		LOGGER.info("DestFile: "+destFile);
		
		if (!sourceFile.exists()) return;
		if (!destFile.exists())
			try {
				destFile.createNewFile();
				FileChannel source = new FileInputStream(sourceFile).getChannel();
				FileChannel destination = new FileOutputStream(destFile).getChannel();
				
				if (destination != null && source != null) 	destination.transferFrom(source, 0, source.size());
				if (source != null) source.close();
				if (destination != null) destination.close();
			} catch (IOException e) {
				LOGGER.error("ERROR: " + e.getMessage());
			} 
		
		LOGGER.info("copyFile(File sourceFile, File destFile) - Exit");

	}

}