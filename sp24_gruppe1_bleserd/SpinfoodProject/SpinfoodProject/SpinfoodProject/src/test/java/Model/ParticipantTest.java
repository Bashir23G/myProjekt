package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private Participant participant1;
    private Participant participant2;
    private Participant participant3;
    private Participant participant4;
    private Participant participant5;
    private Participant participant6;
    private Participant participant7;
    private Participant participant8;
    private Participant participant9;
    private Participant successor1;
    private Participant successor2;
    private Participant successor3;

    @BeforeEach
    void setUp() {
        //Initialisiere Teilnehmer
        participant1 = new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", FoodPreference.VEGGIE,
                16, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.673368271555807, 50.5941282715558),
                null, false);

        participant2 = new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", FoodPreference.ANY,
                22, Gender.MALE, Kitchen.YES, 3.0, new Location(8.718914539788807, 50.590899839788804),
                null, true);

        participant3 = new Participant("01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", FoodPreference.VEGAN,
                25, Gender.FEMALE, Kitchen.YES, 5.0, new Location(8.681372017093311, 50.5820794170933),
                participant2, true);

        participant4 = new Participant("01c1372d-d120-4459-9b65-39d56d1ad430", "Person4", FoodPreference.VEGAN,
                29, Gender.MALE, Kitchen.MAYBE, 0, new Location(8.683278559866123, 50.58156255986612),
                null, false);

        participant5 = new Participant("033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", FoodPreference.VEGGIE,
                32, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.681891196038887, 50.576791396038885),
                null, false);

        participant6 = new Participant("03ec42b3-de71-424d-a51d-f8a3977dac38", "Person6", FoodPreference.VEGAN,
                37, Gender.MALE, Kitchen.NO, 2.0, new Location(8.661605134221313, 50.57631493422131),
                null, false);

        participant7 = new Participant("04b46884-c678-450a-a38e-9087bb67bf97", "Person7", FoodPreference.MEAT,
                45, Gender.FEMALE, Kitchen.YES, 2.0, new Location(8.668413178974852, 50.574996578974854),
                null, false);

        participant8 = new Participant("05381170-b888-4457-a5f8-a528ac763236", "Person8", FoodPreference.ANY,
                48, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        participant9 = new Participant("06d17797-9452-49e2-8e46-e1067a5fb901", "Person11", FoodPreference.MEAT,
                60, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
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
    }

    @Test
    void calculateAgeRangeTest() {
        //Ergebnisse in Variablen speichern
        int result = participant1.calculateAgeRange(16);
        int result2 = participant2.calculateAgeRange(22);
        int result3 = participant3.calculateAgeRange(25);
        int result4 = participant4.calculateAgeRange(29);
        int result5 = participant5.calculateAgeRange(32);
        int result6 = participant6.calculateAgeRange(37);
        int result7 = participant7.calculateAgeRange(45);
        int result8 = participant8.calculateAgeRange(48);
        int result9 = participant9.calculateAgeRange(60);

        assertEquals(0 ,result);
        assertEquals(1 ,result2);
        assertEquals(2 ,result3);
        assertEquals(3 ,result4);
        assertEquals(4 ,result5);
        assertEquals(5 ,result6);
        assertEquals(6 ,result7);
        assertEquals(7 ,result8);
        assertEquals(8 ,result9);
    }

    @Test
    void findBestPossiblePartnerFromSuccessorListTest() {
        List<Participant> successorList = new ArrayList<>();
        successorList.add(successor1);
        successorList.add(successor2);
        successorList.add(successor3);

        List<ParticipantWeightPair> preferredPartnerList = new ArrayList<>();
        preferredPartnerList.add(new ParticipantWeightPair(7, successorList.get(0)));
        preferredPartnerList.add(new ParticipantWeightPair(3, successorList.get(1)));
        preferredPartnerList.add(new ParticipantWeightPair(5, successorList.get(2)));

        participant1.setPreferredPartnerList(preferredPartnerList);

        ParticipantWeightPair bestPartner = participant1.findBestPossiblePartnerFromSuccessorList(successorList);

        assertNotNull(bestPartner);
        assertEquals("14a89517-2e29-4bda-8ee6-733d502a41f5", bestPartner.getParticipant().getId());
    }

    @Test
    void printParticipantTest() {
        // Fange participant.printParticipant() in einen String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        participant1.printParticipant();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = "004670cb-47f5-40a4-87d8-5276c18616ec Person1 VEGGIE 16 FEMALE MAYBE 1.0 50.5941282715558 8.673368271555807";

        // Überprüfung der Ausgabe
        assertEquals(expectedOutput, output);
    }
    @Test
    void isPartneredTest() {
        //Ergebnisse in Variablen speichern
        boolean result = participant1.isPartnered();
        boolean result2 = participant3.isPartnered();

        assertFalse(result);
        assertTrue(result2);
    }
}