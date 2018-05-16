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
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    private String csvPath;
    private RecordReader rr;
    private MultiDataSetIterator iterator;
    private MultiLayerConfiguration conf;
    private MultiLayerNetwork model;
    private RecordReader sampleReader;
    private List<Writable> record;
    private INDArray convert;
    private INDArray output;
    private File locationToSaveModel;
    private boolean saveUpdater = false;

    public NeuralNetwork(String courseName, int nEpochs, int numInputs, int numOutputs, int numHiddenNodes, String datasetPath) throws IOException, InterruptedException {
        this.courseName = courseName;
        this.locationToSaveModel = new File(courseName+".zip");
        this.nEpochs = nEpochs;
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.numHiddenNodes = numHiddenNodes;
        this.csvPath = datasetPath;

        //Initialize Record Reader
        this.rr = new CSVRecordReader(numLinesToSkip, filedeimeter);
        this.rr.initialize(new FileSplit(new File(this.csvPath)));

        //Initialize iterator
        this.iterator = new RecordReaderMultiDataSetIterator.Builder(this.batch_size)
                .addReader("NeuroGenReader", rr)
                .addInput("NeuroGenReader", 0, this.numInputs-1)
                .addOutput("NeuroGenReader", this.numInputs, this.numInputs+this.numOutputs-1)
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
                    .activation(Activation.SIGMOID)
                    .build())
                .layer(1, new DenseLayer.Builder()
                    .nIn(this.numHiddenNodes)
                    .nOut(this.numHiddenNodes)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.SIGMOID)
                    .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SIGMOID)
                        .nIn(this.numHiddenNodes)
                        .nOut(this.numOutputs)
                    .build())
                .pretrain(false).backprop(true).build();

        //Initialize Model
        if (this.locationToSaveModel.exists()){
            System.out.println("Loading Model...");
            loadModel();
        }
        else {
            System.out.println("Train Model...");
            trainModel();
            System.out.println("Save Moel...");
            saveModel();
        }
    }

    public void trainModel(){
        this.model = new MultiLayerNetwork(this.conf);
        this.model.init();
        this.model.setListeners(new ScoreIterationListener(10));

        for (int i=0; i<this.nEpochs; i++){
            model.fit(this.iterator);
        }
    }

    public void saveModel() throws IOException {
        ModelSerializer.writeModel(this.model, this.locationToSaveModel, this.saveUpdater);
    }

    public void loadModel() throws IOException {
        this.model = ModelSerializer.restoreMultiLayerNetwork(this.locationToSaveModel);
    }

    public INDArray getPrediction(String sample) throws IOException, InterruptedException {
        this.sampleReader = new CSVRecordReader(this.numLinesToSkip, this.filedeimeter);
        sampleReader.initialize(new FileSplit(new File(sample)));

        this.record = sampleReader.next();
        this.convert = RecordConverter.toArray(this.record);
        this.output = this.model.output(convert);

        return output;
    }
}

/*
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
        INDArray loadedOutput = loadedModel.output(convert);
        System.out.println("Prediction INDArray from loaded model: "+loadedOutput);
 */