/**
 * XML comparator. Compares two subtitle files, writes end timecodes from en file two appropriate
 * ua file where start timecodes match
 * 
 * @author Ilya Semenov
 * @version 1.0
 * XML subtitle files comparator/end timecodes writer
 */

import edu.duke.FileResource;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.HashMap;

public class XML_compare {
    private static String addon = " style=\"style.default\" region=\"region.after.center\"";
    
    public static void main(String[] args) {
        XML_compare conv = new XML_compare();
        conv.compareFiles();
    }
    
    public void compareFiles() {
        FileResource inputEn = new FileResource();
        FileResource inputUa = new FileResource();
        String outputFileName = "output.xml";
        HashMap<String, String> timecodesMap = new HashMap<String, String>();
        System.out.println("First  file: EN timecodes");
        System.out.println("Second file: UA timecodes");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream(outputFileName), "utf-8"))) {
                      System.out.println("Reading EN timecodes");
                      for (String str : inputEn.lines()) {
                          int startPos = str.indexOf("<p begin");
                          if (startPos > 0) {
                              String beginCode = str.substring(startPos + 10, startPos + 21);
                              int endPos = str.indexOf(" end=");
                              if (endPos > 0) {
                                  String endCode = str.substring(endPos + 6, endPos + 17);
                                  timecodesMap.put(beginCode, endCode);
                                  //System.out.println(beginCode + " - " + endCode);
                              }
                          }
                      }
                      System.out.println("Timecodes read done. Total EN timecodes read: " + timecodesMap.size());
                      System.out.println("Correcting UA file");
                      for (String str : inputUa.lines()) {
                         int startPos = str.indexOf("<p begin=");
                         String toWrite = str;
                         if (startPos > 0) {
                              String beginCode = str.substring(startPos + 10, startPos + 21);
                              int endPos = str.indexOf(" end=");
                              if (endPos > 0) {
                                  String endCode = str.substring(endPos + 6, endPos + 17);
                                  if (timecodesMap.containsKey(beginCode) && !endCode.equals(timecodesMap.get(beginCode))) {
                                      System.out.println("Replacing: " + endCode + " with " + timecodesMap.get(beginCode));
                                      toWrite = str.substring(0, endPos + 6) + timecodesMap.get(beginCode) + str.substring(endPos + 17);
//                                      System.out.println("New string: " + toWrite);
                                  }
                                  //System.out.println(beginCode + " - " + endCode);
                              }
                         }
                         writer.write(toWrite + System.getProperty("line.separator"));
                      }
                  System.out.println("Done. File created: " + outputFileName);
        }
        catch (Exception e) {
            System.out.println("Writing to file failed");
        }
    }
}