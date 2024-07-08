package Model;

import java.io.Serializable;
import java.util.*;

public class PairList implements Serializable {
    private static final long serialVersionUID = 1L; // Update this serialVersionUID if needed

    private List<Pair> pairList = new ArrayList<>();
    private ParticipantList successorList;
    private HashMap<String, Integer> kitchenCount = new HashMap<>();
    private Location partyLocation;
    private int uniqueId = 1;
    private double ageDifference;
    private double genderDiversity;
    private double preferenceDeviation;

    public PairList(ParticipantList successorList) {
        this.successorList =  successorList;
    }



    public List<Pair> getUnassignedPairs() {
        List<Pair> unassignedPairs = new ArrayList<>();
        for (Pair pair : pairList) {
            if (!pair.isAssignedToGroup()) {
                unassignedPairs.add(pair);
            }
        }
        return unassignedPairs;
    }
    public double calculateAverageAgeDifference() {
        double totalAgeDifference = 0.0;
        int pairCount = 0;
        for (Pair pair : pairList) {
            totalAgeDifference += pair.getAgeDifference();
            pairCount++;
        }
        return (pairCount > 0) ? totalAgeDifference / pairCount : 0.0;
    }

    public double calculateGenderDiversity() {
        int maleCount = 0;
        int femaleCount = 0;
        for (Pair pair : pairList) {
            if (pair.getParticipant_1().getGender() == Gender.MALE) maleCount++;
            if (pair.getParticipant_2().getGender() == Gender.MALE) maleCount++;
            if (pair.getParticipant_1().getGender() == Gender.FEMALE) femaleCount++;
            if (pair.getParticipant_2().getGender() == Gender.FEMALE) femaleCount++;
        }
        return (maleCount + femaleCount > 0) ? (double) Math.min(maleCount, femaleCount) / (maleCount + femaleCount) : 0.0;
    }

    public double calculatePreferenceDeviation() {
        double totalPreferenceDeviation = 0.0;
        int pairCount = 0;
        for (Pair pair : pairList) {
            totalPreferenceDeviation += pair.getPreferenceDeviation();
            pairCount++;
        }
        return (pairCount > 0) ? totalPreferenceDeviation / pairCount : 0.0;
    }
    public void setupKitchenInformationForEachPair() {
        List<Pair> pairsToRemove = new ArrayList<>();

        for(Pair pair : pairList) {
            Participant participant1 = pair.getParticipant_1();
            Participant participant2 = pair.getParticipant_2();
            Location p1KitchenLocation = participant1.getKitchenLocation();
            Location p2KitchenLocation = participant2.getKitchenLocation();
            Kitchen p1Kitchen = participant1.getKitchen();
            Kitchen p2Kitchen = participant2.getKitchen();

            // Verwende Küche 2
            if(((p1Kitchen == null) || (p1Kitchen == Kitchen.MAYBE && p2Kitchen == Kitchen.YES)) && checkForValidKitchenCount(p2KitchenLocation)) {
                setupKitchenInformation(pair, p2KitchenLocation, 2);
                continue;
            }
            // Verwende Küche 1
            else if(((p2Kitchen  == null) || (p2Kitchen == Kitchen.MAYBE && p1Kitchen == Kitchen.YES)) && checkForValidKitchenCount(p1KitchenLocation)) {
                setupKitchenInformation(pair, p1KitchenLocation, 1);
                continue;
            }
            // MAYBE/MAYBE und YES/YES verwende die, die näher an der PartyLocation ist
            else  {
                // Verwende Küche 2,da von p1Küche die Distanz höher ist
                if((p1KitchenLocation.distanceTo(partyLocation) > p2KitchenLocation.distanceTo(partyLocation)) && checkForValidKitchenCount(p2KitchenLocation)) {
                    setupKitchenInformation(pair, p2KitchenLocation, 2);
                    continue;
                } else if(checkForValidKitchenCount(p1KitchenLocation)){
                    setupKitchenInformation(pair, p1KitchenLocation, 1);
                    continue;
                }

            }

            //Pärchen muss aufgelöst werden, weil Küche bereits von 3 anderen Pärchen verwendet wird, und Partner keine Küche bereit stellt.
            successorList.add(participant1);
            successorList.add(participant2);
            pairsToRemove.add(pair);
        }
        pairList.removeAll(pairsToRemove);

    }
    public void removePair(Pair pair) {
        pairList.remove(pair);
        updateKitchenCountOnRemoval(pair);
        calculatePairListQuality();
    }

