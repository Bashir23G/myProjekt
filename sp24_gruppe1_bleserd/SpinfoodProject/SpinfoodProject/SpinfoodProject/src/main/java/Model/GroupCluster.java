package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Ein GruppenCluster besteht aus 9 Pärchen welche genau 9 Gruppen bilden. Innerhalb der groupClusterPairList befindet sich jedes Pärchen des Clusters
 * wobei die Pärchen, welche am unzufriedensten mit ihren Gruppen sind vorne stehen.
 */
public class GroupCluster implements Serializable {
    private List<Pair> groupClusterPairList; // Besteht aus 9 Pärchen, die am schlechtesten in aktuelles Cluster passen stehen vorne
    private List<Group>  finalGroupClusterGroupList = new ArrayList<>(); // Besteht aus 9 Gruppen
    private  List<Pair> hostingStarterDishPairs = new ArrayList<>();
    private List<Pair> hostingMainDishPairs = new ArrayList<>();
    private List<Pair> hostingDessertPairs = new ArrayList<>();

    private int minimizePathLengthMultiplier;
   private  int ageDifferenceMultiplier;
   private int foodPreferenceMultiplier;
   private int genderDiversityMultiplier;
    private final int criteriaMultiplier = 100;
    private int fitness = 0;
    private Location partyLocation;
    private double distanceTraveledByCluster;
    private double avgDistanceTraveledByGroup;

    public GroupCluster(List<Pair> groupClusterPairList, List<Criteria> groupGenerationCriteriaRanking, Location partyLocation) {
        this.groupClusterPairList = groupClusterPairList;
        this.partyLocation = partyLocation;

        groupGenerationCriteriaRanking.remove(Criteria.minimizeLeftoverParticipants);  //  Keine Relevanz hier
        this.minimizePathLengthMultiplier =  (5 - (groupGenerationCriteriaRanking.indexOf(Criteria.minimizePathLength) +1)) * criteriaMultiplier;
        this.ageDifferenceMultiplier = (5 - (groupGenerationCriteriaRanking.indexOf(Criteria.ageDifference) +1)) * criteriaMultiplier;
        this.foodPreferenceMultiplier = (5 - (groupGenerationCriteriaRanking.indexOf(Criteria.foodPreference) +1)) * criteriaMultiplier;
        this.genderDiversityMultiplier = (5 - (groupGenerationCriteriaRanking.indexOf(Criteria.genderDiversity) +1)) * criteriaMultiplier;

        distributeDishesDependingOnDistanceToPartyLocation();
        setupGroups();
        sortPairsByFitness();
    }

    public GroupCluster(GroupCluster otherCluster, List<Pair> groupClusterPairList, int fitness) {
        this.groupClusterPairList = new ArrayList<>(groupClusterPairList);
        this.finalGroupClusterGroupList = new ArrayList<>(otherCluster.finalGroupClusterGroupList);
        this.hostingStarterDishPairs = new ArrayList<>(otherCluster.hostingStarterDishPairs);
        this.hostingMainDishPairs = new ArrayList<>(otherCluster.hostingMainDishPairs);
        this.hostingDessertPairs = new ArrayList<>(otherCluster.hostingDessertPairs);
        this.minimizePathLengthMultiplier = otherCluster.minimizePathLengthMultiplier;
        this.genderDiversityMultiplier = otherCluster.genderDiversityMultiplier;
        this.ageDifferenceMultiplier = otherCluster.ageDifferenceMultiplier;
        this.foodPreferenceMultiplier = otherCluster.foodPreferenceMultiplier;
        this.fitness = fitness;
        this.partyLocation = otherCluster.partyLocation;
        this.distanceTraveledByCluster = otherCluster.distanceTraveledByCluster;
        this.avgDistanceTraveledByGroup = otherCluster.avgDistanceTraveledByGroup;
    }

    public void setupGroups() {
        this.finalGroupClusterGroupList = findBestGroupConstellation(); // Best mögliche Gruppen aus aktuellen Teilnehmern
    }


