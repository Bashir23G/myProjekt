package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PairListTest {

    private PairList pairList1;
    private PairList pairList2;
    private ParticipantList successorList;
    private Location partyLocation;
    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Pair pair4;
    private Pair pair5;
    private Pair pair6;
    private Pair newPair;
    private Participant successor1;
    private Participant successor2;
    private Participant successor3;

    @BeforeEach
    void setUp() {
        successorList = new ParticipantList();
        partyLocation = new Location(8.681572017093311, 50.5818794170933);

        //Teilnehmer initialisieren
        Participant participant1 = new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", FoodPreference.VEGGIE,
                21, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.673368271555807, 50.5941282715558),
                null, false);

        Participant participant2 = new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", FoodPreference.ANY,
                26, Gender.MALE, Kitchen.YES, 3.0, new Location(8.718914539788807, 50.590899839788804),
                null, false);

        Participant participant3 = new Participant("01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", FoodPreference.VEGAN,
                22, Gender.FEMALE, Kitchen.YES, 5.0, new Location(8.681372017093311, 50.5820794170933),
                null, false);

        Participant participant4 = new Participant("01c1372d-d120-4459-9b65-39d56d1ad430", "Person4", FoodPreference.VEGGIE,
                23, Gender.MALE, Kitchen.YES, 0, new Location(8.683278559866123, 50.58156255986612),
                null, false);

        Participant participant5 = new Participant("033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", FoodPreference.MEAT,
                28, Gender.FEMALE, Kitchen.YES, 1.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant6 = new Participant("03ec42b3-de71-424d-a51d-f8a3977dac38", "Person6", FoodPreference.ANY,
                24, Gender.MALE, Kitchen.YES, 2.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant7 = new Participant("04b46884-c678-450a-a38e-9087bb67bf97", "Person7", FoodPreference.ANY,
                25, Gender.FEMALE, Kitchen.YES, 2.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant8 = new Participant("05381170-b888-4457-a5f8-a528ac763236", "Person8", FoodPreference.ANY,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant9 = new Participant("060800e6-8a64-4131-a292-22e5e1626b67", "Person9", FoodPreference.VEGAN,
                20, Gender.FEMALE, Kitchen.YES, 3.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant10 = new Participant("06082fb2-4297-4cf0-8840-c246d99f9700", "Person10", FoodPreference.VEGAN,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant11 = new Participant("06d17797-9452-49e2-8e46-e1067a5fb901", "Person11", FoodPreference.MEAT,
                20, Gender.FEMALE, Kitchen.YES, 3.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant12 = new Participant("07b46a18-a534-4c2c-b154-ec28c1aae8a7", "Person12", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        successor1 = new Participant("14a89517-2e29-4bda-8ee6-733d502a41f5", "successor1", FoodPreference.ANY,
                250, Gender.MALE, Kitchen.YES, 2.0, new Location(8.67299054580773,50.576247145807734),
                null, false);

        successor2 = new Participant("157329ba-094f-470d-ba76-84d96cd85096", "successor2", FoodPreference.ANY,
                250, Gender.MALE, Kitchen.MAYBE, 1.0, new Location(8.678239596150528,50.59166039615053),
                null, false);

        successor3 = new Participant("15abe192-f8d9-4958-89fb-a1b84b26204c", "successor3", FoodPreference.MEAT,
                250, Gender.MALE, Kitchen.YES, 2.0, new Location(8.690711820698867,50.598416220698866),
                null, false);

        successorList.add(successor1);
        successorList.add(successor2);
        successorList.add(successor3);
        pairList1 = new PairList(successorList);
        pairList2 = new PairList(successorList);
        pairList1.setPartyLocation(partyLocation);
        pairList2.setPartyLocation(partyLocation);

        //Paare erstellen
        pair1 = new Pair(participant1, participant2);
        pair2 = new Pair(participant3, participant4);
        pair3 = new Pair(participant5, participant6);
        pair4 = new Pair(participant7, participant8);
        pair5 = new Pair(participant9, participant10);
        pair6 = new Pair(participant11, participant12);

        newPair = new Pair(participant1, participant12);
    }

    @Test
    void setupKitchenInformationForEachPairTest() {
        //Paare in eine Paarliste einfügen
        pairList1.addPairToList(pair1);
        pairList2.addPairToList(pair2);
        pairList2.addPairToList(pair3);
        pairList2.addPairToList(pair4);
        pairList2.addPairToList(pair5);
        pairList2.addPairToList(pair6);


        pairList1.setupKitchenInformationForEachPair();
        pairList2.setupKitchenInformationForEachPair();


        //Bedingung 1&2 wird geprüft
        assertEquals(new Location(8.718914539788807, 50.590899839788804), pairList1.getPairList().get(0).getKitchenLocation());
        //Bedingung else wird geprüft
        assertEquals(new Location(8.681372017093311, 50.5820794170933), pairList2.getPairList().get(0).getKitchenLocation());
        //Überprüfung ob Pair6 aus PairList2 entfernt wurde, da bereits 3 Pärchen die Küche verwenden
        assertEquals(4, pairList2.getPairList().size());
    }

    @Test
    void addPairToListTest() {
        pairList1.addPairToList(pair1);

        assertEquals(1, pairList1.getPairList().size());
        assertEquals(pair1, pairList1.getPairList().get(0));
    }

    @Test
    void setupKitchenForSuccessorPairTest() {
        pairList2.addPairToList(pair2);

        pairList2.setupKitchenForSuccessorPair(newPair);

        assertNotNull(newPair.getKitchenLocation());
        assertTrue(newPair.isParticipantThatProvidesKitchen());
    }

    @Test
    void findPairOfParticipantTest() {
        pairList1.addPairToList(pair1);

        Pair foundPair = pairList1.findPairOfParticipant(pair1.getParticipant_1());

        assertNotNull(foundPair);
        assertEquals(pair1, foundPair);
    }

    @Test
    void printPairListQualityTest() {
        pairList1.addPairToList(pair1);
        pairList1.addPairToList(pair2);

        // Fange den pairList1.printPairListQuality() in ein String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        pairList1.printPairListQuality();

        System.setOut(originalOut);

        String output = outputStream.toString();

        //Teste Zeile für Zeile, ob String im Output vorhanden ist
        assertTrue(output.contains("Pairlist size: 2"));
        assertTrue(output.contains("SuccessorList size: 3"));
        assertTrue(output.contains("Age difference: 0.5"));
        assertTrue(output.contains("Gender diversity: 0.5"));
        assertTrue(output.contains("Preference Deviation: 1"));
    }

    @Test
    void printPairListTest() {
        pairList1.addPairToList(pair1);
        pairList1.addPairToList(pair2);

        // Fange den pairList1.printPairList() in ein String
        pairList1.setupKitchenInformationForEachPair();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        pairList1.printPairList();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = "Id: 1  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: true KitchenLocation: 50.590899839788804 8.718914539788807 Etage: 0.0 distanceToPartyLocation: 2.82km isRegisteredTogether: false\n" +
                "Pair besteht aus:\n" +
                "004670cb-47f5-40a4-87d8-5276c18616ec Person1 VEGGIE 21 FEMALE MAYBE 1.0 50.5941282715558 8.673368271555807\n" +
                "01a099db-22e1-4fc3-bbf5-db738bc2c10b Person2 ANY 26 MALE YES 3.0 50.590899839788804 8.718914539788807\n" +
                "------------------------------------------\n" +
                "Id: 2  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.5820794170933 8.681372017093311 Etage: 0.0 distanceToPartyLocation: 0.03km isRegisteredTogether: false\n" +
                "Pair besteht aus:\n" +
                "01be5c1f-4aa1-458d-a530-b1c109ffbb55 Person3 VEGAN 22 FEMALE YES 5.0 50.5820794170933 8.681372017093311\n" +
                "01c1372d-d120-4459-9b65-39d56d1ad430 Person4 VEGGIE 23 MALE YES 0.0 50.58156255986612 8.683278559866123\n" +
                "------------------------------------------";
        expectedOutput = expectedOutput.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Überprüfe die Ausgabe
        assertEquals(expectedOutput, output);
    }
}