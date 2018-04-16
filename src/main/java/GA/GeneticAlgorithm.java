package GA;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;

import java.util.Random;
import java.util.Set;

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

    public GeneticAlgorithm(int popSize, int maxPhenAge, double crossProp, double mutProp, int numOfGen, Set<Integer> idList){
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
        Random random = new Random();
        int maximum = 15;
        int minimum = 1;
        int n = maximum - minimum + 1;
        int i = random.nextInt() % n;
        int randomnum = minimum + i;
        return randomnum;
    }

}