    /**
     * Prüft jede mögliche Gruppenkonstellation unter einhaltung aller Kriterien
     * @return, die beste Gruppenkonstellation aus den aktuellen Pärchen im Cluster
     */
    public List<Group> findBestGroupConstellation() {
        List<Group> firstUniqueGroupConstellation = firstUniqueGroupConstellation();
        List<Group> secondUniqueGroupConstellation = secondUniqueGroupConstellation();
        List<Group> thirdUniqueGroupConstellation=  thirdUniqueGroupConstellation();
        List<Group> fourthUniqueGroupConstellation=  fourthUniqueGroupConstellation();
        List<Group> fifthUniqueGroupConstellation=  fifthUniqueGroupConstellation();
        List<Group> sixthUniqueGroupConstellation=  sixthUniqueGroupConstellation();
        List<Group> bestConstellation;

        double firstGroupConstellationFitness = calculateFitness(firstUniqueGroupConstellation, ageDifferenceMultiplier, foodPreferenceMultiplier,genderDiversityMultiplier);
        double secondGroupConstellationFitness = calculateFitness(secondUniqueGroupConstellation, ageDifferenceMultiplier, foodPreferenceMultiplier,genderDiversityMultiplier);
        double thirdGroupConstellationFitness = calculateFitness(thirdUniqueGroupConstellation, ageDifferenceMultiplier, foodPreferenceMultiplier,genderDiversityMultiplier);
        double fourthGroupConstellationFitness = calculateFitness(fourthUniqueGroupConstellation, ageDifferenceMultiplier, foodPreferenceMultiplier,genderDiversityMultiplier);
        double fifthGroupConstellationFitness = calculateFitness(fifthUniqueGroupConstellation, ageDifferenceMultiplier, foodPreferenceMultiplier,genderDiversityMultiplier);
        double sixthGroupConstellationFitness = calculateFitness(sixthUniqueGroupConstellation, ageDifferenceMultiplier, foodPreferenceMultiplier,genderDiversityMultiplier);
        double maxFitness = getMax(firstGroupConstellationFitness, secondGroupConstellationFitness, thirdGroupConstellationFitness, fourthGroupConstellationFitness, fifthGroupConstellationFitness, sixthGroupConstellationFitness);


        if(maxFitness == firstGroupConstellationFitness) {
            bestConstellation = firstUniqueGroupConstellation;
            this.fitness = (int)firstGroupConstellationFitness;
        } else if(maxFitness == (int)secondGroupConstellationFitness) {
            bestConstellation  = secondUniqueGroupConstellation;
            this.fitness = (int)secondGroupConstellationFitness;
        } else if(maxFitness == thirdGroupConstellationFitness)  {
            bestConstellation = thirdUniqueGroupConstellation;
            this.fitness = (int)thirdGroupConstellationFitness;
        } else if(maxFitness == fourthGroupConstellationFitness) {
            bestConstellation = fourthUniqueGroupConstellation;
            this.fitness = (int)fourthGroupConstellationFitness;
        } else if(maxFitness == fifthGroupConstellationFitness)  {
            bestConstellation = fifthUniqueGroupConstellation;
            this.fitness = (int)fifthGroupConstellationFitness;
        } else {
            this.fitness = (int)sixthGroupConstellationFitness;
            bestConstellation = sixthUniqueGroupConstellation;
        }

        // Von der besten Gruppe dann noch die Gesamtestrecke miteinberechnen in Fitness
        double distance = calculateMaxDistanceTraveledByGroupCluster(bestConstellation);
        this.distanceTraveledByCluster =  distance;
        this.avgDistanceTraveledByGroup  = distance / 9;
        this.fitness -=  (int) ((distance  / 50)  * minimizePathLengthMultiplier);

        return bestConstellation;
    }



    /**
     * Sortiert die Pärchen nach Fitness, damit die Pärchen die am schlechtesten ins Cluster passen vorne stehen und diese
     * beim vertauschen zwischen Cluster Priorität haben
     */
    public void sortPairsByFitness() {
        List<PairWeightPair> sortedPairsByFitness = new ArrayList<>();
        List<Pair> sortedPairList = new ArrayList<>();

        for(Pair pair : groupClusterPairList) {
            double fitness = calculatePairFitness(pair);
            sortedPairsByFitness.add(new PairWeightPair(pair, fitness));
        }
        sortedPairsByFitness.sort(Comparator.comparingDouble(PairWeightPair::getFitness));

        for(PairWeightPair pairWeightPair : sortedPairsByFitness) {
            sortedPairList.add(pairWeightPair.getPair());
        }

        this.groupClusterPairList =  sortedPairList;

    }

