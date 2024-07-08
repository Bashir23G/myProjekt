package Model;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group implements Serializable {
    private List<Pair> pairs = new ArrayList<>();
    private int id;
    private  Dish dish;
    private Pair hostingPair;
    private static int lastGroupId = 0;

    public Group() {
        this.pairs = new ArrayList<>();
        this.id = ++lastGroupId; // Assign the next ID
        this.dish = Dish.STARTER; // set a default dish
        this.hostingPair = null; // or set a default hosting pair
    }
    public Group(List<Pair> pairs, Dish dish, Pair hostingPair) {
        this.id = ++lastGroupId; // Assign the next ID

        this.pairs = new ArrayList<>(pairs);
        this.dish = dish;
        this.hostingPair = hostingPair;
    }
    public Group(Pair pair1, Pair pair2, Pair pair3, Dish dish, Pair hostingPair) {
        this.pairs.add(pair1);
        this.pairs.add(pair2);
        this.pairs.add(pair3);
        this.dish = dish;
        this.hostingPair = hostingPair;
    }
    public Group(int id, List<Pair> pairs, Dish dish, Pair hostingPair) {
        this.id = id;
        this.pairs.addAll(pairs);
        this.dish = dish;
        this.hostingPair = hostingPair;
        lastGroupId = Math.max(lastGroupId, id); // Update the lastGroupId if necessary

    }
    public Set<Participant> getParticipants() {
        Set<Participant> participants = new HashSet<>();

        for (Pair pair : pairs) {
            participants.add(pair.getParticipant_1());
            participants.add(pair.getParticipant_2());
        }

        return participants;
    }
    public boolean removePair(Pair pairToRemove) {
        return pairs.remove(pairToRemove);
    }
    public void addPair(Pair pair) {
        pairs.add(pair);
    }


    /**
     * Prüft ob pair in Gruppe enthalten sind
     * @param pair, pair welches in Gruppe gesucht werden soll
     * @return, true wenn es enthalten sind, sonst false
     */
    public boolean containsPair(Pair pair) {

        for(Pair groupPair : pairs) {
            if(pair.getPairId() == groupPair.getPairId())  {
                return true;
            }
        }
        return false;
    }

    public double calculateAgeDifference() {
        double pair1AgeRange= pairs.get(0).getAgeRange();
        double pair2AgeRange = pairs.get(1).getAgeRange();
        double pair3AgeRange = pairs.get(2).getAgeRange();

        return (pair1AgeRange + pair2AgeRange + pair3AgeRange) / 3;
    }
    public double calculateGenderDiversity() {
        double pair1Diversity = pairs.get(0).getGenderDiversity();
        double pair2Diversity = pairs.get(1).getGenderDiversity();
        double pair3Diversity = pairs.get(2).getGenderDiversity();

        return (pair1Diversity + pair2Diversity + pair3Diversity) / 3;
    }

    public double calculatePreferenceDeviation() {
        double pair1Preference = pairs.get(0).getPreferenceDeviation();
        double pair2Preference = pairs.get(1).getPreferenceDeviation();
        double pair3Preference = pairs.get(2).getPreferenceDeviation();

        return (double) (Math.abs(pair1Preference - pair2Preference) + Math.abs(pair2Preference - pair3Preference) + Math.abs(pair1Preference - pair3Preference));
    }

    /**
     *
     * @param currentPair, pair von dem die Fitness bestimmt werden soll
     * @return double, welcher repräsentiert wie gut das Paar in die aktuelle Gruppe passt(Foodpreference, Age und GenderDiversität  werden berücksichtigt)
     */
    public double fitnessForPairInGroup(Pair currentPair, int ageDifferenceMultiplier, int foodPreferenceMultiplier, int genderDiversityMultiplier) {
        double fitness = 0;


        for(Pair pair : pairs) {

            if(pair.getPairId() == currentPair.getPairId()) {
                continue;
            }

            fitness += calculateAgeDifferenceBetweenPairs(currentPair, pair,ageDifferenceMultiplier);
            fitness += calculateGenderDiversityBetweenPairs(currentPair, pair, genderDiversityMultiplier);
            fitness += calculatePreferenceDeviationBetweenPairs(currentPair, pair, foodPreferenceMultiplier);
        }
        return fitness;
    }

    private double calculateAgeDifferenceBetweenPairs(Pair pair1,  Pair  pair2, int ageDifferenceMultiplier) {
        int pair1AgeRange=  pair1.getAgeRange();
        int pair2AgeRange = pair2.getAgeRange();
        int ageRangeDifference = Math.abs(pair1AgeRange - pair2AgeRange);

        return switch (ageRangeDifference) {
            case 0, 1 -> 10 * ageDifferenceMultiplier;
            case 2, 3 -> 8 * ageDifferenceMultiplier;
            case 4, 5 -> 5 * ageDifferenceMultiplier;
            default -> ageDifferenceMultiplier;
        };


    }


    private double calculateGenderDiversityBetweenPairs(Pair pair1, Pair pair2, int genderDiversityMultiplier) {
        double averageDiversity = (pair1.getGenderDiversity() + pair2.getGenderDiversity()) / 2;
        double distanceFromOptimal = Math.abs(averageDiversity - 0.5);

        if(distanceFromOptimal < 0.1) {
            return 10 * genderDiversityMultiplier;
        } else if(distanceFromOptimal < 0.2) {
            return 6 * genderDiversityMultiplier;
        } else if(distanceFromOptimal < 0.3) {
            return 3 * genderDiversityMultiplier;
        } else {
            return genderDiversityMultiplier;
        }
    }


    private double calculatePreferenceDeviationBetweenPairs(Pair pair1, Pair pair2, int  foodPreferenceMultiplier) {
        FoodPreference p1Preference = pair1.getFoodPreference();
        FoodPreference p2Preference= pair2.getFoodPreference();

        if((p1Preference == FoodPreference.MEAT && p2Preference == FoodPreference.ANY) ||
                (p1Preference == FoodPreference.ANY && p2Preference == FoodPreference.MEAT)) {
            return 8 * foodPreferenceMultiplier;
        }

        if(p1Preference.name().equals(p2Preference.name()))  {
            return 10 * foodPreferenceMultiplier;
        }

        if((p1Preference == FoodPreference.VEGAN && p2Preference == FoodPreference.VEGGIE) || (p2Preference == FoodPreference.VEGAN && p1Preference == FoodPreference.VEGGIE) ) {
            return 6 * foodPreferenceMultiplier;
        }

        return -1000;
    }


    public void printGroup() {
        System.out.println("Gruppe serviert:  " + dish.name() + " UniqueID: " + id);
        pairs.get(0).printPair();
        pairs.get(1).printPair();
        pairs.get(2).printPair();
    }


    public void setId(int id) {
        this.id = id;
        lastGroupId = Math.max(lastGroupId, id); // Update the lastGroupId if necessary
    }


    public List<Pair> getPairs() {
        return pairs;
    }

    public Dish getDish() {
        return dish;
    }

    public Pair getHostingPair() {
        return hostingPair;
    }

    public int getId() {
        return id;
    }
    public int getNumberOfLeftoverParticipants() {
        return pairs.get(0).getLeftoverParticipants();
    }

    public double getFoodPreferenceScore() {
        return calculatePreferenceDeviation();
    }

    public double getGenderDiversityScore() {
        return calculateGenderDiversity();
    }

    public double getAgeDifferenceScore() {
        return calculateAgeDifference();
    }
    public Location getHostingLocation()  {
      return  hostingPair.getKitchenLocation();
    }
}
