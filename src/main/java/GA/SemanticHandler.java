package GA;

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

public class SemanticHandler {
    public File inputfile = new File("semantic.xml");
    public DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    public DocumentBuilder dBuilder;

    {
        try{
            dBuilder = dbFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e){
            e.printStackTrace();
        }
    }
    public Document doc;

    {
        try{
            doc = dBuilder.parse(inputfile);
        }
        catch (SAXException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void init(){
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