    /**
     * Berechnet für jedes Pärchen eine Fitness, welche repräsentiert wie gut sie in das aktuelle Cluster passen, dabei
     * wird das Pärchen mit allen 6 Pärchen welches es trifft verglichen
     * @param pair, Pärchen dessen Fitness bestimmt werden soll
     * @return Fitness des Pärchens im Cluster
     */
    private  double calculatePairFitness(Pair pair)  {
        List<Group> starterGroups = finalGroupClusterGroupList.subList(0,3);
        List<Group> mainGroups = finalGroupClusterGroupList.subList(3, 6);
        List<Group> dessertGroups = finalGroupClusterGroupList.subList(6,9);
        double pairFitness  = 0;
        double distanceTraveledByPair = 0;

        // Finde jede Gruppe in der sich das Paar befindet

        for(Group starterGroup : starterGroups) {
            if(starterGroup.containsPair(pair)) {
                Location starterLocation = starterGroup.getHostingLocation();
                pairFitness += starterGroup.fitnessForPairInGroup(pair, ageDifferenceMultiplier,  foodPreferenceMultiplier, genderDiversityMultiplier);

                for(Group mainGroup : mainGroups) {
                    if(mainGroup.containsPair(pair))   {
                        Location mainLocation = mainGroup.getHostingLocation();
                        distanceTraveledByPair += starterLocation.distanceTo(mainLocation);
                        pairFitness += mainGroup.fitnessForPairInGroup(pair, ageDifferenceMultiplier,  foodPreferenceMultiplier, genderDiversityMultiplier);

                        for(Group dessertGroup : dessertGroups) {

                            if(dessertGroup.containsPair(pair)) {
                                Location dessertLocation = dessertGroup.getHostingLocation();
                                distanceTraveledByPair += mainLocation.distanceTo(dessertLocation);
                                distanceTraveledByPair += dessertLocation.distanceTo(partyLocation);
                                pairFitness += dessertGroup.fitnessForPairInGroup(pair, ageDifferenceMultiplier,  foodPreferenceMultiplier, genderDiversityMultiplier);
                                pairFitness -= distanceTraveledByPair * minimizePathLengthMultiplier;
                                break;
                            }
                        }
                        break;
                    }

                }
                return pairFitness;
            }
        }
        return 0;
    }


    /**
     *
     * @param uniqueGroup, berechnet Fitness für jede Gruppenkonstellation
     * @param ageDifferenceMultiplier
     * @param foodPreferenceMultiplier
     * @param genderDiversityMultiplier
     * @return Fitness für die Gruppenkonstellation
     */
    private double calculateFitness(List<Group> uniqueGroup, int ageDifferenceMultiplier, int foodPreferenceMultiplier, int genderDiversityMultiplier) {
        double fitness = 0;

        fitness += calculateAgeDifferenceFitness(uniqueGroup, ageDifferenceMultiplier);
        fitness += calculateGenderDiversityFitness(uniqueGroup, genderDiversityMultiplier);
        fitness += calculateFoodPreferenceFitness(uniqueGroup, foodPreferenceMultiplier);

        return fitness;
    }


    /**
     *
     * @param groups, alle Gruppen der besten Gruppenkonstellation
     * @return die Gesamtdistanz aller Pärchen welche das Cluster zurücklegt
     */
    private double calculateMaxDistanceTraveledByGroupCluster(List<Group> groups) {
        List<Group> starterGroups = groups.subList(0,3);
        List<Group> mainGroups = groups.subList(3, 6);
        List<Group> dessertGroups = groups.subList(6,9);
        double distance = 0;

        //Vorspeiße -> Hauptgericht -> Dessert -> PartyLocation gesamtDistanz aller Pärchen
        for(Group starterGroup : starterGroups) {
            Location starterHostLocation = starterGroup.getHostingLocation();
            for (Pair starterPair : starterGroup.getPairs()) {
                for (Group mainGroup : mainGroups) {
                    if (mainGroup.containsPair(starterPair)) {
                        Location mainGroupHostLocation = mainGroup.getHostingLocation();
                        distance += starterHostLocation.distanceTo(mainGroupHostLocation);
                        for(Group dessertGroup : dessertGroups) {

                            if (dessertGroup.containsPair(starterPair)) {
                                Location dessertGroupLocation = dessertGroup.getHostingLocation();
                                distance += starterHostLocation.distanceTo(dessertGroupLocation);
                                // Dessert -> PartyLocation, für alle gleich ab hier
                                distance += starterHostLocation.distanceTo(partyLocation);
                            }

                    }
                }
            }
        }}



        return distance;
    }


