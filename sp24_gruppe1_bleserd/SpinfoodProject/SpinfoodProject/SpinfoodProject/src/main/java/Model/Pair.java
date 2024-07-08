package Model;


import java.io.Serializable;
import java.util.List;

public class Pair implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pairId;
    private Participant participant_1;
    private Participant participant_2;
    private Location kitchenLocation;
    private boolean participantThatProvidesKitchen;
    private double distanceToPartyLocation;
    private FoodPreference foodPreference;
    private int ageDifference;
    private int averageAge;
    private double genderDiversity;
    private int preferenceDeviation;
    private boolean is_registered_together;
    private int starterGroupId;
    private int mainGroupId;
    private int dessertGroupId;
    private Dish hostingDish;
    private int groupId;


    public Pair(Participant participant_1, Participant participant_2) {
        this.participant_1 = participant_1;
        this.participant_2 = participant_2;
        this.ageDifference = calculateAgeDifference();
        this.genderDiversity = calculateGenderDiversity();
        this.preferenceDeviation = calculatePreferenceDeviation();
        this.foodPreference = calculateFoodPreference();
        this.is_registered_together = participant_1.isIs_registered_together();
        this.averageAge = (participant_1.getAge() + participant_2.getAge()) / 2;

    }
    public Pair(int pairId, Participant participant_1, Participant participant_2) {
        this.pairId = pairId;
        this.participant_1 = participant_1;
        this.participant_2 = participant_2;
        this.ageDifference = calculateAgeDifference();
        this.genderDiversity = calculateGenderDiversity();
        this.preferenceDeviation = calculatePreferenceDeviation();
        this.foodPreference = calculateFoodPreference();
        this.is_registered_together = participant_1.isIs_registered_together();
        this.averageAge = (participant_1.getAge() + participant_2.getAge()) / 2;

    }
    public Pair(int pairId, Participant participant_1, Participant participant_2, int groupId) {
        this.pairId = pairId;
        this.participant_1 = participant_1;
        this.participant_2 = participant_2;
        this.groupId = groupId;
        // Initialize other fields
    }


    private double calculateGenderDiversity() {
        Gender gender1 = participant_1.getGender();
        Gender gender2 = participant_2.getGender();

        if ((gender1 == Gender.MALE && gender2 == Gender.FEMALE) ||
                (gender1 == Gender.FEMALE && gender2 == Gender.MALE)) {
            return 0.5;
        } else if ((gender1 == Gender.FEMALE && gender2 == Gender.FEMALE) ||
                (gender1 == Gender.MALE && gender2 == Gender.MALE)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    private int calculatePreferenceDeviation() {
        FoodPreference foodPreference_p1 = participant_1.getFoodPreference();
        FoodPreference foodPreference_p2 = participant_2.getFoodPreference();

        return Math.abs(foodPreference_p1.getIntValue() - foodPreference_p2.getIntValue());
    }

    private int calculateAgeDifference()  {
        int ageRangeParticipant_1 = participant_1.getAgeRange();
        int ageRangeParticipant_2 = participant_2.getAgeRange();
        return ageRangeParticipant_1 >= ageRangeParticipant_2 ? ageRangeParticipant_1 - ageRangeParticipant_2 : ageRangeParticipant_2 - ageRangeParticipant_1 ;
    }

    private FoodPreference calculateFoodPreference()  {
        FoodPreference foodPreference_p1 = participant_1.getFoodPreference();
        FoodPreference foodPreference_p2 = participant_2.getFoodPreference();

        if(foodPreference_p1.name().equals(foodPreference_p2.name())) {
            return foodPreference_p1;
        }

        if(foodPreference_p1 == FoodPreference.ANY) {
            return foodPreference_p2;
        }

        if(foodPreference_p2 == FoodPreference.ANY){
            return foodPreference_p1;
        }


        return FoodPreference.VEGAN;
    }
    public int getLeftoverParticipants() {
        int leftoverParticipants = 0;
        if (participantThatProvidesKitchen) {
            leftoverParticipants = 0; // or some calculation based on participants and kitchen status
        } else {
            leftoverParticipants = 2; // example logic, adjust based on your actual implementation
        }
        return leftoverParticipants;
    }

    public double getFoodPreferenceScore() {
        return foodPreference.getIntValue() - preferenceDeviation;
    }

    public double getPathLength() {
        return distanceToPartyLocation; // Assuming distanceToPartyLocation is the path length
    }
    public void printPair() {
        System.out.println("Id: " + this.pairId + "  STARTERGROUP: " + starterGroupId + " MAINGROUP: " + mainGroupId + " DESSERTGROUP " + dessertGroupId
                + " PAIR HOSTED: " +  this.getHostingDish() + " FoodPreference: " + this.foodPreference.name() + " ageDifference: " + this.ageDifference
                + " genderDiversity: " + this.genderDiversity  + " preferenceDevitation: " +
                this.preferenceDeviation + " Teilnehmer der Küche stellt: " + participantThatProvidesKitchen + " KitchenLocation: " +
                kitchenLocation.getLatitude() + " " + kitchenLocation.getLongitude() + " Etage: " + kitchenLocation.getKitchenStory() + " distanceToPartyLocation: " + distanceToPartyLocation + "km "
                + "isRegisteredTogether: " + this.is_registered_together);
    }
    public boolean isAssignedToGroup() {

        return starterGroupId != 0 || mainGroupId != 0 || dessertGroupId != 0; // Adjust based on your logic
    }
    public void setAssignedToGroup(boolean assigned) {
        if (assigned) {
            this.starterGroupId = 1;  // or any appropriate logic
        } else {
            this.starterGroupId = 0;
            this.mainGroupId = 0;
            this.dessertGroupId = 0;
        }
    }

    public String toString() {
        return participant_1.getName() + " & " + participant_2.getName();
    }
    public Participant getNewParticipantWithoutPair(Participant participant) {

        if(participant == getParticipant_1()) {
            return participant_2;
        } else if(participant == getParticipant_2()) {
            participant_2 = null;
            return participant_1;
        }

        return null;
    }

    public int getAverageAge() {
        return averageAge;
    }

    public int getAgeRange() {
        return 1;// participant_1.calculateAgeRange(averageAge);
    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pairId).append(",");
        sb.append(participant_1.getName()).append(",");
        sb.append(participant_2.getName()).append(",");
        sb.append(kitchenLocation.getLatitude()).append(",").append(kitchenLocation.getLongitude()).append(",");
        sb.append(distanceToPartyLocation).append(",");
        sb.append(foodPreference.name()).append(",");
        sb.append(ageDifference).append(",");
        sb.append(averageAge).append(",");
        sb.append(genderDiversity).append(",");
        sb.append(preferenceDeviation).append(",");
        sb.append(is_registered_together).append(",");
        sb.append(starterGroupId).append(",");
        sb.append(mainGroupId).append(",");
        sb.append(dessertGroupId).append(",");
        sb.append(getDishNameFromId(hostingDish));  // Getting dish name based on id
        return sb.toString();
    }
    private String getDishNameFromId(Dish dish) {
        switch (dish) {
            case STARTER:
                return "Starter";
            case MAIN:
                return "Main";
            case DESSERT:
                return "Dessert";
            default:
                return "";
        }
    }

    public Participant getParticipant_1() {
        return participant_1;
    }

    public Participant getParticipant_2() {
        return participant_2;
    }

    public int getAgeDifference() {
        return ageDifference;
    }

    public double getGenderDiversity() {
        return genderDiversity;
    }

    public int getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public void setPairId(int pairId) {
        this.pairId = pairId;
    }
    public void setParticipant_1(Participant participant){this.participant_1 = participant;}
    public void setParticipant_2(Participant participant){this.participant_2 = participant;}

    public void setKitchenLocation(Location kitchenLocation) {
        this.kitchenLocation = kitchenLocation;
    }

    public void setParticipantThatProvidesKitchen(boolean participantThatProvidesKitchen) {
        this.participantThatProvidesKitchen = participantThatProvidesKitchen;
    }

    public void setDistanceToPartyLocation(double distanceToPartyLocation) {
        this.distanceToPartyLocation = distanceToPartyLocation;
    }

    public FoodPreference getFoodPreference() {
        return foodPreference;
    }

    public double getDistanceToPartyLocation() {
        return distanceToPartyLocation;
    }

    public Location getKitchenLocation() {
        return kitchenLocation;
    }

    public int getPairId() {
        return pairId;
    }


    public void setStarterGroupId(int starterGroupId) {
        this.starterGroupId = starterGroupId;
    }

    public void setMainGroupId(int mainGroupId) {
        this.mainGroupId = mainGroupId;
    }

    public void setDessertGroupId(int dessertGroupId) {
        this.dessertGroupId = dessertGroupId;
    }

    public void setHostingDish(Dish hostingDish) {
        this.hostingDish = hostingDish;
    }

    public Dish getHostingDish() {
        return hostingDish;
    }

    public int getMainGroupId() {
        return mainGroupId;
    }

    public int getDessertGroupId() {
        return dessertGroupId;
    }

    public int getStarterGroupId() {
        return starterGroupId;
    }


    public boolean isParticipantThatProvidesKitchen() {
        return participantThatProvidesKitchen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair that = (Pair) o;
        return participant_1.equals(that.participant_1) && participant_2.equals(that.participant_2) ;
    }
    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
