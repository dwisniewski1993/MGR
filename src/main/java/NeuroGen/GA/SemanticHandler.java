package NeuroGen.GA;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * MGR project Semantic Handler for GA part of NeuroGen Engine
 * MADE BY Dominik Wiśniewski
 */
public class SemanticHandler {
    public String pathfile; //"semantic.xml"
    public File inputfile;
    public DocumentBuilderFactory dbFactory;
    public DocumentBuilder dBuilder;
    public Document doc;

    public SemanticHandler(String filepath){
        this.pathfile = filepath;
        this.inputfile = new File(filepath);
        this.dbFactory = DocumentBuilderFactory.newInstance();
        try {
            this.dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            doc = dBuilder.parse(inputfile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();
    }

    public String getRootElement(){
        return doc.getDocumentElement().getNodeName();
    }

    public int getNumberOfUnits(){
        NodeList unitList = doc.getElementsByTagName("UNIT");
        return unitList.getLength();
    }

    public int getUnitDifLvl(int elementID){
        NodeList unitList = doc.getElementsByTagName("UNIT");
        Node nUnit = unitList.item(elementID-1);
        if (nUnit.getNodeType()==Node.ELEMENT_NODE){
            Element eElement = (Element) nUnit;
            return Integer.parseInt(eElement.getElementsByTagName("DIFFICULTY_LEVEL").item(0).getTextContent());
        }
        else {return 0;}
    }

    public int getDistanceToElemnt(int elementID, int searchDistElemId){
        NodeList unitList = doc.getElementsByTagName("UNIT");
        Node nUnit = unitList.item(elementID-1);
        if (nUnit.getNodeType()==Node.ELEMENT_NODE){
            Element eElement = (Element) nUnit;
            Element distEl = (Element) eElement.getElementsByTagName("DISTANCE").item(0);
            int len = getNumberOfUnits()-1;
            int flag = 0;
            for (int j=0; j<len; j++){
                Element id = (Element) distEl.getElementsByTagName("FROM").item(j);
                if (searchDistElemId == Integer.parseInt(id.getAttribute("id"))){
                    flag = j;
                }
            }
            return Integer.parseInt(distEl.getElementsByTagName("FROM").item(flag).getTextContent());
        }
        else {return 0;}
    }

    public Set<Integer> getIDArrays(){
        ArrayList<Integer> idList = new ArrayList<Integer>();
        NodeList unitList = doc.getElementsByTagName("UNIT");
        int len = getNumberOfUnits();
        for (int g=0; g<len; g++){
            Node nUnit = unitList.item(g);
            if (nUnit.getNodeType()==Node.ELEMENT_NODE){
                Element eElement = (Element) nUnit;
                for (int i = 0; i < getNumberOfUnits(); i++){
                    idList.add(Integer.parseInt(eElement.getAttribute("id")));
                }
            }
        }
        Set<Integer> ids = new HashSet<Integer>(idList);
        return ids;
    }
}

/*
String semanticFilePAth = "semantic.xml";
        System.out.println("**************************************");
        System.out.println("Semantic Handl");
        SemanticHandler ontology = new SemanticHandler(semanticFilePAth);
        System.out.println("Root Element: "+ontology.getRootElement());
        System.out.println("Number Of Units: "+ontology.getNumberOfUnits());
        int number = 7; // main Unit ID
        int numer2 = 9; // pursuit Unit ID
        System.out.println("Unit ID: "+number+"Difficulty level: "+ontology.getUnitDifLvl(number));
        System.out.println("Distance from: "+number+" to "+numer2+" = "+ontology.getDistanceToElemnt(number, numer2));
        Set<Integer> idList = ontology.getIDArrays();
        for (int x: idList){System.out.println(x);}
 */
