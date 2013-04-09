package mestrado.arquitetura.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileModificator {

    public static void normalize(String fileToNormalize, String contentToChange, String changeTo){

        File f=new File(fileToNormalize);

        FileInputStream fs = null;
        InputStreamReader in = null;
        BufferedReader br = null;

        StringBuilder sb = new StringBuilder();

        String textinLine;

        try {
             fs = new FileInputStream(f);
             in = new InputStreamReader(fs);
             br = new BufferedReader(in);

            while(true)
            {
                textinLine=br.readLine();
                if(textinLine==null)
                    break;
                sb.append(textinLine);
            }
              try{
            	  replaceAll(sb, contentToChange, changeTo);
              }catch(Exception e){}
              fs.close();
              in.close();
              br.close();

            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }

            try{
                FileWriter fstream = new FileWriter(f);
                BufferedWriter outobj = new BufferedWriter(fstream);
                outobj.write(sb.toString());
                outobj.close();

            }catch (Exception e){
              System.err.println("Error: " + e.getMessage());
            }
    }
    
    private static void replaceAll(StringBuilder builder, String from, String to)
    {
        int index = builder.indexOf(from);
        while (index != -1)
        {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }
}