package mestrado.arquitetura.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	public static void moveFiles(String to, String from){
	 	InputStream inStream = null;
		OutputStream outStream = null;
 
    	try{
 
    	    File afile =new File(to);
    	    File bfile =new File(from);
 
    	    inStream = new FileInputStream(afile);
    	    outStream = new FileOutputStream(bfile);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = inStream.read(buffer)) > 0){
 
    	    	outStream.write(buffer, 0, length);
 
    	    }
 
    	    inStream.close();
    	    outStream.flush();
    	    outStream.close();
 
    	    //delete the original file
    	    afile.delete();
 
    	    System.out.println("File is copied successful!");
 
    	}catch(IOException e){
    	    e.printStackTrace();
    	}
    }
}