    private void updateKitchenCountOnRemoval(Pair pair) {
        String kitchenLocationString = pair.getKitchenLocation().toString();
        if (kitchenCount.containsKey(kitchenLocationString)) {
            int count = kitchenCount.get(kitchenLocationString);
            if (count > 0) {
                kitchenCount.put(kitchenLocationString, count - 1);
            }
        }
    }


    private void setupKitchenInformation(Pair pair, Location choosenKitchenLocation, int participantNumber) {
        pair.setKitchenLocation(choosenKitchenLocation);

        boolean participantThatProvidesKitchenBoolean;

        participantThatProvidesKitchenBoolean = participantNumber != 1;

        pair.setParticipantThatProvidesKitchen(participantThatProvidesKitchenBoolean);
        pair.setDistanceToPartyLocation(choosenKitchenLocation.distanceTo(partyLocation));
    }


    /**
     * Prüft, ob die Küche bereits von 3 anderen Pärchen verwendet wird
     * @param kitchenLocation, location zum prüfen
     * @return false,wenn sie bereits von 3 anderen verwendet wird, sonst true
     */
    private boolean checkForValidKitchenCount(Location kitchenLocation)  {
        String kitchenLocationString = kitchenLocation.toString();

        if(kitchenCount.containsKey(kitchenLocationString)) {
            int count =  kitchenCount.get(kitchenLocationString);
            if(count == 3) {
                System.out.println("Küche wird schon von 3 Pärchen verwendet.");
                return false;
            } else {
                count++;
                kitchenCount.put(kitchenLocationString, count);
            }
        } else {
            kitchenCount.put(kitchenLocationString, 1);
        }
        return true;
    }


    public void addPairToList(Pair pair) {
        pair.setPairId(uniqueId);
        this.uniqueId = this.uniqueId +1;
        pairList.add(pair);

        calculatePairListQuality();
    }

