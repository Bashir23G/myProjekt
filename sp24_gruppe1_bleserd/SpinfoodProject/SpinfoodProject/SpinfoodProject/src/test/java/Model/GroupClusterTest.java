package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupClusterTest {

    private GroupCluster groupCluster;
    private List<Pair> groupClusterPairList;
    private List<Criteria> groupGenerationCriteriaRanking;
    private Location partyLocation;
    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Pair pair4;
    private Pair pair5;
    private Pair pair6;
    private Pair pair7;
    private Pair pair8;
    private Pair pair9;
    private List<Group> uniqueGroup1;
    private List<Group> uniqueGroup2;
    private List<Group> uniqueGroup3;
    private List<Group> uniqueGroup4;
    private List<Group> uniqueGroup5;
    private List<Group> uniqueGroup6;

    @BeforeEach
    void setUp() {
        groupClusterPairList = new ArrayList<>();
        groupGenerationCriteriaRanking = new ArrayList<>();
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

        Participant participant5 = new Participant("033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", FoodPreference.VEGGIE,
                28, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.681891196038887, 50.576791396038885),
                null, false);

        Participant participant6 = new Participant("03ec42b3-de71-424d-a51d-f8a3977dac38", "Person6", FoodPreference.VEGAN,
                24, Gender.MALE, Kitchen.NO, 2.0, null,
                null, false);

        Participant participant7 = new Participant("04b46884-c678-450a-a38e-9087bb67bf97", "Person7", FoodPreference.MEAT,
                25, Gender.FEMALE, Kitchen.YES, 2.0, new Location(8.668413178974852, 50.574996578974854),
                null, false);

        Participant participant8 = new Participant("05381170-b888-4457-a5f8-a528ac763236", "Person8", FoodPreference.ANY,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        Participant participant9 = new Participant("06d17797-9452-49e2-8e46-e1067a5fb901", "Person9", FoodPreference.MEAT,
                20, Gender.FEMALE, Kitchen.MAYBE, 3.0, new Location(8.681190328226592, 50.57991322822659),
                null, false);

        Participant participant10 = new Participant("07b46a18-a534-4c2c-b154-ec28c1aae8a7", "Person10", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.NO, 3.0, null,
                null, false);

        Participant participant11 = new Participant("07b7446a-9d8b-478b-b3e9-e95b992fcf50", "Person11", FoodPreference.MEAT,
                20, Gender.FEMALE, Kitchen.NO, 3.0, null,
                null, false);

        Participant participant12 = new Participant("08a7b852-9074-43bd-bc68-b2940fdf6bb9", "Person12", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.674109948270395, 50.5764905482704),
                null, false);

        Participant participant13 = new Participant("08f022cf-64f7-4b42-b3ae-01d24679c734", "Person13", FoodPreference.VEGAN,
                20, Gender.FEMALE, Kitchen.YES, 1.0, new Location(8.683101670107648, 50.584839070107655),
                null, false);

        Participant participant14 = new Participant("0979c6b8-6a54-41e0-938e-92e9bb86fdf7", "Person14", FoodPreference.VEGAN,
                20, Gender.MALE, Kitchen.YES, 4.0, new Location(8.688248785207652, 50.58700358520764),
                null, false);

        Participant participant15 = new Participant("0b9f4fb1-bfdb-4b2d-afc7-919ae82c6d70", "Person15", FoodPreference.VEGGIE,
                20, Gender.FEMALE, Kitchen.MAYBE, 3.0, new Location(8.677666037868752, 50.593429237868754),
                null, false);

        Participant participant16 = new Participant("0bd876d2-7c12-48ba-bcf2-ecc016e2e024", "Person16", FoodPreference.ANY,
                20, Gender.MALE, Kitchen.NO, 3.0, null,
                null, false);

        Participant participant17 = new Participant("0c6a98a2-8c1f-4865-a43c-dc11c56f45dc", "Person17", FoodPreference.VEGGIE,
                20, Gender.FEMALE, Kitchen.MAYBE, 2.0, new Location(8.713355020601862, 50.60878552060185),
                null, false);

        Participant participant18 = new Participant("0d005a3f-2e48-4b4d-9c8d-b222996ebad1", "Person18", FoodPreference.VEGGIE,
                20, Gender.MALE, Kitchen.MAYBE, 1.0, new Location(8.685734197139844, 50.576635397139846),
                null, false);

        //Paare bilden, die Kitchenlocation initialisieren, alle Paare in eine Gruppenclusterpaarliste einfügen und die Distanz von der Küche zur Partylocation initialisieren
        pair1 = new Pair(participant1, participant2);
        pair1.setKitchenLocation(new Location(8.718914539788807,50.590899839788804));
        groupClusterPairList.add(pair1);
        pair1.setDistanceToPartyLocation(pair1.getKitchenLocation().distanceTo(partyLocation));
        pair2 = new Pair(participant3, participant4);
        pair2.setKitchenLocation(new Location(8.681372017093311,50.5820794170933));
        groupClusterPairList.add(pair2);
        pair2.setDistanceToPartyLocation(pair2.getKitchenLocation().distanceTo(partyLocation));
        pair3 = new Pair(participant5, participant6);
        pair3.setKitchenLocation(new Location(8.681891196038887,50.576791396038885));
        groupClusterPairList.add(pair3);
        pair3.setDistanceToPartyLocation(pair3.getKitchenLocation().distanceTo(partyLocation));
        pair4 = new Pair(participant7, participant8);
        pair4.setKitchenLocation(new Location(8.668413178974852,50.574996578974854));
        groupClusterPairList.add(pair4);
        pair4.setDistanceToPartyLocation(pair4.getKitchenLocation().distanceTo(partyLocation));
        pair5 = new Pair(participant9, participant10);
        pair5.setKitchenLocation(new Location(8.681190328226592,50.57991322822659));
        groupClusterPairList.add(pair5);
        pair5.setDistanceToPartyLocation(pair5.getKitchenLocation().distanceTo(partyLocation));
        pair6 = new Pair(participant11, participant12);
        pair6.setKitchenLocation(new Location(8.674109948270395,50.5764905482704));
        groupClusterPairList.add(pair6);
        pair6.setDistanceToPartyLocation(pair6.getKitchenLocation().distanceTo(partyLocation));
        pair7 = new Pair(participant13, participant14);
        pair7.setKitchenLocation(new Location(8.683101670107648,50.584839070107655));
        groupClusterPairList.add(pair7);
        pair7.setDistanceToPartyLocation(pair7.getKitchenLocation().distanceTo(partyLocation));
        pair8 = new Pair(participant15, participant16);
        pair8.setKitchenLocation(new Location(8.677666037868752,50.593429237868754));
        groupClusterPairList.add(pair8);
        pair8.setDistanceToPartyLocation(pair8.getKitchenLocation().distanceTo(partyLocation));
        pair9 = new Pair(participant17, participant18);
        pair9.setKitchenLocation(new Location(8.685734197139844,50.576635397139846));
        groupClusterPairList.add(pair9);
        pair9.setDistanceToPartyLocation(pair9.getKitchenLocation().distanceTo(partyLocation));

        //Kriterien in Kriterienliste einfügen
        groupGenerationCriteriaRanking.add(Criteria.minimizePathLength);
        groupGenerationCriteriaRanking.add(Criteria.ageDifference);
        groupGenerationCriteriaRanking.add(Criteria.foodPreference);
        groupGenerationCriteriaRanking.add(Criteria.genderDiversity);

        groupCluster = new GroupCluster(groupClusterPairList, groupGenerationCriteriaRanking, partyLocation);

        //Unique Gruppen initialisieren
        uniqueGroup1 = groupCluster.firstUniqueGroupConstellation();
        uniqueGroup2 = groupCluster.secondUniqueGroupConstellation();
        uniqueGroup3 = groupCluster.thirdUniqueGroupConstellation();
        uniqueGroup4 = groupCluster.fourthUniqueGroupConstellation();
        uniqueGroup5 = groupCluster.fifthUniqueGroupConstellation();
        uniqueGroup6 = groupCluster.sixthUniqueGroupConstellation();
    }
    @Test
    void distributeDishesDependingOnDistanceToPartyLocationTest() {
        //Methode wurde im Konstruktor benutzt
        //Paare in jeweilige Gängelisten einspeichern
        List<Pair> hostingStarterDishPairs = groupCluster.getHostingStarterDishPairs();
        List<Pair> hostingMainDishPairs = groupCluster.getHostingMainDishPairs();
        List<Pair> hostingDessertPairs = groupCluster.getHostingDessertPairs();

        //Prüfen, ob jeweils 3 paare in alle Gänge eingeteilt wurden
        assertEquals(3, hostingStarterDishPairs.size());
        assertEquals(3, hostingMainDishPairs.size());
        assertEquals(3, hostingDessertPairs.size());

        //Distanz von hostingStarterDishPair ist größer zu Partylocation, als zu hostingMainDishPair
        assertTrue(hostingStarterDishPairs.get(0).getDistanceToPartyLocation() > hostingMainDishPairs.get(0).getDistanceToPartyLocation());
        //Distanz von hostingMainDishPair ist größer zu Partylocation, als zu hostingDessertPair
        assertTrue(hostingMainDishPairs.get(0).getDistanceToPartyLocation() > hostingDessertPairs.get(0).getDistanceToPartyLocation());
    }

    @Test
    void calculateTotalPreferenceDifferenceTest() {
        assertEquals(6, groupCluster.calculateTotalPreferenceDifference(pair1, pair2, pair3));
        assertEquals(0, groupCluster.calculateTotalPreferenceDifference(pair4, pair5, pair6));
        assertEquals(2, groupCluster.calculateTotalPreferenceDifference(pair7, pair8, pair9));
    }

    @Test
    void countFoodPreferenceTypesTest() {
        int resultVegan = groupCluster.countFoodPreferenceTypes(groupClusterPairList, FoodPreference.VEGAN);
        int resultVeggie = groupCluster.countFoodPreferenceTypes(groupClusterPairList, FoodPreference.VEGGIE);
        int resultMeat = groupCluster.countFoodPreferenceTypes(groupClusterPairList, FoodPreference.MEAT);
        int resultNone = groupCluster.countFoodPreferenceTypes(groupClusterPairList, FoodPreference.ANY);
        assertEquals(3, resultVegan);
        assertEquals(2, resultVeggie);
        assertEquals(4, resultMeat);
        assertEquals(0, resultNone);
    }

    @Test
    void extractAllPairsTest() {
        HashMap<Character, Pair> pairs = groupCluster.extractAllPairs();

        assertEquals(groupCluster.getHostingStarterDishPairs().get(0), pairs.get('a'));
        assertEquals(groupCluster.getHostingStarterDishPairs().get(1), pairs.get('b'));
        assertEquals(groupCluster.getHostingStarterDishPairs().get(2), pairs.get('c'));
        assertEquals(groupCluster.getHostingMainDishPairs().get(0), pairs.get('d'));
        assertEquals(groupCluster.getHostingMainDishPairs().get(1), pairs.get('e'));
        assertEquals(groupCluster.getHostingMainDishPairs().get(2), pairs.get('f'));
        assertEquals(groupCluster.getHostingDessertPairs().get(0), pairs.get('g'));
        assertEquals(groupCluster.getHostingDessertPairs().get(1), pairs.get('h'));
        assertEquals(groupCluster.getHostingDessertPairs().get(2), pairs.get('i'));
    }

    @Test
    void firstUniqueGroupConstellationTest() {
        //check ob uniqueGruppe1 sich von anderen unique Gruppen unterscheidet
        assertTrue(uniqueGroup1 != uniqueGroup2);
        assertTrue(uniqueGroup1 != uniqueGroup3);
        assertTrue(uniqueGroup1 != uniqueGroup4);
        assertTrue(uniqueGroup1 != uniqueGroup5);
        assertTrue(uniqueGroup1 != uniqueGroup6);
    }

    @Test
    void secondUniqueGroupConstellationTest() {
        //check ob uniqueGruppe2 sich von anderen unique Gruppen unterscheidet
        assertTrue(uniqueGroup2 != uniqueGroup1);
        assertTrue(uniqueGroup2 != uniqueGroup3);
        assertTrue(uniqueGroup2 != uniqueGroup4);
        assertTrue(uniqueGroup2 != uniqueGroup5);
        assertTrue(uniqueGroup2 != uniqueGroup6);
    }

    @Test
    void thirdUniqueGroupConstellationTest() {
        //check ob uniqueGruppe3 sich von anderen unique Gruppen unterscheidet
        assertTrue(uniqueGroup3 != uniqueGroup1);
        assertTrue(uniqueGroup3 != uniqueGroup2);
        assertTrue(uniqueGroup3 != uniqueGroup4);
        assertTrue(uniqueGroup3 != uniqueGroup5);
        assertTrue(uniqueGroup3 != uniqueGroup6);
    }

    @Test
    void fourthUniqueGroupConstellationTest() {
        //check ob uniqueGruppe4 sich von anderen unique Gruppen unterscheidet
        assertTrue(uniqueGroup4 != uniqueGroup1);
        assertTrue(uniqueGroup4 != uniqueGroup2);
        assertTrue(uniqueGroup4 != uniqueGroup3);
        assertTrue(uniqueGroup4 != uniqueGroup5);
        assertTrue(uniqueGroup4 != uniqueGroup6);
    }

    @Test
    void fifthUniqueGroupConstellationTest() {
        //check ob uniqueGruppe5 sich von anderen unique Gruppen unterscheidet
        assertTrue(uniqueGroup5 != uniqueGroup1);
        assertTrue(uniqueGroup5 != uniqueGroup2);
        assertTrue(uniqueGroup5 != uniqueGroup3);
        assertTrue(uniqueGroup5 != uniqueGroup4);
        assertTrue(uniqueGroup5 != uniqueGroup6);
    }

    @Test
    void sixthUniqueGroupConstellationTest() {
        //check ob uniqueGruppe6 sich von anderen unique Gruppen unterscheidet
        assertTrue(uniqueGroup6 != uniqueGroup1);
        assertTrue(uniqueGroup6 != uniqueGroup2);
        assertTrue(uniqueGroup6 != uniqueGroup3);
        assertTrue(uniqueGroup6 != uniqueGroup4);
        assertTrue(uniqueGroup6 != uniqueGroup5);
    }

    @Test
    void printClusterTest() {
        // Fange den groupCluster.printCluster(); in ein String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        groupCluster.printCluster();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = "Vorspeisen Gruppen:\n" +
                "Gruppe serviert:  STARTER UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.590899839788804 8.718914539788807 Etage: 0.0 distanceToPartyLocation: 2.77km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.576791396038885 8.681891196038887 Etage: 0.0 distanceToPartyLocation: 0.7km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.5820794170933 8.681372017093311 Etage: 0.0 distanceToPartyLocation: 0.12km isRegisteredTogether: false\n" +
                "Gruppe serviert:  STARTER UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.574996578974854 8.668413178974852 Etage: 0.0 distanceToPartyLocation: 1.3km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.5764905482704 8.674109948270395 Etage: 0.0 distanceToPartyLocation: 0.91km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.57991322822659 8.681190328226592 Etage: 0.0 distanceToPartyLocation: 0.36km isRegisteredTogether: false\n" +
                "Gruppe serviert:  STARTER UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.593429237868754 8.677666037868752 Etage: 0.0 distanceToPartyLocation: 1.18km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.576635397139846 8.685734197139844 Etage: 0.0 distanceToPartyLocation: 0.78km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.584839070107655 8.683101670107648 Etage: 0.0 distanceToPartyLocation: 0.22km isRegisteredTogether: false\n" +
                "Hauptspeißen Gruppen: \n" +
                "Gruppe serviert:  MAIN UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.5764905482704 8.674109948270395 Etage: 0.0 distanceToPartyLocation: 0.91km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.590899839788804 8.718914539788807 Etage: 0.0 distanceToPartyLocation: 2.77km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.584839070107655 8.683101670107648 Etage: 0.0 distanceToPartyLocation: 0.22km isRegisteredTogether: false\n" +
                "Gruppe serviert:  MAIN UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.576635397139846 8.685734197139844 Etage: 0.0 distanceToPartyLocation: 0.78km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.574996578974854 8.668413178974852 Etage: 0.0 distanceToPartyLocation: 1.3km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.5820794170933 8.681372017093311 Etage: 0.0 distanceToPartyLocation: 0.12km isRegisteredTogether: false\n" +
                "Gruppe serviert:  MAIN UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.576791396038885 8.681891196038887 Etage: 0.0 distanceToPartyLocation: 0.7km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.593429237868754 8.677666037868752 Etage: 0.0 distanceToPartyLocation: 1.18km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.57991322822659 8.681190328226592 Etage: 0.0 distanceToPartyLocation: 0.36km isRegisteredTogether: false\n" +
                "Dessert Gruppen: \n" +
                "Gruppe serviert:  DESSERT UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.57991322822659 8.681190328226592 Etage: 0.0 distanceToPartyLocation: 0.36km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.590899839788804 8.718914539788807 Etage: 0.0 distanceToPartyLocation: 2.77km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.576635397139846 8.685734197139844 Etage: 0.0 distanceToPartyLocation: 0.78km isRegisteredTogether: false\n" +
                "Gruppe serviert:  DESSERT UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.584839070107655 8.683101670107648 Etage: 0.0 distanceToPartyLocation: 0.22km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.574996578974854 8.668413178974852 Etage: 0.0 distanceToPartyLocation: 1.3km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 1 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.576791396038885 8.681891196038887 Etage: 0.0 distanceToPartyLocation: 0.7km isRegisteredTogether: false\n" +
                "Gruppe serviert:  DESSERT UniqueID: 0\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGAN ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.5820794170933 8.681372017093311 Etage: 0.0 distanceToPartyLocation: 0.12km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: VEGGIE ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 1 Teilnehmer der Küche stellt: false KitchenLocation: 50.593429237868754 8.677666037868752 Etage: 0.0 distanceToPartyLocation: 1.18km isRegisteredTogether: false\n" +
                "Id: 0  STARTERGROUP: 0 MAINGROUP: 0 DESSERTGROUP 0 PAIR HOSTED: null FoodPreference: MEAT ageDifference: 0 genderDiversity: 0.5 preferenceDevitation: 0 Teilnehmer der Küche stellt: false KitchenLocation: 50.5764905482704 8.674109948270395 Etage: 0.0 distanceToPartyLocation: 0.91km isRegisteredTogether: false";
        expectedOutput = expectedOutput.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Überprüfe die Ausgabe
        assertEquals(expectedOutput, output);
    }
}