package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CSVReaderTest {

    private CSVReader csvReader;
    private ParticipantList participantList;
    private PairList pairList;

    @BeforeEach
    void setUp() {
        csvReader = new CSVReader();
        participantList = new ParticipantList();
        pairList = new PairList(participantList);
    }

    @Test
    void testParticipantGeneration() {
        String[] parts = new String[]{"0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1",
                "veggie", "21", "male" ,"maybe", "3.0","8.673368271555807","50.5941282715558"};

        Participant expectedParticipant = new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1",
                FoodPreference.VEGGIE, 21, Gender.MALE,  Kitchen.MAYBE, 3.0,
                new Location(8.673368271555807, 50.5941282715558), null, false);

        assertEquals(expectedParticipant, csvReader.createParticipant(parts));
    }

    @Test
    void testGeneratePartyLocationFromCSV() {
        String filePath = "Data/partylocation.csv";
        Location expectedLocation = new Location(8.6746166676233,50.5909317660173);

        assertEquals(expectedLocation, csvReader.generatePartyLocationFromCSV(filePath));
    }


    @Test
    void testGenerateParticipantsAndPairsFromCSV() {
        String filePath = "src/test/TestData/testTeilnehmerliste.csv";
        Participant expectedParticipant1 = new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1",
                FoodPreference.VEGGIE, 21, Gender.MALE,  Kitchen.MAYBE, 3.0,
                new Location(8.673368271555807, 50.5941282715558), null, false);

        Participant expectedParticipant2 = new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2",
                FoodPreference.ANY, 26, Gender.MALE,  Kitchen.YES, 1.0,
                new Location(8.718914539788807, 50.590899839788804), null, false);

        Participant expectedParticipant1FromPair = new Participant("01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3",
                FoodPreference.VEGAN, 22, Gender.MALE,  Kitchen.YES, 0.0,
                new Location(8.681372017093311, 50.5820794170933), null, true);

        Participant expectedParticipant2FromPair = new Participant("117ee996-14d3-44e8-8bcb-eb2d29fddda5", "Personx1",
                FoodPreference.VEGAN, 25, Gender.MALE,  Kitchen.YES, 0.0,
                new Location(8.681372017093311, 50.5820794170933), expectedParticipant1FromPair, true);
        expectedParticipant1FromPair.setPartnerParticipant(expectedParticipant2FromPair);

        ParticipantList expectedParticipantList = new ParticipantList();
        PairList expectedPairList = new PairList(expectedParticipantList);

        expectedPairList.addPairToList(new Pair(expectedParticipant1FromPair, expectedParticipant2FromPair));
        expectedParticipantList.add(expectedParticipant1);
        expectedParticipantList.add(expectedParticipant2);

        csvReader.generateParticipantsAndPairsFromCSV(filePath, participantList, pairList);

        assertEquals(expectedParticipantList.getParticipantList(), participantList.getParticipantList());
        assertEquals(expectedPairList.getPairList(), pairList.getPairList());
    }


    @Test
    void  testGenerateParticipantsAndPairsFromInvalidCSV() {
        String filePath = "SpinfoodProject/src/test/TestData/invalidTeilnehmerliste.csv";
        assertThrows(IllegalArgumentException.class, () -> csvReader.generateParticipantsAndPairsFromCSV(filePath, participantList, pairList));
    }

    @Test
    void  testGenerateParticipantsAndPairsFromEmptyCSV() {
        String filePath = "SpinfoodProject/src/test/TestData/emptyTeilnehmerliste.csv";
        assertThrows(IllegalArgumentException.class, () -> csvReader.generateParticipantsAndPairsFromCSV(filePath, participantList, pairList));
    }

    @Test
    void testIsCoupleRegistration() {
        String[] partsCoupleRegistration = new String[]{"0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1",
                "veggie", "21", "male" ,"maybe", "3.0","8.673368271555807","50.5941282715558", "117ee996-14d3-44e8-8bcb-eb2d29fddda5","Personx1","25.0","male"};

        String[] partsNoCoupleRegistration = new String[]{"0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1",
                "veggie", "21", "male" ,"maybe", "3.0","8.673368271555807","50.5941282715558"};


        assertTrue(csvReader.isCoupleRegistration(partsCoupleRegistration));
        assertFalse(csvReader.isCoupleRegistration(partsNoCoupleRegistration));
    }



}