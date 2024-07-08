package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupListTest {

    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Pair pair4;
    private Pair pair5;
    private Pair pair6;
    private Pair pair7;
    private Pair pair8;
    private Pair pair9;
    private GroupList groupList;
    private GroupList groupList1;
    private List<Pair> allPairs;
    private Location partyLocation;
    private List<Criteria> criteriaRankingList;
    private Pair pair10;
    private Pair pair11;
    private Pair pair12;
    private Pair pair13;
    private Pair pair14;
    private Pair pair15;
    private Pair pair16;
    private Pair pair17;
    private Pair pair18;
    private List<Pair> successorPairList;

    @BeforeEach
    void setUp() {
        successorPairList = new ArrayList<>();
        allPairs = new ArrayList<>();
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
                24, Gender.MALE, Kitchen.YES, 3.0, new Location(8.719772776126227, 50.591925376126234),
                null, false);

        Participant participant9 = new Participant("06d17797-9452-49e2-8e46-e1067a5fb901", "Person9", FoodPreference.MEAT,
                22, Gender.FEMALE, Kitchen.MAYBE, 3.0, new Location(8.681190328226592, 50.57991322822659),
                null, false);

        Participant participant10 = new Participant("07b46a18-a534-4c2c-b154-ec28c1aae8a7", "Person10", FoodPreference.MEAT,
                21, Gender.MALE, Kitchen.NO, 3.0, null,
                null, false);

        Participant participant11 = new Participant("07b7446a-9d8b-478b-b3e9-e95b992fcf50", "Person11", FoodPreference.MEAT,
                20, Gender.FEMALE, Kitchen.NO, 3.0, null,
                null, false);

        Participant participant12 = new Participant("08a7b852-9074-43bd-bc68-b2940fdf6bb9", "Person12", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.YES, 3.0, new Location(8.674109948270395, 50.5764905482704),
                null, false);

        Participant participant13 = new Participant("08f022cf-64f7-4b42-b3ae-01d24679c734", "Person13", FoodPreference.VEGAN,
                25, Gender.FEMALE, Kitchen.YES, 1.0, new Location(8.683101670107648, 50.584839070107655),
                null, false);

        Participant participant14 = new Participant("0979c6b8-6a54-41e0-938e-92e9bb86fdf7", "Person14", FoodPreference.VEGAN,
                26, Gender.MALE, Kitchen.YES, 4.0, new Location(8.688248785207652, 50.58700358520764),
                null, false);

        Participant participant15 = new Participant("0b9f4fb1-bfdb-4b2d-afc7-919ae82c6d70", "Person15", FoodPreference.VEGGIE,
                27, Gender.FEMALE, Kitchen.MAYBE, 3.0, new Location(8.677666037868752, 50.593429237868754),
                null, false);

        Participant participant16 = new Participant("0bd876d2-7c12-48ba-bcf2-ecc016e2e024", "Person16", FoodPreference.ANY,
                28, Gender.MALE, Kitchen.NO, 3.0, null,
                null, false);

        Participant participant17 = new Participant("0c6a98a2-8c1f-4865-a43c-dc11c56f45dc", "Person17", FoodPreference.VEGGIE,
                20, Gender.FEMALE, Kitchen.MAYBE, 2.0, new Location(8.713355020601862, 50.60878552060185),
                null, false);

        Participant participant18 = new Participant("0d005a3f-2e48-4b4d-9c8d-b222996ebad1", "Person18", FoodPreference.VEGGIE,
                22, Gender.MALE, Kitchen.MAYBE, 1.0, new Location(8.685734197139844, 50.576635397139846),
                null, false);

        Participant participant19 = new Participant("10cf2d4e-541e-4707-8867-4fb296806696", "Person19", FoodPreference.VEGAN,
                24, Gender.FEMALE, Kitchen.YES, 1.0, new Location(8.685734197139844, 50.576635397139846),
                null, false);

        Participant participant20 = new Participant("112a5f61-0708-4071-acaf-2e005a427c58", "Person20", FoodPreference.VEGGIE,
                24, Gender.MALE, Kitchen.YES, 0, new Location(8.677666037868752,50.593429237868754),
                null, false);

        Participant participant21 = new Participant("141d3e33-bd78-4c74-b269-621278dd4788", "Person21", FoodPreference.ANY,
                23, Gender.FEMALE, Kitchen.YES, 1.0, new Location(8.713355020601862,50.60878552060185),
                null, false);

        Participant participant22 = new Participant("14a89517-2e29-4bda-8ee6-733d502a41f5", "Person22", FoodPreference.VEGGIE,
                23, Gender.MALE, Kitchen.NO, 2.0, null,
                null, false);

        Participant participant23 = new Participant("157329ba-094f-470d-ba76-84d96cd85096", "Person23", FoodPreference.MEAT,
                21, Gender.FEMALE, Kitchen.MAYBE, 4.0, new Location(8.685734197139844,50.576635397139846),
                null, false);

        Participant participant24 = new Participant("15abe192-f8d9-4958-89fb-a1b84b26204c", "Person24", FoodPreference.MEAT,
                20, Gender.MALE, Kitchen.YES, -1.0, new Location(8.669084504431382,50.58412450443137),
                null, false);

        Participant participant25 = new Participant("176e1b97-0101-4d0c-b519-75d6dbda35af", "Person25", FoodPreference.VEGAN,
                25, Gender.FEMALE, Kitchen.YES, -1.0, new Location(8.676057363906686,50.586267363906686),
                null, false);

        Participant participant26 = new Participant("1836d0a1-4a1e-40bc-810a-3ae011f961b0", "Person26", FoodPreference.VEGAN,
                27, Gender.MALE, Kitchen.YES, 2.0, new Location(8.67437965355096,50.585509553550956),
                null, false);

        Participant participant27 = new Participant("196b3469-9e46-482b-9365-1b7d088b6394", "Person27", FoodPreference.VEGAN,
                28, Gender.FEMALE, Kitchen.YES, 4.0, new Location(8.67299054580773,50.576247145807734),
                null, false);

        Participant participant28 = new Participant("19aab249-bbdf-4d6e-813f-24cc1c2af397", "Person28", FoodPreference.ANY,
                28, Gender.MALE, Kitchen.MAYBE, 3.0, new Location(8.68117388238734,50.57964918238733),
                null, false);

        Participant participant29 = new Participant("1a1a63a4-4557-4f12-bb54-cb82c51b26b7", "Person29", FoodPreference.MEAT,
                28, Gender.FEMALE, Kitchen.YES, 1.0, new Location(8.678239596150528,50.59166039615053),
                null, false);

        Participant participant30 = new Participant("1ad190d0-a95a-4f74-85b1-22f4812aab1b", "Person30", FoodPreference.MEAT,
                29, Gender.MALE, Kitchen.NO, 1.0, null,
                null, false);

        Participant participant31 = new Participant("1af22822-ab72-4483-9918-f017fe05a71a", "Person31", FoodPreference.MEAT,
                22, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.690711820698867,50.598416220698866),
                null, false);

        Participant participant32 = new Participant("1c30b2f3-ef9e-4f23-bcc8-73b885fd11c5", "Person32", FoodPreference.ANY,
                23, Gender.MALE, Kitchen.NO, 1.0, null,
                null, false);

        Participant participant33 = new Participant("1d363c90-7332-4f02-bba3-692704d862e9", "Person33", FoodPreference.ANY,
                21, Gender.FEMALE, Kitchen.MAYBE, 1.0, new Location(8.715372110353728,50.58857771035373),
                null, false);

        Participant participant34 = new Participant("1e3bed45-21d6-474d-9425-c16f206fb982", "Person34", FoodPreference.VEGGIE,
                21, Gender.MALE, Kitchen.YES, 2.0, new Location(8.712342110353728,50.58852341035373),
                null, false);

        Participant participant35 = new Participant("21487e36-1815-4de0-875b-f649f2364320", "Person35", FoodPreference.ANY,
                21, Gender.FEMALE, Kitchen.YES, 2.0, new Location(8.7343312453728,50.53547223435643),
                null, false);

        Participant participant36 = new Participant("217bc984-371e-4303-bb87-064c172614db", "Person36", FoodPreference.VEGGIE,
                23, Gender.MALE, Kitchen.MAYBE, 2.0, new Location(8.735433115453728,50.53547776435643),
                null, false);

        //Paare bilden, die Kitchenlocation initialisieren und alle Paare in eine Liste einfügen
        pair1 = new Pair(participant1, participant2);
        pair1.setKitchenLocation(new Location(8.718914539788807,50.590899839788804));
        allPairs.add(pair1);
        pair2 = new Pair(participant3, participant4);
        pair2.setKitchenLocation(new Location(8.681372017093311,50.5820794170933));
        allPairs.add(pair2);
        pair3 = new Pair(participant5, participant6);
        pair3.setKitchenLocation(new Location(8.681891196038887,50.576791396038885));
        allPairs.add(pair3);
        pair4 = new Pair(participant7, participant8);
        pair4.setKitchenLocation(new Location(8.668413178974852,50.574996578974854));
        allPairs.add(pair4);
        pair5 = new Pair(participant9, participant10);
        pair5.setKitchenLocation(new Location(8.681190328226592,50.57991322822659));
        allPairs.add(pair5);
        pair6 = new Pair(participant11, participant12);
        pair6.setKitchenLocation(new Location(8.674109948270395,50.5764905482704));
        allPairs.add(pair6);
        pair7 = new Pair(participant13, participant14);
        pair7.setKitchenLocation(new Location(8.683101670107648,50.584839070107655));
        allPairs.add(pair7);
        pair8 = new Pair(participant15, participant16);
        pair8.setKitchenLocation(new Location(8.677666037868752,50.593429237868754));
        allPairs.add(pair8);
        pair9 = new Pair(participant17, participant18);
        pair9.setKitchenLocation(new Location(8.685734197139844,50.576635397139846));
        allPairs.add(pair9);
        pair10 = new Pair(participant19,participant20);
        pair10.setKitchenLocation(new Location(8.685734197139844, 50.576635397139846));
        allPairs.add(pair10);
        pair11 = new Pair(participant21,participant22);
        pair11.setKitchenLocation(new Location(8.713355020601862,50.60878552060185));
        allPairs.add(pair11);
        pair12 = new Pair(participant23,participant24);
        pair12.setKitchenLocation(new Location(8.669084504431382,50.58412450443137));
        allPairs.add(pair12);
        pair13 = new Pair(participant25,participant26);
        pair13.setKitchenLocation(new Location(8.676057363906686,50.586267363906686));
        allPairs.add(pair13);
        pair14 = new Pair(participant27,participant28);
        pair14.setKitchenLocation(new Location(8.67299054580773,50.576247145807734));
        allPairs.add(pair14);
        pair15 = new Pair(participant29,participant30);
        pair15.setKitchenLocation(new Location(8.678239596150528,50.59166039615053));
        allPairs.add(pair15);
        pair16 = new Pair(participant31,participant32);
        pair16.setKitchenLocation(new Location(8.690711820698867,50.598416220698866));
        allPairs.add(pair16);
        pair17 = new Pair(participant33,participant34);
        pair17.setKitchenLocation(new Location(8.715372110353728,50.58857771035373));
        allPairs.add(pair17);
        pair18 = new Pair(participant35,participant36);
        pair18.setKitchenLocation(new Location(8.735433115453728,50.53547776435643));
        allPairs.add(pair18);

        //Kriterien in Kriterienliste einfügen
        criteriaRankingList.add(Criteria.minimizePathLength);
        criteriaRankingList.add(Criteria.ageDifference);
        criteriaRankingList.add(Criteria.foodPreference);
        criteriaRankingList.add(Criteria.genderDiversity);

        groupList = new GroupList(allPairs, partyLocation, criteriaRankingList);

        groupList1 = new GroupList(groupList);

        groupList1.finalizeGroupList();
    }

    @Test
    void printGroupListQualityTest() {
        // Fange den groupCluster.printCluster() in ein String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        groupList1.printGroupListQuality();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = "Gruppen in der Liste: " + groupList1.getAllGroups().size() +"\n" +
                "Anzahl nachrückender Pärchen: " + successorPairList.size() +"\n" +
                "Genderdiversität: " + groupList1.getGenderDiversity() +"\n" +
                "Altersdifferenz: " + groupList1.getAgeDifference() +"\n" +
                "Vorliebenabweichung: " + groupList1.getPreferenceDeviation() +"\n" +
                "Mittelwert der Weglänge für jedes Paar: " + groupList1.getAverageTravelDistanceForPair() +"\n" +
                "Standardabweichung der Weglängen der Paare: " + groupList1.getStandardDeviationForPair();

        expectedOutput = expectedOutput.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Überprüfe die Ausgabe
        assertEquals(expectedOutput, output);
    }

    @Test
    void removeGroupsOverMaxAllowedLimitTest() {
        groupList1.removeGroupsOverMaxAllowedLimit(9);
        int size = groupList1.getAllGroups().size();
        assertEquals(9, size);
    }


    @Test
    void finalizeGroupListTest() {
        groupList1.finalizeGroupList();
        assertTrue(groupList1.getAllGroups().size() > 0);
    }

    @Test
    void setupRandomGeneratedGroupListTest() {
        groupList1.setupRandomGeneratedGroupList(allPairs);
        assertNotNull(groupList1.getAllGroups());
        assertTrue(groupList1.getAllGroups().size() > 0);
    }

    @Test
    void calculateFitnessTest() {
        groupList1.calculateFitness();
        assertTrue(groupList1.getFitness() < 0);
    }

    @Test
    void printEachClusterFitnessTest() {
        // Fange den groupList1.printEachClusterFitness() in ein String
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        groupList1.printEachClusterFitness();

        System.setOut(originalOut);

        // Konvertiere den OutputStream in einen String
        String output = outputStream.toString().trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Erwartete Ausgabe
        String expectedOutput = groupList1.getGroupClusterList().get(0).getFitness()+ "\n" +
                groupList1.getGroupClusterList().get(1).getFitness();

        expectedOutput = expectedOutput.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        // Überprüfe die Ausgabe
        assertEquals(expectedOutput, output);
    }
}