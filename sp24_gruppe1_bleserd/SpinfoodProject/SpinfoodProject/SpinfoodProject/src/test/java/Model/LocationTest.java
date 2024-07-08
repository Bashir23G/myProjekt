package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private Location location;
    private Location targetLocation;

    @BeforeEach
    void setUp() {
        //location und targetLocation initialisieren
        location = new Location(8.6733, 50.5941);
        targetLocation = new Location(8.6783, 50.5991);
    }

    @Test
    void distanceToTest() {
        //Ergebnis in einer Variable einspeichern
        double result = location.distanceTo(targetLocation);

        assertEquals(0.66, result);
    }
}