package Model;

import java.io.Serializable;

/**
 * Repräsentiert die jeweiligen Gerichte
 */
public enum Dish implements Serializable {
    STARTER(1),
    MAIN(2),
    DESSERT(3);

    private final int intValue;


    Dish(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
