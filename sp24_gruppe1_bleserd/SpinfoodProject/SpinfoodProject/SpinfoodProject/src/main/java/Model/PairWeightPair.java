package Model;

import java.io.Serializable;

/**
 * Wird verwendet, um für jedes Pärchen ein Weightwert zu berechnen, ohne dabei direkt das Pair Objekt anzupassen (Wichtig für GeneticAlgorithm)
 */
public class PairWeightPair implements Serializable {
    private Pair pair;
    private double fitness = 0;

    public PairWeightPair(Pair pair, double fitness) {
        this.pair = pair;
        this.fitness =fitness;
    }

    public Pair getPair() {
        return pair;
    }

    public double getFitness() {
        return fitness;
    }
}
