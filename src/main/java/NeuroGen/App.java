package NeuroGen;

import NeuroGen.GA.GeneticAlgorithm;
import NeuroGen.NN.NeuralNetwork;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

/**
 *
 * MADE BY Dominik Wiśniewski
 */
public class App 
{

    public static void main(String[] args ) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        //NeuralNet Data
        String courseName = "tabliczka mnozenia";
        int numEpochs = 100;
        int nIn = 10;
        int nOut = 10;
        int nHiddenNodes = 256;
        String features = "features.csv";
        String labels = "labels.csv";
        String samplePath = "sample.csv";

        //GA Data
        int populationSize = 25;
        int maxPhenotypeAge = 10;
        double crossoverPropability = 0.9;
        double mutationPropability = 0.001;
        int numberOfGenerations = 50;

        //NeuralNet
        System.out.println("Make NeuroGen: NN");
        NeuralNetwork nn = new NeuralNetwork(courseName, numEpochs, nIn, nOut, nHiddenNodes, features, labels);
        Set<Integer> prediction = nn.getPrediction(samplePath);
        System.out.println("PREDICTION...."+prediction);

        //GA
        System.out.println("Make NeuroGen:GA");
        GeneticAlgorithm genAlg = new GeneticAlgorithm(courseName, populationSize, maxPhenotypeAge, crossoverPropability, mutationPropability, numberOfGenerations, prediction);
        System.out.println(genAlg.getStats());
        System.out.println(genAlg.getBest());
        System.out.println(genAlg.getBestList());
    }
}

