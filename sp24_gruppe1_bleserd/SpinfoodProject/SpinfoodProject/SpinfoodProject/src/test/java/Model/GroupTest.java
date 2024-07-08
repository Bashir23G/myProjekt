package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private Group group;
    private Group group2;
    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Pair pair4;
    private Pair pair5;

    @BeforeEach
    void setUp() {
        //Teilnehmer initialisieren
        Participant participant1 = new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", FoodPreference.MEAT,
                21, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.673368271555807, 50.5941282715558),
                null, false);

        Participant participant2 = new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", FoodPreference.ANY,
                26, Gender.MALE, Kitchen.YES, 3.0, new Location(8.718914539788807, 50.590899839788804),
                null, false);

        Participant participant3 = new Participant("01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", FoodPreference.VEGAN,
                22, Gender.FEMALE, Kitchen.YES, 5.0, new Location(8.681372017093311, 50.5820794170933),
                null, false);

        Participant participant4 = new Participant("01c1372d-d120-4459-9b65-39d56d1ad430", "Person4", FoodPreference.VEGAN,
                23, Gender.MALE, Kitchen.MAYBE, 0, new Location(8.683278559866123, 50.58156255986612),
                null, false);

        Participant participant5 = new Participant("033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", FoodPreference.VEGGIE,
                28, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.681891196038887, 50.576791396038885),
                null, false);

        Participant participant6 = new Participant("03ec42b3-de71-424d-a51d-f8a3977dac38", "Person6", FoodPreference.VEGAN,
                24, Gender.MALE, Kitchen.NO, 2.0, new Location(8.661605134221313, 50.57631493422131),
                null, false);

        Participant participant7 = new Participant("04b46884-c678-450a-a38e-9087bb67bf97", "Person7", FoodPreference.MEAT,
                25, Gender.FEMALE, Kitchen.YES, 2.0, new Location(8.668413178974852, 50.574996578974854),
                null, false);

        Participant participant8 = new Participant("05381170-b888-4457-a5f8-a528ac763236", "Person8", FoodPreference.ANY,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        Participant participant9 = new Participant("06d17797-9452-49e2-8e46-e1067a5fb901", "Person11", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        Participant participant10 = new Participant("07b46a18-a534-4c2c-b154-ec28c1aae8a7", "Person12", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        //Paare bilden
        pair1 = new Pair(participant1, participant2);
        pair1.setPairId(0);
        pair1.setKitchenLocation(new Location(8.728914539788807,50.570899839788804 ));
        pair2 = new Pair(participant3, participant4);
        pair2.setPairId(1);
        pair2.setKitchenLocation(new Location(8.716914539788807,50.598899839788804 ));
        pair3 = new Pair(participant5, participant6);
        pair3.setPairId(2);
        pair3.setKitchenLocation(new Location(8.708914539788807,50.580899839788804 ));
        pair4 = new Pair(participant7, participant8);
        pair4.setPairId(3);
        pair5 = new Pair(participant9, participant10);
        pair5.setPairId(4);

        //Gang initialisieren
        Dish dish = Dish.MAIN;

        //Gruppe bilden
        group = new Group(pair1, pair2, pair3, dish, pair1);
        group2 = new Group(pair1, pair4, pair5, dish, pair1);
    }

    @Test
    void containsPairTest() {
        // Ergebnisse in Variablen einspeichern
        boolean result = group.containsPair(pair1); // Paar sollte ein Teil der Gruppe sein
        boolean result2 = group.containsPair(pair4); // Paar sollte nicht ein Teil der Gruppe sein

        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    void calculateAgeDifferenceTest() {
        //Ergebnis in Variable speichern
        double result = group.calculateAgeDifference();

        assertEquals(1.3333333333333333 , result);
    }

    @Test
    void calculateGenderDiversityTest() {
        //Ergebnis in Variable speichern
        double result = group.calculateGenderDiversity();

        assertEquals(0.5, result);
    }

    @Test
    void calculatePreferenceDeviationTest() {
        //Ergebnis in Variable speichern
        double result = group.calculatePreferenceDeviation();

        assertEquals(2, result);
    }

    @Test
    void fitnessForPairInGroupTest() {
        //Benötigten Multiplier deklarieren
        int ageDifferenceMultiplier = 1;
        int foodPreferenceMultiplier = 2;
        int genderDiversityMultiplier = 3;

        //Tatsächliche Fitness in einer Variable speichern
        double actualFitness1 = group.fitnessForPairInGroup(pair1, ageDifferenceMultiplier, foodPreferenceMultiplier, genderDiversityMultiplier);
        double actualFitness2 = group2.fitnessForPairInGroup(pair1, ageDifferenceMultiplier, foodPreferenceMultiplier, genderDiversityMultiplier);

        assertEquals(-1920.0, actualFitness1);
        assertEquals(99.0, actualFitness2);
    }

    @Test
    void printGroupTest() {
        // Fange den group.printGroup() in ein String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        group.printGroup();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = "Gruppe serviert:  MAIN UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5" +
                " preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.5708998397888 8.728914539788807" +
                " Etage: 0.0 distanceToPartyLocation: 0.0km isRegisteredTogether: false\n" +
                "Id: 1  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5" +
                " preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.59889983978881 8.716914539788807" +
                " Etage: 0.0 distanceToPartyLocation: 0.0km isRegisteredTogether: false\n" +
                "Id: 2  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 1 genderDiversity: 0.5" +
                " preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.580899839788806 8.708914539788807" +
                " Etage: 0.0 distanceToPartyLocation: 0.0km isRegisteredTogether: false";
        expectedOutput = expectedOutput.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Überprüfe die Ausgabe
        assertEquals(expectedOutput, output);
    }
}