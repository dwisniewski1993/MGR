package NeuroGen.GA;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;

import java.util.Random;
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
    private static SemanticHandler ontology;
    private static String pathfile;

    public GeneticAlgorithm(int popSize, int maxPhenAge, double crossProp, double mutProp, int numOfGen, Set<Integer> idList, String filename){
        populationSize = popSize;
        maxPhenotypeAge = maxPhenAge;
        crossoverProp = crossProp;
        mutationProp = mutProp;
        numberOfGenerations = numOfGen;
        this.idList = idList;
        allele = ISeq.of(idList);
        ontology = new SemanticHandler(filename);
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
        pathfile = filename;
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

        for (int i = 1; i < gt.getChromosome().length(); i++){
            fitness = fitness + ((wage*ontology.getDistanceToElemnt(i, i-1))
                    + ((1-wage)*ontology.getUnitDifLvl(i)));
        }

        fitInt = (int)fitness;
        return fitInt;
    }

}

/*
//NeuroGen.GA Init
        ISeq<Integer> allele = ISeq.of(idList);
        Factory<Genotype<EnumGene<Integer>>> gtf = Genotype.of(PermutationChromosome.of(allele));

        //Genetic Algorithm
        final Engine<EnumGene<Integer>, Integer> engine = Engine.builder(App::FF, gtf)
                .populationSize(5) //Population Size
                .maximalPhenotypeAge(10) // Phenotyope Age
                .survivorsSelector(new RouletteWheelSelector<>()) //Survive Selector
                .offspringSelector(new TournamentSelector<>()) // Offspring selector
                .alterers(new PartiallyMatchedCrossover<>(0.6), new SwapMutator<>(0.2)) //Crossover & mutation rate
                .build();

        //Evolution statistics consumer
        final EvolutionStatistics<Integer, ?> stats = EvolutionStatistics.ofNumber();

        final Phenotype<EnumGene<Integer>, Integer> best = engine
                .stream()
                .limit(20)
                .peek(stats)
                .collect(toBestPhenotype());

        System.out.println(stats); // NeuroGen.GA stats
        System.out.println(best); // Best Phenotype
 */