    /**
     * Einteilung in Vorspeiße, Hauptgang und Dessert, abhängig von Distanz zur Partylocation.
     * 3 weitesten entfernten Pärchen hosten Vorspeise, 3 mittlere Entfernung hosten Hauptgang und 3 nähsten hosten Desserts
     */
    public void distributeDishesDependingOnDistanceToPartyLocation() {
        groupClusterPairList.sort(Comparator.comparingDouble(Pair::getDistanceToPartyLocation).reversed());

        for (int i = 0; i < groupClusterPairList.size(); i++) {
            if (i < 3) {
                hostingStarterDishPairs.add(groupClusterPairList.get(i)); // 3 nähsten hosten Desserts
            } else if (i < 6) {
                hostingMainDishPairs.add(groupClusterPairList.get(i)); // 3 mittlere Entfernung hosten Hauptgang
            } else {
                hostingDessertPairs.add(groupClusterPairList.get(i)); // 3 weitesten entfernten hosten Vorspeise
            }
        }
    }


    /**
     * Berechnet die Fitness des Clusters
     * @param groups, Gruppen des Clusters
     * @param ageDifferenceMultiplier
     * @return Fitnessbewertung des Alterunterschieds der Gruppe
     */
    private double calculateAgeDifferenceFitness(List<Group> groups, int ageDifferenceMultiplier) {
        double ageDifferenceOverallGroups = 0;

        for(Group group : groups) {
            ageDifferenceOverallGroups +=  group.calculateAgeDifference();
        }

        return ageDifferenceMultiplier - (5 * ageDifferenceOverallGroups);
    }


    /**
     *
     * @param groups, Gruppen des Clusters
     * @param genderDiversityMultiplier
     * @return Fitnessbewertung der Genderdiversität der Gruppe
     */
    private double calculateGenderDiversityFitness(List<Group> groups, int genderDiversityMultiplier) {
        double genderDiversityOverallGroups = 0;

        for(Group group : groups) {
            genderDiversityOverallGroups += group.calculateGenderDiversity();
        }
        double averageGenderDiversity = genderDiversityOverallGroups / groups.size();

        double idealBalance = 0.5;
        double difference = Math.abs(averageGenderDiversity - idealBalance);

        return (1.0 - difference) * genderDiversityMultiplier;
    }

    /**
     *
     * @param groups, Gruppen des Clusters
     * @param foodPreferenceMultiplier
     * @return Fitnessbewertung der Essenspreferenzen der Gruppe
     */
    private double calculateFoodPreferenceFitness(List<Group> groups,  int foodPreferenceMultiplier ) {
        int totalDifference = 0;

        for(Group group : groups) {
            int meatPreferencePairs = countFoodPreferenceTypes(group.getPairs(), FoodPreference.MEAT);
            int anyPreferencePairs = countFoodPreferenceTypes(group.getPairs(), FoodPreference.ANY);
            // Verboten
            if((meatPreferencePairs + anyPreferencePairs) == 2) {
                return -10000;
            } else  {
                totalDifference =  calculateTotalPreferenceDifference(group.getPairs().get(0), group.getPairs().get(1),group.getPairs().get(2));
            }
        }

        return foodPreferenceMultiplier - (totalDifference *  5);
    }


    public int calculateTotalPreferenceDifference(Pair pair1, Pair pair2, Pair pair3) {
        int preference1 = pair1.getFoodPreference().getIntValue();
        int preference2 = pair2.getFoodPreference().getIntValue();
        int preference3 = pair3.getFoodPreference().getIntValue();

        int diff1 = Math.abs(preference1 - preference2);
        int diff2 = Math.abs(preference1 - preference3);
        int diff3 = Math.abs(preference2 - preference3);

        return diff1 + diff2 + diff3;
    }




    public int countFoodPreferenceTypes(List<Pair> pairs, FoodPreference foodPreference) {
        int count = 0;
        for(Pair pair : pairs) {
            if(pair.getFoodPreference() == foodPreference) {
                count++;
            }
        }
        return count;
    }


