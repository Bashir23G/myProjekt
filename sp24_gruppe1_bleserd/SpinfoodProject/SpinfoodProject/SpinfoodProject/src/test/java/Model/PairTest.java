package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    private Participant participant1;
    private Participant participant2;
    private Pair pair;

    @BeforeEach
    void setUp() {
        //Teilnehmer initialisieren
        participant1 = new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", FoodPreference.VEGGIE,
                21, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.673368271555807, 50.5941282715558),
                null, false);

        participant2 = new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", FoodPreference.ANY,
                26, Gender.MALE, Kitchen.YES, 3.0, new Location(8.718914539788807, 50.590899839788804),
                null, false);

        //Paar erstellen
        pair = new Pair(participant1, participant2);
        pair.setKitchenLocation(new Location(8.718914539788807,50.590899839788804 ));
    }

    @Test
    void printPairTest() {
        // Fange den pairList1.printPairList() in ein String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        pair.printPair();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 1" +
                " genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.590899839788804 8.718914539788807" +
                " Etage: 0.0 distanceToPartyLocation: 0.0km isRegisteredTogether: false";
        expectedOutput = expectedOutput.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Überprüfe die Ausgabe
        assertEquals(expectedOutput, output);
    }

    @Test
    void getNewParticipantWithoutPairTest() {
        assertEquals(participant2, pair.getNewParticipantWithoutPair(participant1));
        assertEquals(participant1, pair.getNewParticipantWithoutPair(participant2));
        assertNull(pair.getParticipant_2());
    }
}