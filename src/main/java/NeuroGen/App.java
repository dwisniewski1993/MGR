package NeuroGen;

import NeuroGen.GA.SemanticHandler;
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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
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
        int nEpochs = 100;
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
                .updater(Updater.ADAM)
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
                        .activation(Activation.SIGMOID)
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


        //Take data and predict score
        RecordReader sampleReader = new CSVRecordReader(numLinesToSkip, fileDelimeter);
        sampleReader.initialize(new FileSplit(new File("sample.csv")));

        List<Writable> record = sampleReader.next();
        System.out.println("Record list: "+record);
        INDArray convert = RecordConverter.toArray(record);
        System.out.println("Convert Array: "+convert);
        INDArray output = model.output(convert);
        System.out.println("Predicted INDArray: "+output);

        //Save Trained Model
        File locationToSave = new File("trained_model.zip");
        boolean saveUpdater = false;
        ModelSerializer.writeModel(model, locationToSave, saveUpdater);

        //Load model
        MultiLayerNetwork loadedModel = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        //Use loaded model
        INDArray loadedOutput = model.output(convert);
        System.out.println("Prediction INDArray from loaded model: "+loadedOutput);

        System.out.println("Make NeuroGen:GA");
        //GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 15, 0.7, 0.2, 100, idList, semanticFilePAth);
        //System.out.println(genAlg.getStats());
        //System.out.println(genAlg.getBest());
    }
}