    /**
     *
     * @param values, beliebige double Werte
     * @return höchstes value der übergebenen double Werte
     */
    private double getMax(double... values) {
        double max = -10000;
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * Pärchen geordnet nach Hostingdish in Hashmap einfügen
     * @return
     */
    public  HashMap<Character, Pair> extractAllPairs() {
        HashMap<Character, Pair> pairs = new HashMap<>();
        pairs.put('a', hostingStarterDishPairs.get(0));
        pairs.put('b', hostingStarterDishPairs.get(1));
        pairs.put('c', hostingStarterDishPairs.get(2));
        pairs.put('d', hostingMainDishPairs.get(0));
        pairs.put('e', hostingMainDishPairs.get(1));
        pairs.put('f', hostingMainDishPairs.get(2));
        pairs.put('g', hostingDessertPairs.get(0));
        pairs.put('h', hostingDessertPairs.get(1));
        pairs.put('i', hostingDessertPairs.get(2));

        return pairs;
    }


    /**
     * Erklärung der Konstellationen:
     * - Jeweilige Hosts des selben Gerichts treffen sich nie
     * - Hosts laufen dafür den selben "Weg", der "Weg" wird durch Distanz zur Partylocation bestimmt
     * - Ist in der Konstellation eine unerlaubte Gruppe wird diese Gruppe stark negativ bewertet und wird nicht ausgewählt
     */

    public List<Group> firstUniqueGroupConstellation() {
        HashMap<Character, Pair> allPairs = extractAllPairs();
        List<Group> uniqueGroups = new ArrayList<>();
        uniqueGroups.add(new Group(allPairs.get('a'), allPairs.get('f'), allPairs.get('i'), Dish.STARTER, allPairs.get('a')));
        uniqueGroups.add(new Group(allPairs.get('b'), allPairs.get('d'), allPairs.get('g'), Dish.STARTER, allPairs.get('b')));
        uniqueGroups.add(new Group(allPairs.get('c'), allPairs.get('e'), allPairs.get('h'), Dish.STARTER, allPairs.get('c')));

        uniqueGroups.add(new Group(allPairs.get('d'), allPairs.get('a'), allPairs.get('h'), Dish.MAIN, allPairs.get('d')));
        uniqueGroups.add(new Group(allPairs.get('e'), allPairs.get('b'), allPairs.get('i'), Dish.MAIN, allPairs.get('e')));
        uniqueGroups.add(new Group(allPairs.get('f'), allPairs.get('c'), allPairs.get('g'), Dish.MAIN, allPairs.get('f')));

        uniqueGroups.add(new Group(allPairs.get('g'), allPairs.get('a'), allPairs.get('e'), Dish.DESSERT, allPairs.get('g')));
        uniqueGroups.add(new Group(allPairs.get('h'), allPairs.get('b'), allPairs.get('f'), Dish.DESSERT, allPairs.get('h')));
        uniqueGroups.add(new Group(allPairs.get('i'), allPairs.get('c'), allPairs.get('d'), Dish.DESSERT, allPairs.get('i')));

        return uniqueGroups;
    }

    public List<Group> secondUniqueGroupConstellation() {
        HashMap<Character, Pair> allPairs = extractAllPairs();
        List<Group> uniqueGroups = new ArrayList<>();
        uniqueGroups.add(new Group(allPairs.get('a'), allPairs.get('e'), allPairs.get('h'), Dish.STARTER, allPairs.get('a')));
        uniqueGroups.add(new Group(allPairs.get('b'), allPairs.get('f'), allPairs.get('i'), Dish.STARTER, allPairs.get('b')));
        uniqueGroups.add(new Group(allPairs.get('c'), allPairs.get('d'), allPairs.get('g'), Dish.STARTER, allPairs.get('c')));

        uniqueGroups.add(new Group(allPairs.get('d'), allPairs.get('a'), allPairs.get('i'), Dish.MAIN, allPairs.get('d')));
        uniqueGroups.add(new Group(allPairs.get('e'), allPairs.get('b'), allPairs.get('g'), Dish.MAIN, allPairs.get('e')));
        uniqueGroups.add(new Group(allPairs.get('f'), allPairs.get('c'), allPairs.get('h'), Dish.MAIN, allPairs.get('f')));

        uniqueGroups.add(new Group(allPairs.get('g'), allPairs.get('a'), allPairs.get('f'), Dish.DESSERT, allPairs.get('g')));
        uniqueGroups.add(new Group(allPairs.get('h'), allPairs.get('b'), allPairs.get('d'), Dish.DESSERT, allPairs.get('h')));
        uniqueGroups.add(new Group(allPairs.get('i'), allPairs.get('c'), allPairs.get('e'), Dish.DESSERT, allPairs.get('i')));

        return uniqueGroups;
    }

    public List<Group> thirdUniqueGroupConstellation() {
        HashMap<Character, Pair> allPairs = extractAllPairs();
        List<Group> uniqueGroups = new ArrayList<>();
        uniqueGroups.add(new Group(allPairs.get('a'), allPairs.get('d'), allPairs.get('h'), Dish.STARTER, allPairs.get('a')));
        uniqueGroups.add(new Group(allPairs.get('b'), allPairs.get('e'), allPairs.get('i'), Dish.STARTER, allPairs.get('b')));
        uniqueGroups.add(new Group(allPairs.get('c'), allPairs.get('f'), allPairs.get('g'), Dish.STARTER, allPairs.get('c')));

        uniqueGroups.add(new Group(allPairs.get('d'), allPairs.get('c'), allPairs.get('i'), Dish.MAIN, allPairs.get('d')));
        uniqueGroups.add(new Group(allPairs.get('e'), allPairs.get('a'), allPairs.get('g'), Dish.MAIN, allPairs.get('e')));
        uniqueGroups.add(new Group(allPairs.get('f'), allPairs.get('b'), allPairs.get('h'), Dish.MAIN, allPairs.get('f')));

        uniqueGroups.add(new Group(allPairs.get('g'), allPairs.get('b'), allPairs.get('d'), Dish.DESSERT, allPairs.get('g')));
        uniqueGroups.add(new Group(allPairs.get('h'), allPairs.get('c'), allPairs.get('e'), Dish.DESSERT, allPairs.get('h')));
        uniqueGroups.add(new Group(allPairs.get('i'), allPairs.get('a'), allPairs.get('f'), Dish.DESSERT, allPairs.get('i')));

        return uniqueGroups;
    }


    public List<Group> fourthUniqueGroupConstellation() {
        HashMap<Character, Pair> allPairs = extractAllPairs();
        List<Group> uniqueGroups = new ArrayList<>();
        uniqueGroups.add(new Group(allPairs.get('a'), allPairs.get('f'), allPairs.get('g'), Dish.STARTER, allPairs.get('a')));
        uniqueGroups.add(new Group(allPairs.get('b'), allPairs.get('d'), allPairs.get('h'), Dish.STARTER, allPairs.get('b')));
        uniqueGroups.add(new Group(allPairs.get('c'), allPairs.get('e'), allPairs.get('i'), Dish.STARTER, allPairs.get('c')));

        uniqueGroups.add(new Group(allPairs.get('d'), allPairs.get('c'), allPairs.get('g'), Dish.MAIN, allPairs.get('d')));
        uniqueGroups.add(new Group(allPairs.get('e'), allPairs.get('a'), allPairs.get('h'), Dish.MAIN, allPairs.get('e')));
        uniqueGroups.add(new Group(allPairs.get('f'), allPairs.get('b'), allPairs.get('i'), Dish.MAIN, allPairs.get('f')));

        uniqueGroups.add(new Group(allPairs.get('g'), allPairs.get('b'), allPairs.get('e'), Dish.DESSERT, allPairs.get('g')));
        uniqueGroups.add(new Group(allPairs.get('h'), allPairs.get('c'), allPairs.get('f'), Dish.DESSERT, allPairs.get('h')));
        uniqueGroups.add(new Group(allPairs.get('i'), allPairs.get('a'), allPairs.get('d'), Dish.DESSERT, allPairs.get('i')));

        return uniqueGroups;
    }

    public List<Group> fifthUniqueGroupConstellation() {
        HashMap<Character, Pair> allPairs = extractAllPairs();
        List<Group> uniqueGroups = new ArrayList<>();
        uniqueGroups.add(new Group(allPairs.get('a'), allPairs.get('e'), allPairs.get('g'), Dish.STARTER, allPairs.get('a')));
        uniqueGroups.add(new Group(allPairs.get('b'), allPairs.get('f'), allPairs.get('h'), Dish.STARTER, allPairs.get('b')));
        uniqueGroups.add(new Group(allPairs.get('c'), allPairs.get('d'), allPairs.get('i'), Dish.STARTER, allPairs.get('c')));

        uniqueGroups.add(new Group(allPairs.get('d'), allPairs.get('b'), allPairs.get('g'), Dish.MAIN, allPairs.get('d')));
        uniqueGroups.add(new Group(allPairs.get('e'), allPairs.get('c'), allPairs.get('h'), Dish.MAIN, allPairs.get('e')));
        uniqueGroups.add(new Group(allPairs.get('f'), allPairs.get('a'), allPairs.get('i'), Dish.MAIN, allPairs.get('f')));

        uniqueGroups.add(new Group(allPairs.get('g'), allPairs.get('c'), allPairs.get('f'), Dish.DESSERT, allPairs.get('g')));
        uniqueGroups.add(new Group(allPairs.get('h'), allPairs.get('a'), allPairs.get('d'), Dish.DESSERT, allPairs.get('h')));
        uniqueGroups.add(new Group(allPairs.get('i'), allPairs.get('b'), allPairs.get('e'), Dish.DESSERT, allPairs.get('i')));

        return uniqueGroups;
    }

    public List<Group> sixthUniqueGroupConstellation() {
        HashMap<Character, Pair> allPairs = extractAllPairs();
        List<Group> uniqueGroups = new ArrayList<>();
        uniqueGroups.add(new Group(allPairs.get('a'), allPairs.get('d'), allPairs.get('i'), Dish.STARTER, allPairs.get('a')));
        uniqueGroups.add(new Group(allPairs.get('b'), allPairs.get('e'), allPairs.get('g'), Dish.STARTER, allPairs.get('b')));
        uniqueGroups.add(new Group(allPairs.get('c'), allPairs.get('f'), allPairs.get('h'), Dish.STARTER, allPairs.get('c')));

        uniqueGroups.add(new Group(allPairs.get('d'), allPairs.get('b'), allPairs.get('h'), Dish.MAIN, allPairs.get('d')));
        uniqueGroups.add(new Group(allPairs.get('e'), allPairs.get('c'), allPairs.get('i'), Dish.MAIN, allPairs.get('e')));
        uniqueGroups.add(new Group(allPairs.get('f'), allPairs.get('a'), allPairs.get('g'), Dish.MAIN, allPairs.get('f')));

        uniqueGroups.add(new Group(allPairs.get('g'), allPairs.get('c'), allPairs.get('d'), Dish.DESSERT, allPairs.get('g')));
        uniqueGroups.add(new Group(allPairs.get('h'), allPairs.get('a'), allPairs.get('e'), Dish.DESSERT, allPairs.get('h')));
        uniqueGroups.add(new Group(allPairs.get('i'), allPairs.get('b'), allPairs.get('f'), Dish.DESSERT, allPairs.get('i')));

        return uniqueGroups;
    }


    public void printCluster() {
        System.out.println("Vorspeisen Gruppen:");
        finalGroupClusterGroupList.get(0).printGroup();
        finalGroupClusterGroupList.get(1).printGroup();
        finalGroupClusterGroupList.get(2).printGroup();
        System.out.println("Hauptspeißen Gruppen: ");
        finalGroupClusterGroupList.get(3).printGroup();
        finalGroupClusterGroupList.get(4).printGroup();
        finalGroupClusterGroupList.get(5).printGroup();
        System.out.println("Dessert Gruppen: ");
        finalGroupClusterGroupList.get(6).printGroup();
        finalGroupClusterGroupList.get(7).printGroup();
        finalGroupClusterGroupList.get(8).printGroup();
        }



    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public List<Pair> getGroupClusterPairList() {
        return groupClusterPairList;
    }

    public int getFitness() {
        return fitness;
    }

    public double getAvgDistanceTraveledByGroup() {
        return avgDistanceTraveledByGroup;
    }

    public List<Group> getFinalGroupClusterGroupList() {
        return finalGroupClusterGroupList;
    }

    public List<Pair> getHostingStarterDishPairs() {
        return hostingStarterDishPairs;
    }
    public List<Pair> getHostingMainDishPairs() {
        return hostingMainDishPairs;
    }
    public List<Pair> getHostingDessertPairs() {
        return hostingDessertPairs;
    }

}
