package Model;

import Controller.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DataHandler implements Serializable {
    private static final long serialVersionUID = 1L;
    static  DataHandler instance = new DataHandler();
    private PairList anotherPairList; // Define another PairList for comparison

    private ParticipantList successorParticipants = new ParticipantList();
    private PairList pairList = new PairList(successorParticipants);
    private List<Pair> successorPairList;
    private GroupList groupList;
    private Location partyLocation;
    private CSVReader csvReader = new CSVReader();
    private CSVWriter csvWriter = new CSVWriter();
    private  GeneticAlgorithm geneticAlgorithm;
    private int maxAllowedGroups;
    private List<Criteria> criteriaRankingList;
    private  GaleShapleyAlgorithm galeShapleyAlgorithm = new GaleShapleyAlgorithm();
    private Stack<Pair> undoStack = new Stack<>();
    private Stack<Pair> redoStack = new Stack<>();

    private DataHandler() {}

    public void initialize(String filePathParticipantList, String filePathPartyLocation) {
        csvReader.generateParticipantsAndPairsFromCSV(filePathParticipantList, successorParticipants, pairList);
        partyLocation = csvReader.generatePartyLocationFromCSV(filePathPartyLocation);
        pairList.setPartyLocation(partyLocation);
    }

    public PairList generatePairsFromParticipantList() {
        galeShapleyAlgorithm.generatePairsFromParticipantList(criteriaRankingList, pairList, successorParticipants);
        pairList.setupKitchenInformationForEachPair();
        return pairList;
    }

    public void generateGroupsFromPairList(int maxAllowedGroups) {
        this.maxAllowedGroups = maxAllowedGroups;
        geneticAlgorithm = new GeneticAlgorithm(pairList, criteriaRankingList, partyLocation);
        groupList = geneticAlgorithm.generateGroupListFromPairList();
        groupList.removeGroupsOverMaxAllowedLimit(maxAllowedGroups);
    }

    public void removeCancelledParticipants(List<Participant> participantsToRemove) {
        pairList.removeCancelledParticipants(participantsToRemove);

        if (groupList != null && groupList.getSuccessorPairList().size() >= 3) {
            generateGroupsFromPairList(maxAllowedGroups);
        }
    }

    public void writePairsToCSV(String filename) {
        List<Pair> pairsWithGroup = new ArrayList<>();

        for (GroupCluster groupCluster : groupList.getGroupClusterList()) {
            for (Pair pair : groupCluster.getGroupClusterPairList()) {
                if (!pairsWithGroup.contains(pair)) {
                    pairsWithGroup.add(pair);
                }
            }
        }

        csvWriter.writePairsToCSV(pairsWithGroup, filename);
    }

    public ParticipantList getSuccessorParticipants() {
        return successorParticipants;
    }

    public PairList getPairList() {
        return pairList;
    }

    public Location getPartyLocation() {
        return partyLocation;
    }

    public static DataHandler getInstance() {
        return instance;
    }

    public GroupList getGroupList() {
        return groupList;
    }

    public void setCriteriaRankingList(List<Criteria> criteriaRankingList) {
        this.criteriaRankingList = criteriaRankingList;
    }

    public List<Criteria> getCriteriaRankingList() {
        return criteriaRankingList;
    }
    public void setAnotherPairList(PairList anotherPairList) {
        this.anotherPairList = anotherPairList;
    }
    public void addPair(Pair pair) {
        pairList.addPairToList(pair);
        undoStack.push(pair);
    }

    public void removePair(Pair pair) {
        pairList.removePair(pair);
        undoStack.push(pair);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Pair lastAction = undoStack.pop();
            redoStack.push(lastAction);
            pairList.removePair(lastAction);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Pair lastUndo = redoStack.pop();
            undoStack.push(lastUndo);
            pairList.addPairToList(lastUndo);
        }
    }

    public void backup() {
        csvWriter.writePairsToCSV(pairList.getPairList(), "backup_pair_list.csv");
    }

    public int generateUniquePairId() {
        int maxId = 0;
        for (Pair pair : pairList.getPairList()) {
            if (pair.getPairId() > maxId) {
                maxId = pair.getPairId();
            }
        }
        return maxId + 1;
    }
    public PairList getAnotherPairList() {
        return anotherPairList;
    }
    public List<Pair> getUnassignedPairs() {
        List<Pair> unassignedPairs = new ArrayList<>();
        List<Pair> allPairs = pairList.getPairList(); // Assuming pairList is correctly initialized

        for (Pair pair : allPairs) {
            if (!pair.isAssignedToGroup()) {
                unassignedPairs.add(pair);
            }
        }

        // Debugging output to check unassignedPairs contents
        System.out.println("Unassigned Pairs:");
        for (Pair pair : unassignedPairs) {
            System.out.println(pair.getPairId() + ": " + pair.getParticipant_1().getName() + " & " + pair.getParticipant_2().getName());
        }

        return unassignedPairs;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instance = this; // Ensure singleton instance points to the deserialized object
    }

    private Object readResolve() {
        return instance; // Ensure singleton during deserialization
    }
}
