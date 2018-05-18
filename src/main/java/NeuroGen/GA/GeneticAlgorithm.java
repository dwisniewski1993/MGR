package NeuroGen.GA;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

/**
 * MGR project Genetic Algorithm part of NeuroGen Engine
 * MADE BY Dominik Wiśniewski
 */

public class GeneticAlgorithm {
    private int populationSize;
    private int maxPhenotypeAge;
    private double crossoverProp;
    private double mutationProp;
    private Set<Integer> idList;
    private int numberOfGenerations;
    private ISeq<Integer> allele;
    private Factory<Genotype<EnumGene<Integer>>> gtf;
    private Engine<EnumGene<Integer>, Integer> engine;
    private EvolutionStatistics<Integer, ?> stats;
    private Phenotype<EnumGene<Integer>, Integer> best;
    private static HandleSemantic ontology;
    private static String pathfile;

    public GeneticAlgorithm(int popSize, int maxPhenAge, double crossProp, double mutProp, int numOfGen, Set<Integer> idList, String filename) throws IOException, SAXException, ParserConfigurationException {
        pathfile = filename;
        ontology = new HandleSemantic(filename);
        populationSize = popSize;
        maxPhenotypeAge = maxPhenAge;
        crossoverProp = crossProp;
        mutationProp = mutProp;
        numberOfGenerations = numOfGen;
        this.idList = idList;
        allele = ISeq.of(idList);
        gtf = Genotype.of(PermutationChromosome.of(allele));
        engine = Engine.builder(GeneticAlgorithm::FF, gtf)
                .populationSize(populationSize)
                .maximalPhenotypeAge(maxPhenotypeAge)
                .survivorsSelector(new RouletteWheelSelector<>())
                .offspringSelector(new TournamentSelector<>())
                .alterers(new PartiallyMatchedCrossover<>(crossoverProp), new SwapMutator<>(mutationProp))
                .build();
        stats = EvolutionStatistics.ofNumber();
        best = engine.stream().limit(numberOfGenerations).peek(stats).collect(toBestPhenotype());

    }

    public Phenotype<EnumGene<Integer>, Integer> getBest(){
        return best;
    }

    public EvolutionStatistics<Integer, ?> getStats() {
        return stats;
    }

    //Fitness Function
    public static int FF(final Genotype<EnumGene<Integer>> gt){
        //Fitness=suma(waga * odległość od(i do i-1) + (1-waga) * poziom trudności

        int fitInt;
        double fitness = 0;
        double wage = 0.6;

        for (int i = 1; i <= gt.getChromosome().length(); i++){
            fitness = fitness + ((wage*ontology.getDistanceToElement(i, i-1))
                    + ((1-wage)*ontology.getUnitDifficultyLevel(i)));
        }

        fitInt = (int)fitness;
        return fitInt;
    }

}