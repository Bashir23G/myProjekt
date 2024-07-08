package Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Participant implements Serializable {
    private static final long serialVersionUID = 1L; // Add serialVersionUID

    private final String id;
    private String name;
    private FoodPreference foodPreference;
    private int age;
    private Gender gender;
    private  Kitchen kitchen;
    private double kitchenLatitude;
    private double kitchenLongitude;
    private double kitchenStory;
    private  Location kitchenLocation;
    private  Participant partnerParticipant;
    private boolean is_registered_together;
    private int ageRange;
    private List<ParticipantWeightPair>preferredPartnerList = new ArrayList<>();


    public Participant(String id, String name, FoodPreference foodPreference, int age, Gender gender, Kitchen kitchen,
                       double kitchenStory, Location kitchenLocation, Participant partnerParticipant, boolean is_registered_together) {
        this.id = id;
        this.name = name;
        this.foodPreference =  foodPreference;
        this.age = age;
        this.gender = gender;
        this.kitchen = kitchen;
        this.kitchenStory = kitchenStory;
        this.kitchenLocation = kitchenLocation;
        this.partnerParticipant = partnerParticipant;
        this.is_registered_together = is_registered_together;
        this.ageRange = calculateAgeRange(age);
        this.kitchenLatitude = kitchenLatitude;
        this.kitchenLongitude = kitchenLongitude;

    }

    public int calculateAgeRange(int participant_age) {
        if (participant_age >= 0 && participant_age <= 17) {
            return 0;
        } else if (participant_age >= 18 && participant_age <= 23) {
            return 1;
        } else if (participant_age >= 24 && participant_age <= 27) {
            return 2;
        } else if (participant_age >= 28 && participant_age <= 30) {
            return 3;
        } else if (participant_age >= 31 && participant_age <= 35) {
            return 4;
        } else if (participant_age >= 36 && participant_age <= 41) {
            return 5;
        } else if (participant_age >= 42 && participant_age <= 46) {
            return 6;
        } else if (participant_age >= 47 && participant_age <= 56) {
            return 7;
        } else {
            return 8; // Für alle Altersgruppen ab 57 und älter
        }
    }


    public ParticipantWeightPair findBestPossiblePartnerFromSuccessorList(List<Participant> successorList) {

        for(ParticipantWeightPair preferredParticipant : preferredPartnerList) {

            if(successorList.contains(preferredParticipant.getParticipant())) {
                return preferredParticipant;
            }

        }

        return null;
    }


    public void setPreferredPartnerList(List<ParticipantWeightPair> preferedPartnerList) {
        this.preferredPartnerList = preferedPartnerList;
    }

    public int getAgeRange() {
        return ageRange;
    }

    public List<ParticipantWeightPair> getPreferedPartnerList() {
        return preferredPartnerList;
    }

    public void setPartnerParticipant(Participant partnerParticipant) {
        this.partnerParticipant = partnerParticipant;
    }

    public void setIs_registered_together(boolean is_registered_together) {
        this.is_registered_together = is_registered_together;
    }
    public String toCSVString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",");
        sb.append(name).append(",");
        sb.append(age).append(",");
        sb.append(gender).append(",");
        return sb.toString();
    }

    public void printParticipant() {
        System.out.println(id + " " + name + " " + foodPreference + " " + age + " " +   gender +  " "  + kitchen + " " + kitchenStory +  " " + kitchenLocation.getLatitude() + " "  + kitchenLocation.getLongitude());
    }


    public double getKitchenStory() {
        return kitchenStory;
    }

    public FoodPreference getFoodPreference() {
        return foodPreference;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public Participant getPartnerParticipant() {
        return partnerParticipant;
    }

    public int getAge() {
        return age;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public Location getKitchenLocation() {
        return kitchenLocation;
    }

    public String getId() {
        return id;
    }

    public boolean isIs_registered_together() {
        return is_registered_together;
    }

    public boolean isPartnered() {
        return partnerParticipant != null;
    }

    public double getKitchenLatitude() {
        return kitchenLatitude;
    }

    public double getKitchenLongitude() {
        return kitchenLongitude;
    }
    public void setKitchenLatitude(double kitchenLatitude) {
        this.kitchenLatitude = kitchenLatitude;
    }

    public void setKitchenLongitude(double kitchenLongitude) {
        this.kitchenLongitude = kitchenLongitude;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return age == that.age &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                foodPreference == that.foodPreference &&
                gender == that.gender &&
                kitchen == that.kitchen &&
                Double.compare(that.kitchenStory, kitchenStory) == 0 &&
                Double.compare(that.kitchenLocation.getLatitude(), kitchenLocation.getLatitude()) == 0 &&
                Double.compare(that.kitchenLocation.getLongitude(), kitchenLocation.getLongitude()) == 0 &&
                is_registered_together == that.is_registered_together;
    }
}
