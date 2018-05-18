package NeuroGen;

import NeuroGen.GA.GeneticAlgorithm;
import NeuroGen.GA.HandleSemantic;
import NeuroGen.GA.SemanticHandler;
import NeuroGen.NN.NeuralNetwork;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ndarray.RecordConverter;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.datavec.RecordReaderMultiDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        //SemanticHandler ontologyT = new SemanticHandler(semanticFilePAth);//Temporary line
        //Set<Integer> idList = ontologyT.getIDArrays();

        HandleSemantic ont = new HandleSemantic(semanticFilePAth);
        Set<Integer> idList = ont.getIDArrays();
        System.out.println(ont.getLearningPathLenght());
        for (int i=1; i<=ont.getLearningPathLenght(); i++){
            System.out.println("Element "+i+" DIF LVL: "+ont.getUnitDifficultyLevel(i));
        }
        for (int q=1; q<=ont.getLearningPathLenght();q++){
            for (int w=1; w<=ont.getLearningPathLenght();w++){
                if (q!=w){
                    System.out.println("Q: "+q+", W: "+w);
                    System.out.println(ont.getDistanceToElement(q, w));
                }
            }
        }

        //NeuralNet
        //System.out.println("Make NeuroGen: NN");
        //NeuralNetwork nn = new NeuralNetwork(courseName, numEpochs, nIn, nOut, nHiddenNodes, dataset);

        //System.out.println("TOOK PREDICTION...."+nn.getPrediction(samplePath));

        /*
        for (int i=1; i<=10; i++){
            for (int y=1;y<=10;y++){
                if (i!=y){
                    System.out.println("I: "+i+", Y: "+y);
                    System.out.println(ontologyT.getDistanceToElemnt(i, y));
                }

            }
        }
*/


        System.out.println("Make NeuroGen:GA");
        //GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 15, 0.7, 0.2, 100, idList, semanticFilePAth);
        //System.out.println(genAlg.getStats());
        //System.out.println(genAlg.getBest());
    }
}
