package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GaleShapleyAlgorithmTest {

    private GaleShapleyAlgorithm galeShapleyAlgorithm;
    private List<Criteria> pairGenerationCriteriaRanking;
    private PairList pairList;
    private ParticipantList participantList;
    private ParticipantList successorList;

    @BeforeEach
    void setUp() {
        galeShapleyAlgorithm = new GaleShapleyAlgorithm();
        successorList = new ParticipantList();
        participantList = new ParticipantList();
        pairGenerationCriteriaRanking = new ArrayList<>();

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
                24, Gender.MALE, Kitchen.YES, 2.0, new Location(8.674109948270395, 50.5764905482704),
                null, false);

        Participant participant7 = new Participant("04b46884-c678-450a-a38e-9087bb67bf97", "Person7", FoodPreference.MEAT,
                25, Gender.FEMALE, Kitchen.YES, 2.0, new Location(8.668413178974852, 50.574996578974854),
                null, false);

        Participant participant8 = new Participant("05381170-b888-4457-a5f8-a528ac763236", "Person8", FoodPreference.ANY,
                24, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        Participant participant9 = new Participant("06d17797-9452-49e2-8e46-e1067a5fb901", "Person9", FoodPreference.MEAT,
                22, Gender.FEMALE, Kitchen.MAYBE, 3.0, new Location(8.681190328226592, 50.57991322822659),
                null, false);

        Participant participant10 = new Participant("07b46a18-a534-4c2c-b154-ec28c1aae8a7", "Person10", FoodPreference.MEAT,
                21, Gender.MALE, Kitchen.YES, 3.0, new Location(8.683101670107648, 50.584839070107655),
                null, false);

        //Teilnehmerliste bilden
        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);
        participantList.add(participant4);
        participantList.add(participant5);
        participantList.add(participant6);
        participantList.add(participant7);
        participantList.add(participant8);
        participantList.add(participant9);
        participantList.add(participant10);

        //Kriterien in Kriterienliste einfügen
        pairGenerationCriteriaRanking.add(Criteria.ageDifference);
        pairGenerationCriteriaRanking.add(Criteria.foodPreference);
        pairGenerationCriteriaRanking.add(Criteria.genderDiversity);
        pairGenerationCriteriaRanking.add(Criteria.minimizeLeftoverParticipants);

        pairList = new PairList(successorList);
    }

    @Test
    void generatePairsFromParticipantListTest() {
        //Paarliste aus Teilnehmerliste erstellen
        galeShapleyAlgorithm.generatePairsFromParticipantList(pairGenerationCriteriaRanking, pairList, participantList);

        //Aus 10 Teilnehmern sollten 5 Paare gebildet worden sein
        assertEquals(5, pairList.getPairList().size());

        assertTrue(pairList.getPairList().stream().allMatch(pair -> pair.getParticipant_1().getPartnerParticipant() != null));
        assertTrue(pairList.getPairList().stream().allMatch(pair -> pair.getParticipant_2().getPartnerParticipant() != null));
    }

    @Test
    void findBestPartnerParticipantForFreeParticipantsTest() {
        galeShapleyAlgorithm.generatePreferredPartnerListForParticipants(2, 1, 2, 3, participantList.getParticipantList(), pairList.getPairList());
        galeShapleyAlgorithm.findBestPartnerParticipantForFreeParticipants(participantList.getParticipantList());

        for (Participant participant : participantList.getParticipantList()) {
            assertNotNull(participant.getPartnerParticipant());
        }
    }

    @Test
    void moveParticipantToBeginningOfListAndRemovePartnerTest() {
        Participant participant = participantList.getParticipantList().get(0);
        participant.setPartnerParticipant(participantList.getParticipantList().get(1));
        List<Participant> participants = galeShapleyAlgorithm.moveParticipantToBeginningOfListAndRemovePartner(participant, participantList.getParticipantList());

        assertEquals(participant, participants.get(0));
        assertNull(participant.getPartnerParticipant());
    }

    @Test
    void findIndexFromParticipantInPreferredPartnerListTest() {
        Participant participant = participantList.getParticipantList().get(0);
        Participant participant2 = participantList.getParticipantList().get(0);
        Participant preferredPartner = participantList.getParticipantList().get(1);
        Participant notPrefferedPartner1 = participantList.getParticipantList().get(2);
        Participant notPrefferedPartner2 = participantList.getParticipantList().get(3);
        Participant notPrefferedPartner3 = participantList.getParticipantList().get(4);

        List<ParticipantWeightPair> list = new ArrayList<>();
        list.add(new ParticipantWeightPair(20, notPrefferedPartner1));
        list.add(new ParticipantWeightPair(30, notPrefferedPartner2));
        list.add(new ParticipantWeightPair(10, preferredPartner));
        list.add(new ParticipantWeightPair(40, notPrefferedPartner3));

        participant.setPreferredPartnerList(list);

        int index = galeShapleyAlgorithm.findIndexFromParticipantInPreferredPartnerList(preferredPartner, participant);

        assertEquals(2, index);

        List<ParticipantWeightPair> list2 = new ArrayList<>();
        list2.add(new ParticipantWeightPair(20, notPrefferedPartner1));
        list2.add(new ParticipantWeightPair(30, notPrefferedPartner2));
        list2.add(new ParticipantWeightPair(40, notPrefferedPartner3));

        participant2.setPreferredPartnerList(list2);

        int index2 = galeShapleyAlgorithm.findIndexFromParticipantInPreferredPartnerList(preferredPartner, participant2);

        assertEquals(-1, index2);
    }

    @Test
    void generatePreferredPartnerListForParticipantsTest() {
        galeShapleyAlgorithm.generatePreferredPartnerListForParticipants(2, 1, 2, 3, participantList.getParticipantList(), pairList.getPairList());

        for (Participant participant : participantList.getParticipantList()) {
            assertNotNull(participant.getPreferedPartnerList());
            assertTrue(participant.getPreferedPartnerList().size() > 0);
        }
    }

    @Test
    void generatePreferredPartnerListPostAlgorithmTest() {
        galeShapleyAlgorithm.generatePairsFromParticipantList(pairGenerationCriteriaRanking, pairList, participantList);

        // Vor dem Ausführen der Methode sicherstellen, dass die bevorzugten Partnerlisten leer sind
        for (Participant participant : participantList.getParticipantList()) {
            assertNull(participant.getPreferedPartnerList());
        }

        galeShapleyAlgorithm.generatePreferredPartnerListPostAlgorithm(1,1,1,1, participantList.getParticipantList(), pairList.getPairList());

        // Nach dem Ausführen der Methode sicherstellen, dass die bevorzugten Partnerlisten nicht mehr leer sind
        for (Participant participant : participantList.getParticipantList()) {
            assertNotNull(participant.getPreferedPartnerList());
            assertFalse(participant.getPreferedPartnerList().isEmpty());
        }
    }

    @Test
    void calculateFoodPreferenceWeightTest() {
        int weight = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.VEGAN, FoodPreference.MEAT, 1);
        int weight2 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.MEAT, FoodPreference.MEAT, 2);
        int weight3 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.MEAT, FoodPreference.ANY, 1);
        int weight4 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.VEGAN, FoodPreference.VEGGIE, 1);
        int weight5 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.VEGAN, FoodPreference.ANY, 1);
        int weight6 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.VEGGIE, FoodPreference.ANY, 1);
        int weight7 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.ANY, FoodPreference.ANY, 1);
        int weight8 = galeShapleyAlgorithm.calculateFoodPreferenceWeight(FoodPreference.VEGAN, FoodPreference.VEGAN, 1);

        assertEquals(0, weight);
        assertEquals(20, weight2);
        assertEquals(8, weight3);
        assertEquals(5, weight4);
        assertEquals(1, weight5);
        assertEquals(3, weight6);
        assertEquals(1, weight7);
        assertEquals(10, weight8);
    }

    @Test
    void calculateAgeDifferenceWeightTest() {
        int weight = galeShapleyAlgorithm.calculateAgeDifferenceWeight(22, 22, 1);
        int weight2 = galeShapleyAlgorithm.calculateAgeDifferenceWeight(22, 25, 2);
        int weight3 = galeShapleyAlgorithm.calculateAgeDifferenceWeight(22, 29, 3);
        int weight4 = galeShapleyAlgorithm.calculateAgeDifferenceWeight(24, 32, 2);

        assertEquals(10, weight);
        assertEquals(6, weight2);
        assertEquals(3, weight3);
        assertEquals(2, weight4);
    }

    @Test
    void calculateGenderDiversityWeightTest() {
        int weight = galeShapleyAlgorithm.calculateGenderDiversityWeight(Gender.MALE, Gender.FEMALE, 1);
        int weight2 = galeShapleyAlgorithm.calculateGenderDiversityWeight(Gender.MALE, Gender.MALE, 2);

        assertEquals(10, weight);
        assertEquals(10, weight2);
    }

    @Test
    void calculateValidLocationWeightTest() {
        int weight = galeShapleyAlgorithm.calculateValidLocationWeight(new Location(8.677666037868752, 50.593429237868754),
                new Location(8.677666037868752, 50.593429237868754));

        int weight2 = galeShapleyAlgorithm.calculateValidLocationWeight(new Location(8.667666037868752, 50.586429237868754),
                new Location(8.677666037868752, 50.593429237868754));

        assertEquals(-100000, weight);
        assertEquals(0, weight2);
    }

    @Test
    void calculateKitchenAvailabilityWeightTest() {
        int weight = galeShapleyAlgorithm.calculateKitchenAvailabilityWeight(1, Kitchen.NO, Kitchen.NO);
        int weight2 = galeShapleyAlgorithm.calculateKitchenAvailabilityWeight(1, Kitchen.YES, Kitchen.YES);
        int weight3 = galeShapleyAlgorithm.calculateKitchenAvailabilityWeight(1, Kitchen.MAYBE, Kitchen.MAYBE);
        int weight4 = galeShapleyAlgorithm.calculateKitchenAvailabilityWeight(1, Kitchen.YES, Kitchen.NO);
        int weight5 = galeShapleyAlgorithm.calculateKitchenAvailabilityWeight(1, Kitchen.MAYBE, Kitchen.NO);
        int weight6 = galeShapleyAlgorithm.calculateKitchenAvailabilityWeight(1, Kitchen.YES, Kitchen.MAYBE);

        assertEquals(-100000, weight);
        assertEquals(0, weight2);
        assertEquals(50, weight3);
        assertEquals(300, weight4);
        assertEquals(200, weight5);
        assertEquals(150, weight6);
    }
}