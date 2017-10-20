/**
* Write a description of XML_converter here.
* 
 * @author Ilya Semenov 
 * @version 1.0
* Capital letters converter after </p> and </br> tags for Netflix subtitles format files
*/

import edu.duke.FileResource;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class CapitalLettersConverter {
    private static String addon = " style=\"style.default\" region=\"region.after.center\"";
    
    public static void main(String[] args) {
        CapitalLettersConverter conv = new CapitalLettersConverter();
        conv.convertFile();
    }
    
    public void convertFile() {
        //FileResource inputDiv = new FileResource();
        FileResource input = new FileResource();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("g:/output.dfxp"), "utf-8"))) {
                        System.out.println("Writing header");
                        for (String str : input.lines()) {
                            writer.write(str + System.getProperty("line.separator"));
                            if (str.indexOf("<div>") > 0)
                                break;
                        }
                        System.out.println("Writing body");
                        boolean flagLowerCase = false;
                        for (String str : input.lines()) {
                          int indexBr = str.indexOf("<br/>");
                          if (indexBr > 0) {
                              //System.out.println("String found:" + str);
                              //System.out.println("Symbol to lowercase:" + str.charAt(indexBr + 6) + ", Index = " + (indexBr + 6));
                              if (!(str.charAt(indexBr - 1) == '.' || str.charAt(indexBr - 1) == '!' || str.charAt(indexBr - 1) == '?')) {
                                  StringBuilder newStr = new StringBuilder(str);
                                  char chr = Character.toLowerCase(newStr.charAt(indexBr + 5));
                                  newStr.setCharAt(indexBr + 5, chr);
                                  str = newStr.toString();
                                }
                            }
                          if (flagLowerCase) {
                              int indexFirstChar = str.indexOf("\">");
                              if (indexFirstChar > 0) {
                                  StringBuilder newStr = new StringBuilder(str);
                                  char chr = Character.toLowerCase(newStr.charAt(indexFirstChar + 2));
                                  newStr.setCharAt(indexFirstChar + 2, chr);
                                  str = newStr.toString();
                                }
                            }
                          int lastIndex = str.lastIndexOf("</span></p>");
                          if (lastIndex < 0) {
                              lastIndex = str.lastIndexOf("</p>");
                            }
                          if (lastIndex > 0) {
                              char lastChar = str.charAt(lastIndex - 1);
                              //System.out.println(str);
                              //System.out.println("Last char:" + lastChar);
                              flagLowerCase = !(lastChar == '.' || lastChar == '?' || lastChar == '!');
                            }
                          //int index = str.indexOf("</p>");
                          String toWrite = str;
                          writer.write(toWrite + System.getProperty("line.separator"));
                        }
                  System.out.println("Done");
                  }
        catch (IOException e) {
            System.out.println("Writing to file failed");
        }
    }
}