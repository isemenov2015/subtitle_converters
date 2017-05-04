/**
 * Write a description of XML_converter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 * Converter from XML to Netflix subtitles format
 */

import edu.duke.FileResource;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class XML_converter {
    private static String addon = " style=\"style.default\" region=\"region.after.center\"";
    
    public static void main(String[] args) {
        XML_converter conv = new XML_converter();
        conv.convertFile();
    }
    
    public void convertFile() {
        FileResource inputDiv = new FileResource();
        FileResource input = new FileResource();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("output.dfxp"), "utf-8"))) {
                        System.out.println("Writing header");
                        for (String str : inputDiv.lines()) {
                            writer.write(str + System.getProperty("line.separator"));
                            if (str.indexOf("<div>") > 0)
                                break;
                        }
                        boolean startProcessing = false;
                        System.out.println("Writing body");
                        for (String str : input.lines()) {
                          if (startProcessing) {
                              int index = str.indexOf("<p");
                              String toWrite = str;
                              if (index > 0) {
                                  System.out.println(str);
                                  index += 2;
                                  toWrite = str.substring(0, index);
                                  toWrite += addon;
                                  str = str.substring(index);
                                  index = str.indexOf(" region");
                                  if (index > 0) {
                                      toWrite += str.substring(0, index);
                                      index = str.indexOf(">");
                                      toWrite += str.substring(index);
                                    }
                                  System.out.println(toWrite);
                                }
                                writer.write(toWrite + System.getProperty("line.separator"));
                            }
                            if (!startProcessing && (str.indexOf("<div>") > 0)) {
                              startProcessing = true;
                            }
                        }
                  System.out.println("Done");
        }
        catch (Exception e) {
            System.out.println("Writing to file failed");
        }
    }
}
