package Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class CustomRandom implements Serializable {
    private final Random random;
    private final int x;

    public CustomRandom(int x) {
        this.x = x;
        this.random = new Random();
    }

    /**
     * Wird verwendet, um geringere Indexe im GeneticAlgorithm zu bevorzugen
     * @return Eine Zahl bis definierten x, wobei geringere Zahlen wahrscheinlicher sind zu erhalten
     */

    public int nextInt() {
        double randomNumber = random.nextDouble();
        double exponent = -Math.abs(randomNumber * 10 - 5);
        double scaled = Math.exp(exponent);
        double scaledAndShifted = scaled * (double)x;

        return (int) scaledAndShifted;
    }}

