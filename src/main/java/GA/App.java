package GA;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;
import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;

import java.util.Random;
import java.util.Set;

/**
 * MGR project Genetic Algorithm
 * MADE BY Dominik Wiśniewski
 */
public class App 
{
    //Fitness Function
    private static int FF(final Genotype<EnumGene<Integer>> gt){
        Random rand = new Random();
        int maximum = 15;
        int minimum = 1;
        int n = maximum - minimum +1;
        int i = rand.nextInt() % n;
        int randomnum = minimum +i;
        return randomnum;
    }
    public static void main( String[] args )
    {
        System.out.println( "Genetic Algorithm!" );
        System.out.println("**************************************");
        System.out.println("Semantic Handl");
        SemanticHandler ontology = new SemanticHandler();
        ontology.init();
        System.out.println("Root Element: "+ontology.getRootElement());
        System.out.println("Number Of Units: "+ontology.getNumberOfUnits());
        int number = 7; // main Unit ID
        int numer2 = 9; // pursuit Unit ID
        System.out.println("Unit ID: "+number+"Difficulty level: "+ontology.getUnitDifLvl(number));
        System.out.println("Distance from: "+number+" to "+numer2+" = "+ontology.getDistanceToElemnt(number, numer2));
        Set<Integer> idList = ontology.getIDArrays();

        for (int x: idList){System.out.println(x);}

        //GA Init
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

        System.out.println(stats); // GA stats
        System.out.println(best); // Best Phenotype

        System.out.println("Make GA");
        GeneticAlgorithm genAlg = new GeneticAlgorithm(50, 15, 0.7, 0.2, 100, idList);
        System.out.println(genAlg.getBest());
        System.out.println(genAlg.getBest());
        System.out.println(genAlg.getStats());
    }
}