    public void removeCancelledParticipants(List<Participant> participantsToRemove) {
        List<Pair> removedPairs = new ArrayList<>();

        for(Participant participantToRemove :  participantsToRemove) {
            Pair affectedPair = findPairOfParticipant(participantToRemove);

            if(affectedPair == null) {
                continue;
            }

            boolean pairIsRegisteredTogether = participantToRemove.isIs_registered_together();

            if(!pairIsRegisteredTogether) {
                pairList.remove(affectedPair);
                kitchenCount.put(affectedPair.getKitchenLocation().toString(), kitchenCount.get(affectedPair.getKitchenLocation().toString()) - 1);
                removedPairs.add(affectedPair);
                Participant participantWithoutPartner = affectedPair.getNewParticipantWithoutPair(participantToRemove); // participantWithoutPartner brauch neuen Partner

                if(!successorList.getParticipantList().isEmpty()) {
                    ParticipantWeightPair newPossiblePartnerFromParticipant = participantWithoutPartner.findBestPossiblePartnerFromSuccessorList(successorList.getParticipantList());

                    if(newPossiblePartnerFromParticipant.getWeight() > 0) {
                        Pair pair =  new Pair(participantWithoutPartner, newPossiblePartnerFromParticipant.getParticipant());
                        setupKitchenForSuccessorPair(pair);
                        addPairToList(pair);
                        successorList.getParticipantList().remove(newPossiblePartnerFromParticipant.getParticipant());
                    } else {
                        successorList.getParticipantList().add(participantWithoutPartner);
                    }
                }
            } else {  // Gemeinsame Anmeldung
                // Prüfen, ob sich beide Teilnehmer der Anmeldung abmelden
                if (participantsToRemove.contains(participantToRemove.getPartnerParticipant())) {
                    // Gemeinsame Abmeldung eines Paares
                    if (!removedPairs.contains(affectedPair)) {
                        removedPairs.add(affectedPair);
                        pairList.remove(affectedPair);
                        kitchenCount.put(affectedPair.getKitchenLocation().toString(), kitchenCount.get(affectedPair.getKitchenLocation().toString()) - 1);

                        if (successorList.getParticipantList().size() > 1) {
                            Pair bestPairFromSuccessorList = buildBestPossiblePairFromSuccessorList();

                            if (bestPairFromSuccessorList != null) {
                                setupKitchenForSuccessorPair(bestPairFromSuccessorList);
                                addPairToList(bestPairFromSuccessorList);
                                successorList.getParticipantList().remove(bestPairFromSuccessorList.getParticipant_1());
                                successorList.getParticipantList().remove(buildBestPossiblePairFromSuccessorList().getParticipant_2());
                            }

                        }
                    }

                } else {
                    // Einzelne Abmeldung einer gemeinsamen Abmeldung
                    pairList.remove(affectedPair);
                    Participant singleParticipant = affectedPair.getNewParticipantWithoutPair(participantToRemove);



                    if(!successorList.getParticipantList().isEmpty()) {
                        ParticipantWeightPair bestParticipantForSingleParticipant = singleParticipant.findBestPossiblePartnerFromSuccessorList(successorList.getParticipantList());

                        if(bestParticipantForSingleParticipant != null && bestParticipantForSingleParticipant.getWeight() > 0) {
                            Pair pair =  new Pair(singleParticipant,bestParticipantForSingleParticipant.getParticipant());
                            successorList.getParticipantList().remove(bestParticipantForSingleParticipant.getParticipant());
                            setupKitchenForSuccessorPair(pair);
                            addPairToList(pair);
                        } else {
                            successorList.getParticipantList().add(singleParticipant);
                        }
                    } else {
                        successorList.getParticipantList().add(singleParticipant);
                    }
                }
            }
        }
    }

    public void setupKitchenForSuccessorPair(Pair pair) {
        Participant participant1 = pair.getParticipant_1();
        Participant participant2 = pair.getParticipant_2();
        Location p1KitchenLocation = participant1.getKitchenLocation();
        Location p2KitchenLocation = participant2.getKitchenLocation();
        Kitchen p1Kitchen = participant1.getKitchen();
        Kitchen p2Kitchen = participant2.getKitchen();

        // Verwende Küche 2
        if(((p1Kitchen == null) || (p1Kitchen == Kitchen.MAYBE && p2Kitchen == Kitchen.YES))) {
            setupKitchenInformation(pair, p2KitchenLocation, 2);
        }
        // Verwende Küche 1
        else if(((p2Kitchen  == null) || (p2Kitchen == Kitchen.MAYBE && p1Kitchen == Kitchen.YES))) {
            setupKitchenInformation(pair, p1KitchenLocation, 1);
        }
        // MAYBE/MAYBE und YES/YES verwende die, die näher an der PartyLocation ist
        else  {
            // Verwende Küche 2,da von p1Küche die Distanz höher ist
            if((p1KitchenLocation.distanceTo(partyLocation) > p2KitchenLocation.distanceTo(partyLocation))) {
                setupKitchenInformation(pair, p2KitchenLocation, 2);
            } else{
                setupKitchenInformation(pair, p1KitchenLocation, 1);
            }

        }
    }



