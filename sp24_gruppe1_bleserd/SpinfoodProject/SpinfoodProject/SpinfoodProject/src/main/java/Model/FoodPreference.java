package Model;

import java.io.Serializable;

/**
 * Repräsentiert die jeweiligen FoodPreferenzen, die intValues werden verwendet, um die Essensabweichungen zu bestimmen
 */
public enum FoodPreference implements Serializable {
    ANY(3),
    VEGGIE(2),
    VEGAN(1),
    MEAT(4);

    private final int intValue;

    FoodPreference(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
