// Stephen Ramotowski
// (9/1/24)
// Program takes a folder as input and parses xml, png pairs
// Then parses the xml files for leaf nodes
// Using the locations of leaf nodes, draws rectangles on a copy of the png file

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.DocumentBuilder; 
import org.w3c.dom.Document; 
import org.w3c.dom.NodeList; 
import org.w3c.dom.Node; 
import org.w3c.dom.Element; 
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class xmlHighlighter {

    public static void main(String[] args){
        
        //check if given an argument
        if(args.length == 0) {
            System.out.println("No Path Given");
            return;
        }

        //get path to directory
        String path = args[0];
        File directory = new File(path);
        //list of files
        File[] fileList = directory.listFiles();
        
        //check if folder is empty
        if(fileList == null) {
            System.out.println("No files in directory");
            return;
        }

        //arrayList that contains every xml file in the folder
        ArrayList<File> xmlFiles = new ArrayList<File>();
        //parse directory for xml files
        for(File file : fileList) {
            if (file.getName().endsWith(".xml")){
                xmlFiles.add(file);
            }
        }

        ArrayList<String> bounds = new ArrayList<String>();

        for(File xml : xmlFiles) {
            File png = pngPair(fileList, xml);
            if(png != null){
                //get the bounds
                bounds = xmlParser(xml);
                //draw rectangles
                drawRectangles(png, bounds);
            }
        }
        
        
        xmlParser(xmlFiles.get(1));

    }

    /*
     * xmlParser
     * takes input of an xml file
     * uses DOC to get every node of the xml
     * then iterates through the nodes to find leaf nodes
     * stores leaf node bounds in a arraylist string
     */
    private static ArrayList<String> xmlParser(File xmlFile){

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder(); 

            Document doc = db.parse(xmlFile); 

            doc.getDocumentElement().normalize();
            //get all nodes
            NodeList nodeList = doc.getElementsByTagName("node");

            //array list to hold all of the bounds for all leaf nodes of a xml file
            ArrayList<String> boundList = new ArrayList<String>();
            String bounds;

            //iterate throught every node
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                Element element = (Element)node;

                //check if node is a leaf node (no children)
                if(!node.hasChildNodes()){
                    //get the bounds of the node and add to list
                    bounds = element.getAttribute("bounds");
                    boundList.add(bounds);
                }
            
            }

            return boundList;

        }   catch (Exception e) { 
                System.out.println(e); 
                return null;
            } 
    }

    /*
     * pngPair
     * returns the png file associated with a given xml file
     * returns null if no png file is found
     */
    private static File pngPair(File[] fileList, File xml){
        String name = xml.getName();
            name = name.replace(".xml", ".png");
            //name = name.substring(0, name.length()-3);
            //name = name + ".png";
            for (File file : fileList) {
                if(file.getName().equals(name)){
                    return file;
                }
            }
            return null;
    }
    /*
     * drawRectangles
     * creates a BufferedImage to draw upon
     * uses the bounds from xmlParser to get the coordinates for rectangles
     * draws yellow rectangles
     * creates output file in directory of the program file
     */
    private static void drawRectangles(File png, ArrayList<String> bounds){

        String name = png.getName().substring(0, png.getName().length()-3);

        try{
            BufferedImage originalPng = ImageIO.read(png);
            //create blank canvas same size as og png
            BufferedImage canvas = new BufferedImage(originalPng.getWidth(), originalPng.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics = canvas.createGraphics();
            //draw og png
            graphics.drawImage(originalPng, 0, 0, null);
        
            graphics.setColor(Color.YELLOW);

            Stroke dashed = new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            graphics.setStroke(dashed);
            //graphics.setStroke(new BasicStroke(10));
    
            int x;
            int y;
            int x2;
            int y2;
            String temp;
            int index;
            int index2;
            int width;
            int height;

            //coords are in format [x,y][x,y]
            for(String curBounds : bounds){
                //System.out.println(curBounds);
                temp = curBounds.replace("[","");
                index = temp.indexOf(",");

                x = Integer.parseInt(temp.substring(0, index));

                index2 = temp.indexOf("]");

                y = Integer.parseInt(temp.substring(index+1, index2));

                temp = temp.substring(index2, temp.length()-1);
                index = temp.indexOf(",");

                x2 = Integer.parseInt(temp.substring(1, index));

                y2 = Integer.parseInt(temp.substring(index+1, temp.length()));

                width = x2 - x;
                height = y2 - y;

                graphics.drawRect( x, y, width, height);
            }

            File output = new File(name + "highlighted.png");
            ImageIO.write(canvas, "png", output);

        } catch (IOException e) { 
            System.out.println("Error: " + e); 
        } 

    }
}
