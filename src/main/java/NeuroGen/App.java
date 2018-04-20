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

        for (int i=1; i<10; i++){
            for (int y=1;y<10;y++){
                if (i!=y){
                    System.out.println("I: "+i+", Y: "+y);
                    System.out.println(ontologyT.getDistanceToElemnt(i, y));
                }

            }
        }

        //SillyNet
        int seed = 123;
        double learning_rate = 0.01;
        int batchSize = 50;
        int nEpochs = 30;
        int numInputs = 2;
        int numOutputs = 2;
        int numHiddenNodes = 20;

        System.out.println("Make NeuroGen:GA");
        //GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 15, 0.7, 0.2, 100, idList, semanticFilePAth);
        //System.out.println(genAlg.getStats());
        //System.out.println(genAlg.getBest());
    }
}
