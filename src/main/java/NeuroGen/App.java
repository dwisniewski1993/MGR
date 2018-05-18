package NeuroGen;

import NeuroGen.GA.GeneticAlgorithm;
import NeuroGen.GA.HandleSemantic;
import NeuroGen.NN.NeuralNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
        int nHiddenNodes = 100;
        String dataset = "dataset.csv";
        String samplePath = "sample.csv";

        //Genetic Alg Data
        String semanticFilePAth = "semantic.xml";
        HandleSemantic ont = new HandleSemantic(semanticFilePAth);

        //NeuralNet
        System.out.println("Make NeuroGen: NN");
        NeuralNetwork nn = new NeuralNetwork(courseName, numEpochs, nIn, nOut, nHiddenNodes, dataset);
        Set prediction = nn.getPrediction(samplePath);
        System.out.println("PREDICTION...."+prediction);

        System.out.println("Make NeuroGen:GA");
        GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 20, 0.7, 0.2, 100, prediction, semanticFilePAth);
        System.out.println(genAlg.getStats());
        System.out.println(genAlg.getBest());
    }
}

