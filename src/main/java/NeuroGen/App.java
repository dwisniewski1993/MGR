package NeuroGen;

import NeuroGen.GA.GeneticAlgorithm;
import NeuroGen.GA.SemanticHandler;
import org.datavec.api.records.Record;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ndarray.RecordConverter;
import org.datavec.api.writable.Writable;
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
import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * MADE BY Dominik Wiśniewski
 */
public class App 
{

    public static void main(String[] args ) throws IOException, InterruptedException {
        String semanticFilePAth = "semantic.xml";
        SemanticHandler ontologyT = new SemanticHandler(semanticFilePAth);//Temporary line
        Set<Integer> idList = ontologyT.getIDArrays();
/*
        for (int i=0; i<10; i++){
            for (int y=0;y<10;y++){
                if (i!=y){
                    System.out.println("I: "+i+", Y: "+y);
                    System.out.println(ontologyT.getDistanceToElemnt(i, y));
                }

            }
        }
*/
        //SillyNet
        int seed = 123;
        double learning_rate = 0.01;
        int batchSize = 50;
        int nEpochs = 30;
        int numInputs = 2;
        int numOutputs = 2;
        int numHiddenNodes = 20;
        int numLinesToSkip = 0;
        String fileDelimeter = ",";
        String csvPath = "dataset.csv";


        //Record Reader for training dataset
        RecordReader rr = new CSVRecordReader(numLinesToSkip, fileDelimeter);
        rr.initialize(new FileSplit(new File(csvPath)));

        MultiDataSetIterator iterator = new RecordReaderMultiDataSetIterator.Builder(batchSize)
                .addReader("myReader", rr)
                .addInput("myReader", 0, 9)
                .addOutput("myReader", 10, 19)
                .build();

        //Neural Network Architecture
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(1)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(learning_rate)
                .updater(Updater.NESTEROVS)
                .momentum(0.9)
                .list()
                .layer(0, new DenseLayer.Builder()
                    .nIn(10)
                    .nOut(numHiddenNodes)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.SIGMOID)
                    .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .weightInit(WeightInit.XAVIER)
                        .activation(Activation.IDENTITY)
                        .weightInit(WeightInit.XAVIER)
                        .nIn(numHiddenNodes)
                        .nOut(10)
                        .build()
                ).pretrain(false).backprop(true).build();

        //Training Model
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));

        for (int i = 0; i< nEpochs; i++){
            model.fit(iterator);
        }

        RecordReader sampleReader = new CSVRecordReader(numLinesToSkip, fileDelimeter);
        sampleReader.initialize(new FileSplit(new File("samples.csv")));
        List<Writable> record = sampleReader.next();
        INDArray convert = RecordConverter.toArray(record);
        int[] predicted = model.predict(convert);

        System.out.println(predicted[0]);


        System.out.println("Make NeuroGen:GA");
        //GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 15, 0.7, 0.2, 100, idList, semanticFilePAth);
        //System.out.println(genAlg.getStats());
        //System.out.println(genAlg.getBest());
    }
}
