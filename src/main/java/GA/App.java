package GA;

import java.util.Set;

/**
 * MGR project Genetic Algorithm
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Genetic Algorithm!" );
        System.out.println("**************************************");
        System.out.println("Semantic Handl");
        SemanticHandler ontology = new SemanticHandler();
        ontology.init();
        System.out.println("Root Element: "+ontology.getRootElement());
        System.out.println("Number Of Units: "+ontology.getNumberOfUnits());
        int number = 7; // main Unit ID
        int numer2 = 9; // pursuit Unit ID
        System.out.println("Unit ID: "+number+"Difficulty level: "+ontology.getUnitDifLvl(number));
        System.out.println("Distance from: "+number+" to "+numer2+" = "+ontology.getDistanceToElemnt(number, numer2));
        Set<Integer> idList = ontology.getIDArrays();

        for (int x: idList){System.out.println(x);}
    }
}
