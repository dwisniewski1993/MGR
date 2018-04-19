package NeuroGen;

import NeuroGen.GA.GeneticAlgorithm;
import NeuroGen.GA.SemanticHandler;
import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;

import java.util.Random;
import java.util.Set;

/**
 *
 * MADE BY Dominik Wiśniewski
 */
public class App 
{
    public static void main( String[] args )
    {
        String semanticFilePAth = "semantic.xml";
        SemanticHandler ontologyT = new SemanticHandler(semanticFilePAth);//Temporary line
        Set<Integer> idList = ontologyT.getIDArrays();

        System.out.println("Make NeuroGen:GA");
        GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 15, 0.7, 0.2, 100, idList, semanticFilePAth);
        System.out.println(genAlg.getBest());
        System.out.println(genAlg.getStats());
    }
}
