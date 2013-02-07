package mestrado.arquitetura.helpers;

import java.io.File;
import java.io.FilenameFilter;

public class OnlyCompleteResources implements FilenameFilter {

	public boolean accept(File dir, String name) {
		boolean resoucesComplet1= false;
		boolean resoucesComplet2 = false;
		
		File[] listOfFiles = dir.listFiles(); 
		for(int i=0; i < listOfFiles.length; i++){
			String nameFile = listOfFiles[i].getName();
			if (nameFile.equalsIgnoreCase(name+".notation"))
				resoucesComplet1 = true;
			if (nameFile.equalsIgnoreCase(name+".di"))
				resoucesComplet2 = true;
		}
		if (resoucesComplet1 && resoucesComplet2)
			return true;
		return false;
	}

}