    public Pair buildBestPossiblePairFromSuccessorList() {
        PairWeightPair currentBestPair = null;

        for(Participant successor : successorList.getParticipantList()) {

            for(ParticipantWeightPair possiblePartner : successor.getPreferedPartnerList()) {
                boolean possiblePartnerIsNotPartnered =  !possiblePartner.getParticipant().isPartnered();

                if(currentBestPair == null && possiblePartnerIsNotPartnered) {
                    currentBestPair = new PairWeightPair(new Pair(successor, possiblePartner.getParticipant()), possiblePartner.getWeight());
                    continue;
                }

                if(possiblePartnerIsNotPartnered && possiblePartner.getWeight() > currentBestPair.getFitness()){
                    currentBestPair = new PairWeightPair(new Pair(successor, possiblePartner.getParticipant()), possiblePartner.getWeight());
                }

            }
        }

        assert currentBestPair != null;
        if(currentBestPair.getFitness() < 0) { // Es kann kein Pärchen aus den Nachrückenden erstellt werden
            return null;
        }


        return currentBestPair.getPair();
    }
    public Pair getPairById(int id) {

        for (Pair pair : this.pairList ) {
            if (pair.getPairId() == id) {
                return pair;
            }
        }
        return null; // Return null if no pair is found with the given ID
    }
    public Pair findPairOfParticipant(Participant participant) {

        for(Pair pair : pairList) {
            if(pair.getParticipant_1() == participant || pair.getParticipant_2() == participant) {
                return pair;
            }
        }

        return null;
    }




    private void calculatePairListQuality() {
        int totalAgeDifference = 0;
        double totalGenderDiversity = 0;
        int totalPreferenceDeviation = 0;

        for(Pair pair : pairList) {
            totalGenderDiversity += pair.getGenderDiversity();
            totalAgeDifference += pair.getAgeDifference();
            totalPreferenceDeviation += pair.getPreferenceDeviation();
        }
        this.genderDiversity = totalGenderDiversity / pairList.size();
        this.ageDifference = (double) totalAgeDifference / pairList.size();
        this.preferenceDeviation = (double) totalPreferenceDeviation / pairList.size();
    }

    public void printPairListQuality() {
        System.out.println("Pairlist size: " + pairList.size());
        System.out.println("SuccessorList size: " + successorList.getParticipantList().size());
        System.out.println("Age difference: " + ageDifference);
        System.out.println("Gender diversity: " + genderDiversity);
        System.out.println("Preference Deviation: " + preferenceDeviation);
    }

    public void printPairList() {
        for(Pair pair : pairList) {
            pair.printPair();
            System.out.println("Pair besteht aus:");
            pair.getParticipant_1().printParticipant();
            pair.getParticipant_2().printParticipant();
            System.out.println("------------------------------------------");
        }
    }

    public List<Pair> getPairList() {
        return pairList;
    }

    public void setPartyLocation(Location partyLocation) {
        this.partyLocation = partyLocation;
    }

    public ParticipantList getSuccessorList() {
        return successorList;
    }

    public void setPairList(List<Pair> pairList) {
        this.pairList = pairList;
    }

    public void setSuccessorList(ParticipantList successorList) {
        this.successorList = successorList;
    }
    public void sortByFoodPreference() {
        Collections.sort(pairList, Comparator.comparingInt(pair -> {
            int preferenceSum = pair.getParticipant_1().getFoodPreference().getIntValue() +
                    pair.getParticipant_2().getFoodPreference().getIntValue();
            return -preferenceSum; // Reverse order to show highest ranking first
        }));
    }
    public void sortByCriteria(Criteria criteria) {
        Comparator<Pair> comparator = null;

        switch (criteria) {
            case minimizeLeftoverParticipants:
                comparator = Comparator.comparingInt(Pair::getLeftoverParticipants);
                break;
            case foodPreference:
                comparator = Comparator.comparingDouble(Pair::getFoodPreferenceScore);
                break;
            case genderDiversity:
                comparator = Comparator.comparingDouble(Pair::getGenderDiversity);
                break;
            case ageDifference:
                comparator = Comparator.comparingInt(Pair::getAgeDifference);
                break;
            case minimizePathLength:
                comparator = Comparator.comparingDouble(Pair::getPathLength);
                break;
        }

        if (comparator != null) {
            Collections.sort(pairList, comparator);
        }
    }

    public void sortByGenderDiversity() {
        // Implement sorting by gender diversity logic
    }

    public void sortByAgeDifference() {
        // Implement sorting by age difference logic
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairList that = (PairList) o;
        return Objects.equals(pairList, that.pairList);
    }
}
