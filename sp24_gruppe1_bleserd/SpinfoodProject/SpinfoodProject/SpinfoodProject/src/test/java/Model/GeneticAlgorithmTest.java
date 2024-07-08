package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneticAlgorithmTest {

    private PairList pairList;
    private ParticipantList successorList;
    private List<Criteria> criteriaRankingList;
    private Location partyLocation;
    private Pair pair1;
    private Pair pair2;
    private GeneticAlgorithm geneticAlgorithm;

    @BeforeEach
    void setUp() {
        successorList = new ParticipantList();
        pairList = new PairList(successorList);
        criteriaRankingList = new ArrayList<>();
        partyLocation = new Location(8.681689934567821, 50.5831222345112);

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


        //Paare bilden, die Kitchenlocation initialisieren, alle Paare in eine Paarliste einfügen und die Distanz von der Küche zur Partylocation initialisieren
        pair1 = new Pair(participant1, participant2);
        pair1.setKitchenLocation(new Location(8.718914539788807,50.590899839788804));
        pair1.setDistanceToPartyLocation(pair1.getKitchenLocation().distanceTo(partyLocation));
        pairList.addPairToList(pair1);
        pair2 = new Pair(participant3, participant4);
        pair2.setKitchenLocation(new Location(8.681372017093311,50.5820794170933));
        pair2.setDistanceToPartyLocation(pair2.getKitchenLocation().distanceTo(partyLocation));
        pairList.addPairToList(pair2);

        //Kriterien in Kriterienliste einfügen
        criteriaRankingList.add(Criteria.minimizePathLength);
        criteriaRankingList.add(Criteria.ageDifference);
        criteriaRankingList.add(Criteria.foodPreference);
        criteriaRankingList.add(Criteria.genderDiversity);


        geneticAlgorithm = new GeneticAlgorithm(pairList, criteriaRankingList, partyLocation);
    }
    @Test
    void generateStarterPopulationTest() {
        //Prüfen ob eine Starterpopulation von 100 erstellt wird
        List<GroupList> starterPopulation = geneticAlgorithm.generateStarterPopulation();
        assertEquals(100, starterPopulation.size());
    }
}