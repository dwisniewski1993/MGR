package NeuroGen.NN;

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
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.MultiNormalizerMinMaxScaler;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MGR project Neural Network part of NeuroGen Engine
 * MADE BY Dominik Wiśniewski
 */

public class NeuralNetwork {
    private String courseName;
    private int seed = 123;
    private double learning_rate = 0.001;
    private int batch_size = 50;
    private int nEpochs;
    private int numInputs;
    private int numOutputs;
    private int numHiddenNodes;
    private int numLinesToSkip = 0;
    private String filedeimeter = ",";
    private String featuresCSV;
    private String labelsCSV;
    private MultiDataSetIterator iterator;
    private MultiNormalizerMinMaxScaler normalizer;
    private MultiLayerConfiguration conf;
    private MultiLayerNetwork model;
    private RecordReader sampleReader;
    private RecordReader featuresReader;
    private RecordReader labelsReader;
    private List<Writable> record;
    private INDArray convert;
    private INDArray output;
    private File locationToSaveModel;
    private boolean saveUpdater = false;

    public NeuralNetwork(String courseName, int nEpochs, int numInputs, int numOutputs, int numHiddenNodes, String features, String labels) throws IOException, InterruptedException {
        this.courseName = courseName;
        this.locationToSaveModel = new File(courseName+".zip");
        this.nEpochs = nEpochs;
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.numHiddenNodes = numHiddenNodes;

        this.featuresCSV = features;
        this.labelsCSV = labels;

        this.featuresReader = new CSVRecordReader(numLinesToSkip, filedeimeter);
        this.featuresReader.initialize(new FileSplit(new File(this.featuresCSV)));

        this.labelsReader = new CSVRecordReader(numLinesToSkip, filedeimeter);
        this.labelsReader.initialize(new FileSplit(new File(this.labelsCSV)));

        //Initialize iterator
        this.iterator = new RecordReaderMultiDataSetIterator.Builder(this.batch_size)
                .addReader("csvInput", this.featuresReader)
                .addReader("csvOutput", this.labelsReader)
                .addInput("csvInput")
                .addOutput("csvOutput")
                .build();

        //Initialize Neural Network Confifuration
        this.conf = new NeuralNetConfiguration.Builder()
                .seed(this.seed)
                .iterations(1)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(this.learning_rate)
                .updater(Updater.NESTEROVS)
                .momentum(0.9)
                .list()
                .layer(0, new DenseLayer.Builder()
                    .nIn(this.numInputs)
                    .nOut(this.numHiddenNodes)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.TANH)
                    .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SIGMOID)
                        .nIn(this.numHiddenNodes)
                        .nOut(this.numOutputs)
                    .build())
                .pretrain(false).backprop(true).build();

        //Normalize Data
        this.normalizer = new MultiNormalizerMinMaxScaler();
        this.normalizer.fit(this.iterator);
        this.iterator.setPreProcessor(this.normalizer);

        //Initialize Model
        if (this.locationToSaveModel.exists()){
            System.out.println("Loading Model...");
            loadModel();
        }
        else {
            System.out.println("Train Model...");
            trainModel();
            System.out.println("Save Model...");
            saveModel();
        }
    }

    private void trainModel(){
        this.model = new MultiLayerNetwork(this.conf);
        this.model.init();
        this.model.setListeners(new ScoreIterationListener(10));

        for (int i=0; i<this.nEpochs; i++){
            model.fit(this.iterator);
        }
    }

    private void saveModel() throws IOException {
        ModelSerializer.writeModel(this.model, this.locationToSaveModel, this.saveUpdater);
    }

    private void loadModel() throws IOException {
        this.model = ModelSerializer.restoreMultiLayerNetwork(this.locationToSaveModel);
    }

    public Set<Integer> getPrediction(String sample) throws IOException, InterruptedException {
        this.sampleReader = new CSVRecordReader(this.numLinesToSkip, this.filedeimeter);
        sampleReader.initialize(new FileSplit(new File(sample)));

        ArrayList<Integer> idsList = new ArrayList<>();

        this.record = sampleReader.next();
        this.convert = RecordConverter.toArray(this.record);
        this.output = this.model.output(convert);

        for (int i=0;i<this.output.length();i++){
            if (this.output.getDouble(i)>0.5){
                idsList.add(i+1);
            }
        }
        Set<Integer> prediction = new HashSet<Integer>(idsList);

        return prediction;
    }
}