package Model;

import java.io.Serializable;
import java.util.*;

public class GroupList implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Group> allGroups  =  new ArrayList<>();
    private List<GroupCluster> groupClusterList = new ArrayList<>();
    private List<Pair> successorPairList = new ArrayList<>();
    private Location partyLocation;
    private List<Criteria> criteriaRankingList;

    private double fitness = 0;


    public GroupList(GroupList groupList){
        this.partyLocation = groupList.getPartyLocation();
        this.successorPairList = new ArrayList<>(groupList.getSuccessorPairList());
        this.fitness = 0;
        this.groupClusterList = new ArrayList<>(groupList.getGroupClusterList());
        this.criteriaRankingList = groupList.getCriteriaRankingList();
    }

    public GroupList(List<Pair> allPairs, Location partyLocation, List<Criteria> criteriaRankingList) {
        this.partyLocation =  partyLocation;
        this.criteriaRankingList = criteriaRankingList;
        setupRandomGeneratedGroupList(allPairs);
        calculateFitness();
    }

    public void printGroupListQuality() {
        System.out.println("Gruppen in der Liste: " + allGroups.size());
        System.out.println("Anzahl nachrückender Pärchen: " + successorPairList.size());
        System.out.println("Genderdiversität: " + calculateGenderDiversity());
        System.out.println("Altersdifferenz: " + calculateAgeDifference());
        System.out.println("Vorliebenabweichung: " + calculatePreferenceDeviation());
        System.out.println("Mittelwert der Weglänge für jedes Paar: " + calculateAverageTravelDistanceForPair());
        System.out.println("Standardabweichung der Weglängen der Paare: " + calculateStandardDeviationForPair());
    }


    /**
     * Entfernt das schlechteste Cluster, bis die Größe aller Gruppen geringer ist als die maximalanzahl
     * @param maxAllowedGroups
     */
    public void removeGroupsOverMaxAllowedLimit(int maxAllowedGroups) {
        while(allGroups.size() > maxAllowedGroups) {
            GroupCluster worstCluster = groupClusterList.get(0);
            groupClusterList.remove(worstCluster);

            for(Group groupToRemove : worstCluster.getFinalGroupClusterGroupList()) {
                allGroups.remove(groupToRemove);

                for(Pair pairToRemove : groupToRemove.getPairs()) {

                    if(!(successorPairList.contains(pairToRemove))) {
                        successorPairList.add(pairToRemove);
                    }

                }

            }


        }


    }


    /**
     * Entfernt das schlechteste Cluster der GruppenListe, fügt alle Pärchen in dem Cluster in die Nachrückendenliste ein
     */
    public void removeWorstCluster() {
        GroupCluster worstCluster =  groupClusterList.get(0);
        groupClusterList.remove(worstCluster);
        successorPairList.addAll(worstCluster.getGroupClusterPairList());

    }

    private double calculateAverageTravelDistanceForPair() {
        double completeDistance = 0;

        for(GroupCluster cluster : groupClusterList) {
            completeDistance += cluster.getAvgDistanceTraveledByGroup();
        }
        return completeDistance / groupClusterList.size();
    }

    private double calculateStandardDeviationForPair() {
        double average = calculateAverageTravelDistanceForPair();
        double varianceSum = 0;

        for (GroupCluster cluster : groupClusterList) {
            double distance = cluster.getAvgDistanceTraveledByGroup();
            varianceSum += Math.pow(distance - average, 2);
        }

        double variance = varianceSum / groupClusterList.size();
        return Math.sqrt(variance);
    }

    private double calculateGenderDiversity()  {
        double allGroupGenderDiversitys = 0;
        for(Group group : allGroups) {
            allGroupGenderDiversitys+=  group.calculateGenderDiversity();
        }

        return allGroupGenderDiversitys / allGroups.size();
    }

    private double calculateAgeDifference(){
        double allGroupAgeDifference = 0;

        for(Group group : allGroups) {
            allGroupAgeDifference += group.calculateAgeDifference();
        }

        return allGroupAgeDifference / allGroups.size();
    }


    public void removePairFromGroup(Group group, Pair pairToRemove) {
        for (Group existingGroup : allGroups) {
            if (existingGroup.equals(group)) {
                existingGroup.removePair(pairToRemove); // Assuming Group class has a method to remove pair
                break;
            }
        }
    }
    public void addPairToGroup(Group group, Pair pairToAdd) {
        for (Group existingGroup : allGroups) {
            if (existingGroup.equals(group)) {
                existingGroup.addPair(pairToAdd); // Assuming Group class has a method to add pair
                break;
            }
        }
    }


    private double calculatePreferenceDeviation() {
        double allGroupPreferenceDeviation = 0;

        for(Group group : allGroups) {
            allGroupPreferenceDeviation += group.calculatePreferenceDeviation();
        }

        return allGroupPreferenceDeviation / allGroups.size();
    }


    /**
     * Setzt UniqueIDs für jede Gruppe und setzt in jedem Pärchen das hostingDish
     */
    public void finalizeGroupList() {
        int uniqueId = 0;
        allGroups = new ArrayList<>();

        // Setzen der uniqueIds für jede Gruppe
        for (GroupCluster groupCluster : groupClusterList) {

            for(Group group : groupCluster.getFinalGroupClusterGroupList() ) {
                uniqueId++;
                group.setId(uniqueId);
                allGroups.add(group);
            }

        }

        // Jedes Pärchen erhält die GruppenIds in denen es sich befindet
        for(Group group : allGroups) {
            for(Pair pair : group.getPairs()) {
                if(group.getDish() == Dish.STARTER) {
                    pair.setStarterGroupId(group.getId());

                    if(pair.getPairId() ==  group.getHostingPair().getPairId()) {
                        pair.setHostingDish(Dish.STARTER);
                    }

                } else if(group.getDish() == Dish.MAIN) {
                    pair.setMainGroupId(group.getId());
                    if(pair.getPairId() ==  group.getHostingPair().getPairId()) {
                        pair.setHostingDish(Dish.MAIN);
                    }
                } else {
                    pair.setDessertGroupId(group.getId());
                    if(pair.getPairId() ==  group.getHostingPair().getPairId()) {
                        pair.setHostingDish(Dish.DESSERT);
                    }
                }
            }
        }
    }

    /**
     * Vertauscht zwei Pärchen zwischen Clustern
     * @param from, Ausgangscluster
     * @param to, ZielCluster
     * @param pair1, pärchen aus Ausgangscluster
     * @param pair2, pärchen aus Zielcluster
     */
    public void swapPairsBetweenCluster(GroupCluster from, GroupCluster to, Pair pair1, Pair pair2) {
        List<Pair> groupsFromCluster = new ArrayList<>(from.getGroupClusterPairList());
        List<Pair> groupsToCluster = new ArrayList<>(to.getGroupClusterPairList());

        // Entferne pair1 aus from und füge pair2 hinzu
        groupsFromCluster.remove(pair1);
        groupsFromCluster.add(pair2);

        // Entferne pair2 aus to und füge pair1 hinzu
        groupsToCluster.remove(pair2);
        groupsToCluster.add(pair1);

        // Erstelle die aktualisierten Cluster und füge sie der Liste hinzu
        GroupCluster updatedFromCluster = new GroupCluster(groupsFromCluster, criteriaRankingList, partyLocation);
        GroupCluster updatedToCluster = new GroupCluster(groupsToCluster, criteriaRankingList, partyLocation);

        // Aktualisiere die entsprechenden Cluster in der groupClusterList
        groupClusterList.remove(from);
        groupClusterList.remove(to);
        groupClusterList.add(updatedToCluster);
        groupClusterList.add(updatedFromCluster);
        calculateFitness();
    }

    /**
     * Tauscht Pärchen aus der Successorliste mit Pärchen aus einem Cluster
     * @param successor, Pärchen aus Nachrückerliste
     * @param pairToRemove, Pärchen aus Clusterr
     * @param to, Zielcluster
     */
    public void swapPairsBetweenClusterAndSuccessorList(Pair successor, Pair pairToRemove, GroupCluster to) {
        List<Pair> groupsToCluster = new ArrayList<>(to.getGroupClusterPairList());

        groupsToCluster.remove(pairToRemove);
        groupsToCluster.add(successor);

        this.successorPairList.remove(successor);
        this.successorPairList.add(pairToRemove);

        GroupCluster updatedCluster = new GroupCluster(groupsToCluster, criteriaRankingList, partyLocation);
        groupClusterList.remove(to);
        groupClusterList.add(updatedCluster);

        calculateFitness();
    }


    /**
     * Generiert zufällig aus einer Pärchenliste Gruppenlisten
     * @param allPairs
     */
    public void setupRandomGeneratedGroupList(List<Pair> allPairs) {
        int neededClusters = allPairs.size() / 9;
        List<GroupCluster> allClusters = new ArrayList<>();
        Random random = new Random();
        List<Pair> pairsForCluster = new ArrayList<>();

        for (int i = 0; i < neededClusters; i++) {
            pairsForCluster = new ArrayList<>();

            for (int j = 0; j < 9; j++) {
                int randomIndex = random.nextInt(allPairs.size());
                Pair selectedPair = allPairs.remove(randomIndex);
                pairsForCluster.add(selectedPair);
            }

            // Erstelle einen neuen GroupCluster mit den ausgewählten Paaren
            GroupCluster cluster = new GroupCluster(pairsForCluster, criteriaRankingList, partyLocation);
            allClusters.add(cluster);
        }

        successorPairList.addAll(allPairs);
        allClusters.sort(Comparator.comparingDouble(GroupCluster::getFitness));
        this.groupClusterList = allClusters;
    }

    /**
     * Prüft ob Kitchen zur selben Zeit (In einem Dish mehrmals) benutzt werden
     * @return, true wenn Kitchen mehrmals benutzt wird, sonst false
     */
    public boolean checkIfKitchenIsUsedAtSameTime() {
        HashMap<String, Integer> countKitchenStarter = new HashMap<>();
        HashMap<String, Integer> countKitchenMain = new HashMap<>();
        HashMap<String, Integer> countKitchenDessert = new HashMap<>();

        for(Group group : allGroups) {
            String groupLocation = group.getHostingLocation().toString();


            if(group.getDish() == Dish.STARTER) {

                if(countKitchenStarter.containsKey(groupLocation)) {
                    return true;
                } else {
                    countKitchenStarter.put(groupLocation, 1);
                }
                continue;
            }

            if(group.getDish() == Dish.MAIN) {

                if(countKitchenMain.containsKey(groupLocation)) {
                    return true;
                } else {
                    countKitchenMain.put(groupLocation, 1);
                }
                continue;
            }

            if(group.getDish() == Dish.DESSERT) {

                if(countKitchenDessert.containsKey(groupLocation)) {
                    return true;
                } else {
                    countKitchenDessert.put(groupLocation, 1);
                }
            }

        }
        return false;
    }

    /**
     * Findet die Pärchen, die zur selben Zeit küche verwenden
     */
    public void handleKitchenIsUsedAtSameTime() {
        HashMap<String, List<Pair>> findDoubleUsedKitchenStarter = new HashMap<>();
        HashMap<String, List<Pair>> findDoubleUsedKitchenMain = new HashMap<>();
        HashMap<String, List<Pair>> findDoubleUsedKitchenDessert = new HashMap<>();
        Random random = new Random();

        for(Group group : allGroups) {
            String groupHostingLocation = group.getHostingLocation().toString();

            if(group.getDish() == Dish.STARTER) {
                countKitchenUsesPerDish(findDoubleUsedKitchenStarter, group, groupHostingLocation);
                continue;
            }

            if(group.getDish() == Dish.MAIN) {
                countKitchenUsesPerDish(findDoubleUsedKitchenMain, group, groupHostingLocation);
                continue;
            }

            if(group.getDish() == Dish.DESSERT) {
                countKitchenUsesPerDish(findDoubleUsedKitchenDessert, group, groupHostingLocation);
            }
        }
        handleDoubleUsedKitchens(findDoubleUsedKitchenStarter, random);
        handleDoubleUsedKitchens(findDoubleUsedKitchenMain, random);
        handleDoubleUsedKitchens(findDoubleUsedKitchenDessert, random);

        calculateFitness();
    }

    /**
     * Zählt wie oft eine Küche doppelt verwendet wird
     * @param findDoubleUsedKitchenMain
     * @param group
     * @param groupHostingLocation
     */
    private void countKitchenUsesPerDish(HashMap<String, List<Pair>> findDoubleUsedKitchenMain, Group group, String groupHostingLocation) {
        if(findDoubleUsedKitchenMain.containsKey(groupHostingLocation)) {
            List<Pair> insertPairList = new ArrayList<>(findDoubleUsedKitchenMain.get(groupHostingLocation));
            insertPairList.add(group.getHostingPair());
            findDoubleUsedKitchenMain.put(groupHostingLocation,insertPairList);
        } else {
            List<Pair> pairListForDish = new ArrayList<>();
            pairListForDish.add(group.getHostingPair());
            findDoubleUsedKitchenMain.put(groupHostingLocation, pairListForDish);
        }
    }

    /**
     * Zieht ein Pärchen in dem betroffenen Cluster an den Anfang,damit es von dem genetischen Algorithmus vertauscht wird
     * @param kitchenMap, Map in der jedes Value repräsentiert, wie oft eine Küche verwendet wird
     * @param random, zufälligezahl
     */
    private void handleDoubleUsedKitchens(HashMap<String, List<Pair>> kitchenMap, Random random) {
        for(List<Pair> pairsThatUseKitchenAtSameTime : kitchenMap.values()) {
            if(pairsThatUseKitchenAtSameTime.size() > 1) {
                Pair randomChosenPair = pairsThatUseKitchenAtSameTime.get(random.nextInt(pairsThatUseKitchenAtSameTime.size()));
                findPairInClusterAndMoveToFront(randomChosenPair);
            }
        }
    }

    /**
     * Findet ein Pärche im Cluster und zieht es an den Anfang
     * @param pair
     */
    private void findPairInClusterAndMoveToFront(Pair pair) {

        List<GroupCluster> clusterToRemove = new ArrayList<>();
        List<GroupCluster> newCluster = new ArrayList<>();
        for(GroupCluster currentCluster : groupClusterList) {
            if(currentCluster.getGroupClusterPairList().contains(pair)) {
                clusterToRemove.add(currentCluster);
                List<Pair> clusterPairList = new ArrayList<>(currentCluster.getGroupClusterPairList());
                clusterPairList.remove(pair);
                clusterPairList.add(0, pair);
                newCluster.add(new GroupCluster(currentCluster, clusterPairList, -10000));
            }
        }
        this.groupClusterList.removeAll(clusterToRemove);
        this.groupClusterList.addAll(newCluster);
    }


        public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void calculateFitness()  {
        int tempFitness  = 0;


        groupClusterList.sort(Comparator.comparingInt(GroupCluster::getFitness));
        for(GroupCluster cluster : groupClusterList) {
            tempFitness += cluster.getFitness();
        }
        setFitness(tempFitness);
    }
    public void printEachClusterFitness() {
        for(GroupCluster cluster : groupClusterList) {
            System.out.println(cluster.getFitness());
        }
    }

    public void printAllGroups() {
        int count = 0;

        for (Group group : allGroups) {
            count++;
            System.out.println("Gruppe Nr." + count + " :" );
            group.printGroup();
        }

    }

    public List<Group> getAllGroups() {
        return allGroups;
    }

    public List<GroupCluster> getGroupClusterList() {
        return groupClusterList;
    }

    public List<Pair> getSuccessorPairList() {
        return successorPairList;
    }

    public Location getPartyLocation() {
        return partyLocation;
    }

    public List<Criteria> getCriteriaRankingList() {
        return criteriaRankingList;
    }
    public String getGroupListQuality() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gruppen in der Liste: ").append(allGroups.size()).append("\n");
        sb.append("Anzahl nachrückender Pärchen: ").append(successorPairList.size()).append("\n");
        sb.append("Genderdiversität: ").append(calculateGenderDiversity()).append("\n");
        sb.append("Altersdifferenz: ").append(calculateAgeDifference()).append("\n");
        sb.append("Vorliebenabweichung: ").append(calculatePreferenceDeviation()).append("\n");
        sb.append("Mittelwert der Weglänge für jedes Paar: ").append(calculateAverageTravelDistanceForPair()).append("\n");
        sb.append("Standardabweichung der Weglängen der Paare: ").append(calculateStandardDeviationForPair()).append("\n");
        return sb.toString();
    }

    public double getFitness() {
        return fitness;
    }

    public double getGenderDiversity(){
        return calculateGenderDiversity();
    }
    public double getAgeDifference(){
        return calculateAgeDifference();
    }
    public double getPreferenceDeviation(){
        return calculatePreferenceDeviation();
    }
    public double getAverageTravelDistanceForPair(){
        return calculateAverageTravelDistanceForPair();
    }
    public double getStandardDeviationForPair(){
        return calculateStandardDeviationForPair();
    }
    public void addGroup(Group group) {
        allGroups.add(group);
    }
    public void removeGroup(Group group) {
        allGroups.remove(group);
    }

}
