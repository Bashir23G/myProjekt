package Model;

import java.io.Serializable;
import java.util.*;

public class
GeneticAlgorithm implements Serializable {
    private PairList pairList;
    private List<Criteria> criteriaRankingList;
    private Location partyLocation;


    public GeneticAlgorithm(PairList pairList, List<Criteria> criteriaRankingList, Location partyLocation) {
        this.pairList = pairList;
        this.criteriaRankingList = criteriaRankingList;
        this.partyLocation = partyLocation;
    }

    /**
     * Generiert eine mögliche Gruppenliste, welche alle Anforderungen einhält
     * @return, die beste erstellte Gruppenliste
     */
    public GroupList generateGroupListFromPairList() {
        // Generieren der Starterpopulation (100 Gruppenlisten)
        List<GroupList> starterPopulation = generateStarterPopulation();
        starterPopulation.sort(Comparator.comparingDouble(GroupList::getFitness).reversed());
        // Die 10 besten Individuals werden extrahiert und als Eltern verwendet für neue Generation
        List<GroupList> topTenIndividuals =  new ArrayList<>(generateBestIndividuals(starterPopulation));


        double highestFitness = topTenIndividuals.get(0).getFitness();
        while(highestFitness < 0) {
            removeClusterFromEachIndividual(topTenIndividuals);
            topTenIndividuals = new ArrayList<>(generateBestIndividuals(topTenIndividuals));
            highestFitness = topTenIndividuals.get(0).getFitness();
        }

        topTenIndividuals.get(0).finalizeGroupList();
        return topTenIndividuals.get(0);
    }

    /**
     * Generiert aus einer übergebenen Population durch Mutationen bessere Individuen
     * @param starterPopulation
     * @return Die besten 10 generierten GruppenListen
     */
    public List<GroupList> generateBestIndividuals(List<GroupList> starterPopulation) {
        List<GroupList> bestTenIndividuals =starterPopulation.subList(0, 9);
        List<GroupList> currentPopulation = new ArrayList<>(bestTenIndividuals);
        double highestFitnessEachTenGenerations  = bestTenIndividuals.get(0).getFitness();
        double currentHighestFitness;


        // Algorithmus bricht ab, wenn über 10 Generation die Fitness nicht um mindestens 10 Punkte zugenommen hat, oder 200 Iterationen erreicht wurden
        for(int i = 0; i < 500; i++) {
            for(GroupList parent : bestTenIndividuals) {
                List<GroupList> mutationsResult = mutateParent(parent);
                currentPopulation.addAll(mutationsResult);
                currentPopulation.sort(Comparator.comparingDouble(GroupList::getFitness).reversed());
                parent.getGroupClusterList().sort(Comparator.comparingInt(GroupCluster::getFitness));
            }
            bestTenIndividuals = new ArrayList<>(currentPopulation.subList(0, 10));
            currentPopulation = new ArrayList<>(bestTenIndividuals);
            currentHighestFitness = bestTenIndividuals.get(0).getFitness();

            if(i % 10 == 0) {
                if((currentHighestFitness < highestFitnessEachTenGenerations + 1) && currentHighestFitness > 0) {
                    break;
                } else {
                    highestFitnessEachTenGenerations = currentHighestFitness;
                }
            }
        }
        return bestTenIndividuals;
    }

    /**
     *
     * @param parent, Gruppenliste, die mutiert werden soll
     * @return Eine Liste von 10 neuen Gruppenliste, welche aus modifikation des Parents entstanden sind
     */
    private List<GroupList> mutateParent(GroupList parent)  {
        CustomRandom randomCluster = new CustomRandom(parent.getGroupClusterList().size());
        CustomRandom randomPairInCluster = new CustomRandom(9);
        CustomRandom randomSuccessor = new CustomRandom(parent.getSuccessorPairList().size());
        List<GroupList> newGeneratedLists = new ArrayList<>();

        // 5 Mutationen durch vertauschung zwischen Clustern (Focus auf schwächere Cluster)
        for(int i = 0; i < 5; i++) {
            GroupList generatedList = new GroupList(parent);

            int indexFromCluster = randomCluster.nextInt();
            int indexToCluster = randomCluster.nextInt();

            while(indexFromCluster ==  indexToCluster) {
                indexToCluster = randomCluster.nextInt();
            }
            GroupCluster fromCluster = generatedList.getGroupClusterList().get(indexFromCluster);
            GroupCluster toCluster = generatedList.getGroupClusterList().get(indexToCluster);
            Pair pairFromCluster = fromCluster.getGroupClusterPairList().get(randomPairInCluster.nextInt());
            Pair pairToCluster = toCluster.getGroupClusterPairList().get(randomPairInCluster.nextInt());

            generatedList.swapPairsBetweenCluster(fromCluster, toCluster, pairFromCluster, pairToCluster);
            finalizeListAndAddToGeneration(newGeneratedLists, generatedList);
        }

        // 5 Mutationen durch vertauschen mit Successorlistpairs
        if(!parent.getSuccessorPairList().isEmpty()) {
            for(int i = 0; i < 5; i++) {
                GroupList generatedList = new GroupList(parent);
                int indexFromCluster = randomCluster.nextInt();
                int indexFromSuccessor = randomSuccessor.nextInt();

                GroupCluster fromCluster = generatedList.getGroupClusterList().get(indexFromCluster);
                Pair pairFromCluster = fromCluster.getGroupClusterPairList().get(randomPairInCluster.nextInt());
                Pair successorPair = generatedList.getSuccessorPairList().get(indexFromSuccessor);

                generatedList.swapPairsBetweenClusterAndSuccessorList(successorPair, pairFromCluster, fromCluster);
                finalizeListAndAddToGeneration(newGeneratedLists, generatedList);
            }
        }
        return newGeneratedLists;
    }

    /**
     * Prüft, ob eine korrekte Gruppenliste mit ausreichender Fitness generiert wurde
     * @param newGeneratedLists
     * @param generatedList
     * @return, korrekte Gruppenliste mit ausreichender Fitness
     */
    private void finalizeListAndAddToGeneration(List<GroupList> newGeneratedLists, GroupList generatedList) {
        generatedList.finalizeGroupList();

        if(generatedList.checkIfKitchenIsUsedAtSameTime()) {
            generatedList.handleKitchenIsUsedAtSameTime();
        }
        newGeneratedLists.add(generatedList);

    }

    /**
     *
     * @return, eine Liste mit 100 zufällig generierten GruppenListen
     */
    public List<GroupList> generateStarterPopulation() {
        List<GroupList> starterPopulation = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            List<Pair> copyOfList = new ArrayList<>(pairList.getPairList());
            starterPopulation.add(new GroupList(copyOfList, partyLocation, criteriaRankingList));
        }

        return starterPopulation;
    }

    public void removeClusterFromEachIndividual(List<GroupList> individuals) {

        for(GroupList groupList :  individuals) {
            groupList.removeWorstCluster();
        }

    }

}
